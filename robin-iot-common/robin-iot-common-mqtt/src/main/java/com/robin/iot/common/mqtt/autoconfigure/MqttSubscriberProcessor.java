package com.robin.iot.common.mqtt.autoconfigure;

import com.robin.iot.common.mqtt.subscriber.SubscribeEndpoint;
import com.robin.iot.common.mqtt.subscriber.Subscriber;
import lombok.Getter;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.util.LinkedList;

/**
 * MQTT 订阅者初始化之前的处理器
 *
 * @author zhao peng
 * @date 2024/8/21 23:38
 **/
@Getter
@Component
@ConditionalOnProperty(prefix = "mqtt", name = "disable", havingValue = "false", matchIfMissing = true)
public class MqttSubscriberProcessor implements BeanPostProcessor {

    private final LinkedList<Subscriber> subscribers = new LinkedList<>();

    public void add(Subscriber subscriber) {
        subscribers.add(subscriber);
    }

    @Override
    public Object postProcessBeforeInitialization(Object bean, @Nullable String beanName) throws BeansException {
        Method[] methods = bean.getClass().getMethods();
        for (Method method : methods) {
            if (method.isAnnotationPresent(com.robin.iot.common.mqtt.annotation.Subscriber.class)) {
                SubscribeEndpoint subscribeEndpoint = SubscribeEndpoint.of(method.getAnnotation(com.robin.iot.common.mqtt.annotation.Subscriber.class));
                add(Subscriber.of(subscribeEndpoint, bean, method));
            }
        }
        return bean;
    }
}
