package com.bc.redis.dao.impl;

import com.bc.redis.dao.RedisDao;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

/**
 * redis数据操作实现类
 *
 * @author zhou
 */
@Component
public class RedisDaoImpl implements RedisDao {
    /**
     * logger
     */
    private static final Logger logger = LoggerFactory.getLogger(RedisDaoImpl.class);

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    /**
     * 设置值
     *
     * @param key   键
     * @param value 值
     * @return true:设置成功 false:设置失败
     */
    @Override
    public boolean set(String key, Object value) {
        try {
            redisTemplate.opsForValue().set(key, value);
            return true;
        } catch (Exception e) {
            logger.error("set error: " + e.getMessage());
            return false;
        }
    }
}
