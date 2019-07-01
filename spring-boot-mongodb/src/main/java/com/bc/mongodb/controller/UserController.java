package com.bc.mongodb.controller;

import com.bc.mongodb.entity.User;
import com.bc.mongodb.enums.ResponseMsg;
import com.bc.mongodb.service.UserService;
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
 * 用户
 *
 * @author zhou
 */
@RestController
@RequestMapping("/users")
public class UserController {
    private static final Logger logger = LoggerFactory.getLogger(UserController.class);

    @Resource
    private UserService userService;

    /**
     * 创建用户
     *
     * @param id       id
     * @param userName 用户名
     * @param password 密码
     * @return ResponseEntity
     */
    @ApiOperation(value = "创建用户", notes = "创建用户")
    @PostMapping(value = "")
    public ResponseEntity<String> saveUser(@RequestParam Long id,
                                           @RequestParam String userName,
                                           @RequestParam String password) {
        User user = new User(id, userName, password);
        try {
            userService.saveUser(user);
            return new ResponseEntity<>(ResponseMsg.ADD_USER_SUCCESS.value(), HttpStatus.OK);
        } catch (Exception e) {
            logger.error("saveUser error: " + e.getMessage());
            return new ResponseEntity<>(ResponseMsg.ADD_USER_ERROR.value(),
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
