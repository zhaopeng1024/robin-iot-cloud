package com.robin.iot.common.mqtt.convert.jackson;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.robin.iot.common.mqtt.convert.PayloadSerializer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.lang.NonNull;

import java.nio.charset.StandardCharsets;

/**
 * 默认的对象转 Json 序列化器
 *
 * @author zhao peng
 * @date 2024/8/20 23:30
 **/
@Slf4j
public class JacksonPayloadSerializer implements PayloadSerializer {

    private final ObjectMapper objectMapper;

    public JacksonPayloadSerializer(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @Override
    public byte[] convert(@NonNull Object source) {
        try {
            if (source instanceof byte[]) {
                return (byte[]) source;
            } else if (source instanceof String) {
                return ((String) source).getBytes(StandardCharsets.UTF_8);
            } else {
                return objectMapper.writeValueAsBytes(source);
            }
        } catch (JsonProcessingException e) {
            log.error("Payload serialize error", e);
        }
        return null;
    }

}
