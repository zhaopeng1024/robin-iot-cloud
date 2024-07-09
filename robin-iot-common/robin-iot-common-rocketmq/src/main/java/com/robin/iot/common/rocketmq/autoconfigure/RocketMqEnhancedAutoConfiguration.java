package com.robin.iot.common.rocketmq.autoconfigure;

import com.robin.iot.common.rocketmq.template.RocketMQEnhancedTemplate;
import org.apache.rocketmq.spring.core.RocketMQTemplate;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * RocketMQ 增强的自动配置类
 *
 * @author zhao peng
 * @date 2024/7/9 23:12
 **/
@Configuration
@EnableConfigurationProperties(RocketMqEnhancedProperties.class)
public class RocketMqEnhancedAutoConfiguration {

    @Bean
    public RocketMQEnhancedTemplate rocketMQEnhancedTemplate(RocketMQTemplate rocketMQTemplate) {
        return new RocketMQEnhancedTemplate(rocketMQTemplate);
    }

    @Bean
    @ConditionalOnProperty(name = "spring.rocketmq.enhance.enableIsolation", havingValue = "true")
    public EnvironmentIsolationProcessor environmentIsolationProcessor(RocketMqEnhancedProperties rocketMqEnhancedProperties) {
        return new EnvironmentIsolationProcessor(rocketMqEnhancedProperties);
    }

}
