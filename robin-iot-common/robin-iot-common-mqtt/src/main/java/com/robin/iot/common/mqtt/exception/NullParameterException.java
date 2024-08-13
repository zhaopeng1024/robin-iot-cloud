package com.robin.iot.common.mqtt.exception;

import com.robin.iot.common.mqtt.subscriber.ParameterModel;

/**
 * 空参数异常
 *
 * @author zhao peng
 * @date 2024/7/30 22:45
 **/
public class NullParameterException extends RuntimeException {

    public NullParameterException() {
        super("param is null");
    }

    public NullParameterException(ParameterModel parameter) {
        super("param name '" + parameter.getName() + "' type '" + parameter.getType().getName() + "' is required.");
    }

}
