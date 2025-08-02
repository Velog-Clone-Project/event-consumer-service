package com.example.eventconsumerservice.consumer;

import com.example.eventconsumerservice.client.UserServiceClient;
import com.example.eventconsumerservice.event.UserCreatedEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class UserCreatedConsumer {

    // FeignClient를 통해 user-service와 통신
    private final UserServiceClient userServiceClient;

    // RabbitMQ에서 UserCreatedEvent를 수신하는 리스너
    @RabbitListener(
            // 어떤 큐에서 메시지를 받을지 지정
            queues = "${queue.userCreated}",
            // 메시지 수신 컨테이너에 재시도/예외 처리 설정을 적용하기 위한 팩토리 사용
            containerFactory = "eventContainerFactory"
    )
    public void handleUserCreated(UserCreatedEvent event) {

        log.info("[UserCreated] Received: {}", event);

        try {
            // 받은 메시지(UserCreatedEvent)를 user-service로 요청
            userServiceClient.createUser(event);
        } catch (Exception e) {
            // 예외가 발생시 로그 출력 후
            // throw e를 통해 예외를 다시 던져 RabbitMQ가 재시도할 수 있도록 유도
            // 단, containerFactory 에서 재시도 1회로 제한되어 있음)
            log.error("Failed to process UserCreatedEvent: {}", event, e);
            throw e;
        }
    }
}