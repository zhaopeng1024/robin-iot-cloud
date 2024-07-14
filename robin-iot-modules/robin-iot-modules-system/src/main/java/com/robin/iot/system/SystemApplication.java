package com.robin.iot.system;

import com.robin.iot.common.security.annotation.EnableCustomConfig;
import com.robin.iot.common.security.annotation.EnableRyFeignClients;
import com.robin.iot.common.swagger.annotation.EnableCustomSwagger2;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * 系统模块
 * 
 * @author zhaopeng
 */
@EnableCustomConfig
@EnableCustomSwagger2
@EnableRyFeignClients
@SpringBootApplication
public class SystemApplication
{
    public static void main(String[] args)
    {
        SpringApplication.run(SystemApplication.class, args);
    }
}
