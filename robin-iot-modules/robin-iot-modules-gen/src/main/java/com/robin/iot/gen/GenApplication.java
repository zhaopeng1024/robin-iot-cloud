package com.robin.iot.gen;

import com.robin.iot.common.security.annotation.EnableCustomConfig;
import com.robin.iot.common.security.annotation.EnableRyFeignClients;
import com.robin.iot.common.swagger.annotation.EnableCustomSwagger2;
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
    public static void main(String[] args)
    {
        SpringApplication.run(GenApplication.class, args);
    }
}
