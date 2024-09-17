package com.robin.iot.common.mybatis.autoconfigure;

import com.baomidou.mybatisplus.annotation.DbType;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.Collections;
import java.util.Set;

/**
 * 增强的 mybatis 配置属性
 *
 * @author zhao peng
 * @date 2024/9/10 22:04
 **/
@Data
@ConfigurationProperties(prefix = "spring.mybatis")
public class EnhancedMybatisProperties {

    /**
     * 数据库类型
     */
    private DbType dbType = DbType.MYSQL;

    /**
     * 是否启用多租户
     */
    private Boolean enableMultiTenant = Boolean.FALSE;

    /**
     * 启用多租户插件后需要忽略的数据表
     */
    private Set<String> ignoredTables = Collections.emptySet();

    /**
     * 是否启用公共字段自动注入
     */
    private Boolean enablePublicFieldsInject = Boolean.TRUE;

    /**
     * 是否启用乐观锁插件
     */
    private Boolean enableOptimisticLock = Boolean.FALSE;
}
