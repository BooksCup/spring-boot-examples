package com.bc.redis.dao;

/**
 * redis数据操作接口
 *
 * @author zhou
 */
public interface RedisDao {
    /**
     * 设置值
     *
     * @param key   键
     * @param value 值
     * @return true:设置成功 false:设置失败
     */
    boolean set(String key, Object value);
}
