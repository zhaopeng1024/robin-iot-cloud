package com.robin.iot.common.core.util.branch;

import java.util.function.Consumer;

/**
 * 存在或不存在函数式处理接口
 *
 * @author zhao peng
 * @date 2024/7/14 13:34
 **/
@FunctionalInterface
public interface PresentOrElseHandler<T> {

    /**
     * 存在或不存在处理接口
     * @param consumer 主体不为空时执行消费操作
     * @param nullHandler 主体为空时执行其他的操作
     */
    @SuppressWarnings("unused")
    void presentOrElseHandle(Consumer<? super T> consumer, Runnable nullHandler);

}
