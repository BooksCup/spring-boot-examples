package com.bc.redis.test;

import com.bc.redis.RedisApplication;
import com.bc.redis.dao.RedisDao;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.annotation.Resource;

/**
 * 测试redis操作string
 *
 * @author zhou
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = RedisApplication.class)
public class TestRedisOpsForString {
    @Resource
    private RedisDao redisDao;

    @Test
    public void testSet() {
        String key = "testKey";
        String value = "testValue";
        redisDao.set(key, value);
    }
}
