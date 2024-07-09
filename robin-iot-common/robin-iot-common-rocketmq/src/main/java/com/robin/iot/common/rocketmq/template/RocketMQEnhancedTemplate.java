package com.robin.iot.common.rocketmq.template;

import com.alibaba.fastjson.JSONObject;
import com.robin.iot.common.rocketmq.autoconfigure.RocketMqEnhancedProperties;
import com.robin.iot.common.rocketmq.domain.BaseMessage;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.spring.core.RocketMQTemplate;
import org.apache.rocketmq.spring.support.RocketMQHeaders;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;

import javax.annotation.Resource;

/**
 * RocketMQ 增强模板类
 *
 * @author zhao peng
 * @date 2024/7/9 22:42
 **/
@Slf4j
@RequiredArgsConstructor(onConstructor_={@Autowired})
public class RocketMQEnhancedTemplate {

    @Getter
    private final RocketMQTemplate rocketTemplate;

    @Resource
    private RocketMqEnhancedProperties rocketEnhanceProperties;

    public String buildDestination(String topic, String tag) {
        topic = rebuildTopic(topic);
        return topic + ":" + tag;
    }

    private String rebuildTopic(String topic) {
        if (rocketEnhanceProperties.isEnableIsolation() &&
                StringUtils.isNotBlank(rocketEnhanceProperties.getEnvironment())) {
            return topic + "_" + rocketEnhanceProperties.getEnvironment();
        }
        return topic;
    }

    /**
     * 发送同步消息
     * @param topic 主题
     * @param tag 标签
     * @param message 消息
     * @return 发送结果
     * @param <T> 消息泛型
     */
    public <T extends BaseMessage> SendResult sendMessage(String topic, String tag, T message) {
        return sendMessage(buildDestination(topic, tag), message);
    }

    /**
     * 发送同步消息
     * @param destination 目的地
     * @param message 消息
     * @return 发送结果
     * @param <T> 消息泛型
     */
    public <T extends BaseMessage> SendResult sendMessage(String destination, T message) {
        Message<T> mqMessage = MessageBuilder.withPayload(message).setHeader(RocketMQHeaders.KEYS, message.getKey()).build();
        SendResult sendResult = rocketTemplate.syncSend(destination, mqMessage);
        log.info("发送同步消息，destination-[{}], message-[{}], sendResult-[{}]", destination, JSONObject.toJSONString(message), sendResult);
        return sendResult;
    }

    /**
     * 发送延迟消息
     * @param topic 主题
     * @param tag 标签
     * @param message 消息
     * @param delayLevel 延迟等级
     * @return 发送结果
     * @param <T> 消息泛型
     */
    public <T extends BaseMessage> SendResult sendMessage(String topic, String tag, T message, int delayLevel) {
        return sendMessage(buildDestination(topic, tag), message, delayLevel);
    }

    /**
     * 发送延迟消息
     * @param destination 目的地
     * @param message 消息
     * @param delayLevel 延迟等级
     * @return 发送结果
     * @param <T> 消息泛型
     */
    public <T extends BaseMessage> SendResult sendMessage(String destination, T message, int delayLevel) {
        Message<T> mqMessage = MessageBuilder.withPayload(message).setHeader(RocketMQHeaders.KEYS, message.getKey()).build();
        SendResult sendResult = rocketTemplate.syncSend(destination, mqMessage, 3000, delayLevel);
        log.info("发送延迟消息，destination-[{}], delayLevel-[{}], message-[{}], sendResult-[{}]", destination, delayLevel, JSONObject.toJSONString(message), sendResult);
        return sendResult;
    }

}
