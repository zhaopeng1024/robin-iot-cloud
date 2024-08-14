package com.robin.iot.common.mqtt.subscriber;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 通配的主题
 *
 * @author zhao peng
 * @date 2024/7/18 22:52
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
public class WildcardTopic {

    /**
     * 主题名
     */
    private String name;

    /**
     * 正则匹配的参数位置
     */
    private int at;

}
