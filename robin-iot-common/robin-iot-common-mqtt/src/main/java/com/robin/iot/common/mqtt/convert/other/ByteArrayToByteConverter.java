package com.robin.iot.common.mqtt.convert.other;

import org.springframework.core.convert.converter.Converter;

/**
 * 字节数组转字节
 *
 * @author zhao peng
 * @date 2024/7/29 23:52
 **/
public interface ByteArrayToByteConverter extends Converter<byte[], Byte> {
}
