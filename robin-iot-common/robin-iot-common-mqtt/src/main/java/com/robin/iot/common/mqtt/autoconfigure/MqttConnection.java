package com.robin.iot.common.mqtt.autoconfigure;

import lombok.Data;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;

/**
 * MQTT 连接
 *
 * @author zhao peng
 * @date 2024/7/15 22:13
 **/
@Data
public class MqttConnection {

    /**
     * MQTT 服务器地址，必填，可以配置多个地址
     *
     * @see MqttConnectOptions#setServerURIs(String[])
     */
    private String[] uris = new String[]{"tcp://127.0.0.1:1883"};

    /**
     * 客户端ID，必填
     */
    private String clientId;

    /**
     * 用户名
     *
     * @see MqttConnectOptions#setUserName(String)
     */
    private String username;

    /**
     * 密码
     *
     * @see MqttConnectOptions#setPassword(char[])
     */
    private String password;

    /**
     * 是否启用共享订阅，对于不同的 Broker，共享订阅可能无效（EMQ有效）
     */
    private Boolean enableSharedSubscription = true;

    /**
     * 发布消息默认实用的 QoS，默认为 0
     */
    private Integer defaultPublishQos = 0;

    /**
     * 最大重连等待时间，单位：秒
     *
     * @see MqttConnectOptions#setMaxReconnectDelay(int)
     */
    private Integer maxReconnectDelay;

    /**
     * KeepAlive 周期（秒），代表客户端发送两次 MQTT 协议数据包之间的最大间隔时间
     *
     * @see MqttConnectOptions#setKeepAliveInterval(int)
     */
    private Integer keepAliveInterval;

    /**
     * 连接超时时间（秒）
     *
     * @see MqttConnectOptions#setConnectionTimeout(int)
     */
    private Integer connectionTimeout;

    /**
     * 发送超时时间（秒）
     *
     * @see MqttConnectOptions#setExecutorServiceTimeout(int)
     */
    private Integer executorServiceTimeout;

    /**
     * 是否清除会话
     *
     * @see MqttConnectOptions#setCleanSession(boolean)
     */
    private Boolean cleanSession;

    /**
     * 断开是否自动重连
     *
     * @see MqttConnectOptions#setAutomaticReconnect(boolean)
     */
    private Boolean automaticReconnect;

    /**
     * 遗愿消息相关配置
     */
    private Will will;

}
