package com.robin.iot.common.mqtt.convert.jackson;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.convert.converter.Converter;
import org.springframework.core.convert.converter.ConverterFactory;
import org.springframework.lang.NonNull;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

/**
 * json 字符串转对象反序列化器
 *
 * @author zhao peng
 * @date 2024/8/20 23:19
 **/
@Slf4j
public class JacksonStringDeserializer implements ConverterFactory<String, Object> {

    private final ObjectMapper objectMapper;

    public JacksonStringDeserializer(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @SuppressWarnings("unchecked")
    @NonNull
    @Override
    public <T> Converter<String, T> getConverter(@NonNull Class<T> targetType) {
        return source -> {
            try {
                if (targetType == byte[].class) {
                    return (T) source.getBytes(StandardCharsets.UTF_8);
                } else if (targetType == String.class) {
                    return (T) source;
                }
                return objectMapper.readValue(source, targetType);
            } catch (IOException e) {
                log.warn("Payload deserialize error", e);
            }
            return null;
        };
    }

}
