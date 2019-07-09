package com.bc.rabbitmq.message.fanout;

import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class FanoutSender {
    @Autowired
    private AmqpTemplate rabbitTemplate;

    public void send(String message) {
        this.rabbitTemplate.convertAndSend("fanoutExchange", "", message);
    }
}
