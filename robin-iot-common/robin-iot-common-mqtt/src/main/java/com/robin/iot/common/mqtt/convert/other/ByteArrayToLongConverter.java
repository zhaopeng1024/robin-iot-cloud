package com.robin.iot.common.mqtt.convert.other;

import org.springframework.core.convert.converter.Converter;

/**
 * 字节数组转Long
 *
 * @author zhao peng
 * @date 2024/7/29 23:56
 **/
public interface ByteArrayToLongConverter extends Converter<byte[], Long> {
}
