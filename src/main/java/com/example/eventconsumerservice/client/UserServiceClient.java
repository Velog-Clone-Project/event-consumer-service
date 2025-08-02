package com.example.eventconsumerservice.client;

import com.example.eventconsumerservice.event.UserCreatedEvent;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "user-service", url = "http://user-service:8002")
public interface UserServiceClient {

    @PostMapping("/internal/users")
    void createUser(@RequestBody UserCreatedEvent event);
}
