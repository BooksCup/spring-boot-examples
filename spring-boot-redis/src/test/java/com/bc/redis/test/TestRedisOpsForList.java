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
import java.util.List;
import java.util.concurrent.TimeUnit;

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
        String value4 = "value4";
        String value5 = "value2";
        long listSize = redisDao.lRightPushAll(key, value1, value2, value3, value4, value5);
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

    /**
     * 测试rpop
     */
    @Test
    public void testRightPop() {
        String key = "listKey";
        logger.info("rpop remove value: " + redisDao.lRightPop(key));
    }

    /**
     * 测试brpop
     */
    @Test
    public void testBlockRightPop() {
        String key = "listKey1";
        logger.info("brpop remove value: " + redisDao.lRightPop(key, 5L, TimeUnit.SECONDS));
    }

    /**
     * 测试rpoplpush
     */
    @Test
    public void testRightPopAndLeftPush() {
        String sourceKey = "listKey";
        String destinationKey = "listKey1";
        logger.info("rpoplpush remove value: " + redisDao.lRightPopAndLeftPush(sourceKey, destinationKey));
    }

    /**
     * 测试brpoplpush
     */
    @Test
    public void testBlockRightPopAndLeftPush() {
        String sourceKey = "listKey";
        String destinationKey = "listKey1";
        logger.info("brpoplpush remove value: " +
                redisDao.lRightPopAndLeftPush(sourceKey, destinationKey, 5L, TimeUnit.SECONDS));
    }

    /**
     * 测试lpush
     */
    @Test
    public void testLeftPush() {
        String key = "listKey1";
        String value = "listValue1";
        long listSize = redisDao.lLeftPush(key, value);
        logger.info("list.size: " + listSize);
    }

    /**
     * 测试lpush(带支点)
     */
    @Test
    public void testLeftPushWithPivot() {
        String key = "listKey";
        String pivot = "value2";
        String value = "newValue";
        long listSize = redisDao.lLeftPush(key, pivot, value);
        logger.info("list.size: " + listSize);
    }

    /**
     * 测试批量lpush
     */
    @Test
    public void testBatchLeftPush() {
        String key = "listKey";
        String value1 = "value1";
        String value2 = "value2";
        String value3 = "value3";
        String value4 = "value4";
        String value5 = "value5";
        long listSize = redisDao.lLeftPushAll(key, value1, value2, value3, value4, value5);
        logger.info("list.size: " + listSize);
    }

    /**
     * 测试leftPushIfPresent
     */
    @Test
    public void testLeftPushIfPresent() {
        String key = "listKey1";
        String value = "value4";
        long listSize = redisDao.lLeftPushIfPresent(key, value);
        logger.info("list.size: " + listSize);
    }

    /**
     * 测试lpop
     */
    @Test
    public void testLeftPop() {
        String key = "listKey";
        logger.info("lpop remove value: " + redisDao.lLeftPop(key));
    }

    /**
     * 测试blpop
     */
    @Test
    public void testBlockLeftPop() {
        String key = "listKey2";
        logger.info("blpop remove value: " + redisDao.lLeftPop(key, 5L, TimeUnit.SECONDS));
    }

    /**
     * 测试lRange
     */
    @Test
    public void testListRange() {
        String key = "listKey";
        List<Object> valueList = redisDao.lRange(key, 0, -1);
        logger.info("=== list value ===");
        for (Object value : valueList) {
            logger.info(null == value ? "" : value.toString());
        }
    }

    /**
     * 测试ltrim
     */
    @Test
    public void testListTrim() {
        String key = "listKey";
        redisDao.lTrim(key, 1, -1);
        logger.info("trim finish.");
        List<Object> valueList = redisDao.lRange(key, 0, -1);
        logger.info("=== list value ===");
        for (Object value : valueList) {
            logger.info(null == value ? "" : value.toString());
        }
    }

    /**
     * 测试lindex
     */
    @Test
    public void testListIndex() {
        String key = "listKey";
        List<Object> valueList = redisDao.lRange(key, 0, -1);
        for (int i = 0; i < valueList.size(); i++) {
            long index = i;
            logger.info("valueList[" + index + "] = " + redisDao.lIndex(key, index));
        }
    }
}
