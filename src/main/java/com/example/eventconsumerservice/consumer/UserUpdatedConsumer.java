package com.example.eventconsumerservice.consumer;

import com.example.eventconsumerservice.client.AuthServiceClient;
import com.example.eventconsumerservice.client.CommentServiceClient;
import com.example.eventconsumerservice.client.PostServiceClient;
import com.example.eventconsumerservice.config.RabbitProperties;
import com.example.eventconsumerservice.event.UpdateAuthorInfoEvent;
import com.example.eventconsumerservice.event.UserDeletedEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class UserUpdatedConsumer {

    private final PostServiceClient postServiceClient;
    private final CommentServiceClient commentServiceClient;

    @RabbitListener(
            queues = "#{@rabbitProperties.queues.user.updated}",
            containerFactory = "eventContainerFactory"
    )
    public void handleUserUpdated(UpdateAuthorInfoEvent event) {

        log.info("[UserUpdated] Received: {}", event);

        try {
            postServiceClient.updateAuthorInfo(event);
            commentServiceClient.updateAuthorInfo(event);
        } catch (Exception e) {
            // 로그만 남기고 throw (→ 재시도 유도)
            log.error("Failed to process UserUpdatedEvent: {}", event, e);
            throw e;
        }
    }
}