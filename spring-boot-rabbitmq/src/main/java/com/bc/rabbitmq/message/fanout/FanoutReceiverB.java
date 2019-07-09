package com.bc.rabbitmq.message.fanout;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

/**
 * 消息接受者
 * 交换机类型: fanout
 * 交换机: fanoutExchange
 * 绑定队列: fanout.B
 *
 * @author zhou
 */
@Component
@RabbitListener(queues = "fanout.B")
public class FanoutReceiverB {
    /**
     * 日志
     */
    private static final Logger logger = LoggerFactory.getLogger(FanoutReceiverB.class);

    @RabbitHandler
    public void process(String message) {
        logger.info("fanout receiver B: " + message);
    }
}
