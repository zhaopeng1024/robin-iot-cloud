package com.robin.iot.common.mqtt.convert.other;

import org.springframework.core.convert.converter.Converter;

/**
 * 字节数组转布尔值
 *
 * @author zhao peng
 * @date 2024/7/29 23:51
 **/
public interface ByteArrayToBooleanConverter extends Converter<byte[], Boolean> {
}
