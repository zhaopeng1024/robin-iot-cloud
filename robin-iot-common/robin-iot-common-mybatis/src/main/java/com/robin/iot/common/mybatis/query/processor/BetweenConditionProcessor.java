package com.robin.iot.common.mybatis.query.processor;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.robin.iot.common.mybatis.query.*;

import java.lang.annotation.Annotation;

/**
 * TODO
 *
 * @author zhao peng
 * @date 2024/10/7 22:08
 **/
public class BetweenConditionProcessor implements ConditionProcessor {
    @Override
    public <T> void process(QueryWrapper<T> queryWrapper, QueryCondition queryCondition) {
        String column = queryCondition.getColumn();
        Range<?> range = (Range<?>) queryCondition.getValue();
        queryWrapper.between(column, range.getLower(), range.getUpper());
    }

    @Override
    public <T> ConditionProcessor preProcess(Annotation annotation, QueryWrapper<T> queryWrapper) {
        Condition.BETWEEN between = (Condition.BETWEEN) annotation;
        if (between.logic() == Logic.OR) {
            queryWrapper.or();
        }
        return this;
    }
}
