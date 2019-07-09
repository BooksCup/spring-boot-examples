package com.bc.rabbitmq.controller;

import com.bc.rabbitmq.enums.ResponseMsg;
import com.bc.rabbitmq.message.fanout.FanoutSender;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * 消息
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
    private FanoutSender fanoutSender;

    /**
     * 发送消息至fanout交换机
     *
     * @param message 消息
     * @return ResponseEntity
     */
    @ApiOperation(value = "发送消息至fanout交换机", notes = "发送消息至fanout交换机")
    @PostMapping(value = "/fanout")
    public ResponseEntity<String> sendMessageToFanoutExchange(@RequestParam String message) {
        ResponseEntity<String> responseEntity;
        try {
            fanoutSender.send(message);
            responseEntity = new ResponseEntity<>(ResponseMsg.MESSAGE_SEND_SUCCESS.value(),
                    HttpStatus.OK);
        } catch (Exception e) {
            logger.error("send message to fanout exchange error: " + e.getMessage());
            responseEntity = new ResponseEntity<>(ResponseMsg.MESSAGE_SEND_ERROR.value(),
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return responseEntity;
    }
}
