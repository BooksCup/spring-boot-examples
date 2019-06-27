package com.bc.redis.dao.impl;

import com.bc.redis.dao.RedisDao;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

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

    // ===== ops for string begin =====

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

    /**
     * 设置值并设置过期时间
     *
     * @param key     键
     * @param value   值
     * @param timeout 过期时间(秒) timeout>0:timeout秒后过期  timeout<=0:无限期
     * @return
     */
    @Override
    public boolean set(String key, Object value, long timeout) {
        try {
            if (timeout > 0) {
                redisTemplate.opsForValue().set(key, value, timeout, TimeUnit.SECONDS);
            } else {
                redisTemplate.opsForValue().set(key, value);
            }
            return true;
        } catch (Exception e) {
            logger.error("set(expire) param[timeout]: " + timeout + ", error:" + e.getMessage());
            return false;
        }
    }

    /**
     * 在指定的key不存在时，为key设置指定的值
     * 如果指定key存在时，则会设置失败
     *
     * @param key   键
     * @param value 值
     * @return true: 设置成功   false: 设置失败
     */
    @Override
    public boolean setIfAbsent(String key, Object value) {
        return redisTemplate.opsForValue().setIfAbsent(key, value);
    }

    /**
     * 在指定的key不存在时，为key设置指定的值
     * 如果指定key存在时，则会设置失败
     *
     * @param key     键
     * @param value   值
     * @param timeout 过期时间
     * @param unit    时间颗粒度转换单元
     * @return true: 设置成功   false: 设置失败
     */
    @Override
    public boolean setIfAbsent(String key, Object value, long timeout, TimeUnit unit) {
        return redisTemplate.opsForValue().setIfAbsent(key, value, timeout, unit);
    }

    /**
     * 同时设置一个或多个key-value对
     *
     * @param map 一个或多个key-value对
     * @return true: 设置成功  false: 设置失败
     */
    @Override
    public boolean multiSet(Map<String, Object> map) {
        try {
            redisTemplate.opsForValue().multiSet(map);
            return true;
        } catch (Exception e) {
            logger.error("multiSet error: " + e.getMessage() + ", param[map]: " + map);
            return false;
        }
    }

    /**
     * 同时设置一个或多个key-value对，当且仅当所有给定key都不存在。
     * 只要存在一个key，则所有的key-value对都会设置失败
     *
     * @param map 一个或多个key-value对
     * @return true: 设置成功  false: 设置失败
     */
    @Override
    public boolean multiSetIfAbsent(Map<String, Object> map) {
        return redisTemplate.opsForValue().multiSetIfAbsent(map);
    }

    /**
     * 用于为指定的key追加值
     * 如果 key已经存在并且是一个字符串，APPEND命令将value追加到key原来的值的末尾
     * 如果 key不存在，APPEND就简单地将给定key设为value，就像执行SET key value一样
     *
     * @param key   键
     * @param value 值
     * @return 追加指定值之后，key中字符串的长度
     */
    @Override
    public Integer append(String key, String value) {
        return redisTemplate.opsForValue().append(key, value);
    }

    /**
     * 获取值
     *
     * @param key 键
     * @return 值
     */
    @Override
    public Object get(String key) {
        return key == null ? null : redisTemplate.opsForValue().get(key);
    }

    /**
     * 获取所有(一个或多个)给定key的值
     *
     * @param keyList (一个或多个)给定key
     * @return 一个包含所有给定key的值的列表
     */
    @Override
    public List<Object> multiGet(List<String> keyList) {
        List<Object> valueList = redisTemplate.opsForValue().multiGet(keyList);
        return valueList;
    }

    /**
     * 返回给定key的旧值。当key没有旧值时，即key不存在时，返回null
     *
     * @param key   键
     * @param value 新值
     * @return 旧值
     */
    @Override
    public Object getAndSet(String key, String value) {
        return redisTemplate.opsForValue().getAndSet(key, value);
    }

    /**
     * 获取key中字符串的长度
     *
     * @param key 键
     * @return key中字符串的长度
     */
    @Override
    public long size(String key) {
        return redisTemplate.opsForValue().size(key);
    }

    /**
     * 将key中储存的数字值增delta
     * 如果key不存在，那么key的值会先被初始化为0，然后再执行incr操作
     * 如果值包含错误的类型，或字符串类型的值不能表示为数字，那么返回一个错误
     *
     * @param key   键
     * @param delta 递增因子
     * @return 执行incr命令之后key的值
     */
    @Override
    public long increment(String key, long delta) {
        if (delta < 0) {
            throw new RuntimeException("递增因子必须大于0");
        }
        return redisTemplate.opsForValue().increment(key, delta);
    }

    /**
     * 将key中储存的数字值减delta
     * 如果key不存在，那么key的值会先被初始化为0，然后再执行decr操作
     * 如果值包含错误的类型，或字符串类型的值不能表示为数字，那么返回一个错误
     *
     * @param key   键
     * @param delta 递减因子
     * @return 执行decr命令之后key的值
     */
    @Override
    public long decrement(String key, long delta) {
        if (delta < 0) {
            throw new RuntimeException("递减因子必须大于0");
        }
        return redisTemplate.opsForValue().decrement(key, delta);
    }
    // ===== ops for string end =====
}
