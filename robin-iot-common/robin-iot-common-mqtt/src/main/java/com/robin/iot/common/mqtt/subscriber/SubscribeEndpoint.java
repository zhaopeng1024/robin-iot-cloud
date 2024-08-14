package com.robin.iot.common.mqtt.subscriber;

import com.robin.iot.common.mqtt.annotation.Subscriber;

/**
 * 订阅端点
 *
 * @author zhao peng
 * @date 2024/7/25 21:50
 **/
public record SubscribeEndpoint(String[] value, int[] qos, String[] clients, String[] groups) {

    public SubscribeEndpoint(String[] value, int[] qos, String[] clients, String[] groups) {
        this.value = value == null ? new String[0] : value;
        this.qos = qos == null ? new int[0] : qos;
        this.clients = clients == null ? new String[0] : clients;
        this.groups = groups == null ? new String[0] : groups;
    }

    public static SubscribeEndpoint of(Subscriber subscriber) {
        return new SubscribeEndpoint(subscriber.value(), subscriber.qos(), subscriber.clients(), subscriber.groups());
    }

}
