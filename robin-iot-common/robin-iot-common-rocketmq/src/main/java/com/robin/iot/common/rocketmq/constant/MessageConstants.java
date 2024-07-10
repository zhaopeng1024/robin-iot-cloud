package com.robin.iot.common.rocketmq.constant;

import org.apache.logging.log4j.util.Strings;

/**
 * 消息常量
 *
 * @author zhao peng
 * @date 2024/7/9 23:35
 **/
public interface MessageConstants {

    /**
     * 重试消息来源标识
     */
    String RETRY_PREFIX = "RETRY_";

    /**
     * 延迟等级
     * 在start版本中，延时消息一共分为18个等级：1s 5s 10s 30s 1m 2m 3m 4m 5m 6m 7m 8m 9m 10m 20m 30m 1h 2h
     */
    int FIVE_SECONDS = 5;

    /**
     * 默认灰度标识
     */
    String DEFAULT_GRAY_FLAG = Strings.EMPTY;

}
