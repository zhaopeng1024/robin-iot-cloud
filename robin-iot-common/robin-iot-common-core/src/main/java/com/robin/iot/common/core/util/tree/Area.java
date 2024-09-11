package com.robin.iot.common.core.util.tree;

import com.alibaba.fastjson2.annotation.JSONField;

import java.util.List;

/**
 * 区域
 *
 * @author zhao peng
 * @date 2024/9/12 0:05
 **/
public class Area {

    private Long id;

    private Long parentId;

    @JSONField(serialize = false)
    private String code;

    private String name;

    @JSONField(serialize = false)
    private Integer sort;

    @JSONField(serialize = false)
    private String ancestor;

    private List<Area> subAreas;

    public Area(Long id, Long parentId, String code, String name, Integer sort, String ancestor) {
        this.id = id;
        this.parentId = parentId;
        this.code = code;
        this.name = name;
        this.sort = sort;
        this.ancestor = ancestor;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getParentId() {
        return parentId;
    }

    public void setParentId(Long parentId) {
        this.parentId = parentId;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getSort() {
        return sort;
    }

    public void setSort(Integer sort) {
        this.sort = sort;
    }

    public String getAncestor() {
        return ancestor;
    }

    public void setAncestor(String ancestor) {
        this.ancestor = ancestor;
    }


    public List<Area> getSubAreas() {
        return subAreas;
    }

    public void setSubAreas(List<Area> subAreas) {
        this.subAreas = subAreas;
    }
}
