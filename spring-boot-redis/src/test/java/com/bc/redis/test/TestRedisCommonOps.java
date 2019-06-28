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
import java.util.concurrent.TimeUnit;

/**
 * 测试redis通用操作
 *
 * @author zhou
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = RedisApplication.class)
public class TestRedisCommonOps {
    /**
     * logger
     */
    private static final Logger logger = LoggerFactory.getLogger(TestRedisCommonOps.class);

    @Resource
    private RedisDao redisDao;

    /**
     * 测试设置过期时间
     */
    @Test
    public void testExpire() {
        String key = "key1";
        long timeout = 10L;
        boolean expireResult = redisDao.expire(key, timeout, TimeUnit.SECONDS);
        logger.info("expire result: " + expireResult);
    }

    /**
     * 测试获取过期时间
     */
    @Test
    public void testGetExpire() {
        String key = "key3";
        redisDao.expire(key, 10000L, TimeUnit.MILLISECONDS);
        logger.info("key: " + key + ", timeout: " + redisDao.getExpire(key));
        String newKey = "key4";
        logger.info("key: " + newKey + ", timeout: " + redisDao.getExpire(newKey));
    }

    /**
     * 测试key是否存在
     */
    @Test
    public void testHasKey() {
        String key = "key3";
        boolean isExists = redisDao.hasKey(key);
        logger.info("key: " + key + ", exists: " + isExists);
    }

    /**
     * 测试删除key
     */
    @Test
    public void testDeleteKey() {
//        String key = "key1";
//        String key = "";
//        String key = null;
        String key = "中文key";
        boolean deleteResult = redisDao.delete(key);
        logger.info("delete key: " + key + ", result: " + deleteResult);
    }

    /**
     * 测试批量删除key
     */
    @Test
    public void testBatchDeleteKey() {
        String key1 = "key1";
        String key2 = "key2";
        String key3 = "key3";
        String key4 = "key4";
//        String key5 = null;
        List<String> keyList = new ArrayList<>();
        keyList.add(key1);
        keyList.add(key2);
        keyList.add(key3);
        keyList.add(key4);
//        keyList.add(key5);
        long deleteNum = redisDao.delete(keyList);
        logger.info("delete num: " + deleteNum);
    }

    /**
     * 测试随机获取一个key
     */
    @Test
    public void testRandomKey() {
        logger.info("-random");
        logger.info("chosen key: " + redisDao.randomKey());
    }

    /**
     * 测试修改key
     */
    @Test
    public void testRename() {
        String oldKey = "key11";
        String newKey = "key33";
        boolean result = redisDao.rename(oldKey, newKey);
        logger.info("rename result: " + result);
    }

    /**
     * 测试renameIfAbsent
     */
    @Test
    public void testRenameIfAbsent() {
        String oldKey = "key11";
        String newKey = "key33";
        boolean result = redisDao.renameIfAbsent(oldKey, newKey);
        logger.info("renameIfAbsent result: " + result);
    }

    /**
     * 测试返回key所储存的值的类型
     */
    @Test
    public void testType() {
        String key = "key999";
        String type = redisDao.type(key);
        logger.info("key: " + key + "'s type: " + type);
    }

}
