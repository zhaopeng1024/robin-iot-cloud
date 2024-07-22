package com.robin.iot.common.kafka.support;

import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.ProducerListener;
import org.springframework.util.Assert;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

import java.util.concurrent.ExecutionException;

/**
 * 消息处理器
 */
public class MessageHandler<K, V> {

    /**
     * 同步发送消息
     * @param kafkaTemplate kafka 模板
     * @param topic 主题
     * @param message 消息
     * @param producerListener 监听器
     * @return true - 发送成功
     */
    public boolean syncSend(KafkaTemplate<K, V> kafkaTemplate, String topic, V message, ProducerListener<K, V> producerListener) {
        Assert.isTrue(StringUtils.hasText(topic), "topic must not be blank.");
        Assert.isTrue(!ObjectUtils.isEmpty(message), "message must not be empty.");
        try {
            if (producerListener != null) {
                kafkaTemplate.setProducerListener(producerListener);
            }
            kafkaTemplate.send(topic, message).get();
        } catch (InterruptedException | ExecutionException e) {
            Thread.currentThread().interrupt();
            throw new RuntimeException("send message failed.", e);
        }
        return true;
    }

    /**
     * 同步发送消息
     * @param kafkaTemplate kafka 模板
     * @param topic 主题
     * @param message 消息
     * @return true - 发送成功
     */
    public boolean syncSend(KafkaTemplate<K, V> kafkaTemplate, String topic, V message) {
        return syncSend(kafkaTemplate, topic, message, null);
    }

    /**
     * 异步发送消息
     * @param kafkaTemplate kafka 模板
     * @param topic 主题
     * @param message 消息
     * @param producerListener 监听器
     */
    public void asyncSend(KafkaTemplate<K, V> kafkaTemplate, String topic, V message, ProducerListener<K, V> producerListener) {
        Assert.isTrue(StringUtils.hasText(topic), "topic must not be blank.");
        Assert.isTrue(!ObjectUtils.isEmpty(message), "message must not be empty.");
        if (producerListener != null) {
            kafkaTemplate.setProducerListener(producerListener);
        }
        kafkaTemplate.send(topic, message);
    }

    /**
     * 异步发送消息
     * @param kafkaTemplate kafka 模板
     * @param topic 主题
     * @param message 消息
     */
    public void asyncSend(KafkaTemplate<K, V> kafkaTemplate, String topic, V message) {
        asyncSend(kafkaTemplate, topic, message, null);
    }

}
