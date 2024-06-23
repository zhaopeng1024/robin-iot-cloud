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

    private static final Logger log = LoggerFactory.getLogger(JobApplication.class);

    public static void main(String[] args)
    {
        SpringApplication.run(JobApplication.class, args);
        log.info("JobApplication - 定时任务服务启动成功");
    }
}
