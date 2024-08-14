package com.robin.iot.common.mqtt.autoconfigure;

import com.robin.iot.common.mqtt.subscriber.Subscriber;
import com.robin.iot.common.mqtt.subscriber.TopicPairer;
import org.eclipse.paho.client.mqttv3.IMqttAsyncClient;
import org.eclipse.paho.client.mqttv3.MqttAsyncClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;

import java.util.LinkedList;
import java.util.Set;

/**
 * Mqtt 客户端适配器，用于扩展
 *
 * @author zhao peng
 * @date 2024/7/16 23:23
 **/
public abstract class MqttClientAdapter {

    /**
     * 返回默认的 MqttConfigurationAdapter 实例。
     * 当前实现直接新建并返回一个该类匿名子类的对象，未添加额外逻辑。
     * @return 默认的 MqttConfigurationAdapter 实例。
     */
    public static MqttClientAdapter defaultAdapter() {
        return new MqttClientAdapter() {
        };
    }

    protected MqttProperties mqttProperties;

    public final void setMqttProperties(MqttProperties mqttProperties) {
        this.mqttProperties = mqttProperties;
        MqttClientRegister clientRegister = new MqttClientRegister(mqttProperties);
        beforeCreate(clientRegister);
    }

    /**
     * 在处理注解内参数之前，可以在这里对订阅者进行加工。
     * 程序启动之后只会执行一次。
     * @param subscribers 订阅者列表
     */
    public void beforeResolveEmbeddedValue(LinkedList<Subscriber> subscribers) {

    }

    /**
     * 在处理注解内参数之后，可以在这里对订阅者进行加工。
     * 程序启动之后只会执行一次。
     * @param subscribers 订阅者列表
     */
    public void afterResolveEmbeddedValue(LinkedList<Subscriber> subscribers) {

    }

    /**
     * 创建客户端之前，可在此处对客户端配置进行加工。
     * @param clientRegister 客户端注册器实例
     */
    public void beforeCreate(MqttClientRegister clientRegister) {

    }

    /**
     * 创建客户端
     * @param clientId 客户端 ID
     * @param serverURIs 服务器 URI 数组
     * @return IMqttAsyncClient 客户端实例
     * @throws MqttException MQTT 异常
     */
    public IMqttAsyncClient postCreate(String clientId, String[] serverURIs) throws MqttException {
        return new MqttAsyncClient(serverURIs[0], clientId, new MemoryPersistence());
    }

    /**
     * 在连接前，可在这里对连接选项进行加工。
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
     * @param topicPairers 订阅主题
     */
    public void beforeSubscribe(String clientId, Set<TopicPairer> topicPairers) {

    }

}
