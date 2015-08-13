package com.lms;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.context.web.SpringBootServletInitializer;

/**
 * Spring-Boot服务启动类。
 * Created by ZhangKejie on 2015/8/12.
 */
@SpringBootApplication
public class Application /*extends SpringBootServletInitializer*/ {
    /*
    @Override
    protected SpringApplicationBuilder configure(
            SpringApplicationBuilder application) {
        return application.sources(Application.class);
    }
*/
    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
       // SpringApplication app = new SpringApplication(Application.class);
    }
}