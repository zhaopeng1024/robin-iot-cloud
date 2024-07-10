package com.robin.iot.common.rocketmq.autoconfigure;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.robin.iot.common.rocketmq.template.RocketMQEnhancedTemplate;
import org.apache.rocketmq.spring.autoconfigure.RocketMQAutoConfiguration;
import org.apache.rocketmq.spring.core.RocketMQTemplate;
import org.apache.rocketmq.spring.support.RocketMQMessageConverter;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.messaging.converter.CompositeMessageConverter;
import org.springframework.messaging.converter.MappingJackson2MessageConverter;
import org.springframework.messaging.converter.MessageConverter;

import java.util.List;

/**
 * RocketMQ 增强的自动配置类
 *
 * @author zhao peng
 * @date 2024/7/9 23:12
 **/
@AutoConfiguration(after = RocketMQAutoConfiguration.class)
@EnableConfigurationProperties(RocketMqEnhancedProperties.class)
public class RocketMqEnhancedAutoConfiguration {

    @Bean
    @ConditionalOnBean(RocketMQTemplate.class)
    @ConditionalOnMissingBean(RocketMQEnhancedTemplate.class)
    public RocketMQEnhancedTemplate rocketMQEnhancedTemplate(RocketMQTemplate rocketMQTemplate) {
        return new RocketMQEnhancedTemplate(rocketMQTemplate);
    }

    /**
     * 解决 RocketMQ Jackson 不支持 Java 8 时间类型序列化的问题
     */
    @Bean
    @Primary
    public RocketMQMessageConverter enhancedRocketMQMessageConverter() {
        RocketMQMessageConverter converter = new RocketMQMessageConverter();
        CompositeMessageConverter compositeMessageConverter = (CompositeMessageConverter) converter.getMessageConverter();
        List<MessageConverter> messageConverters = compositeMessageConverter.getConverters();
        messageConverters.forEach(messageConverter -> {
            if (messageConverter instanceof MappingJackson2MessageConverter) {
                MappingJackson2MessageConverter jackson2MessageConverter = (MappingJackson2MessageConverter) messageConverter;
                ObjectMapper objectMapper = jackson2MessageConverter.getObjectMapper();
                objectMapper.registerModules(new JavaTimeModule());
            }
        });
        return converter;
    }


    @Bean
    @ConditionalOnProperty(name = "spring.rocketmq.enhance.enableIsolation", havingValue = "true")
    public EnvironmentIsolationProcessor environmentIsolationProcessor(RocketMqEnhancedProperties rocketMqEnhancedProperties) {
        return new EnvironmentIsolationProcessor(rocketMqEnhancedProperties);
    }

}
