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
}
