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
 * MQTT 配置类
 *
 * @author zhao peng
 * @date 2024/7/16 0:02
 **/
@EqualsAndHashCode(callSuper = true)
@Data
@ConfigurationProperties(prefix = "spring.mqtt")
public class MqttProperties extends MqttConnection {

    /**
     * 是否禁用
     */
    private Boolean disable = false;

    /**
     * 客户端配置，key: clientId，value: MqttConnectProperties
     */
    private Map<String, MqttConnection> clients = new LinkedHashMap<>();

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
                MqttConnection mqttConnection = clients.get(clientId);
                String localClientId = mqttConnection.getClientId();
                if (StringUtils.hasText(localClientId) && !localClientId.equals(clientId)) {
                    clients.remove(clientId);
                    clients.put(localClientId, mqttConnection);
                } else {
                    mqttConnection.setClientId(clientId);
                }
            }
            clients.forEach((clientId, mqttConnection) -> {
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
        MqttConnection mqttConnection = clients.get(clientId);
        if (mqttConnection == null) {
            if (clientId.equals(getClientId())) {
                mqttConnection = this;
            } else {
                return null;
            }
        }
        merge(mqttConnection);
        return toOptions(mqttConnection);
    }

    public MqttConnectOptions toOptions(MqttConnection mqttConnection) {
        MqttConnectOptions options = new MqttConnectOptions();
        options.setMaxReconnectDelay(mqttConnection.getMaxReconnectDelay() * 1000);
        options.setKeepAliveInterval(mqttConnection.getKeepAliveInterval());
        options.setConnectionTimeout(mqttConnection.getConnectionTimeout());
        options.setCleanSession(mqttConnection.getCleanSession());
        options.setAutomaticReconnect(mqttConnection.getAutomaticReconnect());
        options.setExecutorServiceTimeout(mqttConnection.getExecutorServiceTimeout());
        options.setServerURIs(mqttConnection.getUris());
        if (StringUtils.hasText(mqttConnection.getUsername()) && StringUtils.hasText(mqttConnection.getPassword())) {
            options.setUserName(mqttConnection.getUsername());
            options.setPassword(mqttConnection.getPassword().toCharArray());
        }
        if (mqttConnection.getWill() != null) {
            Will will = mqttConnection.getWill();
            if (StringUtils.hasText(will.getTopic()) && StringUtils.hasText(will.getPayload())) {
                options.setWill(will.getTopic(), will.getPayload().getBytes(StandardCharsets.UTF_8), will.getQos(), will.getRetained());
            }
        }
        return options;
    }

    public void merge(MqttConnection target) {
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
        target.setWill(mergeValue(getWill(), target.getWill(), null));
        target.setEnableSharedSubscription(mergeValue(getEnableSharedSubscription(), target.getEnableSharedSubscription(), true));
        if (target.getWill() != null && getWill() != null) {
            Will will = getWill();
            Will targetWill = target.getWill();
            targetWill.setTopic(mergeValue(will.getTopic(), targetWill.getTopic(), null));
            targetWill.setPayload(mergeValue(will.getPayload(), targetWill.getPayload(), null));
            targetWill.setQos(mergeValue(will.getQos(), targetWill.getQos(), 0));
            targetWill.setRetained(mergeValue(will.getRetained(), targetWill.getRetained(), false));
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
            MqttConnection mqttConnection = clients.get(clientId);
            if (mqttConnection == null) {
                return false;
            }
            return mqttConnection.getEnableSharedSubscription();
        }
    }

    public int getDefaultPublishQos(String clientId) {
        if (clientId.equals(getClientId())) {
            return getDefaultPublishQos();
        } else {
            MqttConnection mqttConnection = clients.get(clientId);
            if (mqttConnection == null) {
                return 0;
            }
            return mqttConnection.getDefaultPublishQos();
        }
    }
}
