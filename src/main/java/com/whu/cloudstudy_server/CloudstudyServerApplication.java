package com.whu.cloudstudy_server;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

@SpringBootApplication
@MapperScan("com.whu.cloudstudy_server.mapper")
public class CloudstudyServerApplication {

    public static void main(String[] args) {
        SpringApplication.run(CloudstudyServerApplication.class, args);
    }

}
