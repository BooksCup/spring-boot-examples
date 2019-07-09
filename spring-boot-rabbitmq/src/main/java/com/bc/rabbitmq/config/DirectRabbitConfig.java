package com.bc.rabbitmq.config;

import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 交换机: direct的配置
 *
 * @author zhou
 */
@Configuration
public class DirectRabbitConfig {
    @Bean
    public Queue directQueue() {
        return new Queue("direct.queue");
    }
}
