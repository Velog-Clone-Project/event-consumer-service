package com.example.eventconsumerservice.client;

import com.example.eventconsumerservice.event.UpdateAuthorInfoEvent;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

@FeignClient(name = "post-service", url = "http://post-service:8003")
@RequestMapping("/internal/posts")
public interface PostServiceClient {

    @PutMapping("/author-info")
    void updateAuthorInfo(@RequestBody UpdateAuthorInfoEvent event);

    @DeleteMapping("/by-user")
    void deleteAllCommentsByUserId(@RequestParam(value = "userId") String userId);
}
