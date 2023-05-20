package com.heima;

import org.mybatis.spring.annotation.MapperScan;
import org.mybatis.spring.annotation.MapperScans;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.heima.user.mapper")
public class LeadnewsUserApplication {
    public static void main(String[] args) {
        SpringApplication.run(LeadnewsUserApplication.class, args);
    }
}
