package com.robin.iot.common.mybatis.query;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;

import java.lang.annotation.Annotation;

/**
 * 条件处理器（接口）
 *
 * @author zhao peng
 * @date 2024/10/7 21:07
 **/
public interface ConditionProcessor {

    <T> void process(QueryWrapper<T> queryWrapper, QueryCondition queryCondition);

    /**
     * 前置处理
     * 由子处理器根据注解的 Logic 值处理逻辑运算
     * @param annotation 注解
     * @param queryWrapper 查询包装器
     * @return 条件处理器
     * @param <T> 查询包装器泛型
     */
    <T> ConditionProcessor preProcess(Annotation annotation, QueryWrapper<T> queryWrapper);

}
