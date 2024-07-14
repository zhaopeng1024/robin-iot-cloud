package com.robin.iot.common.banner.runner;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;

import java.util.concurrent.TimeUnit;

/**
 * 应用程序启动扩展
 * <p>
 * 通过实现 ApplicationRunner 接口，可以在项目启动时对程序进行扩展。
 * <p>
 * ApplicationRunner 和 CommandLineRunner 都是在 SpringBoot 程序成功启动之后回调，其扩展
 * 只需要将实现类注册到 Spring 容器中即可。
 * @see ApplicationRunner
 * @author zhao peng
 * @date 2024/7/14 14:48
 **/
@Slf4j
public class BannerApplicationRunner implements ApplicationRunner {

    @Value("${spring.application.name}")
    private String applicationName;

    @Value("${spring.profiles.active}")
    private String profile;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        new Thread(() -> {
            try {
                // 线程休眠1s，确保输出到结尾
                TimeUnit.SECONDS.sleep(1);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            log.info("(♥◠‿◠)ﾉﾞ  {} 启动成功   ಠಿ_ಠ", applicationName);
            if (!profile.equals("prod") && !applicationName.equals("robin-iot-auth")) {
                String documentUrl = "http://127.0.0.1:8080/swagger-ui/index.html";
                log.info("接口文档：{}", documentUrl);
            }
        }).start();
    }

}
