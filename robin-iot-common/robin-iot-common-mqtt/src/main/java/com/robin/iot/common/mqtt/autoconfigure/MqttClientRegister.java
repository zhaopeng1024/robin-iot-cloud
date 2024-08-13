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
        MqttConnection mqttConnection = new MqttConnection();
        if (uris != null && uris.length > 0) {
            mqttConnection.setUris(uris);
        } else {
            mqttConnection.setUris(null);
        }
        mqttConnection.setClientId(clientId);
        mqttProperties.getClients().put(clientId, mqttConnection);
        resetClientId(clientId);
        return this;
    }

    /**
     * 添加客户端
     * @param clientId 客户端 ID
     * @param mqttConnection MQTT 连接配置
     * @return 注册器实例
     */
    public MqttClientRegister add(String clientId, MqttConnection mqttConnection) {
        Assert.notNull(clientId, "clientId must not be null");
        Assert.notNull(mqttConnection, "mqttConnection must not be null");
        mqttConnection.setClientId(clientId);
        mqttProperties.getClients().put(clientId, mqttConnection);
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
     * @param mqttConnection 默认客户端配置
     * @return 注册器实例
     */
    public MqttClientRegister setDefault(MqttConnection mqttConnection) {
        mqttProperties.setClientId(mqttConnection.getClientId());
        mqttProperties.setUsername(mqttConnection.getUsername());
        mqttProperties.setPassword(mqttConnection.getPassword());
        mqttProperties.setWill(mqttConnection.getWill());
        mqttProperties.setAutomaticReconnect(mqttConnection.getAutomaticReconnect());
        mqttProperties.setCleanSession(mqttConnection.getCleanSession());
        mqttProperties.setConnectionTimeout(mqttConnection.getConnectionTimeout());
        mqttProperties.setExecutorServiceTimeout(mqttConnection.getExecutorServiceTimeout());
        mqttProperties.setKeepAliveInterval(mqttConnection.getKeepAliveInterval());
        mqttProperties.setMaxReconnectDelay(mqttConnection.getMaxReconnectDelay());
        mqttProperties.setUris(mqttConnection.getUris());
        mqttProperties.setEnableSharedSubscription(mqttConnection.getEnableSharedSubscription());
        mqttProperties.setDefaultPublishQos(mqttConnection.getDefaultPublishQos());
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
