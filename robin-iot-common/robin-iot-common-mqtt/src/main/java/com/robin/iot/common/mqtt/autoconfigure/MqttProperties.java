package com.robin.iot.common.mqtt.autoconfigure;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * MQTT 配置类
 *
 * @author zhao peng
 * @date 2024/7/16 0:02
 **/
@EqualsAndHashCode(callSuper = true)
@Data
@ConfigurationProperties(prefix = "spring.mqtt")
public class MqttProperties extends MqttConnection {

    /**
     * 是否禁用
     */
    private Boolean disable = false;

    /**
     * 客户端配置，key: clientId，value: MqttConnectProperties
     */
    private Map<String, MqttConnection> clients = new LinkedHashMap<>();

}
