package com.robin.iot.common.mqtt.publisher;

import com.robin.iot.common.mqtt.autoconfigure.MqttClientManager;
import org.eclipse.paho.client.mqttv3.IMqttActionListener;

/**
 * 发布者
 *
 * @author zhao peng
 * @date 2024/8/12 23:40
 **/
@SuppressWarnings("unused")
public class Publisher {

    private final MqttClientManager mqttClientManager;

    public Publisher(MqttClientManager mqttClientManager) {
        this.mqttClientManager = mqttClientManager;
    }

    public SimpleMqttClient client() {
        return mqttClientManager.getClientOrDefault(null);
    }

    public SimpleMqttClient client(String clientId) {
        return mqttClientManager.getClientOrDefault(clientId);
    }

    public void send(String topic, Object payload) {
        client().send(topic, payload);
    }

    public void send(String topic, Object payload, boolean retained) {
        client().send(topic, payload, retained);
    }

    public void send(String topic, Object payload, IMqttActionListener callback) {
        client().send(topic, payload, callback);
    }

    public void send(String topic, Object payload, boolean retained, IMqttActionListener callback) {
        client().send(topic, payload, retained, callback);
    }

    public void send(String topic, Object payload, int qos) {
        client().send(topic, payload, qos);
    }

    public void send(String topic, Object payload, int qos, boolean retained) {
        client().send(topic, payload, qos, retained);
    }

    public void send(String topic, Object payload, int qos, IMqttActionListener callback) {
        client().send(topic, payload, qos, callback);
    }

    public void send(String topic, Object payload, int qos, boolean retained, IMqttActionListener callback) {
        client().send(topic, payload, qos, retained, callback);
    }

}
