package com.robin.iot.file;

import com.robin.iot.common.swagger.annotation.EnableCustomSwagger2;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

/**
 * 文件服务
 * 
 * @author zhaopeng
 */
@EnableCustomSwagger2
@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class })
public class FileApplication
{

    private static final Logger log = LoggerFactory.getLogger(FileApplication.class);

    public static void main(String[] args)
    {
        SpringApplication.run(FileApplication.class, args);
        log.info("FileApplication - 文件服务模块启动成功");
    }
}
