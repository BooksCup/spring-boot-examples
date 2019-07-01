package com.bc.mongodb.service.impl;

import com.bc.mongodb.entity.User;
import com.bc.mongodb.service.UserService;
import com.mongodb.client.result.UpdateResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

/**
 * 用户业务类实现
 *
 * @author zhou
 */
@Service("userService")
public class UserServiceImpl implements UserService {
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

    /**
     * 获取用户
     *
     * @param id 用户id
     * @return 用户
     */
    @Override
    public User getUserById(Long id) {
        Query query = new Query(Criteria.where("id").is(id));
        return mongoTemplate.findOne(query, User.class);
    }

    /**
     * 根据用户名查询获取用户
     *
     * @param userName 用户名
     * @return 用户
     */
    @Override
    public User getUserByUserName(String userName) {
        Query query = new Query(Criteria.where("userName").is(userName));
        return mongoTemplate.findOne(query, User.class);
    }

    /**
     * 修改用户
     *
     * @param user 用户
     * @return 修改成功的数量
     */
    @Override
    public long updateUser(User user) {
        Query query = new Query(Criteria.where("id").is(user.getId()));
        Update update = new Update().set("userName", user.getUserName()).set("passWord", user.getPassWord());
        //更新查询返回结果集的第一条
        UpdateResult result = mongoTemplate.updateFirst(query, update, User.class);
        return result.getMatchedCount();
    }

    /**
     * 根据用户id删除用户
     *
     * @param id 用户id
     */
    @Override
    public void deleteUserById(Long id) {
        Query query = new Query(Criteria.where("id").is(id));
        mongoTemplate.remove(query, User.class);
    }
}
