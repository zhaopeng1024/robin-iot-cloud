package com.robin.iot.common.kafka.support;

import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.springframework.kafka.support.ProducerListener;

/**
 * 消息生产者监听器
 */
@Slf4j
public class MessageProducerListener<K, V> implements ProducerListener<K, V> {

    /**
     * 发送成功回调
     * @param producerRecord 记录
     * @param recordMetadata 元数据
     */
    @Override
    public void onSuccess(ProducerRecord<K, V> producerRecord, RecordMetadata recordMetadata) {
        log.info("消息发送成功: topic: {}, partition: {}, offset: {}", recordMetadata.topic(), recordMetadata.partition(), recordMetadata.offset());
    }

    /**
     * 发送失败回调
     * @param producerRecord 记录
     * @param recordMetadata 元数据
     * @param exception 异常
     */
    @Override
    public void onError(ProducerRecord<K, V> producerRecord, RecordMetadata recordMetadata, Exception exception) {
        assert recordMetadata != null;
        log.error("消息发送失败: topic: {}, partition: {}, offset: {}", recordMetadata.topic(), recordMetadata.partition(), recordMetadata.offset(), exception);
    }
}
