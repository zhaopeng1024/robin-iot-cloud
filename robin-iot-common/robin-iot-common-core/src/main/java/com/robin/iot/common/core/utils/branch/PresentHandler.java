package com.robin.iot.common.core.utils.branch;

import java.util.function.Consumer;

/**
 * 存在时函数式处理接口
 *
 * @author zhao peng
 * @date 2024/7/14 13:27
 **/
@FunctionalInterface
public interface PresentHandler<T> {

    /**
     * 存在时则消费
     * @param consumer 消费逻辑
     */
    void presentHandle(Consumer<? super T> consumer);

}
