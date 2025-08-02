package com.example.eventconsumerservice.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "comment-service", url = "http://comment-service:8004")
public interface CommentServiceClient {

    @DeleteMapping("/internal/comments/by-post")
    void deleteAllCommentsByPostId(@RequestParam(value = "postId") Long postId);
}
