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
import java.util.*;

/**
 * 测试redis操作set
 *
 * @author zhou
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = RedisApplication.class)
public class TestRedisOpsForZSet {
    /**
     * logger
     */
    private static final Logger logger = LoggerFactory.getLogger(TestRedisOpsForString.class);

    @Resource
    private RedisDao redisDao;

    /**
     * 测试zadd
     */
    @Test
    public void testZAdd() {
        String key = "zsetKey";
        String value = "zsetValue";
        boolean addResult = redisDao.zAdd(key, value, 1.3);
        logger.info("add element to zset, add result: " + addResult);
    }

    /**
     * 测试批量zadd
     */
    @Test
    public void testBatchZadd() {
        String key = "zsetKey";
        Map<String, Double> valueScoreMap = new HashMap<>();
        String value1 = "zsetValue";
        double score1 = 1.0;
        valueScoreMap.put(value1, score1);
        String value2 = "zsetValue2";
        double score2 = 2.0;
        valueScoreMap.put(value2, score2);
        long addNum = redisDao.zAdd(key, valueScoreMap);
        logger.info("add element to zset, add num: " + addNum);
    }

    /**
     * 测试zcard
     */
    @Test
    public void testZcard() {
        String key = "zsetKey";
        long setSize = redisDao.zCard(key);
        logger.info("set's size: " + setSize);
    }

    /**
     * 测试zcount
     */
    @Test
    public void testZcount() {
        String key = "zsetKey";
        double min = 0.3;
        double max = 2.3;
        logger.info("section: [" + min + ", " + max + "], count: " +
                redisDao.zCount(key, min, max));
    }

    /**
     * 测试zincrby
     */
    @Test
    public void testZincrementScore() {
        String key = "zsetKey";
        String value = "zsetValue";
        double incrResult = redisDao.zIncrementScore(key, value, -6);
        logger.info("zset incr, result: " + incrResult);
    }

    /**
     * 测试zinterstore
     */
    @Test
    public void testZintersectAndStore() {
        String key = "zsetKey";
        String otherKey = "zsetKey2";
        String destKey = "zsetKey3";
        long destZsetSize = redisDao.zIntersectAndStore(key, otherKey, destKey);
        logger.info("dest zset size: " + destZsetSize);
    }

    /**
     * 测试zinterstore
     */
    @Test
    public void testZintersectAndStoreWithManyOtherKeys() {
        String key = "zsetKey";
        String otherKey = "zsetKey2";
        String otherKey2 = "zsetKey3";
        List<String> otherKeys = new ArrayList<>();
        otherKeys.add(otherKey);
        otherKeys.add(otherKey2);
        String destKey = "zsetKey4";
        long destZsetSize = redisDao.zIntersectAndStore(key, otherKeys, destKey);
        logger.info("dest zset size: " + destZsetSize);
    }

    /**
     * 测试zrank
     */
    @Test
    public void testZrank() {
        String key = "zsetKey";
        String value = "zsetValue";
        long rank = redisDao.zRank(key, value);
        logger.info("key: " + key + ", member: "
                + value + ", rank:" + rank);
    }

    @Test
    public void testZremove() {
        String key = "zsetKey";
        String value1 = "zsetValue";
        String value2 = "zsetValue2";
        long removeNum = redisDao.zRemove(key, value1, value2);
        logger.info("remove num: " + removeNum);
    }
}
