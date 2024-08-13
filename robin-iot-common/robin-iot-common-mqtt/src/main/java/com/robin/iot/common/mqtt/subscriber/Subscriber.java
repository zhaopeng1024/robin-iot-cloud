package com.robin.iot.common.mqtt.subscriber;

import com.robin.iot.common.mqtt.convert.MqttConversionService;
import com.robin.iot.common.mqtt.exception.NullParameterException;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.core.convert.converter.Converter;

import java.lang.reflect.Method;
import java.util.*;

/**
 * 订阅者。
 *
 * @author zhao peng
 * @date 2024/7/18 22:48
 **/
@Slf4j
public class Subscriber {

    private SubscriberModel subscriberModel;
    private String[] clientIds;
    private IMessageHandler handler;
    private LinkedList<ParameterModel> parameters;
    /**
     * 是否已经填充参数了
     */
    private boolean hasResolveEmbeddedValue;

    @Getter
    private final LinkedList<TopicPair> topics = new LinkedList<>();

    public void accept(String clientId, String topic, MqttMessage message) {
        Optional<TopicPair> matched = matched(clientId, topic);
        if (matched.isPresent()) {
            try {
                Object[] parameters = fillParameters(matched.get(), topic, message);
                handler.receive(parameters);
            } catch (NullParameterException e) {
                log.debug("message params error: {}", e.getMessage());
            } catch (Exception e) {
                log.error("message handler error: {}", e.getMessage(), e);
            }
        }
    }

    public static Subscriber of(SubscriberModel subscriberModel, Object bean, Method method) {
        LinkedList<ParameterModel> parameters = ParameterModel.of(method);
        IMessageHandler handler = (params) -> method.invoke(bean, params);
        return of(subscriberModel, parameters, handler);
    }

    /**
     * 创建消息处理对象
     *
     * @param subscriberModel  订阅模型
     * @param parameters 处理方法的参数
     * @param handler    消息处理方法
     */
    public static Subscriber of(SubscriberModel subscriberModel, LinkedList<ParameterModel> parameters, IMessageHandler handler) {
        Subscriber subscriber = new Subscriber();
        subscriber.subscriberModel = subscriberModel;
        subscriber.handler = handler;
        subscriber.parameters = parameters;
        return subscriber;
    }

    private void setTopics(SubscriberModel subscribe, HashMap<String, Class<?>> paramTypeMap) {
        String[] topics = subscribe.value();
        int[] qos = fillQos(topics, subscribe.qos());
        String[] groups = fillGroups(topics, subscribe.groups());
        LinkedHashSet<TopicPair> temps = new LinkedHashSet<>();
        for (int i = 0; i < topics.length; i++) {
            temps.add(TopicPair.of(topics[i], qos[i], groups[i], paramTypeMap));
        }
        this.topics.addAll(temps);
        this.topics.sort(Comparator.comparingInt(TopicPair::order));
    }

    private int[] fillQos(String[] topics, int[] qos) {
        int topic_len = topics.length;
        int qos_len = qos.length;
        if (topic_len > qos_len) {
            int[] temp = new int[topic_len];
            System.arraycopy(qos, 0, temp, 0, qos_len);
            Arrays.fill(temp, qos_len, topic_len, qos[qos_len - 1]);
            return temp;
        } else if (qos_len > topic_len) {
            int[] temp = new int[topic_len];
            System.arraycopy(qos, 0, temp, 0, topic_len);
            return temp;
        }
        return qos;
    }

    private String[] fillGroups(String[] topics, String[] groups) {
        int topic_len = topics.length;
        int qos_len = groups.length;
        if (topic_len > qos_len) {
            String[] temp = new String[topic_len];
            System.arraycopy(groups, 0, temp, 0, qos_len);
            Arrays.fill(temp, qos_len, topic_len, groups[qos_len - 1]);
            return temp;
        } else if (qos_len > topic_len) {
            String[] temp = new String[topic_len];
            System.arraycopy(groups, 0, temp, 0, topic_len);
            return temp;
        }
        return groups;
    }

