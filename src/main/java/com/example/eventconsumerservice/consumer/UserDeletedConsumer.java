package com.example.eventconsumerservice.consumer;

import com.example.eventconsumerservice.client.AuthServiceClient;
import com.example.eventconsumerservice.client.CommentServiceClient;
import com.example.eventconsumerservice.client.PostServiceClient;
import com.example.eventconsumerservice.event.UserDeletedEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class UserDeletedConsumer {

    private final AuthServiceClient authServiceClient;
    private final PostServiceClient postServiceClient;
    private final CommentServiceClient commentServiceClient;

    @RabbitListener(
            queues = "${rabbitmq.queues.user.deleted}",
            containerFactory = "eventContainerFactory"
    )
    public void handleUserDeleted(UserDeletedEvent event) {

        log.info("[UserDeleted] Received: {}", event);

        try {
            authServiceClient.deleteUser(event.getUserId());
            postServiceClient.deleteAllCommentsByUserId(event.getUserId());
            commentServiceClient.deleteAllCommentsByUserId(event.getUserId());
        } catch (Exception e) {
            // 로그만 남기고 throw (→ 재시도 유도)
            log.error("Failed to process UserDeletedEvent: {}", event, e);
            throw e;
        }
    }
}