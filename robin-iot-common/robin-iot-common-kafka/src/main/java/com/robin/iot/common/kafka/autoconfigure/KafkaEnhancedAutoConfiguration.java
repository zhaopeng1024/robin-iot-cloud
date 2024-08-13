package com.robin.iot.common.kafka.autoconfigure;

import com.robin.iot.common.kafka.datasource.KafkaDataSourceRegister;
import com.robin.iot.common.kafka.support.MessageHandler;
import com.robin.iot.common.kafka.support.MessageProducerListener;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.beans.factory.config.SmartInstantiationAwareBeanPostProcessor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.kafka.annotation.EnableKafka;

/**
 * kafka 自动配置类
 */
@EnableKafka
@Configuration(proxyBeanMethods = false)
@EnableConfigurationProperties(KafkaEnhancedProperties.class)
@ConditionalOnWebApplication
@Import({KafkaDataSourceRegister.class, MessageProducerListener.class, MessageHandler.class})
public class KafkaEnhancedAutoConfiguration implements BeanFactoryAware, SmartInstantiationAwareBeanPostProcessor {

    @Override
    public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
        beanFactory.getBean(KafkaDataSourceRegister.class);
    }

}
