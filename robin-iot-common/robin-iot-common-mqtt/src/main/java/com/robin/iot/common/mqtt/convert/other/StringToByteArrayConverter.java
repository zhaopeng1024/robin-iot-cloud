package com.robin.iot.common.mqtt.convert.other;

import org.springframework.core.convert.converter.Converter;

/**
 * 字符串转字节数组
 *
 * @author zhao peng
 * @date 2024/7/29 23:49
 **/
public interface StringToByteArrayConverter extends Converter<String, byte[]> {
}
