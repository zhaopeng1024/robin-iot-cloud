package com.robin.iot.common.mqtt.subscriber;

import com.robin.iot.common.mqtt.annotation.Subscriber;

/**
 * 订阅者模型
 *
 * @author zhao peng
 * @date 2024/7/25 21:50
 **/
public record SubscriberModel(String[] value, int[] qos, String[] clients, String[] groups) {

    public SubscriberModel(String[] value, int[] qos, String[] clients, String[] groups) {
        this.value = value == null ? new String[0] : value;
        this.qos = qos == null ? new int[0] : qos;
        this.clients = clients == null ? new String[0] : clients;
        this.groups = groups == null ? new String[0] : groups;
    }

    public static SubscriberModel of(Subscriber subscriber) {
        return new SubscriberModel(subscriber.value(), subscriber.qos(), subscriber.clients(), subscriber.groups());
    }

}
