package com.robin.iot.common.rocketmq.autoconfigure;

import org.apache.commons.lang3.StringUtils;
import org.apache.rocketmq.spring.support.DefaultRocketMQListenerContainer;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;

/**
 * 环境隔离处理器
 *
 * @author zhao peng
 * @date 2024/7/9 22:59
 **/
public class EnvironmentIsolationProcessor implements BeanPostProcessor {

    private final RocketMqEnhancedProperties rocketMqEnhancedProperties;

    public EnvironmentIsolationProcessor(RocketMqEnhancedProperties rocketMqEnhancedProperties) {
        this.rocketMqEnhancedProperties = rocketMqEnhancedProperties;
    }

    @Override
    public Object postProcessBeforeInitialization(@SuppressWarnings("NullableProblems") Object bean, @SuppressWarnings("NullableProblems") String beanName) throws BeansException {
        if (bean instanceof DefaultRocketMQListenerContainer) {
            DefaultRocketMQListenerContainer container = (DefaultRocketMQListenerContainer) bean;
            if (rocketMqEnhancedProperties.isEnableIsolation() && StringUtils.isNotEmpty(rocketMqEnhancedProperties.getEnvironment())) {
                container.setTopic(String.join("_", container.getTopic(), rocketMqEnhancedProperties.getEnvironment()));
            }
            return container;
        }
        return bean;
    }

}
