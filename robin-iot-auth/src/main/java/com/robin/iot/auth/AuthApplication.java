package com.robin.iot.auth;

import com.robin.iot.common.security.annotation.EnableRyFeignClients;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

/**
 * 认证授权中心
 */
@EnableRyFeignClients
@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class })
public class AuthApplication
{
    private static final Logger log = LoggerFactory.getLogger(AuthApplication.class);

    public static void main(String[] args)
    {
        SpringApplication.run(AuthApplication.class, args);
        log.info("AuthApplication - 认证授权中心启动成功");
    }
}
