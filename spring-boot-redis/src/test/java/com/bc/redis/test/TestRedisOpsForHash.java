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

/**
 * 测试redis操作hash
 *
 * @author zhou
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = RedisApplication.class)
public class TestRedisOpsForHash {
    /**
     * logger
     */
    private static final Logger logger = LoggerFactory.getLogger(TestRedisOpsForHash.class);

    @Resource
    private RedisDao redisDao;

    /**
     * 测试hset
     */
    @Test
    public void testHput() {
        String key = "hKey";
        String hashKey = "hField";
        String value = "hValue";
        boolean result = redisDao.hPut(key, hashKey, value);
        logger.info("hash put result: " + result);
    }

    /**
     * 测试hsetnx
     */
    @Test
    public void testHputIfAbsent() {
        String key = "hKey";
        String hashKey = "hField";
        String value = "hValue";
        boolean result = redisDao.hPutIfAbsent(key, hashKey, value);
        logger.info("hash put result: " + result);
    }

    /**
     * 测试hmset
     */
    @Test
    public void testHputAll() {
        String key = "hKey";
        Map<String, Object> hValueMap = new HashMap<>();
        hValueMap.put("hField1", "hValue1");
        hValueMap.put("hField2", "hValue2");
        hValueMap.put("hField3", "hValue3");
        boolean result = redisDao.hPutAll(key, hValueMap);
        logger.info("hash put all result: " + result);
    }

    /**
     * 测试hget
     */
    @Test
    public void testHget() {
        String key = "hKey";
        String hashKey = "hField";
        logger.info("key: " + key + ", hashKey: " + hashKey + ", value: " + redisDao.hGet(key, hashKey));
    }

    /**
     * 测试hgetall
     */
    @Test
    public void testHgetAll() {
        String key = "hKey";
        List<Object> hashKeyList = new ArrayList<>();
        hashKeyList.add("hField1");
        hashKeyList.add("hField3");
        List<Object> valueList = redisDao.hMultiGet(key, hashKeyList);
        for (Object value : valueList) {
            logger.info("value: " + value);
        }
    }

    /**
     * 测试hdel
     */
    @Test
    public void testHdelete() {
        String key = "hKey";
        long deleteNum = redisDao.hDelete(key, "hField2", "hField4");
        logger.info("delete num: " + deleteNum);
    }

    /**
     * 测试hvals
     */
    @Test
    public void testHvalues() {
        String key = "hKey";
        List<Object> valueList = redisDao.hValues(key);
        for (Object value : valueList) {
            logger.info("value: " + value);
        }
    }

    /**
     * 测试hlen
     */
    @Test
    public void testHlen() {
        String key = "hKey";
        long hashLength = redisDao.hSize(key);
        logger.info("hash length: " + hashLength);
    }

    /**
     * 测试hincrby
     */
    @Test
    public void testHincrement() {
        String key = "hKey";
        String hashKey = "hField2";
        long delta = 2L;
        long result = redisDao.hIncrement(key, hashKey, delta);
        logger.info("hash incr result: " + result);
    }

    /**
     * 测试hincrby(浮点数增量值)
     */
    @Test
    public void testHincrementWithDoubleDelta() {
        String key = "hKey";
        String hashKey = "hField2";
        double delta = -1.2;
        double result = redisDao.hIncrement(key, hashKey, delta);
        logger.info("hash incr result: " + result);
    }
}
