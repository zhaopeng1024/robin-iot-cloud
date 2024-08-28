package com.robin.iot.common.mybatis.base;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 实体基类
 *
 * @author zhao peng
 * @date 2024/8/26 22:43
 **/
@Getter
@Setter
public abstract class BaseEntity implements Serializable {

    /**
     * 自增主键
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 创建时间
     */
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createdTime;

    /**
     * 创建人
     */
    @TableField(fill = FieldFill.INSERT)
    private String createdUser;

    /**
     * 更新时间
     */
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updatedTime;

    /**
     * 更新人
     */
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private String updatedUser;

    /**
     * 是否删除
     */
    @TableLogic
    private Boolean isDeleted;

}
