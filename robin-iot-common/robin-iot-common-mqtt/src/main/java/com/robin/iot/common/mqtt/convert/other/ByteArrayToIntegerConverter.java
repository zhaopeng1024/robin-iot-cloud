package com.robin.iot.common.mqtt.convert.other;

import org.springframework.core.convert.converter.Converter;

/**
 * 字节数组转整数
 *
 * @author zhao peng
 * @date 2024/7/29 23:55
 **/
public interface ByteArrayToIntegerConverter extends Converter<byte[], Integer> {
}
