package com.example.eventconsumerservice.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@FeignClient(name = "auth-service", url = "http://auth-service:8001")
@RequestMapping("/internal")
public interface AuthServiceClient {

    @DeleteMapping("/users/{userId}")
    void deleteUser(@PathVariable("userId") String userId);
}
