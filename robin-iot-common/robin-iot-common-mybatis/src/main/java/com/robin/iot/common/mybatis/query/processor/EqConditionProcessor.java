package com.robin.iot.common.mybatis.query.processor;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.robin.iot.common.mybatis.query.Condition;
import com.robin.iot.common.mybatis.query.ConditionProcessor;
import com.robin.iot.common.mybatis.query.Logic;
import com.robin.iot.common.mybatis.query.QueryCondition;

import java.lang.annotation.Annotation;

/**
 * EQ 条件处理器
 *
 * @author zhao peng
 * @date 2024/10/7 21:37
 **/
public class EqConditionProcessor implements ConditionProcessor {
    @Override
    public <T> void process(QueryWrapper<T> queryWrapper, QueryCondition queryCondition) {
        queryWrapper.eq(queryCondition.getColumn(), queryCondition.getValue());
    }

    @Override
    public <T> ConditionProcessor preProcess(Annotation annotation, QueryWrapper<T> queryWrapper) {
        Condition.EQ eq = (Condition.EQ) annotation;
        if (eq.logic() == Logic.OR) {
            queryWrapper.or();
        }
        return this;
    }
}