    /**
     * 该函数的功能是在给定的主题列表中，查找与指定客户端 ID 和主题匹配的主题对。
     * 如果找到了匹配的主题对，则返回一个包含该主题对的 Optional 对象；否则返回一个空的 Optional 对象。
     * <p>
     * 函数首先检查 clientIds 数组是否为空或长度为 0，或者给定的客户端 ID 是否存在于 clientIds 数
     * 组中（使用 Arrays.binarySearch() 方法进行二分查找）。如果满足条件，则从 topics 列表中筛选
     * 出匹配给定主题的 TopicPair 对象，并使用 findFirst() 方法获取第一个匹配项。如果未找到匹配项，
     * 则返回一个空的 Optional 对象。
     * @param clientId 客户端 ID
     * @param topic 主题
     * @return Optional<TopicPair>
     */
    private Optional<TopicPair> matched(final String clientId, final String topic) {
        if (clientIds == null || clientIds.length == 0 || Arrays.binarySearch(clientIds, clientId) >= 0) {
            return topics.stream().filter(pair -> pair.isMatched(topic)).findFirst();
        }
        return Optional.empty();
    }

    private Object[] fillParameters(TopicPair topicPair, String topic, MqttMessage mqttMessage) {
        HashMap<String, String> pathValueMap = topicPair.getPathValueMap(topic);
        LinkedList<Object> objects = new LinkedList<>();
        for (ParameterModel parameter : parameters) {
            Class<?> target = parameter.getType();
            String name = parameter.getName();
            LinkedList<Converter<Object, Object>> converters = parameter.getConverters();
            Object value = null;
            if (target == MqttMessage.class) {
                value = mqttMessage;
            } else if (parameter.isPayload() && mqttMessage != null) {
                value = MqttConversionService.getSharedInstance().fromBytes(mqttMessage.getPayload(), target, converters);
            } else if (name != null) {
                if (pathValueMap.containsKey(name)) {
                    value = fromTopic(pathValueMap.get(name), target);
                }
            } else if (target == String.class) {
                value = topic;
            } else if (target.getClassLoader() != null && mqttMessage != null) {
                value = MqttConversionService.getSharedInstance().fromBytes(mqttMessage.getPayload(), target, converters);
            }
            if (value == null) {
                if (parameter.isRequired()) {
                    throw new NullParameterException(parameter);
                }
                value = parameter.getDefaultValue();
            }
            objects.add(value);
        }
        return objects.toArray();
    }

    private Object fromTopic(String value, Class<?> target) {
        if (MqttConversionService.getSharedInstance().canConvert(String.class, target)) {
            return MqttConversionService.getSharedInstance().convert(value, target);
        } else {
            log.warn("Unsupported covert from {} to {}", String.class.getName(), target.getName());
            return null;
        }
    }

    public boolean containsClientId(String clientId) {
        if (this.clientIds == null || this.clientIds.length == 0) {
            return true; // for all client
        }
        for (String id : clientIds) {
            if (id.equals(clientId)) {
                return true;
            }
        }
        return false;
    }

    /**
     * 用来填充 spring 的参数。
     *
     * @param factory spring。
     */
    public void resolveEmbeddedValue(ConfigurableBeanFactory factory) {
        if (hasResolveEmbeddedValue) {
            return;
        }
        hasResolveEmbeddedValue = true;
        if (factory != null) {
            String[] clients = subscriberModel.clients();
            for (int i = 0; i < clients.length; i++) {
                clients[i] = factory.resolveEmbeddedValue(clients[i]);
            }
            String[] value = subscriberModel.value();
            for (int i = 0; i < value.length; i++) {
                value[i] = factory.resolveEmbeddedValue(value[i]);
            }
        }

        HashMap<String, Class<?>> paramTypeMap = new HashMap<>();
        this.parameters.stream()
                .filter(param -> param.getName() != null)
                .forEach(param -> paramTypeMap.put(param.getName(), param.getType()));
        this.clientIds = subscriberModel.clients();
        this.setTopics(subscriberModel, paramTypeMap);
    }

}
