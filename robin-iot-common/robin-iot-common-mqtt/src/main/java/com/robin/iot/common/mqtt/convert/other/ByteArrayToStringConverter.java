package com.robin.iot.common.mqtt.convert.other;

import org.springframework.core.convert.converter.Converter;

/**
 * 字节数组转字符串
 *
 * @author zhao peng
 * @date 2024/7/29 23:57
 **/
public interface ByteArrayToStringConverter extends Converter<byte[], String> {
}
