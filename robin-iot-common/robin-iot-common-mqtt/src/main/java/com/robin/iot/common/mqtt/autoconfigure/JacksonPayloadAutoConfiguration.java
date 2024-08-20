package com.robin.iot.common.mqtt.autoconfigure;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.Version;
import com.fasterxml.jackson.core.util.VersionUtil;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.robin.iot.common.mqtt.convert.MqttConversionService;
import com.robin.iot.common.mqtt.convert.PayloadDeserializer;
import com.robin.iot.common.mqtt.convert.PayloadSerializer;
import com.robin.iot.common.mqtt.convert.jackson.JacksonPayloadDeserializer;
import com.robin.iot.common.mqtt.convert.jackson.JacksonPayloadSerializer;
import com.robin.iot.common.mqtt.convert.jackson.JacksonStringDeserializer;
import com.robin.iot.common.mqtt.convert.jackson.JacksonStringSerializer;
import org.springframework.beans.factory.ListableBeanFactory;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.jackson.JacksonAutoConfiguration;
import org.springframework.core.annotation.Order;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.Map;

/**
 * Jackson Payload 自动配置类
 *
 * @author zhao peng
 * @date 2024/8/20 22:16
 **/
@Order
@ConditionalOnClass(ObjectMapper.class)
@AutoConfiguration(after = {JacksonAutoConfiguration.class})
public class JacksonPayloadAutoConfiguration {

    public JacksonPayloadAutoConfiguration(ListableBeanFactory beanFactory) {
        MqttConversionService conversionService = MqttConversionService.getSharedInstance();
        ObjectMapper objectMapper = objectMapper();
        // 默认转换类
        Map<String, PayloadDeserializer> deserializerMap = beanFactory.getBeansOfType(PayloadDeserializer.class);
        if (deserializerMap.isEmpty()) {
            conversionService.addConverterFactory(jacksonPayloadDeserializer(objectMapper));
            conversionService.addConverterFactory(jacksonStringDeserializer(objectMapper));
        } else {
            deserializerMap.values().forEach(conversionService::addConverterFactory);
        }
        Map<String, PayloadSerializer> serializeMap = beanFactory.getBeansOfType(PayloadSerializer.class);
        if (serializeMap.isEmpty()) {
            conversionService.addConverter(jacksonPayloadSerializer(objectMapper));
            conversionService.addConverter(jacksonStringSerializer(objectMapper));
        } else {
            serializeMap.values().forEach(conversionService::addConverter);
        }
    }

    private ObjectMapper objectMapper() {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        objectMapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
        objectMapper.disable(DeserializationFeature.FAIL_ON_IGNORED_PROPERTIES);
        objectMapper.disable(SerializationFeature.FAIL_ON_EMPTY_BEANS);
        objectMapper.registerModule(new MqttDefaultJacksonModule());
        return objectMapper;
    }

    public JacksonPayloadSerializer jacksonPayloadSerializer(ObjectMapper objectMapper) {
        return new JacksonPayloadSerializer(objectMapper);
    }

    public JacksonPayloadDeserializer jacksonPayloadDeserializer(ObjectMapper objectMapper) {
        return new JacksonPayloadDeserializer(objectMapper);
    }

    public JacksonStringSerializer jacksonStringSerializer(ObjectMapper objectMapper) {
        return new JacksonStringSerializer(objectMapper);
    }

    public JacksonStringDeserializer jacksonStringDeserializer(ObjectMapper objectMapper) {
        return new JacksonStringDeserializer(objectMapper);
    }

    public static class MqttDefaultJacksonModule extends SimpleModule {
        public static final Version VERSION = VersionUtil.parseVersion("2.0.0", "com.robin", "robin-iot-common-mqtt");
        private static final ZoneId ZONE_ID = ZoneId.of("GMT+8");
        private final static DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        private final static DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        private final static DateTimeFormatter TIME_FORMATTER = DateTimeFormatter.ofPattern("HH:mm:ss");

