package com.robin.iot.common.banner.autoconfigure;

import com.robin.iot.common.banner.runner.BannerApplicationRunner;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;

/**
 * Banner 自动配置类
 *
 * @author zhao peng
 * @date 2024/7/14 15:08
 **/
@AutoConfiguration
public class BannerAutoConfiguration {

    @Bean
    @ConditionalOnMissingBean
    public BannerApplicationRunner bannerApplicationRunner() {
        return new BannerApplicationRunner();
    }

}
