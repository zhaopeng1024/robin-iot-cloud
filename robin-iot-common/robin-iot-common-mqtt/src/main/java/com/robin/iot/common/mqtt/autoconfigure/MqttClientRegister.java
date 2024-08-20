package com.robin.iot.common.mqtt.autoconfigure;

import org.springframework.util.Assert;

/**
 * Mqtt 客户端注册器
 *
 * @author zhao peng
 * @date 2024/7/15 21:21
 **/
@SuppressWarnings("unused")
public class MqttClientRegister {

    private final MqttProperties mqttProperties;

    public MqttClientRegister(MqttProperties mqttProperties) {
        this.mqttProperties = mqttProperties;
    }

    /**
     * 禁用
     * @return 注册器实例
     */
    public MqttClientRegister disable() {
        mqttProperties.setDisable(true);
        return this;
    }

    /**
     * 添加客户端
     * @param clientId 客户端 ID
     * @param uris 连接地址可变数组
     * @return 注册器实例
     */
    public MqttClientRegister add(String clientId, String... uris) {
        Assert.notNull(clientId, "clientId must not be null");
        MqttConnectionProperties mqttConnectionProperties = new MqttConnectionProperties();
        if (uris != null && uris.length > 0) {
            mqttConnectionProperties.setUris(uris);
        } else {
            mqttConnectionProperties.setUris(null);
        }
        mqttConnectionProperties.setClientId(clientId);
        mqttProperties.getClients().put(clientId, mqttConnectionProperties);
        resetClientId(clientId);
        return this;
    }

    /**
     * 添加客户端
     * @param clientId 客户端 ID
     * @param mqttConnectionProperties MQTT 连接配置
     * @return 注册器实例
     */
    public MqttClientRegister add(String clientId, MqttConnectionProperties mqttConnectionProperties) {
        Assert.notNull(clientId, "clientId must not be null");
        Assert.notNull(mqttConnectionProperties, "mqttConnection must not be null");
        mqttConnectionProperties.setClientId(clientId);
        mqttProperties.getClients().put(clientId, mqttConnectionProperties);
        resetClientId(clientId);
        return this;
    }

    /**
     * 移除客户端
     * @param clientId 客户端 ID
     * @return 注册器实例
     */
    public MqttClientRegister remove(String clientId) {
        Assert.notNull(clientId, "clientId must not be null");
        mqttProperties.getClients().remove(clientId);
        resetClientId(clientId);
        return this;
    }

    /**
     * 清空客户端
     * @return 注册器实例
     */
    public MqttClientRegister clear() {
        mqttProperties.setClientId(null);
        mqttProperties.getClients().clear();
        return this;
    }

    /**
     * 设置默认客户端
     * @param mqttConnectionProperties 默认客户端配置
     * @return 注册器实例
     */
    public MqttClientRegister setDefault(MqttConnectionProperties mqttConnectionProperties) {
        mqttProperties.setClientId(mqttConnectionProperties.getClientId());
        mqttProperties.setUsername(mqttConnectionProperties.getUsername());
        mqttProperties.setPassword(mqttConnectionProperties.getPassword());
        mqttProperties.setWillProperties(mqttConnectionProperties.getWillProperties());
        mqttProperties.setAutomaticReconnect(mqttConnectionProperties.getAutomaticReconnect());
        mqttProperties.setCleanSession(mqttConnectionProperties.getCleanSession());
        mqttProperties.setConnectionTimeout(mqttConnectionProperties.getConnectionTimeout());
        mqttProperties.setExecutorServiceTimeout(mqttConnectionProperties.getExecutorServiceTimeout());
        mqttProperties.setKeepAliveInterval(mqttConnectionProperties.getKeepAliveInterval());
        mqttProperties.setMaxReconnectDelay(mqttConnectionProperties.getMaxReconnectDelay());
        mqttProperties.setUris(mqttConnectionProperties.getUris());
        mqttProperties.setEnableSharedSubscription(mqttConnectionProperties.getEnableSharedSubscription());
        mqttProperties.setDefaultPublishQos(mqttConnectionProperties.getDefaultPublishQos());
        return this;
    }
    
    /**
     * 重置 clientId
     * @param clientId 客户端 ID
     */
    private void resetClientId(String clientId) {
        if (mqttProperties.getClientId() != null && clientId.equals(mqttProperties.getClientId())) {
            // 如果 当前 clientId 与默认 clientId 相同，则重置默认 clientId
            // 原因：修改后的是通过 clients 集合保存的，如果与默认的 clientId 冲突，则将默认的 clientId 置空
            mqttProperties.setClientId(null);
        }
    }
}
