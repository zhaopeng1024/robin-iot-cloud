package com.robin.iot.common.kafka.datasource;

import com.robin.iot.common.kafka.autoconfigure.KafkaEnhancedProperties;
import com.robin.iot.common.kafka.model.Consumer;
import com.robin.iot.common.kafka.model.Producer;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.AbstractBeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.listener.ContainerProperties;
import org.springframework.util.Assert;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * Kafka 数据源注册器
 */
@Slf4j
public class KafkaDataSourceRegister implements InitializingBean {

    @Resource
    private KafkaEnhancedProperties kafkaProperties;

    @Resource
    private DefaultListableBeanFactory beanFactory;

    /**
     * 生产者配置
     * @param producer 生产者
     * @return 生产者配置
     */
    private Map<String, Object> producerConfig(Producer producer) {
        Map<String, Object> producerConfig = new HashMap<>(16);
        producerConfig.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, producer.getBootstrapServers());
        producerConfig.put(ProducerConfig.LINGER_MS_CONFIG, producer.getLingerMs());
        producerConfig.put(ProducerConfig.MAX_REQUEST_SIZE_CONFIG, producer.getMaxRequestSize());
        producerConfig.put(ProducerConfig.BATCH_SIZE_CONFIG, producer.getBatchSize());
        producerConfig.put(ProducerConfig.BUFFER_MEMORY_CONFIG, producer.getBufferMemory());
        producerConfig.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        producerConfig.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        return producerConfig;
    }

    /**
     * 消费者配置
     *
     * @return 配置信息
     */
    private Map<String, Object> consumerConfig(Consumer consumer) {
        Map<String, Object> consumerConfig = new HashMap<>(16);
        consumerConfig.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, consumer.getBootstrapServers());
        consumerConfig.put(ConsumerConfig.GROUP_ID_CONFIG, consumer.getGroupId());
        consumerConfig.put(ConsumerConfig.HEARTBEAT_INTERVAL_MS_CONFIG, consumer.getHeartbeatInterval());
        consumerConfig.put(ConsumerConfig.FETCH_MIN_BYTES_CONFIG, consumer.getFetchMinSize());
        consumerConfig.put(ConsumerConfig.FETCH_MAX_WAIT_MS_CONFIG, consumer.getFetchMaxWait());
        consumerConfig.put(ConsumerConfig.MAX_POLL_RECORDS_CONFIG, consumer.getMaxPollRecords());
        consumerConfig.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, consumer.getEnableAutoCommit());
        consumerConfig.put(ConsumerConfig.SESSION_TIMEOUT_MS_CONFIG, consumer.getSessionTimeoutMs());
        consumerConfig.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, consumer.getAutoOffsetReset());
        consumerConfig.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        consumerConfig.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        return consumerConfig;
    }

    private KafkaEnhancedProperties getDataSources(KafkaEnhancedProperties dataSource) {
        KafkaEnhancedProperties kafkaProperties = new KafkaEnhancedProperties();
        BeanUtils.copyProperties(this.kafkaProperties, kafkaProperties);
        if (Objects.isNull(dataSource)) {
            return kafkaProperties;
        }
        Assert.notNull(dataSource.getBootstrapServers(), "kafka bootstrap servers must not be null.");
        if (Objects.nonNull(dataSource.getConsumer())) {
            dataSource.getConsumer().setBootstrapServers(dataSource.getBootstrapServers());
        }
        if (Objects.nonNull(dataSource.getProducer())) {
            dataSource.getProducer().setBootstrapServers(dataSource.getBootstrapServers());
        }
        BeanUtils.copyProperties(dataSource.getProducer(), kafkaProperties.getProducer());
        BeanUtils.copyProperties(dataSource.getConsumer(), kafkaProperties.getConsumer());
        return kafkaProperties;
    }

    private void registerContainerFactory(String dataSourceName, KafkaEnhancedProperties dataSource) {
        if (ObjectUtils.isEmpty(dataSource.getConsumer())) {
            return;
        }
        Assert.isTrue(StringUtils.hasText(dataSource.getConsumer().getContainerFactory()), "concurrent kafka listener container must not be null.");
        DefaultKafkaConsumerFactory<Object, Object> consumerFactory = new DefaultKafkaConsumerFactory<>(consumerConfig(dataSource.getConsumer()));
        ConcurrentKafkaListenerContainerFactory<String, String> factory = new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(consumerFactory);
        factory.getContainerProperties().setPollTimeout(1500);
        if (!dataSource.getConsumer().getEnableAutoCommit()) {
            factory.getContainerProperties().setAckMode(ContainerProperties.AckMode.MANUAL);
        }
        AbstractBeanDefinition beanDefinition = BeanDefinitionBuilder.rootBeanDefinition(DefaultKafkaConsumerFactory.class)
                .setScope(BeanDefinition.SCOPE_SINGLETON)
                .getBeanDefinition();
        if (Objects.equals(kafkaProperties.getPrimary(), dataSourceName)) {
            beanDefinition.setPrimary(true);
        }
        if (ObjectUtils.isEmpty(beanFactory.getSingleton(dataSource.getConsumer().getContainerFactory()))) {
            beanFactory.registerSingleton(dataSource.getConsumer().getContainerFactory(), factory);
            log.info("register a kafka listener container factory named [{}] success.", dataSource.getConsumer().getContainerFactory());
        }
    }

    private void registerKafkaTemplate(String dataSourceName, KafkaEnhancedProperties dataSource) {
        if (ObjectUtils.isEmpty(dataSource.getProducer())) {
            return;
        }
        Assert.isTrue(StringUtils.hasText(dataSource.getProducer().getKafkaTemplate()), "kafka template must not be null.");
        DefaultKafkaProducerFactory<Object, Object> producerFactory = new DefaultKafkaProducerFactory<>(producerConfig(dataSource.getProducer()));
        KafkaTemplate<Object, Object> kafkaTemplate = new KafkaTemplate<>(producerFactory);
        AbstractBeanDefinition beanDefinition = BeanDefinitionBuilder.rootBeanDefinition(DefaultKafkaProducerFactory.class)
                .setScope(BeanDefinition.SCOPE_PROTOTYPE)
                .getBeanDefinition();
        if (Objects.equals(kafkaProperties.getPrimary(), dataSourceName)) {
            beanDefinition.setPrimary(true);
        }
        if (ObjectUtils.isEmpty(beanFactory.getSingleton(dataSource.getProducer().getKafkaTemplate()))) {
            beanFactory.registerSingleton(dataSource.getProducer().getKafkaTemplate(), kafkaTemplate);
            log.info("register a kafka template named [{}] success.", dataSource.getProducer().getKafkaTemplate());
        }
    }

    @Override
    public void afterPropertiesSet() {
        if (ObjectUtils.isEmpty(kafkaProperties) || ObjectUtils.isEmpty(kafkaProperties.getDataSources())) {
            return;
        }
        Assert.isTrue(StringUtils.hasText(kafkaProperties.getPrimary()), "kafka primary must not be null.");
        kafkaProperties.getDataSources().forEach((dataSourceName, dataSource) -> {
            dataSource = getDataSources(dataSource);
            if (ObjectUtils.isEmpty(dataSource)) {
                return;
            }
            registerKafkaTemplate(dataSourceName, dataSource);
            registerContainerFactory(dataSourceName, dataSource);
        });
        log.info("kafka initial loaded [{}] datasource, primary datasource named [{}]", kafkaProperties.getDataSources().size(), kafkaProperties.getPrimary());
    }

}
