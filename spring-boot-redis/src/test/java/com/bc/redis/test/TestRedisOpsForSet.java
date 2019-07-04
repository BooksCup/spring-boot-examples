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
import java.util.List;
import java.util.Set;

/**
 * 测试redis操作set
 *
 * @author zhou
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = RedisApplication.class)
public class TestRedisOpsForSet {
    /**
     * logger
     */
    private static final Logger logger = LoggerFactory.getLogger(TestRedisOpsForString.class);

    @Resource
    private RedisDao redisDao;

    /**
     * 测试sadd
     */
    @Test
    public void testSetAdd() {
        String key = "setKey3";
        long addNum = redisDao.sAdd(key, "a", "c", "e");
        logger.info("add element to set, add num: " + addNum);
    }

    /**
     * 测试scard
     */
    @Test
    public void testGetSetSize() {
        String key = "setKey";
        long size = redisDao.sSize(key);
        logger.info("get size. key: " + key + ", size: " + size);
    }

    /**
     * 测试sdiff(两个key比较)
     */
    @Test
    public void testSetDifference() {
        String key = "setKey";
        String otherKey = "setKey2";
        Set<Object> resultSet = redisDao.sDifference(key, otherKey);
        logger.info("===== get difference =====");
        for (Object result : resultSet) {
            logger.info(result == null ? "" : result.toString());
        }
    }

    /**
     * 测试sdiff(多个key比较)
     */
    @Test
    public void testSetDifferenceWithManyOtherKeys() {
        String key = "setKey";
        String otherKey1 = "setKey2";
        String otherKey2 = "setKey3";
        List<String> otherKeyList = new ArrayList<>();
        otherKeyList.add(otherKey1);
        otherKeyList.add(otherKey2);
        Set<Object> resultSet = redisDao.sDifference(key, otherKeyList);
        logger.info("===== get difference =====");
        for (Object result : resultSet) {
            logger.info(result == null ? "" : result.toString());
        }
    }

    /**
     * 测试sdiffstore(两个key)
     */
    @Test
    public void testSetDifferenceAndStore() {
        String key = "setKey";
        String otherKey = "setKey2";
        String destKey = "setKey4";
        long destSetSize = redisDao.sDifferenceAndStore(key, otherKey, destKey);
        logger.info("dest set size: " + destSetSize);
    }

    /**
     * 测试sdiffstore(多个key)
     */
    @Test
    public void testSetDifferenceAndStoreWithManyOtherKeys() {
        String key = "setKey";
        String otherKey1 = "setKey2";
        String otherKey2 = "setKey3";
        String destKey = "setKey4";
        List<String> otherKeyList = new ArrayList<>();
        otherKeyList.add(otherKey1);
        otherKeyList.add(otherKey2);
        long destSetSize = redisDao.sDifferenceAndStore(key, otherKeyList, destKey);
        logger.info("dest set size: " + destSetSize);
    }
}
