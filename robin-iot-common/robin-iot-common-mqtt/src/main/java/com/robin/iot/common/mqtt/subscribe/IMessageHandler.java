package com.robin.iot.common.mqtt.subscribe;

/**
 * 消息处理函数式接口
 *
 * @author zhao peng
 * @date 2024/7/16 0:21
 **/
@FunctionalInterface
public interface IMessageHandler {

    void receive(Object[] parameters) throws Exception;

}
