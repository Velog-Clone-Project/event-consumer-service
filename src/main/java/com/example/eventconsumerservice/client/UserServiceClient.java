package com.example.eventconsumerservice.client;

import com.example.eventconsumerservice.event.UserCreatedEvent;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@FeignClient(name = "user-service", url = "http://user-service:8002")
@RequestMapping("/internal/users")
public interface UserServiceClient {

    @PostMapping
    void createUser(@RequestBody UserCreatedEvent event);
}
