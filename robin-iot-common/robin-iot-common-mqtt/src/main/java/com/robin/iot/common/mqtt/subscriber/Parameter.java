package com.robin.iot.common.mqtt.subscriber;

import com.robin.iot.common.mqtt.annotation.Param;
import com.robin.iot.common.mqtt.annotation.Payload;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.convert.converter.Converter;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.LinkedList;

/**
 * 参数模型
 *
 * @author zhao peng
 * @date 2024/7/25 22:22
 **/
@Getter
@Slf4j
public final class Parameter {

    /**
     * 是否是消息内容, 若参数为 String 类型, 并且不是消息内容, 则赋值 topic。
     */
    private boolean payload;

    private boolean required;

    private Class<?> type;

    private String name;

    private Object defaultValue;

    private LinkedList<Converter<Object, Object>> converters;

    private Parameter() {

    }

    /**
     * 该函数的功能是根据给定的 Method 对象，创建一个包含参数信息的 LinkedList。
     * <p>
     * 每个参数都用 Parameter 对象表示，其中包含了参数类型、默认值、是否必须、参数名以及是否为 Payload 等信息。
     * <p>
     * 函数通过 method.getParameterTypes() 获取参数类型数组，通过 method.getParameterAnnotations() 获取参数注解数组，
     * 然后遍历每个参数，根据注解类型分别设置ParameterModel对象的属性值。最后返回包含所有参数信息的LinkedList。
     * @param method Method
     * @return LinkedList<Parameter>
     */
    public static LinkedList<Parameter> of(Method method) {
        LinkedList<Parameter> parameters = new LinkedList<>();
        Class<?>[] parameterTypes = method.getParameterTypes();
        Annotation[][] parameterAnnotations = method.getParameterAnnotations();
        for (int i = 0; i < parameterTypes.length; i++) {
            Parameter model = new Parameter();
            parameters.add(model);
            model.type = parameterTypes[i];
            model.defaultValue = defaultValue(model.type);
            Annotation[] annotations = parameterAnnotations[i];
            if (annotations != null) {
                for (Annotation annotation : annotations) {
                    if (annotation.annotationType() == Param.class) {
                        Param param = (Param) annotation;
                        model.required = model.required || param.required();
                        model.name = param.value();
                    }
                    if (annotation.annotationType() == Payload.class) {
                        Payload payload = (Payload) annotation;
                        model.payload = true;
                        model.required = model.required || payload.required();
                        model.converters = toConverters(payload.value());
                    }
                }
            }
        }
        return parameters;
    }

    /**
     * 该函数将一个 Class 数组转换为 LinkedList 类型的 Converter 对象列表。
     * <p>
     * 如果输入的 Class 数组为空或长度为 0，则返回 null。
     * <p>
     * 否则，遍历数组中的每个 Class 对象，尝试通过无参构造方法创建其实例，并将其转换为 Converter<Object, Object> 类型后。
     * 添加到 LinkedList 中。如果创建实例失败，则记录错误日志。
     * @param classes Class<? extends Converter<?, ?>>[]
     * @return LinkedList<Converter<Object, Object>>
     */
    @SuppressWarnings("unchecked")
    public static LinkedList<Converter<Object, Object>> toConverters(Class<? extends Converter<?, ?>>[] classes) {
        if (classes == null || classes.length == 0) {
            return null;
        } else {
            LinkedList<Converter<Object, Object>> converters = new LinkedList<>();
            for (Class<? extends Converter<?, ?>> covert : classes) {
                try {
                    converters.add((Converter<Object, Object>) covert.getDeclaredConstructor().newInstance());
                } catch (Exception e) {
                    log.error("create converter instance failed.", e);
                }
            }
            return converters;
        }
    }

    private static Object defaultValue(Class<?> type) {
        if (type.isPrimitive()) {
            if (type == boolean.class) {
                return false;
            }
            if (type == char.class) {
                return (char) 0;
            }
            if (type == byte.class) {
                return (byte) 0;
            }
            if (type == short.class) {
                return (short) 0;
            }
            if (type == int.class) {
                return 0;
            }
            if (type == long.class) {
                return 0L;
            }
            if (type == float.class) {
                return 0.0f;
            }
            if (type == double.class) {
                return 0.0d;
            }
        }
        return null;
    }

}