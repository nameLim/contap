package com.project.contap;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling // 스케쥴링
@SpringBootApplication
public class ContapApplication {

    public static void main(String[] args) {
        SpringApplication.run(ContapApplication.class, args);
    }

}
