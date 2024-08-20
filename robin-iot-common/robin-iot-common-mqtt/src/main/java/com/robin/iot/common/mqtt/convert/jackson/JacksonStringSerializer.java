package com.robin.iot.common.mqtt.convert.jackson;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.convert.converter.Converter;
import org.springframework.lang.NonNull;

/**
 * 对象转字符串序列化器
 *
 * @author zhao peng
 * @date 2024/8/20 23:15
 **/
@Slf4j
public class JacksonStringSerializer implements Converter<Object, String> {

    private final ObjectMapper objectMapper;

    public JacksonStringSerializer(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @Override
    public String convert(@NonNull Object source) {
        try {
            if (source instanceof String) {
                return (String) source;
            }
            return objectMapper.writeValueAsString(source);
        } catch (com.fasterxml.jackson.core.JsonProcessingException e) {
            log.warn("Payload serialize error: {}", e.getMessage(), e);
        }
        return null;
    }

}
