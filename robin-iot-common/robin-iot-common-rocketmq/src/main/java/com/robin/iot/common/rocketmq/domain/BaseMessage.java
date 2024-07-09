package com.robin.iot.common.rocketmq.domain;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * 消息基类
 *
 * @author zhao peng
 * @date 2024/7/9 21:52
 **/
@Data
public abstract class BaseMessage {

    /**
     * 业务 Key
     */
    protected String key;

    /**
     * 消息源
     */
    protected String source = "";

    /**
     * 消息发送时间
     */
    protected LocalDateTime sendTime = LocalDateTime.now();

    /**
     * 重试次数
     */
    protected Integer retryTimes = 0;

}
