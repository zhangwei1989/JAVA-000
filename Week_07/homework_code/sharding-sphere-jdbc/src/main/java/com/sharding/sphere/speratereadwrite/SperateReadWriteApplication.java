package com.sharding.sphere.speratereadwrite;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan(basePackages = "com.sharding.sphere.speratereadwrite.mapper")
public class SperateReadWriteApplication {

    public static void main(String[] args) {
        SpringApplication.run(SperateReadWriteApplication.class, args);
    }

}
