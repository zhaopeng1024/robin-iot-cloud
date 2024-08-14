package com.robin.iot.common.mqtt.annotation;

import org.springframework.core.convert.converter.Converter;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 消息内容注解
 * <p>
 * 如果参数中没有这个注解，则默认自定义类型具有此注释。
 * <p>
 * 如果参数中有这个注解，则只会将消息内容分配给该注解注释的参数。
 *
 * @author zhao peng
 * @date 2024/7/16 21:02
 **/
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.PARAMETER})
public @interface Payload {

    /**
     * 转换前的处理按顺序执行，从 byte[] 开始，到目标类型结束。
     * <p>
     * 如果依次执行后的结果与目标类型相同，则直接分配；如果不同，则调用 MqttConversionService 进行转换。
     * @return Converter
     */
    Class<? extends Converter<?, ?>>[] value() default {};

    /**
     * 如果 required 为 true 且 value 为空，则不执行该方法。
     * @return boolean
     */
    boolean required() default false;
}
