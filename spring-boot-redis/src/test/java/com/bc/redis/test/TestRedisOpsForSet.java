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

    /**
     * 测试sinter(两个key)
     */
    @Test
    public void testSetIntersect() {
        String key = "setKey";
        String otherKey = "setKey2";
        Set<Object> resultSet = redisDao.sIntersect(key, otherKey);
        logger.info("===== get intersect =====");
        for (Object result : resultSet) {
            logger.info(result == null ? "" : result.toString());
        }
    }


    /**
     * 测试sinter(多个key)
     */
    @Test
    public void testSetIntersectWithManyOtherKeys() {
        String key = "setKey";
        String otherKey1 = "setKey2";
        String otherKey2 = "setKey3";
        List<String> otherKeyList = new ArrayList<>();
        otherKeyList.add(otherKey1);
        otherKeyList.add(otherKey2);
        Set<Object> resultSet = redisDao.sIntersect(key, otherKeyList);
        logger.info("===== get intersect =====");
        for (Object result : resultSet) {
            logger.info(result == null ? "" : result.toString());
        }
    }

    /**
     * 测试sinterstore(两个key)
     */
    @Test
    public void testSetIntersectAndStore() {
        String key = "setKey";
        String otherKey = "setKey2";
        String destKey = "setKey4";
        long destSetSize = redisDao.sIntersectAndStore(key, otherKey, destKey);
        logger.info("dest set size: " + destSetSize);
    }

    /**
     * 测试sinterstore(多个key)
     */
    @Test
    public void testSetIntersectAndStoreWithManyOtherKeys() {
        String key = "setKey";
        String otherKey1 = "setKey2";
        String otherKey2 = "setKey3";
        String destKey = "setKey4";
        List<String> otherKeyList = new ArrayList<>();
        otherKeyList.add(otherKey1);
        otherKeyList.add(otherKey2);
        long destSetSize = redisDao.sIntersectAndStore(key, otherKeyList, destKey);
        logger.info("dest set size: " + destSetSize);
    }

    /**
     * 测试sismember
     */
    @Test
    public void testSetIsMember() {
        String key = "setKey";
        String member = "a";
        logger.info(member + (redisDao.sIsMember(key, member) ? " is " : " is not ")
                + "the member of " + key);
    }

    /**
     * 测试smembers
     */
    @Test
    public void testGetSetMembers() {
        String key = "setKey";
        Set<Object> memberSet = redisDao.sMembers(key);
        logger.info("===== get members =====");
        for (Object member : memberSet) {
            logger.info(member == null ? "" : member.toString());
        }
    }

    /**
     * 测试smove
     */
    @Test
    public void testSetMove() {
        String key = "setKey";
        String value = "a";
        String destKey = "setKey4";
        redisDao.sMove(key, value, destKey);
    }

    /**
     * 测试spop(一个随机元素)
     */
    @Test
    public void testSetPop() {
        String key = "setKey";
        logger.info("set pop element: " + redisDao.sPop(key));
    }

    /**
     * 测试spop(多个随机元素)
     */
    @Test
    public void testSetPopManyElements() {
        String key = "setKey";
        List<Object> popElementList = redisDao.sPop(key, 3);
        for (Object popElement : popElementList) {
            logger.info("set pop element: " + popElement);
        }
    }
}
