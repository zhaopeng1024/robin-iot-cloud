package com.robin.iot.gen;

import com.robin.iot.common.security.annotation.EnableCustomConfig;
import com.robin.iot.common.security.annotation.EnableRyFeignClients;
import com.robin.iot.common.swagger.annotation.EnableCustomSwagger2;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * 代码生成
 * 
 * @author zhaopeng
 */
@EnableCustomConfig
@EnableCustomSwagger2
@EnableRyFeignClients
@SpringBootApplication
public class GenApplication
{
    private static final Logger log = LoggerFactory.getLogger(GenApplication.class);

    public static void main(String[] args)
    {
        SpringApplication.run(GenApplication.class, args);
        log.info("GenApplication - 代码生成服务启动成功");
    }
}
