package com.robin.iot.common.mqtt.publisher;

import com.robin.iot.common.mqtt.autoconfigure.MqttClientAdapter;
import com.robin.iot.common.mqtt.convert.MqttConversionService;
import com.robin.iot.common.mqtt.subscriber.Subscriber;
import com.robin.iot.common.mqtt.subscriber.TopicPairer;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.paho.client.mqttv3.*;
import org.springframework.util.Assert;

import java.util.*;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * 简单的 Mqtt 客户端封装类
 *
 * @author zhao peng
 * @date 2024/8/12 23:41
 **/
@Slf4j
public record SimpleMqttClient(String id,
                               IMqttAsyncClient client,
                               MqttConnectOptions options,
                               boolean enableShared,
                               int qos,
                               LinkedList<Subscriber> subscribers,
                               MqttClientAdapter adapter) {

    private static final ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();

    public void connect() {
        adapter.beforeConnect(id, options);
        try {
            client.connect(options, new IMqttActionListener() {
                @Override
                public void onSuccess(IMqttToken asyncActionToken) {
                    log.info("mqtt client {} connect success, brokers is {}", id, String.join(",", options.getServerURIs()));
                    subscribe();
                }

                @Override
                public void onFailure(IMqttToken asyncActionToken, Throwable exception) {
                    log.error("mqtt client {} connect error, brokers is {}, retry after {} ms.", id,
                            String.join(",", options.getServerURIs()),
                            options.getMaxReconnectDelay());
                    scheduler.schedule(() -> connect(), options.getMaxReconnectDelay(), TimeUnit.MILLISECONDS);
                }

            });

            client.setCallback(new MqttCallbackExtended() {

                @Override
                public void connectionLost(Throwable cause) {
                    log.warn("mqtt client {} connection lost.", id);
                }

                @Override
                public void messageArrived(String topic, MqttMessage message) throws Exception {
                    subscribers.forEach(subscriber -> subscriber.accept(id, topic, message));
                }

                @Override
                public void deliveryComplete(IMqttDeliveryToken token) {

                }

                @Override
                public void connectComplete(boolean reconnect, String serverURI) {
                    if (reconnect) {
                        log.info("mqtt client {} reconnection success.", id);
                        subscribe();
                    }
                }

            });
        } catch (MqttException e) {
            log.error("mqtt client {} connect error.", id, e);
        }
    }

    /**
     * 合并客户端订阅的主题，实际没有什么用
     * @param clientId 客户端ID
     * @param enableShared 是否启用共享订阅
     * @return 合并后的主题集合
     */
    private Set<TopicPairer> mergeTopics(String clientId, boolean enableShared) {
        Set<TopicPairer> topicPairers = new HashSet<>();
        subscribers.forEach(subscriber -> {
            if (subscriber.containsClientId(clientId)) {
                topicPairers.addAll(subscriber.getTopics());
            }
        });
        if (topicPairers.isEmpty()) {
            return topicPairers;
        }
        TopicPairer[] pairs = new TopicPairer[topicPairers.size()];
        topicPairers.forEach(topic -> {
            for (int i = 0; i < pairs.length; i++) {
                TopicPairer pair = pairs[i];
                if (pair == null) {
                    pairs[i] = topic;
                    break;
                }
                if (pair.getQos() != topic.getQos()) {
                    continue;
                }
                String temp = pair.getTopic(enableShared)
                        .replace('+', '\u0000')
                        .replace("#", "\u0000/\u0000");
                if (MqttTopic.isMatched(topic.getTopic(enableShared), temp)) {
                    pairs[i] = topic;
                    continue;
                }
                temp = topic.getTopic(enableShared)
                        .replace('+', '\u0000')
                        .replace("#", "\u0000/\u0000");
                if (MqttTopic.isMatched(pair.getTopic(enableShared), temp)) {
                    break;
                }
            }
        });
        return Arrays.stream(pairs).filter(Objects::nonNull).collect(Collectors.toSet());
    }

    private void subscribe() {
        Set<TopicPairer> topics = mergeTopics(id, enableShared);
        adapter.beforeSubscribe(id, topics);
        if (topics.isEmpty()) {
            log.warn("mqtt client {} has no topic to subscribe.", id);
        } else {
            StringJoiner joiner = new StringJoiner(",");
            String[] topic = new String[topics.size()];
            int[] qos = new int[topics.size()];
            int i = 0;
            for (TopicPairer pair : topics) {
                topic[i] = pair.getTopic(enableShared);
                qos[i] = pair.getQos();
                joiner.add("('" + topic[i] + "', " + qos[i] + ")");
                ++i;
            }
            try {
                client.subscribe(topic, qos);
                log.info("mqtt client {} subscribe success. topics: {}", id, joiner);
            } catch (MqttException e) {
                log.error("mqtt client {} subscribe error.", id, e);
            }
        }
    }

    public void close() {
        try (IMqttAsyncClient c = client()) {
            if (c.isConnected()) {
                c.disconnect();
            }
        } catch (MqttException e) {
            log.error("close mqtt client {} error.", id, e);
        }
    }

    public void send(String topic, Object payload) {
        send(topic, payload, qos(), false, null);
    }

    public void send(String topic, Object payload, boolean retained) {
        send(topic, payload, qos(), retained, null);
    }

    public void send(String topic, Object payload, IMqttActionListener callback) {
        send(topic, payload, qos(), false, callback);
    }

    public void send(String topic, Object payload, boolean retained, IMqttActionListener callback) {
        send(topic, payload, qos(), retained, callback);
    }

    public void send(String topic, Object payload, int qos) {
        send(topic, payload, qos, false, null);
    }

    public void send(String topic, Object payload, int qos, boolean retained) {
        send(topic, payload, qos, retained, null);
    }

    public void send(String topic, Object payload, int qos, IMqttActionListener callback) {
        send(topic, payload, qos, false, callback);
    }

    /**
     * 发送消息到指定主题，指定 Qos、是否保留
     * @param topic 主题
     * @param payload 消息内容
     * @param qos 服务质量
     * @param retained 保留消息
     * @param callback 消息发送完成后的回调
     */
    public void send(String topic, Object payload, int qos, boolean retained, IMqttActionListener callback) {
        Assert.isTrue(topic != null && !topic.isBlank(), "topic must not be blank");
        byte[] bytes = MqttConversionService.getSharedInstance().toBytes(payload);
        if (bytes == null) {
            return;
        }
        MqttMessage message = toMessage(bytes, qos, retained);
        try {
            client.publish(topic, message, null, callback);
        } catch (MqttException e) {
            log.error("send message error.", e);
        }
    }

    private MqttMessage toMessage(byte[] payload, int qos, boolean retained) {
        MqttMessage message = new MqttMessage();
        message.setPayload(payload);
        message.setQos(qos);
        message.setRetained(retained);
        return message;
    }
}
