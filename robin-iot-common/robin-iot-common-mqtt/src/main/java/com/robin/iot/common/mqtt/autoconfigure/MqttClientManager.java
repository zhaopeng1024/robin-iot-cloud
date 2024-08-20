package com.robin.iot.common.mqtt.autoconfigure;

import com.robin.iot.common.mqtt.convert.MqttConversionService;
import com.robin.iot.common.mqtt.publisher.SimpleMqttClient;
import com.robin.iot.common.mqtt.subscriber.Subscriber;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.paho.client.mqttv3.IMqttAsyncClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

import java.util.LinkedHashMap;
import java.util.LinkedList;

/**
 * Mqtt 客户端管理器
 *
 * @author zhao peng
 * @date 2024/8/12 23:45
 **/
@Slf4j
public class MqttClientManager implements DisposableBean {

    private final LinkedHashMap<String, SimpleMqttClient> clients = new LinkedHashMap<>();
    private final LinkedList<Subscriber> subscribers;
    private final MqttProperties mqttProperties;
    private final MqttClientAdapter mqttClientAdapter;

    private String defaultClientId = null;

    public MqttClientManager(LinkedList<Subscriber> subscribers, MqttProperties mqttProperties, MqttClientAdapter mqttClientAdapter) {
        this.subscribers = subscribers;
        this.mqttProperties = mqttProperties;
        this.mqttClientAdapter = mqttClientAdapter;
        this.mqttClientAdapter.setMqttProperties(mqttProperties);
    }

    public SimpleMqttClient open(MqttConnectionProperties mqttConnectionProperties) {
        String clientId = mqttConnectionProperties.getClientId();
        Assert.hasText(clientId, "ClientId cannot be blank");
        Assert.notEmpty(mqttProperties.getUris(), "uris cannot be empty");
        Assert.hasText(mqttProperties.getUris()[0], "uri cannot be blank");
        if (clients.containsKey(clientId)) {
            close(clientId);
        }
        this.mqttProperties.merge(mqttConnectionProperties);
        MqttConnectOptions options = this.mqttProperties.toOptions(mqttConnectionProperties);
        return open(clientId, options, null);
    }

    void open(String clientId, MqttConnectOptions options) {
        open(clientId, options, null);
    }

    public SimpleMqttClient open(String clientId, MqttConnectOptions options, Integer defaultPublishQos) {
        Assert.hasText(clientId, "ClientId cannot be blank");
        if (clients.containsKey(clientId)) {
            close(clientId);
        }
        IMqttAsyncClient client;
        try {
            client = mqttClientAdapter.postCreate(clientId, options.getServerURIs());
        } catch (MqttException e) {
            log.error("Client {} create error", clientId, e);
            throw new RuntimeException(e);
        }
        boolean enableSharedSubscription = this.mqttProperties.isEnableSharedSubscription(clientId);
        int qos = defaultPublishQos != null ? defaultPublishQos : this.mqttProperties.getDefaultPublishQos();
        SimpleMqttClient simpleMqttClient = new SimpleMqttClient(clientId, client, options,
                enableSharedSubscription, qos, subscribers, mqttClientAdapter);
        clients.put(clientId, simpleMqttClient);
        return simpleMqttClient;
    }

    public void close(String clientId) {
        if (clients.containsKey(clientId)) {
            if (defaultClientId != null && defaultClientId.equals(clientId)) {
                String oldDefaultClientId = defaultClientId;
                String newDefaultClientId = null;
                for (SimpleMqttClient value : clients.values()) {
                    if (!value.id().equals(clientId)) {
                        newDefaultClientId = value.id();
                        break;
                    }
                }
                if (newDefaultClientId != null) {
                    defaultClientId = newDefaultClientId;
                    log.warn("Default clientId changed from {} to {}", oldDefaultClientId, newDefaultClientId);
                } else {
                    log.warn("Default clientId {} is closed, no other clientId is available", oldDefaultClientId);
                }
            }
            SimpleMqttClient client = clients.remove(clientId);
            client.close();
        }
    }

    public SimpleMqttClient getClientOrDefault(String clientId) {
        if (StringUtils.hasText(clientId) && clients.containsKey(clientId)) {
            return clients.get(clientId);
        }
        return clients.get(defaultClientId);
    }

    public boolean setDefaultClientId(String clientId) {
        if (StringUtils.hasText(clientId) && clients.containsKey(clientId)) {
            defaultClientId = clientId;
            return true;
        }
        return false;
    }

    void afterInit() {
        clients.forEach((id, client) -> {
            try {
                if (defaultClientId == null) {
                    defaultClientId = id;
                }
                client.connect();
            } catch (Exception e) {
                log.error("Client {} connect error", id, e);
            }
        });
    }

    @Override
    public void destroy() {
        log.warn("Closing all mqtt client");
        clients.forEach((id, client) -> {
            try {
                client.close();
            } catch (Exception e) {
                log.error("Client {} close error", id, e);
            }
        });
        clients.clear();
        subscribers.clear();
        MqttConversionService.destroy();
    }

}
