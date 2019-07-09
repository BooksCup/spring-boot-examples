package com.bc.rabbitmq.message.fanout;

import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 消息发送者
 * 交换机类型: fanout
 * 交换机: fanoutExchange
 *
 * @author zhou
 */
@Component
public class FanoutSender {
    @Autowired
    private AmqpTemplate rabbitTemplate;

    public void send(String message) {
        this.rabbitTemplate.convertAndSend("fanoutExchange", "", message);
    }
}
