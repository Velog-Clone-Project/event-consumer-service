package com.example.eventconsumerservice.client;

import com.example.eventconsumerservice.event.UpdateAuthorInfoEvent;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

@FeignClient(name = "comment-service",
//        url = "http://comment-service:8004",
path = "/internal/comments")
public interface CommentServiceClient {

    @DeleteMapping("/by-post")
    void deleteAllCommentsByPostId(@RequestParam(value = "postId") Long postId);

    @PutMapping("/author-info")
    void updateAuthorInfo(@RequestBody UpdateAuthorInfoEvent event);

    @DeleteMapping("/by-user")
    void deleteAllCommentsByUserId(@RequestParam(value = "userId") String userId);
}
