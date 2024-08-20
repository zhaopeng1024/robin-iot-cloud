package com.robin.iot.common.mqtt.autoconfigure;

import lombok.Data;

/**
 * 遗嘱配置属性
 *
 * @author zhao peng
 * @date 2024/7/15 23:47
 **/
@Data
public class WillProperties {

    /**
     * 遗嘱主题
     */
    private String topic;

    /**
     * 遗嘱消息
     */
    private String payload;

    /**
     * 遗嘱消息 QoS
     */
    private Integer qos;

    /**
     * 遗嘱消息是否保留
     */
    private Boolean retained;

}
