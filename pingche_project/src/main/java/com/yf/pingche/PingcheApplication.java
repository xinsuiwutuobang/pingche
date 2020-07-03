package com.yf.pingche;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@EnableTransactionManagement
@SpringBootApplication
public class PingcheApplication {

    public static void main(String[] args) {
        SpringApplication.run(PingcheApplication.class, args);
    }
}
