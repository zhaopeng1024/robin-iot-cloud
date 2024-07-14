package com.robin.iot.common.core.utils.branch;

/**
 * True Or False 函数式处理接口
 *
 * @author zhao peng
 * @date 2024/7/11 0:43
 **/
@FunctionalInterface
public interface TrueOrFalseHandler {

    /**
     * true or false 处理接口
     * @param trueHandler 条件为 true 时执行的处理逻辑
     * @param falseHandler 条件为 false 时执行的处理逻辑
     */
    void trueOrFalseHandle(Runnable trueHandler, Runnable falseHandler);

}
