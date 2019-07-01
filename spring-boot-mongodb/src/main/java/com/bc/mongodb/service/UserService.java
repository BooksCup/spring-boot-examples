package com.bc.mongodb.service;

import com.bc.mongodb.entity.User;

/**
 * 用户业务类接口
 *
 * @author zhou
 */
public interface UserService {
    /**
     * 保存用户
     *
     * @param user 用户
     */
    void saveUser(User user);
}
