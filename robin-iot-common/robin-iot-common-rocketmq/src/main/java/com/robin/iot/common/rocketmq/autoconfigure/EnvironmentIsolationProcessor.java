package com.robin.iot.common.rocketmq.autoconfigure;

import org.apache.rocketmq.spring.support.DefaultRocketMQListenerContainer;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.util.StringUtils;

/**
 * 环境隔离处理器
 *
 * @author zhao peng
 * @date 2024/7/9 22:59
 **/
public class EnvironmentIsolationProcessor implements BeanPostProcessor {

    private final RocketMqEnhancedProperties rocketEnhancedProperties;

    public EnvironmentIsolationProcessor(RocketMqEnhancedProperties rocketEnhancedProperties) {
        this.rocketEnhancedProperties = rocketEnhancedProperties;
    }

    /**
     * 在监听器实例初始化前修改对应的 topic
     * @param bean the new bean instance
     * @param beanName the name of the bean
     * @return the bean instance to use, either the original or a wrapped one
     * @throws BeansException Bean 异常
     */
    @Override
    public Object postProcessBeforeInitialization(@SuppressWarnings("NullableProblems") Object bean, @SuppressWarnings("NullableProblems") String beanName) throws BeansException {
        if (bean instanceof DefaultRocketMQListenerContainer) {
            DefaultRocketMQListenerContainer container = (DefaultRocketMQListenerContainer) bean;
            if (rocketEnhancedProperties.isEnableIsolation() && StringUtils.hasText(rocketEnhancedProperties.getEnvironment())) {
                String topicSuffix = rocketEnhancedProperties.getEnvironment();
                if (StringUtils.hasText(rocketEnhancedProperties.getGrayFlag())) {
                    topicSuffix = String.join("_", topicSuffix, rocketEnhancedProperties.getGrayFlag());
                }
                container.setTopic(String.join("_", container.getTopic(), topicSuffix));
            }
            return container;
        }
        return bean;
    }

}
