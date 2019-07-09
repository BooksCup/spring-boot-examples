package com.bc.rabbitmq.message.topic;

import com.bc.rabbitmq.message.fanout.FanoutReceiverC;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;


/**
 * 消息接受者
 * 交换机类型: topic
 * 交换机: topicExchange
 * 绑定队列: topic.message
 * routingKey: topic.message
 *
 * @author zhou
 */
@Component
@RabbitListener(queues = "topic.message")
public class TopicReceiverA {
    /**
     * 日志
     */
    private static final Logger logger = LoggerFactory.getLogger(FanoutReceiverC.class);

    @RabbitHandler
    public void process(String message) {
        logger.info("topic receiver A: " + message);
    }
}
