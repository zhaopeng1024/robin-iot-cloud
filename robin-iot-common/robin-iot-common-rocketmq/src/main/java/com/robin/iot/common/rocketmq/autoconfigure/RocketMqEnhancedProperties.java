package com.robin.iot.common.rocketmq.autoconfigure;

import com.robin.iot.common.rocketmq.constant.MessageConstants;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * RocketMQ 增强配置类
 *
 * @author zhao peng
 * @date 2024/7/9 22:53
 **/
@Data
@ConfigurationProperties(prefix = "spring.rocketmq.enhance")
public class RocketMqEnhancedProperties {

    /**
     * 是否启用环境隔离
     */
    private boolean enableIsolation;

    /**
     * 环境
     */
    private String environment;

    /**
     * 灰度标识
     */
    private String grayFlag = MessageConstants.DEFAULT_GRAY_FLAG;

}
