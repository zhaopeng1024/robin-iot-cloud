package com.robin.iot.common.mybatis.page;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

/**
 * 分页工具类
 *
 * @author zhao peng
 * @date 2024/8/28 23:58
 **/
public final class PageUtils {

    /**
     * 构建 {@link Page} 实例
     * @param pageParam 分页请求参数
     * @return {@link Page} 实例
     * @param <T> 分页泛型
     */
    public static <T> Page<T> buildPage(PageParam pageParam) {
        return new Page<>(pageParam.getPageNo(), pageParam.getPageSize());
    }

}
