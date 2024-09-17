package com.robin.iot.common.mybatis.autoconfigure;

import com.baomidou.mybatisplus.autoconfigure.MybatisPlusAutoConfiguration;
import com.baomidou.mybatisplus.extension.plugins.MybatisPlusInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.PaginationInnerInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.TenantLineInnerInterceptor;
import com.robin.iot.common.mybatis.handler.MultiTenantHandler;
import com.robin.iot.common.mybatis.handler.PublicFieldsHandler;
import org.springframework.boot.autoconfigure.AutoConfiguration;
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
        mybatisPlusInterceptor.addInnerInterceptor(new PaginationInnerInterceptor(properties.getDbType()));
        return mybatisPlusInterceptor;
    }

    @Bean
    @ConditionalOnProperty(prefix = "spring.mybatis", name = "enablePublicFieldsInject", havingValue = "true", matchIfMissing = true)
    public PublicFieldsHandler publicFieldsHandler() {
        return new PublicFieldsHandler();
    }

}
