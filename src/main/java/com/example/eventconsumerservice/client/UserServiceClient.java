package com.example.eventconsumerservice.client;

import com.example.eventconsumerservice.event.UserCreatedEvent;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "user-service",
//        url = "http://user-service:8002",
        path = "/internal")
public interface UserServiceClient {

    @PostMapping("/users")
    void createUser(@RequestBody UserCreatedEvent event);
}
