package com.bc.redis.test;

import com.bc.redis.RedisApplication;
import com.bc.redis.dao.RedisDao;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * 测试redis操作string
 *
 * @author zhou
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = RedisApplication.class)
public class TestRedisOpsForString {
    /**
     * logger
     */
    private static final Logger logger = LoggerFactory.getLogger(TestRedisOpsForString.class);
    @Resource
    private RedisDao redisDao;

    /**
     * 测试设置缓存
     */
    @Test
    public void testSet() {
        String key = "testKey";
        String value = "testValue";
        redisDao.set(key, value);
    }

    /**
     * 测试设置缓存(带过期时间)
     */
    @Test
    public void testSetExpire() {
        String key = "testExpireKey";
        String value = "testExpireValue";
        redisDao.set(key, value, 10);
    }

    /**
     * 测试setnx(SET if Not eXists)
     */
    @Test
    public void testSetIfAbsent() {
        String key = "testSetIfAbsentKey";
        String value = "testSetIfAbsentValue";
        boolean setResult = redisDao.setIfAbsent(key, value);
        logger.info("setIfAbsent result: " + setResult);
    }

    /**
     * 测试setnx(SET if Not eXists)，带过期时间
     */
    @Test
    public void testSetIfAbsentWithExpire() {
        String key = "testSetIfAbsentKey";
        String value = "testSetIfAbsentValue";
        long timeout = 10L;
        boolean setResult = redisDao.setIfAbsent(key, value, timeout, TimeUnit.SECONDS);
        logger.info("setIfAbsent result: " + setResult);
    }

    /**
     * 测试批量set
     */
    @Test
    public void testMultiSet() {
        Map<String, Object> map = new HashMap<>();
        String key1 = "key1";
        String value1 = "value1";
        String key2 = "key2";
        String value2 = "value2";
        String key3 = "key3";
        String value3 = "value3";
        map.put(key1, value1);
        map.put(key2, value2);
        map.put(key3, value3);
        redisDao.multiSet(map);
    }

    /**
     * 测试批量setnx
     */
    @Test
    public void testMultiSetIfAbsent() {
        Map<String, Object> map = new HashMap<>();
//        String key1 = "key1";
//        String value1 = "value1";
        String key4 = "key4";
        String value4 = "value4";
//        map.put(key1, value1);
        map.put(key4, value4);
        boolean setResult = redisDao.multiSetIfAbsent(map);
        logger.info("multiSetIfAbsent result: " + setResult);
    }

    /**
     * 测试批量获取value
     */
    @Test
    public void testMultiGet() {
        String key1 = "key1";
        String key2 = "key2";
        String key3 = "key3";
        List<String> keyList = new ArrayList<>();
        keyList.add(key1);
        keyList.add(key2);
        keyList.add(key3);
        List<Object> valueList = redisDao.multiGet(keyList);
        logger.info("=== multi get ===");
        for (Object value : valueList) {
            logger.info("value: " + value);
        }
    }

    /**
     * 测试append
     */
    @Test
    public void testAppend() {
        String key = "key1";
        String value = "appendValue";
        Integer newValueLength = redisDao.append(key, value);
        logger.info("new value length: " + newValueLength);
    }

    /**
     * 测试获取缓存
     */
    @Test
    public void testGet() {
        String key = "testKey";
        Object value = redisDao.get(key);
        logger.info("get Value: " + value + ", key: " + key);
    }

    /**
     * 测试getset
     */
    @Test
    public void testGetAndSet() {
        String key = "testGetAndSetKey";
        String newValue = "newValue22";
        Object oldValue = redisDao.getAndSet(key, newValue);
        logger.info("get oldValue: " + oldValue + ", key: " + key + ", newValue: " + newValue);
    }

    /**
     * 测试size
     */
    @Test
    public void testSize() {
        String key = "key1";
        logger.info("get Size: " + redisDao.size(key) + ", key: " + key);
    }

    /**
     * 测试increment
     */
    @Test
    public void testIncrement() {
        String key = "testIncrementKey";
        Long delta = 2L;
        Long value = redisDao.increment(key, delta);
        logger.info("get Value: " + value + ", key: " + key);
    }

    @Test
    public void testDecrement() {
        String key = "testDecrementKey";
        long delta = 2L;
        Long value = redisDao.decrement(key, delta);
        logger.info("get Value: " + value + ", key: " + key);
    }
}
