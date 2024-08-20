package com.robin.iot.common.mqtt.convert;

import org.springframework.core.convert.converter.Converter;

/**
 * 对象序列化
 *
 * @author zhao peng
 * @date 2024/7/29 23:59
 **/
public interface PayloadSerializer extends Converter<Object, byte[]> {
}
