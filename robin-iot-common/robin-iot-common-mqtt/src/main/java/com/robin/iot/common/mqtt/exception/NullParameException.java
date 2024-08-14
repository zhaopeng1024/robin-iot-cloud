package com.robin.iot.common.mqtt.exception;

import com.robin.iot.common.mqtt.subscriber.Parameter;

/**
 * 空参数异常
 *
 * @author zhao peng
 * @date 2024/7/30 22:45
 **/
public class NullParameException extends RuntimeException {

    public NullParameException() {
        super("param is null");
    }

    public NullParameException(Parameter parameter) {
        super("param name '" + parameter.getName() + "' type '" + parameter.getType().getName() + "' is required.");
    }

}
