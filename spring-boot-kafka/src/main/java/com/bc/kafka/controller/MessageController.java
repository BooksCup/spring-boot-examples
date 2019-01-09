package com.bc.kafka.controller;

import com.bc.kafka.enums.ResponseMsg;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * 消息控制器
 *
 * @author zhou
 */
@RestController
@RequestMapping("/messages")
public class MessageController {
    /**
     * 日志
     */
    private static final Logger logger = LoggerFactory.getLogger(MessageController.class);

    @Resource
    private KafkaTemplate<String, String> kafkaTemplate;

    @ApiOperation(value = "发消息", notes = "发消息")
    @GetMapping(value = "")
    public ResponseEntity<String> sendMessage(
            @RequestParam String topic,
            @RequestParam String message) {
        ResponseEntity<String> responseEntity;
        try {
            kafkaTemplate.send(topic, message);
            responseEntity = new ResponseEntity<>(ResponseMsg.MESSAGE_SEND_SUCCESS.value(),
                    HttpStatus.OK);
        } catch (Exception e) {
            logger.error("send message error: " + e.getMessage());
            responseEntity = new ResponseEntity<>(ResponseMsg.MESSAGE_SEND_ERROR.value(),
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return responseEntity;
    }
}
