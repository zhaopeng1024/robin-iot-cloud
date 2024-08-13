package com.robin.iot.common.mqtt.autoconfigure;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.DisposableBean;

/**
 * Mqtt 客户端管理器
 *
 * @author zhao peng
 * @date 2024/8/12 23:45
 **/
@Slf4j
public class MqttClientManager implements DisposableBean {

    @Override
    public void destroy() throws Exception {

    }

}
