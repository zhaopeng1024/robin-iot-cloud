package com.robin.iot.common.mqtt.autoconfigure;

import com.robin.iot.common.mqtt.subscriber.TopicPair;
import org.eclipse.paho.client.mqttv3.IMqttAsyncClient;
import org.eclipse.paho.client.mqttv3.MqttAsyncClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;

import java.util.Set;

/**
 * Mqtt 配置适配器
 *
 * @author zhao peng
 * @date 2024/7/16 23:23
 **/
public abstract class MqttConfigurationAdapter {

    public static MqttConfigurationAdapter defaultAdapter() {
        return new MqttConfigurationAdapter() {
        };
    }

    protected MqttProperties mqttProperties;

    public final void setMqttProperties(MqttProperties mqttProperties) {
        this.mqttProperties = mqttProperties;
        MqttClientRegister mqttClientRegister = new MqttClientRegister(mqttProperties);
        beforeCreate(mqttClientRegister);
    }

    /**
     * 创建客户端之前，可在此处对客户端配置进行加工
     * @param mqttClientRegister 客户端注册器实例
     */
    private void beforeCreate(MqttClientRegister mqttClientRegister) {

    }

    /**
     * 在创建客户端后, 订阅主题前, 修改订阅的主题。
     *
     * @param clientId 客户端ID
     * @param options  MqttConnectOptions
     */
    public void beforeConnect(String clientId, MqttConnectOptions options) {

    }

    /**
     * 在创建客户端后, 订阅主题前, 修改订阅的主题.
     *
     * @param clientId   客户端ID
     * @param topicPairs 订阅主题
     */
    public void beforeSubscribe(String clientId, Set<TopicPair> topicPairs) {

    }

    /**
     * 创建客户端
     * @param clientId 客户端 ID
     * @param serverURIs 服务器 URI 数组
     * @return IMqttAsyncClient 客户端实例
     * @throws MqttException MQTT 异常
     */
    public IMqttAsyncClient postCreate(String clientId, String[] serverURIs) throws MqttException {
        return new MqttAsyncClient(serverURIs[0], clientId, null);
    }

}
