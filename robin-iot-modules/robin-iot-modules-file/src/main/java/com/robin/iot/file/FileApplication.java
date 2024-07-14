package com.robin.iot.file;

import com.robin.iot.common.swagger.annotation.EnableCustomSwagger2;
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

    public static void main(String[] args)
    {
        SpringApplication.run(FileApplication.class, args);
    }
}
