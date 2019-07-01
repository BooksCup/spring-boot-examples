package com.bc.mongodb.service.impl;

import com.bc.mongodb.entity.User;
import com.bc.mongodb.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Service;

/**
 * 用户业务类实现
 *
 * @author zhou
 */
@Service("userService")
public class UserServiceImpl implements UserService {
    private static final Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);

    @Autowired
    private MongoTemplate mongoTemplate;

    /**
     * 保存用户
     *
     * @param user 用户
     */
    @Override
    public void saveUser(User user) {
        mongoTemplate.save(user);
    }
}
