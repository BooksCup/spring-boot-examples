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
 * 绑定队列: topic.messages
 * routingKey: topic.#
 * 可以使用通配符进行模糊匹配
 * 符号"#"匹配一个或多个词
 * 符号"*"匹配不多不少一个词
 * 例如:"log.#"能够匹配到"log.info.oa"
 * "log.*"只会匹配到"log.error"
 *
 * @author zhou
 */
@Component
@RabbitListener(queues = "topic.messages")
public class TopicReceiverB {
    /**
     * 日志
     */
    private static final Logger logger = LoggerFactory.getLogger(FanoutReceiverC.class);

    @RabbitHandler
    public void process(String message) {
        logger.info("topic receiver B: " + message);
    }

}
