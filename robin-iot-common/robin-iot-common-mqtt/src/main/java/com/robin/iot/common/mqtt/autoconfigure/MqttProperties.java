package com.robin.iot.common.mqtt.autoconfigure;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.util.StringUtils;

import java.nio.charset.StandardCharsets;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;
import java.util.function.BiConsumer;

/**
 * MQTT 配置属性
 *
 * @author zhao peng
 * @date 2024/7/16 0:02
 **/
@EqualsAndHashCode(callSuper = true)
@Data
@ConfigurationProperties(prefix = "spring.mqtt")
public class MqttProperties extends MqttConnectionProperties {

    /**
     * 是否禁用
     */
    private Boolean disable = false;

    /**
     * 客户端配置，key: clientId，value: MqttConnectProperties
     */
    private Map<String, MqttConnectionProperties> clients = new LinkedHashMap<>();

    /**
     * 遍历所有的客户端配置
     * @param biConsumer String, MqttConnectOptions
     */
    public void foreach(BiConsumer<String, MqttConnectOptions> biConsumer) {
        MqttConnectOptions defaultOptions = toOptions();
        if (defaultOptions != null) {
            biConsumer.accept(getClientId(), defaultOptions);
        }
        if (clients != null && !clients.isEmpty()) {
            // 先遍历一遍解决 clientId 冲突的问题
            String[] clientIds = clients.keySet().toArray(new String[0]);
            for (String clientId : clientIds) {
                MqttConnectionProperties mqttConnectionProperties = clients.get(clientId);
                String localClientId = mqttConnectionProperties.getClientId();
                if (StringUtils.hasText(localClientId) && !localClientId.equals(clientId)) {
                    clients.remove(clientId);
                    clients.put(localClientId, mqttConnectionProperties);
                } else {
                    mqttConnectionProperties.setClientId(clientId);
                }
            }
            clients.forEach((clientId, mqttConnectionProperties) -> {
                MqttConnectOptions options = toOptions(clientId);
                if (options != null) {
                    biConsumer.accept(clientId, options);
                }
            });
        }
    }

    private MqttConnectOptions toOptions() {
        if (StringUtils.hasText(getClientId())) {
            return toOptions(getClientId());
        }
        return null;
    }

    public MqttConnectOptions toOptions(String clientId) {
        MqttConnectionProperties mqttConnectionProperties = clients.get(clientId);
        if (mqttConnectionProperties == null) {
            if (clientId.equals(getClientId())) {
                mqttConnectionProperties = this;
            } else {
                return null;
            }
        }
        merge(mqttConnectionProperties);
        return toOptions(mqttConnectionProperties);
    }

    public MqttConnectOptions toOptions(MqttConnectionProperties mqttConnectionProperties) {
        MqttConnectOptions options = new MqttConnectOptions();
        options.setMaxReconnectDelay(mqttConnectionProperties.getMaxReconnectDelay() * 1000);
        options.setKeepAliveInterval(mqttConnectionProperties.getKeepAliveInterval());
        options.setConnectionTimeout(mqttConnectionProperties.getConnectionTimeout());
        options.setCleanSession(mqttConnectionProperties.getCleanSession());
        options.setAutomaticReconnect(mqttConnectionProperties.getAutomaticReconnect());
        options.setExecutorServiceTimeout(mqttConnectionProperties.getExecutorServiceTimeout());
        options.setServerURIs(mqttConnectionProperties.getUris());
        if (StringUtils.hasText(mqttConnectionProperties.getUsername()) && StringUtils.hasText(mqttConnectionProperties.getPassword())) {
            options.setUserName(mqttConnectionProperties.getUsername());
            options.setPassword(mqttConnectionProperties.getPassword().toCharArray());
        }
        if (mqttConnectionProperties.getWillProperties() != null) {
            WillProperties willProperties = mqttConnectionProperties.getWillProperties();
            if (StringUtils.hasText(willProperties.getTopic()) && StringUtils.hasText(willProperties.getPayload())) {
                options.setWill(willProperties.getTopic(), willProperties.getPayload().getBytes(StandardCharsets.UTF_8), willProperties.getQos(), willProperties.getRetained());
            }
        }
        return options;
    }

    public void merge(MqttConnectionProperties target) {
        target.setUris(mergeValue(getUris(), target.getUris(), new String[]{"tcp://127.0.0.1:1883"}));
        target.setUsername(mergeValue(getUsername(), target.getUsername(), null));
        target.setPassword(mergeValue(getPassword(), target.getPassword(), null));
        target.setDefaultPublishQos(mergeValue(getDefaultPublishQos(), target.getDefaultPublishQos(), 0));
        target.setMaxReconnectDelay(mergeValue(getMaxReconnectDelay(), target.getMaxReconnectDelay(), 60));
        target.setKeepAliveInterval(mergeValue(getKeepAliveInterval(), target.getKeepAliveInterval(), 60));
        target.setConnectionTimeout(mergeValue(getConnectionTimeout(), target.getConnectionTimeout(), 30));
        target.setExecutorServiceTimeout(mergeValue(getExecutorServiceTimeout(), target.getExecutorServiceTimeout(), 10));
        target.setCleanSession(mergeValue(getCleanSession(), target.getCleanSession(), true));
        target.setAutomaticReconnect(mergeValue(getAutomaticReconnect(), target.getAutomaticReconnect(), true));
        target.setWillProperties(mergeValue(getWillProperties(), target.getWillProperties(), null));
        target.setEnableSharedSubscription(mergeValue(getEnableSharedSubscription(), target.getEnableSharedSubscription(), true));
        if (target.getWillProperties() != null && getWillProperties() != null) {
            WillProperties willProperties = getWillProperties();
            WillProperties targetWillProperties = target.getWillProperties();
            targetWillProperties.setTopic(mergeValue(willProperties.getTopic(), targetWillProperties.getTopic(), null));
            targetWillProperties.setPayload(mergeValue(willProperties.getPayload(), targetWillProperties.getPayload(), null));
            targetWillProperties.setQos(mergeValue(willProperties.getQos(), targetWillProperties.getQos(), 0));
            targetWillProperties.setRetained(mergeValue(willProperties.getRetained(), targetWillProperties.getRetained(), false));
        }
    }
    
    public <T> T mergeValue(T parentValue, T targetValue, T defaultValue) {
        if (parentValue == null && targetValue == null) {
            return defaultValue;
        } else {
            return Objects.requireNonNullElse(targetValue, parentValue);
        }
    }

    public boolean isEnableSharedSubscription(String clientId) {
        if (clientId.equals(getClientId())) {
            return getEnableSharedSubscription();
        } else {
            MqttConnectionProperties mqttConnectionProperties = clients.get(clientId);
            if (mqttConnectionProperties == null) {
                return false;
            }
            return mqttConnectionProperties.getEnableSharedSubscription();
        }
    }

    public int getDefaultPublishQos(String clientId) {
        if (clientId.equals(getClientId())) {
            return getDefaultPublishQos();
        } else {
            MqttConnectionProperties mqttConnectionProperties = clients.get(clientId);
            if (mqttConnectionProperties == null) {
                return 0;
            }
            return mqttConnectionProperties.getDefaultPublishQos();
        }
    }
}
