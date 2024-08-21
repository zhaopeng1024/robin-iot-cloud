package com.robin.iot.common.mqtt.autoconfigure;

import com.robin.iot.common.mqtt.convert.MqttConversionService;
import com.robin.iot.common.mqtt.publisher.Publisher;
import com.robin.iot.common.mqtt.subscriber.Subscriber;
import org.eclipse.paho.client.mqttv3.MqttAsyncClient;
import org.springframework.beans.factory.ListableBeanFactory;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.core.annotation.Order;

import java.util.LinkedList;

/**
 * MQTT 自动配置类
 *
 * @author zhao peng
 * @date 2024/8/21 19:55
 **/
@Order
@ConditionalOnClass(MqttAsyncClient.class)
@ConditionalOnProperty(prefix = "spring.mqtt", name = "disable", havingValue = "false", matchIfMissing = true)
@AutoConfiguration(after = JacksonPayloadAutoConfiguration.class)
@EnableConfigurationProperties(MqttProperties.class)
public class MqttAutoConfiguration {

    private final ConfigurableBeanFactory factory;

    public MqttAutoConfiguration(ListableBeanFactory beanFactory, ConfigurableBeanFactory factory) {
        // 注册 converters
        MqttConversionService.addBeans(beanFactory);
        this.factory = factory;
    }

    @Bean
    @ConditionalOnMissingBean(MqttClientAdapter.class)
    public MqttClientAdapter mqttClientAdapter() {
        return MqttClientAdapter.defaultAdapter();
    }

    @Order
    @Bean
    @ConditionalOnMissingBean(MqttClientRegister.class)
    public MqttClientManager mqttClientManager(MqttSubscriberProcessor processor, MqttProperties properties, MqttClientAdapter adapter) {
        LinkedList<Subscriber> subscribers = processor.getSubscribers();
        adapter.beforeResolveEmbeddedValue(subscribers);
        subscribers.forEach(subscriber -> subscriber.resolveEmbeddedValue(factory));
        adapter.afterResolveEmbeddedValue(subscribers);
        MqttClientManager manager = new MqttClientManager(subscribers, properties, adapter);
        // 添加客户端
        properties.foreach(manager::open);
        // 建立连接
        manager.afterInit();
        return manager;
    }

    @Bean
    @Order
    @ConditionalOnMissingBean(Publisher.class)
    public Publisher publisher(MqttClientManager mqttClientManager) {
        return new Publisher(mqttClientManager);
    }



}