        public MqttDefaultJacksonModule() {
            super(VERSION);

            addSerializer(LocalDateTime.class, LOCAL_DATE_TIME_JSON_SERIALIZER);
            addSerializer(LocalDate.class, LOCAL_DATE_JSON_SERIALIZER);
            addSerializer(LocalTime.class, LOCAL_TIME_JSON_SERIALIZER);
            addSerializer(Date.class, DATE_JSON_SERIALIZER);

            addDeserializer(LocalDateTime.class, LOCAL_DATE_TIME_JSON_DESERIALIZER);
            addDeserializer(LocalDate.class, LOCAL_DATE_JSON_DESERIALIZER);
            addDeserializer(LocalTime.class, LOCAL_TIME_JSON_DESERIALIZER);
            addDeserializer(Date.class, DATE_JSON_DESERIALIZER);

        }

        private final static JsonSerializer<LocalDateTime> LOCAL_DATE_TIME_JSON_SERIALIZER = new JsonSerializer<>() {
            @Override
            public void serialize(LocalDateTime value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
                if (value == null) {
                    gen.writeNull();
                } else {
                    gen.writeString(value.atZone(ZONE_ID).format(DATE_TIME_FORMATTER));
                }
            }
        };

        private final static JsonSerializer<LocalDate> LOCAL_DATE_JSON_SERIALIZER = new JsonSerializer<>() {

            @Override
            public void serialize(LocalDate value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
                if (value == null) {
                    gen.writeNull();
                } else {
                    gen.writeString(value.format(DATE_FORMATTER));
                }
            }
        };

        private final static JsonSerializer<LocalTime> LOCAL_TIME_JSON_SERIALIZER = new JsonSerializer<>() {

            @Override
            public void serialize(LocalTime value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
                if (value == null) {
                    gen.writeNull();
                } else {
                    gen.writeString(value.format(TIME_FORMATTER));
                }
            }

        };

        private final static JsonSerializer<Date> DATE_JSON_SERIALIZER = new JsonSerializer<>() {

            @Override
            public void serialize(Date value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
                if (value == null) {
                    gen.writeNull();
                } else {
                    gen.writeString(DATE_TIME_FORMATTER.format(value.toInstant().atZone(ZONE_ID)));
                }
            }

        };

        private final static JsonDeserializer<LocalDateTime> LOCAL_DATE_TIME_JSON_DESERIALIZER = new JsonDeserializer<>() {

            @Override
            public LocalDateTime deserialize(JsonParser p, DeserializationContext context) throws IOException {
                String value = p.getValueAsString();
                if (StringUtils.hasText(value)) {
                    return LocalDateTime.parse(value, DATE_TIME_FORMATTER);
                }
                return null;
            }

        };

        private final static JsonDeserializer<LocalDate> LOCAL_DATE_JSON_DESERIALIZER = new JsonDeserializer<>() {

            @Override
            public LocalDate deserialize(JsonParser p, DeserializationContext context) throws IOException {
                String value = p.getValueAsString();
                if (StringUtils.hasText(value)) {
                    return LocalDate.parse(value, DATE_FORMATTER);
                }
                return null;
            }

        };

        private final static JsonDeserializer<LocalTime> LOCAL_TIME_JSON_DESERIALIZER = new JsonDeserializer<>() {

            @Override
            public LocalTime deserialize(JsonParser p, DeserializationContext context) throws IOException {
                String value = p.getValueAsString();
                if (StringUtils.hasText(value)) {
                    return LocalTime.parse(value, TIME_FORMATTER);
                }
                return null;
            }

        };

        private final static JsonDeserializer<Date> DATE_JSON_DESERIALIZER = new JsonDeserializer<>() {

            @Override
            public Date deserialize(JsonParser p, DeserializationContext context) throws IOException {
                String value = p.getValueAsString();
                if (StringUtils.hasText(value)) {
                    return Date.from(LocalDateTime.parse(value, DATE_TIME_FORMATTER).atZone(ZONE_ID).toInstant());
                }
                return null;
            }

        };

    }

}
