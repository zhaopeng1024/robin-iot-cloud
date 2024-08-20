package com.robin.iot.common.mqtt.convert.jackson;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.robin.iot.common.mqtt.convert.PayloadDeserializer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.convert.converter.Converter;
import org.springframework.lang.NonNull;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

/**
 * 默认的 json 字符串转对象序反列化器
 *
 * @author zhao peng
 * @date 2024/8/20 23:32
 **/
@Slf4j
public class JacksonPayloadDeserializer implements PayloadDeserializer {

    private final ObjectMapper objectMapper;

    public JacksonPayloadDeserializer(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @SuppressWarnings("unchecked")
    @NonNull
    @Override
    public <T> Converter<byte[], T> getConverter(@NonNull Class<T> targetType) {
        return source -> {
            try {
                if (targetType == byte[].class) {
                    return (T) source;
                } else if (targetType == String.class) {
                    return (T) new String(source, StandardCharsets.UTF_8);
                }
                return objectMapper.readValue(source, targetType);
            } catch (IOException e) {
                log.warn("Payload deserialize error", e);
            }
            return null;
        };
    }
}
