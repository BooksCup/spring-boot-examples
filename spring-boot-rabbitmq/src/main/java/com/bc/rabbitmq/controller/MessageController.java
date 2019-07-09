package com.bc.rabbitmq.controller;

import com.bc.rabbitmq.message.fanout.FanoutSender;
import io.swagger.annotations.ApiOperation;
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
    @Resource
    private FanoutSender fanoutSender;

    @ApiOperation(value = "发送消息(exchange: fanout)", notes = "发送消息(exchange: fanout)")
    @PostMapping(value = "")
    public ResponseEntity<String> sendMessageToFanoutExchange(@RequestParam String message) {
        fanoutSender.send(message);
        return new ResponseEntity<>("ok", HttpStatus.OK);
    }
}
