package com.example.eventconsumerservice.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.config.RetryInterceptorBuilder;
import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.retry.RejectAndDontRequeueRecoverer;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitConfig {

    // TODO: 큐 이름은 왜 설정파일에서 불러오지?
    // 큐 이름을 설정 파일에서 읽어온다.
    @Value("${queue.userCreated}")
    private String userCreatedQueue;

    @Value("${queue.userDeleted}")
    private String userDeletedQueue;

    // user.created.queue라는 이름으로 큐를 생성 (큐는 메시지를 저장하는 역할을 한다.)
    // 뒤의 true는 durable 옵션으로 RabbitMQ 서버가 재시작되더라도 큐가 유지되도록 한다.
    // false로 설정하면 서버 재시작 시 큐가 사라진다.
    @Bean
    public Queue userCreatedQueue() {
        return new Queue(userCreatedQueue, true);
    }

    @Bean
    public Queue userDeletedQueue() {
        return new Queue(userDeletedQueue, true);
    }

    // user.exchange라는 이름으로 토픽 익스체인지 생성
    // 이 익스체인지는 메시지를 라우팅하는 역할을 한다.
    // TODO: 토픽 익스체인지는 왜 설정파일에서 불러오지 않는지?
    @Bean
    public TopicExchange userExchange() {
        return new TopicExchange("user.exchange");
    }

    // cuser.created 라벨을 가진 메시지는 userCreatedQueue()로 전달하도록 바인딩 설정
    @Bean
    public Binding bindingUserCreated() {
        return BindingBuilder
                .bind(userCreatedQueue())
                .to(userExchange())
                // TODO: 라우팅 키는 왜 설정파일에서 불러오지 않는지?
                .with("user.created");
    }

    @Bean
    public Binding bindingUserDeleted() {
        return BindingBuilder
                .bind(userDeletedQueue())
                .to(userExchange())
                .with("user.deleted");
    }

    @Bean
    public MessageConverter jsonMessageConverter() {
        return new Jackson2JsonMessageConverter();
    }


    @Bean
    public SimpleRabbitListenerContainerFactory eventContainerFactory(ConnectionFactory connectionFactory) {
        SimpleRabbitListenerContainerFactory factory = new SimpleRabbitListenerContainerFactory();
        factory.setConnectionFactory(connectionFactory);
        factory.setMessageConverter(jsonMessageConverter());

        RetryInterceptorBuilder.StatelessRetryInterceptorBuilder retryInterceptor =
                RetryInterceptorBuilder.stateless()
                        .maxAttempts(2) // 최초 + 1번 재시도
                        .backOffOptions(1000, 2.0, 5000) // 1초 → 최대 5초 사이 증가
                        .recoverer(new RejectAndDontRequeueRecoverer()); // 실패 시 재큐하지 않음

        factory.setAdviceChain(retryInterceptor.build());

        return factory;
    }

}

