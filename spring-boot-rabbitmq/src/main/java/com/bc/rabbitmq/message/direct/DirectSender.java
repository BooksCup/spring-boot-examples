package com.bc.rabbitmq.message.direct;

import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 消息发送者
 * 交换机类型: direct
 *
 * @author zhou
 */
@Component
public class DirectSender {
    @Autowired
    private AmqpTemplate rabbitTemplate;

    public void send(String message) {
        this.rabbitTemplate.convertAndSend("direct.queue", message);
    }
}
