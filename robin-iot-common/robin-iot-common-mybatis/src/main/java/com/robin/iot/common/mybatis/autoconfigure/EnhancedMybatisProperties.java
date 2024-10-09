package com.robin.iot.common.mybatis.autoconfigure;

import com.baomidou.mybatisplus.annotation.DbType;
import com.baomidou.mybatisplus.core.toolkit.AES;
import com.robin.iot.common.mybatis.security.Algorithm;
import lombok.Data;
import lombok.Getter;
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

    public static final String AES_KEY = AES.generateRandomKey();

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

    /**
     * 是否启用非法 SQL 拦截插件
     */
    private Boolean enableIllegalSqlIntercept = Boolean.TRUE;

    @Getter
    private final DataEncrypt dataEncrypt = new DataEncrypt();

    @Data
    public static class DataEncrypt {
        /**
         * 加密算法
         */
        private Algorithm algorithm = Algorithm.BASE64;

        /**
         * AES 加密密钥
         */
        private String aesKey = AES_KEY;

        /**
         * AES 偏移参数
         */
        private String aesIv = "0f7687fe684d7ed0";

    }

}
