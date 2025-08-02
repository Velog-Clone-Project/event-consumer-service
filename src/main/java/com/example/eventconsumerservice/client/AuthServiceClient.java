package com.example.eventconsumerservice.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "auth-service", url = "http://auth-service:8001")
public interface AuthServiceClient {

    @DeleteMapping("/internal/users/{userId}")
    void deleteUser(@PathVariable("userId") String userId);
}
