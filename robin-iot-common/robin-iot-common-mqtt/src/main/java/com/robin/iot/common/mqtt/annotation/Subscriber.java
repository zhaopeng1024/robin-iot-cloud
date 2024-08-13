package com.robin.iot.common.mqtt.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 订阅者注解
 *
 * @author zhao peng
 * @date 2024/7/25 22:01
 **/
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface Subscriber {

    /**
     * topics
     * @return topics
     */
    String[] value();

    /**
     * 主题一对一的 QoS。
     * <p>
     * 如果不是一对一。按最后一个 QoS 填充，忽略多余的。
     *
     * @return QoSs
     */
    int[] qos() default 0;

    /**
     * clientIds
     * @return clientIds, default all client
     */
    String[] clients() default {};

    /**
     * 共享订阅组，如果组不为空，则使用“$share/<group>/<topic>”作为主题一对一
     * <p>
     * 如果不是一对一。按最后一个 QoS 填充，忽略多余的。
     * @return groups
     */
    String[] groups() default "";

}
