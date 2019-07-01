package com.bc.mongodb.controller;

import com.bc.mongodb.entity.User;
import com.bc.mongodb.enums.ResponseMsg;
import com.bc.mongodb.service.UserService;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

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

    /**
     * 获取用户
     *
     * @param id id
     * @return ResponseEntity<User>
     */
    @ApiOperation(value = "获取用户", notes = "获取用户")
    @GetMapping(value = "/{id}")
    public ResponseEntity<User> getUserById(@PathVariable Long id) {
        try {
            User user = userService.getUserById(id);
            return new ResponseEntity<>(user, HttpStatus.OK);
        } catch (Exception e) {
            logger.error("saveUser error: " + e.getMessage());
            return new ResponseEntity<>(new User(),
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * 根据用户名查询获取用户
     *
     * @param userName 用户名
     * @return ResponseEntity<User>
     */
    @ApiOperation(value = "获取用户", notes = "获取用户")
    @GetMapping(value = "")
    public ResponseEntity<User> getUserByUserName(@RequestParam String userName) {
        try {
            User user = userService.getUserByUserName(userName);
            return new ResponseEntity<>(user, HttpStatus.OK);
        } catch (Exception e) {
            logger.error("saveUser error: " + e.getMessage());
            return new ResponseEntity<>(new User(),
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * 修改用户
     *
     * @param id       用户id
     * @param userName 用户名
     * @param passWord 用户密码
     * @return ResponseEntity
     */
    @ApiOperation(value = "修改用户", notes = "修改用户")
    @PutMapping(value = "/{id}")
    public ResponseEntity<String> updateUser(@PathVariable Long id,
                                             @RequestParam(value = "userName", required = false) String userName,
                                             @RequestParam(value = "passWord", required = false) String passWord) {
        try {
            User user = userService.getUserById(id);
            if (!StringUtils.isEmpty(userName)) {
                user.setUserName(userName);
            }
            if (!StringUtils.isEmpty(passWord)) {
                user.setPassWord(passWord);
            }
            userService.updateUser(user);
            return new ResponseEntity<>(ResponseMsg.UPDATE_USER_SUCCESS.value(), HttpStatus.OK);
        } catch (Exception e) {
            logger.error("deleteUser error: " + e.getMessage());
            return new ResponseEntity<>(ResponseMsg.UPDATE_USER_ERROR.value(),
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * 删除用户
     *
     * @param id 用户id
     * @return ResponseEntity
     */
    @ApiOperation(value = "删除用户", notes = "删除用户")
    @DeleteMapping(value = "/{id}")
    public ResponseEntity<String> deleteUser(@PathVariable Long id) {
        try {
            userService.deleteUserById(id);
            return new ResponseEntity<>(ResponseMsg.DELETE_USER_SUCCESS.value(), HttpStatus.OK);
        } catch (Exception e) {
            logger.error("deleteUser error: " + e.getMessage());
            return new ResponseEntity<>(ResponseMsg.DELETE_USER_ERROR.value(),
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
