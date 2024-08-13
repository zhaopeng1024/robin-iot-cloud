package com.robin.iot.common.mqtt.convert;

import org.springframework.core.convert.converter.Converter;

/**
 * 对象反序列化
 *
 * @author zhao peng
 * @date 2024/7/30 0:00
 **/
public interface PayloadDeserialize extends Converter<byte[], Object> {
}
