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

/**
 * 测试redis操作list
 *
 * @author zhou
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = RedisApplication.class)
public class TestRedisOpsForList {

    /**
     * logger
     */
    private static final Logger logger = LoggerFactory.getLogger(TestRedisOpsForString.class);

    @Resource
    private RedisDao redisDao;

    /**
     * 测试rpush
     */
    @Test
    public void testRightPush() {
        String key = "listKey";
        String value = "listValue2";
        long listSize = redisDao.lRightPush(key, value);
        logger.info("list.size: " + listSize);
    }

    /**
     * 测试rpush(带支点)
     */
    @Test
    public void testRightPushWithPivot() {
        String key = "listKey";
        String pivot = "listValue2";
        String value = "newListValue";
        long listSize = redisDao.lRightPush(key, pivot, value);
        logger.info("list.size: " + listSize);
    }

    /**
     * 测试批量rpush
     */
    @Test
    public void testBatchRightPush() {
        String key = "listKey";
        String value1 = "value1";
        String value2 = "value2";
        String value3 = "value3";
        long listSize = redisDao.lRightPushAll(key, value1, value2, value3);
        logger.info("list.size: " + listSize);
    }

    /**
     * 测试rightPushIfPresent
     */
    @Test
    public void testRightPushIfPresent() {
        String key = "listKey1";
        String value = "value4";
        long listSize = redisDao.lRightPushIfPresent(key, value);
        logger.info("list.size: " + listSize);
    }
}
