package com.robin.iot.common.mybatis.query;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * 范围值封装类
 *
 * @author zhao peng
 * @date 2024/10/7 21:11
 **/
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Range<T> {

    /**
     * 上限
     */
    private T upper;

    /**
     * 下限
     */
    private T lower;

}
