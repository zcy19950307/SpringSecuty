package com.atuigu.securitydemo1;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

@SpringBootApplication
@MapperScan("com.atuigu.securitydemo1.mapper")
public class Securitydemo1Application {

    public static void main(String[] args) {

        SpringApplication.run(Securitydemo1Application.class, args);
    }

}
