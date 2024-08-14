package com.robin.iot.common.mqtt.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * topic 中的参数注解
 *
 * @author zhao peng
 * @date 2024/7/25 22:16
 **/
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.PARAMETER})
public @interface Param {

    /**
     * 参数名
     * @return 参数名
     */
    String value();

    /**
     * 如果 required 为 true，value 为 null，则 Method 不执行。
     * @return boolean
     */
    boolean required() default false;

}
