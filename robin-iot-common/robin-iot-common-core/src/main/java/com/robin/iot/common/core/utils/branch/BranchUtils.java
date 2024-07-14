package com.robin.iot.common.core.utils.branch;

import java.util.Objects;

/**
 * 分支工具类
 *
 * @author zhao peng
 * @date 2024/7/14 13:22
 **/
public final class BranchUtils {

    /**
     * if or else 处理
     * @param condition 布尔条件
     * @return if else 处理逻辑
     */
    public static TrueOrFalseHandler isTrueOrFalse(boolean condition) {
        return ((trueHandler, falseHandler) -> {
            if (condition) {
                trueHandler.run();
            } else {
                falseHandler.run();
            }
        });
    }

    /**
     * 存在（非空）处理
     * @param object 判断主体
     * @return {@link PresentHandler} 处理逻辑
     */
    public static PresentHandler<?> isPresent(Object object) {
        return (consumer) -> {
            if (Objects.nonNull(object)) {
                consumer.accept(object);
            }
        };
    }

    /**
     * 存在（非空）or 不存在（空）处理
     * @param object 判断主体
     * @return {@link PresentOrElseHandler} 处理逻辑
     */
    public static PresentOrElseHandler<?> isPresentOrElse(Object object) {
        return (consumer, nullHandler) ->
                isTrueOrFalse(Objects.nonNull(object)).trueOrFalseHandle(
                        () -> consumer.accept(object), nullHandler);
    }

}
