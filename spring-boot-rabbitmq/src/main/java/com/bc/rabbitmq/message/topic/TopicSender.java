package com.bc.rabbitmq.message.topic;

import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 消息发送者
 * 交换机类型: topic
 * 交换机: topicExchange
 *
 * @author zhou
 */
@Component
public class TopicSender {
    @Autowired
    private AmqpTemplate rabbitTemplate;

    /**
     * 发送消息
     *
     * @param routingkey 路由Key
     * @param message    消息
     */
    public void send(String routingkey, String message) {
        this.rabbitTemplate.convertAndSend("topicExchange", routingkey, message);
    }
}
