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

    @Test
    public void testHput() {
        String key = "hKey";
        String hashKey = "hField";
        String value = "hValue";
        boolean result = redisDao.hPut(key, hashKey, value);
        logger.info("hash put result: " + result);
    }
}
