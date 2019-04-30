package com.zgl.swsad;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

@MapperScan("com.zgl.swsad.mapper")  //包扫描
@EnableWebMvc
@SpringBootApplication
public class SwsadApplication {

    public static void main(String[] args) {
        SpringApplication.run(SwsadApplication.class, args);
    }
}
