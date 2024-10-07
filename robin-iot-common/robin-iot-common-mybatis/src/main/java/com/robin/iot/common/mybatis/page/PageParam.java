package com.robin.iot.common.mybatis.page;

import lombok.Data;

/**
 * 分页数据参数
 *
 * @author zhao peng
 * @date 2024/8/28 23:57
 **/
@Data
public class PageParam {

    private static final int DEFAULT_PAGE_NO = 1;
    private static final int DEFAULT_PAGE_SIZE = 10;

    private Integer pageNo = DEFAULT_PAGE_NO;

    private Integer pageSize = DEFAULT_PAGE_SIZE;

    public PageParam() {

    }

    public PageParam(Integer pageNo, Integer pageSize) {
        this.pageNo = pageNo;
        this.pageSize = pageSize;
    }

}
