package com.example.eventconsumerservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class EventConsumerServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(EventConsumerServiceApplication.class, args);
    }

}
