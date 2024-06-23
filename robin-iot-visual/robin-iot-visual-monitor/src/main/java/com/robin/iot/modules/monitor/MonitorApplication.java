package com.robin.iot.modules.monitor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import de.codecentric.boot.admin.server.config.EnableAdminServer;

/**
 * 监控中心
 * 
 * @author zhaopeng
 */
@EnableAdminServer
@SpringBootApplication
public class MonitorApplication
{

    private static final Logger log = LoggerFactory.getLogger(MonitorApplication.class);

    public static void main(String[] args)
    {
        SpringApplication.run(MonitorApplication.class, args);
        log.info("MonitorApplication - 监控中心启动成功");
    }
}
