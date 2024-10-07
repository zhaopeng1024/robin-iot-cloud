package com.robin.iot.common.mybatis.query;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * 查询条件封装类
 *
 * @author zhao peng
 * @date 2024/10/7 21:09
 **/
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class QueryCondition {

    private String column;

    private Object value;

}
