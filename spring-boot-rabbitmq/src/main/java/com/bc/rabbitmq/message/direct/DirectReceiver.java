package com.bc.rabbitmq.message.direct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

/**
 * 消息接受者
 * 交换机类型: direct
 *
 * @author zhou
 */
@Component
@RabbitListener(queues = "direct.queue")
public class DirectReceiver {
    /**
     * 日志
     */
    private static final Logger logger = LoggerFactory.getLogger(DirectReceiver.class);

    @RabbitHandler
    public void process(String neo) {
        logger.info("direct receiver: " + neo);
    }
}
