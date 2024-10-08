package com.robin.iot.job;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import com.robin.iot.common.security.annotation.EnableCustomConfig;
import com.robin.iot.common.security.annotation.EnableRyFeignClients;
import com.robin.iot.common.swagger.annotation.EnableCustomSwagger2;

/**
 * 定时任务
 * 
 * @author zhaopeng
 */
@EnableCustomConfig
@EnableCustomSwagger2
@EnableRyFeignClients   
@SpringBootApplication
public class JobApplication
{

    public static void main(String[] args)
    {
        SpringApplication.run(JobApplication.class, args);
    }
}
