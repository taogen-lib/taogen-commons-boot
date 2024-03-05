package com.taogen.commons.boot.mybatisplus;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = "com.taogen.commons.boot")
@MapperScan(value = "com.taogen.commons.boot.mybatisplus.samples.mapper")
public class AppTest {
    public static void main(String[] args) {
        SpringApplication.run(AppTest.class, args);
    }
}
