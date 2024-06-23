package com.robin.iot.gateway;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

/**
 * 网关启动程序
 * 
 * @author zhaopeng
 */
@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class })
public class GatewayApplication
{
    private static final Logger log = LoggerFactory.getLogger(GatewayApplication.class);
    public static void main(String[] args)
    {
        SpringApplication.run(GatewayApplication.class, args);
        log.info("GatewayApplication - 网关服务启动成功");
    }
}
