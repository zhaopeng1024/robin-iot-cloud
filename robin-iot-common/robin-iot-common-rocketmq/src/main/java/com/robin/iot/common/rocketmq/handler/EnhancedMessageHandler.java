package com.robin.iot.common.rocketmq.handler;

import com.alibaba.fastjson.JSONObject;
import com.robin.iot.common.rocketmq.constant.MessageConstants;
import com.robin.iot.common.rocketmq.domain.BaseMessage;
import com.robin.iot.common.rocketmq.template.RocketMQEnhancedTemplate;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.client.producer.SendStatus;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;

import javax.annotation.Resource;
import java.time.Instant;

/**
 * 增强的消息处理器
 *
 * @author zhao peng
 * @date 2024/7/9 23:17
 **/
@Slf4j
public abstract class EnhancedMessageHandler<T extends BaseMessage> {

    /**
     * 默认重试次数
     */
    private static final int MAX_RETRY_TIMES = 3;

    /**
     * 默认延迟等级
     */
    private static final int DELAY_LEVEL = MessageConstants.FIVE_SECONDS;

    @Resource
    private RocketMQEnhancedTemplate rocketMQEnhancedTemplate;

    /**
     * 处理消息
     * @param message 待处理消息
     * @throws Exception 消费异常
     */
    protected abstract void handleMessage(T message) throws Exception;

    /**
     * 处理重试超次的消息
     * @param message 待处理消息
     */
    protected abstract void handleOverMaxRetryTimesMessage(T message);

    /**
     * 是否需要根据业务规则过滤消息，去重逻辑可以在这里处理
     * @param message 待处理消息
     * @return true - 过滤；false - 保留
     */
    protected boolean filter(T message) {
        return false;
    }

    /**
     * 是否需要重试
     * @return true - 是；false - 否
     */
    protected abstract boolean isRetry();

    /**
     * 消费异常时是否直接抛出异常
     * @return true-抛出异常，由RocketMQ机制自动重试；false-不抛出异常，如果没有开启重试则消息会被自动ACK
     */
    protected abstract boolean isThrow();

    /**
     * 返回最大重试次数
     * @return 重试次数
     */
    protected int getMaxRetryTimes() {
        return MAX_RETRY_TIMES;
    }

    /**
     * 返回延迟等级
     * @return 延迟等级，-1：立即入队重试
     */
    protected int getDelayLevel() {
        return DELAY_LEVEL;
    }

    /**
     * 模板模式构建消息消费处理框架，可自由加工
     * @param message 待处理消息
     */
    public void dispatchMessage(T message) {
        log.info("received message:{}", JSONObject.toJSONString(message));
        if (filter(message)) {
            log.warn("message has been filtered, key:[{}]", message.getKey());
            return;
        }
        if (message.getRetryTimes() > getMaxRetryTimes()) {
            handleOverMaxRetryTimesMessage(message);
            return;
        }
        try {
            long now = Instant.now().toEpochMilli();
            handleMessage(message);
            long cost = Instant.now().toEpochMilli() - now;
            log.info("message processing success. key:[{}], cost:[{}]", message.getKey(), cost);
        } catch (Exception e) {
            log.error("message processing exception. key:[{}], exception:[{}]", message.getKey(), e.getMessage());
            if (isThrow()) {
                throw new RuntimeException(e);
            }
            if (isRetry()) {
                retry(message);
            }
        }
    }

    /**
     * 重试
     * @param message 消息
     */
    protected void retry(T message) {
        RocketMQMessageListener listener = this.getClass().getAnnotation(RocketMQMessageListener.class);
        if (listener == null) {
            return;
        }
        String source = message.getSource();
        if (!source.startsWith(MessageConstants.RETRY_PREFIX)) {
            message.setSource(MessageConstants.RETRY_PREFIX + source);
        }
        message.setRetryTimes(message.getRetryTimes() + 1);
        SendResult sendResult;
        try {
            // 如果消息发送不成功，则再次重新发送，如果发送异常则抛出由 MQ 再次处理（异常时不走延迟消息）
            sendResult = rocketMQEnhancedTemplate.sendMessage(listener.topic(), listener.selectorExpression(), message, getDelayLevel());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        if (sendResult.getSendStatus() != SendStatus.SEND_OK) {
            throw new RuntimeException("retry message failed to send.");
        }
    }

}
