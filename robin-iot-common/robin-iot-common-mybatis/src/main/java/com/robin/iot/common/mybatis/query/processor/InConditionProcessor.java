package com.robin.iot.common.mybatis.query.processor;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.robin.iot.common.mybatis.query.Condition;
import com.robin.iot.common.mybatis.query.ConditionProcessor;
import com.robin.iot.common.mybatis.query.Logic;
import com.robin.iot.common.mybatis.query.QueryCondition;

import java.lang.annotation.Annotation;

/**
 * IN 条件处理器
 *
 * @author zhao peng
 * @date 2024/10/7 21:40
 **/
public class InConditionProcessor implements ConditionProcessor {
    @Override
    public <T> void process(QueryWrapper<T> queryWrapper, QueryCondition queryCondition) {
        queryWrapper.in(queryCondition.getColumn(), queryCondition.getValue());
    }

    @Override
    public <T> ConditionProcessor preProcess(Annotation annotation, QueryWrapper<T> queryWrapper) {
        Condition.IN in = (Condition.IN) annotation;
        if (in.logic() == Logic.OR) {
            queryWrapper.or();
        }
        return this;
    }
}
