package com.robin.iot.common.mqtt.publisher;

import com.robin.iot.common.mqtt.autoconfigure.MqttConfigurationAdapter;
import com.robin.iot.common.mqtt.subscriber.Subscriber;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.paho.client.mqttv3.IMqttAsyncClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;

import java.util.LinkedList;

/**
 * 简单的 Mqtt 客户端
 *
 * @author zhao peng
 * @date 2024/8/12 23:41
 **/
@Slf4j
public class SimpleMqttClient {

    private String id;
    private IMqttAsyncClient client;
    private MqttConnectOptions options;
    private boolean enableShared;
    private int qos;
    private LinkedList<Subscriber> subscribers;
    private MqttConfigurationAdapter adapter;

    public void connect() {
    }
}
