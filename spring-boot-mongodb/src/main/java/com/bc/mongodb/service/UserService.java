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

    /**
     * 获取用户
     *
     * @param id 用户id
     * @return 用户
     */
    User getUserById(Long id);

    /**
     * 根据用户名查询获取用户
     *
     * @param userName 用户名
     * @return 用户
     */
    User getUserByUserName(String userName);

    /**
     * 修改用户
     *
     * @param user 用户
     * @return 修改成功的数量
     */
    long updateUser(User user);

    /**
     * 根据用户id删除用户
     *
     * @param id 用户id
     */
    void deleteUserById(Long id);
}
