package com.robin.iot.common.mybatis.query.processor;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.robin.iot.common.mybatis.query.ConditionProcessor;
import com.robin.iot.common.mybatis.query.QueryCondition;

import java.lang.annotation.Annotation;

/**
 * TODO
 *
 * @author zhao peng
 * @date 2024/10/7 21:52
 **/
public class NeConditionProcessor implements ConditionProcessor {
    @Override
    public <T> void process(QueryWrapper<T> queryWrapper, QueryCondition queryCondition) {
        queryWrapper.ne(queryCondition.getColumn(), queryCondition.getValue());
    }

    @Override
    public <T> ConditionProcessor preProcess(Annotation annotation, QueryWrapper<T> queryWrapper) {
        return null;
    }
}
