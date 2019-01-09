package com.bc.kafka.mq.consumer;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

/**
 * 消息消费者
 *
 * @author zhou
 */
@Component
public class MessageConsumer {
    /**
     * 日志
     */
    private static final Logger logger = LoggerFactory.getLogger(MessageConsumer.class);

    @KafkaListener(topics = "test")
    public void listen(ConsumerRecord<String, String> record) {
        String value = record.value();
        logger.info("value: " + value);
        logger.info("record: " + record);
    }
}
