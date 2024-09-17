package com.robin.iot.common.mybatis.handler;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import com.robin.iot.common.mybatis.base.BaseEntity;
import com.robin.iot.common.mybatis.util.UserContextHolder;
import org.apache.ibatis.reflection.MetaObject;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;

/**
 * 公共字段处理器
 *
 * @author zhao peng
 * @date 2024/9/10 21:53
 **/
public class PublicFieldsHandler implements MetaObjectHandler {

    @Override
    public void insertFill(MetaObject metaObject) {
        if (metaObject != null && metaObject.getOriginalObject() instanceof BaseEntity baseEntity) {
            LocalDateTime now = LocalDateTime.now();
            if (baseEntity.getCreatedTime() == null) {
                baseEntity.setCreatedTime(now);
            }
            if (baseEntity.getUpdatedTime() == null) {
                baseEntity.setUpdatedTime(now);
            }
            String currentUser = UserContextHolder.getCurrentUser();
            if (baseEntity.getCreatedUser() == null && StringUtils.hasText(currentUser)) {
                baseEntity.setCreatedUser(currentUser);
            }
            if (baseEntity.getUpdatedUser() == null && StringUtils.hasText(currentUser)) {
                baseEntity.setUpdatedUser(currentUser);
            }
        }
    }

    @Override
    public void updateFill(MetaObject metaObject) {
        Object updatedTime = getFieldValByName("updatedTime", metaObject);
        if (updatedTime == null) {
            setFieldValByName("updatedTime", LocalDateTime.now(), metaObject);
        }
        Object updatedUser = getFieldValByName("updatedUser", metaObject);
        String currentUser = UserContextHolder.getCurrentUser();
        if (updatedUser == null && StringUtils.hasText(currentUser)) {
            setFieldValByName("updatedUser", currentUser, metaObject);
        }
    }

}
