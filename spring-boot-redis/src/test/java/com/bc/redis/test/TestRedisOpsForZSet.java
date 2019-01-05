package com.bc.redis.test;

import com.bc.redis.RedisApplication;
import com.bc.redis.dao.RedisDao;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.ZSetOperations;
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
     * 测试zunionstore
     */
    @Test
    public void testZunionAndStore() {
        String key = "zsetKey";
        String otherKey = "zsetKey2";
        String destKey = "zsetKey3";
        long destZsetSize = redisDao.zUnionAndStore(key, otherKey, destKey);
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

    /**
     * 测试zrem
     */
    @Test
    public void testZremove() {
        String key = "zsetKey";
        String value1 = "zsetValue";
        String value2 = "zsetValue2";
        long removeNum = redisDao.zRemove(key, value1, value2);
        logger.info("remove num: " + removeNum);
    }

    /**
     * 测试zremrangebyrank
     */
    @Test
    public void testZremoveRange() {
        String key = "zsetKey";
        long removeNum = redisDao.zRemoveRange(key, 0, 2);
        logger.info("remove num: " + removeNum);
    }

    /**
     * 测试zrangebyscore
     */
    @Test
    public void testZremoveRangeByScore() {
        String key = "zsetKey";
        long removeNum = redisDao.zRemoveRangeByScore(key, 1.2, 6);
        logger.info("remove num: " + removeNum);
    }

    /**
     * 测试zrevrange
     */
    @Test
    public void testZreverseRange() {
        String key = "zsetKey";
        Set<Object> valueSet = redisDao.zReverseRange(key, 0, 2);
        for (Object value : valueSet) {
            logger.info("value: " + value);
        }
    }

    /**
     * 测试zrevrangeWithScores
     */
    @Test
    public void testZreverseRangeWithScores() {
        String key = "zsetKey";
        Set<ZSetOperations.TypedTuple<Object>> tuples = redisDao.zReverseRangeWithScores(key, 0, 2);
        for (ZSetOperations.TypedTuple<Object> tuple : tuples) {
            logger.info("value: " + tuple.getValue() + ", score: " + tuple.getScore());
        }
    }

    /**
     * 测试zrevrangebyscore
     */
    @Test
    public void testZReverseRangeByScore() {
        String key = "zsetKey";
        Set<Object> valueSet = redisDao.zReverseRangeByScore(key, 1, 9);
        for (Object value : valueSet) {
            logger.info("value: " + value);
        }
    }

    /**
     * 测试zrevrangebyscore(带分页)
     */
    @Test
    public void testZReverseRangeByScoreWithPages() {
        String key = "zsetKey";
        Set<Object> valueSet = redisDao.zReverseRangeByScore(key, 1, 9, 1, 1);
        for (Object value : valueSet) {
            logger.info("value: " + value);
        }
    }

    /**
     * 测试zreverseRangeByScoreWithScores
     */
    @Test
    public void testZreverseRangeByScoreWithScores() {
        String key = "zsetKey";
        Set<ZSetOperations.TypedTuple<Object>> tuples = redisDao.zReverseRangeByScoreWithScores(key, 1, 9);
        for (ZSetOperations.TypedTuple<Object> tuple : tuples) {
            logger.info("value: " + tuple.getValue() + ", score: " + tuple.getScore());
        }
    }

    /**
     * 测试zreverseRangeByScoreWithScores(带分页)
     */
    @Test
    public void testZreverseRangeByScoreWithScoresAndPages() {
        String key = "zsetKey";
        Set<ZSetOperations.TypedTuple<Object>> tuples = redisDao.zReverseRangeByScoreWithScores(key, 1, 9, 1, 1);
        for (ZSetOperations.TypedTuple<Object> tuple : tuples) {
            logger.info("value: " + tuple.getValue() + ", score: " + tuple.getScore());
        }
    }

    /**
     * 测试zrevrank
     */
    @Test
    public void testZreverseRank() {
        String key = "zsetKey";
        String value = "zsetValue1";
        long rank = redisDao.zReverseRank(key, value);
        logger.info("value: " + value + ", rank: " + rank);
    }

    /**
     * 测试zscore
     */
    @Test
    public void testZscore() {
        String key = "zsetKey";
        String value = "zsetValue";
        double score = redisDao.zScore(key, value);
        logger.info("value: " + value + ", score: " + score);
    }
}
