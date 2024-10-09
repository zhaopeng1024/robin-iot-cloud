package com.robin.iot.common.mybatis.autoconfigure;

import com.baomidou.mybatisplus.autoconfigure.MybatisPlusAutoConfiguration;
import com.baomidou.mybatisplus.extension.plugins.MybatisPlusInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.IllegalSQLInnerInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.OptimisticLockerInnerInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.PaginationInnerInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.TenantLineInnerInterceptor;
import com.robin.iot.common.mybatis.handler.DataEncryptHandler;
import com.robin.iot.common.mybatis.handler.MultiTenantHandler;
import com.robin.iot.common.mybatis.handler.PublicFieldsHandler;
import com.robin.iot.common.mybatis.injector.EnhancedSqlInjector;
import com.robin.iot.common.mybatis.security.AesEncryptor;
import com.robin.iot.common.mybatis.security.Algorithm;
import com.robin.iot.common.mybatis.security.Base64Encryptor;
import com.robin.iot.common.mybatis.security.DataEncryptor;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;

/**
 * 增强的 mybatis 自动配置类
 *
 * @author zhao peng
 * @date 2024/9/10 22:23
 **/
@AutoConfiguration(after = MybatisPlusAutoConfiguration.class)
@EnableConfigurationProperties(EnhancedMybatisProperties.class)
public class EnhancedMybatisAutoConfiguration {

    @Bean
    public MybatisPlusInterceptor mybatisPlusInterceptor(EnhancedMybatisProperties properties) {
        MybatisPlusInterceptor mybatisPlusInterceptor = new MybatisPlusInterceptor();
        if (properties.getEnableMultiTenant()) {
            mybatisPlusInterceptor.addInnerInterceptor(new TenantLineInnerInterceptor(new MultiTenantHandler(properties)));
        }
        if (properties.getEnableOptimisticLock()) {
            mybatisPlusInterceptor.addInnerInterceptor(new OptimisticLockerInnerInterceptor());
        }
        if (properties.getEnableIllegalSqlIntercept()) {
            mybatisPlusInterceptor.addInnerInterceptor(new IllegalSQLInnerInterceptor());
        }
        mybatisPlusInterceptor.addInnerInterceptor(new PaginationInnerInterceptor(properties.getDbType()));
        return mybatisPlusInterceptor;
    }

    @Bean
    @ConditionalOnProperty(prefix = "spring.mybatis", name = "enablePublicFieldsInject", havingValue = "true", matchIfMissing = true)
    public PublicFieldsHandler publicFieldsHandler() {
        return new PublicFieldsHandler();
    }

    @Bean
    public DataEncryptHandler<?> encryptHandler() {
        return new DataEncryptHandler<>();
    }

    @Bean
    @ConditionalOnMissingBean(DataEncryptor.class)
    @ConditionalOnProperty(prefix = "spring.mybatis.encrypt", name = "enable", havingValue = "true")
    public DataEncryptor dataEncryptor(EnhancedMybatisProperties enhancedMybatisProperties) {
        Algorithm algorithm = enhancedMybatisProperties.getDataEncrypt().getAlgorithm();
        return switch (algorithm) {
            case BASE64 -> new Base64Encryptor();
            case AES -> new AesEncryptor(enhancedMybatisProperties);
        };
    }

    @Bean
    @ConditionalOnMissingBean
    public EnhancedSqlInjector enhancedSqlInjector() {
        return new EnhancedSqlInjector();
    }

}
