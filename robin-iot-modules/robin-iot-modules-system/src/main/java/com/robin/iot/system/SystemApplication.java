package com.robin.iot.system;

import com.robin.iot.common.security.annotation.EnableCustomConfig;
import com.robin.iot.common.security.annotation.EnableRyFeignClients;
import com.robin.iot.common.swagger.annotation.EnableCustomSwagger2;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

    private static final Logger log = LoggerFactory.getLogger(SystemApplication.class);

    public static void main(String[] args)
    {
        SpringApplication.run(SystemApplication.class, args);
        log.info("SystemApplication - 系统模块启动成功");
    }
}
