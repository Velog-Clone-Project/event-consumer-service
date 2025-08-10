package com.example.eventconsumerservice.consumer;

import com.example.eventconsumerservice.client.CommentServiceClient;
import com.example.eventconsumerservice.config.RabbitProperties;
import com.example.eventconsumerservice.event.PostDeletedEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class PostDeletedConsumer {

    private final CommentServiceClient commentServiceClient;

    @RabbitListener(
            queues = "#{@rabbitProperties.queues.post.deleted}",
            containerFactory = "eventContainerFactory"
    )
    public void handlePostDeleted(PostDeletedEvent event) {
        log.info("[PostDeleted] Received: {}", event);

        try {
            commentServiceClient.deleteAllCommentsByPostId(event.getPostId());
        } catch (Exception e) {
            log.error("Failed to process PostDeletedEvent: {}", event, e);
            throw e;
        }
    }
}