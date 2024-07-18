package com.robin.iot.common.kafka.autoconfigure;

import com.robin.iot.common.kafka.model.Consumer;
import com.robin.iot.common.kafka.model.Producer;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.NestedConfigurationProperty;

import java.util.List;
import java.util.Map;

@Data
@ConfigurationProperties("spring.kafka.multiple")
public class KafkaProperties {

    /**
     * 主数据源
     */
    private String primary;

    /**
     * 服务器列表
     */
    private List<String> bootstrapServers;

    /**
     * 消费者
     */
    @NestedConfigurationProperty
    private Consumer consumer;

    /**
     * 生产者
     */
    @NestedConfigurationProperty
    private Producer producer;

    /**
     * 数据源
     */
    private Map<String, KafkaProperties> dataSources;

}
