package com.bc.redis.dao.impl;

import com.bc.redis.dao.RedisDao;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * redis数据操作实现类
 *
 * @author zhou
 */
@Component
public class RedisDaoImpl implements RedisDao {
    /**
     * logger
     */
    private static final Logger logger = LoggerFactory.getLogger(RedisDaoImpl.class);

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    // ===== common ops begin =====

    /**
     * 设置key的过期时间，key过期后将不再可用
     *
     * @param key     键
     * @param timeout 过期时间
     * @param unit    时间颗粒度转换单元
     * @return true: 设置成功  false: 设置失败
     */
    @Override
    public boolean expire(String key, long timeout, TimeUnit unit) {
        return redisTemplate.expire(key, timeout, unit);
    }

    /**
     * 根据key获取过期时间
     *
     * @param key 键
     * @return 过期时间(默认秒) 返回-1代表永久有效
     */
    @Override
    public long getExpire(String key) {
        return redisTemplate.getExpire(key);
    }

    /**
     * 判断key是否存在
     *
     * @param key 键
     * @return true: 存在  false: 不存在
     */
    @Override
    public boolean hasKey(String key) {
        return redisTemplate.hasKey(key);
    }

    /**
     * 删除已存在的key
     * 删除不存在的key会返回false
     *
     * @param key key
     * @return true: 删除成功  false: 删除失败
     */
    @Override
    public boolean delete(String key) {
        return key == null ? false : redisTemplate.delete(key);
    }

    /**
     * 批量删除key
     *
     * @param keyList 键列表
     * @return 被删除key的数量
     */
    @Override
    public long delete(List<String> keyList) {
        return redisTemplate.delete(keyList);
    }

    /**
     * 从当前数据库中随机返回一个key
     *
     * @return 当数据库不为空时，返回一个key。当数据库为空时，返回null
     */
    @Override
    public String randomKey() {
        return redisTemplate.randomKey();
    }

    /**
     * 修改key名称
     * 当oldKey不存在时，会返回错误
     * 当newKey已经存在时，rename将覆盖旧值
     *
     * @param oldKey 旧key
     * @param newKey 新key
     * @return true: 修改成功  false: 修改失败
     */
    @Override
    public boolean rename(String oldKey, String newKey) {
        try {
            redisTemplate.rename(oldKey, newKey);
            return true;
        } catch (Exception e) {
            logger.error(e.getMessage());
            return false;
        }
    }

    /**
     * newKey不存在时修改key的名称
     * 当newKey已经存在时，将会返回一个错误
     *
     * @param oldKey 旧key
     * @param newKey 新key
     * @return true: 修改成功   false: 修改失败
     */
    @Override
    public boolean renameIfAbsent(String oldKey, String newKey) {
        return redisTemplate.renameIfAbsent(oldKey, newKey);
    }

    /**
     * 返回key所储存的值的类型
     *
     * @param key 键
     * @return key的数据类型
     * 数据类型有： none (key不存在)
     * string (字符串)
     * list (列表)
     * set (集合)
     * zset (有序集)
     * hash (哈希表)
     */
    @Override
    public String type(String key) {
        return redisTemplate.type(key).code();
    }
    // ===== common ops end =====


    // ===== ops for string begin =====

    /**
     * 设置值
     *
     * @param key   键
     * @param value 值
     * @return true:设置成功 false:设置失败
     */
    @Override
    public boolean set(String key, Object value) {
        try {
            redisTemplate.opsForValue().set(key, value);
            return true;
        } catch (Exception e) {
            logger.error("set error: " + e.getMessage());
            return false;
        }
    }

    /**
     * 设置值并设置过期时间
     *
     * @param key     键
     * @param value   值
     * @param timeout 过期时间(秒) timeout>0:timeout秒后过期  timeout<=0:无限期
     * @return true: 设置成功  false: 设置失败
     */
    @Override
    public boolean set(String key, Object value, long timeout) {
        try {
            if (timeout > 0) {
                redisTemplate.opsForValue().set(key, value, timeout, TimeUnit.SECONDS);
            } else {
                redisTemplate.opsForValue().set(key, value);
            }
            return true;
        } catch (Exception e) {
            logger.error("set(expire) param[timeout]: " + timeout + ", error:" + e.getMessage());
            return false;
        }
    }

    /**
     * 在指定的key不存在时，为key设置指定的值
     * 如果指定key存在时，则会设置失败
     *
     * @param key   键
     * @param value 值
     * @return true: 设置成功   false: 设置失败
     */
    @Override
    public boolean setIfAbsent(String key, Object value) {
        return redisTemplate.opsForValue().setIfAbsent(key, value);
    }

    /**
     * 在指定的key不存在时，为key设置指定的值
     * 如果指定key存在时，则会设置失败
     *
     * @param key     键
     * @param value   值
     * @param timeout 过期时间
     * @param unit    时间颗粒度转换单元
     * @return true: 设置成功   false: 设置失败
     */
    @Override
    public boolean setIfAbsent(String key, Object value, long timeout, TimeUnit unit) {
        return redisTemplate.opsForValue().setIfAbsent(key, value, timeout, unit);
    }

    /**
     * 同时设置一个或多个key-value对
     *
     * @param map 一个或多个key-value对
     * @return true: 设置成功  false: 设置失败
     */
    @Override
    public boolean multiSet(Map<String, Object> map) {
        try {
            redisTemplate.opsForValue().multiSet(map);
            return true;
        } catch (Exception e) {
            logger.error("multiSet error: " + e.getMessage() + ", param[map]: " + map);
            return false;
        }
    }

    /**
     * 同时设置一个或多个key-value对，当且仅当所有给定key都不存在。
     * 只要存在一个key，则所有的key-value对都会设置失败
     *
     * @param map 一个或多个key-value对
     * @return true: 设置成功  false: 设置失败
     */
    @Override
    public boolean multiSetIfAbsent(Map<String, Object> map) {
        return redisTemplate.opsForValue().multiSetIfAbsent(map);
    }

    /**
     * 用于为指定的key追加值
     * 如果 key已经存在并且是一个字符串，APPEND命令将value追加到key原来的值的末尾
     * 如果 key不存在，APPEND就简单地将给定key设为value，就像执行SET key value一样
     *
     * @param key   键
     * @param value 值
     * @return 追加指定值之后，key中字符串的长度
     */
    @Override
    public Integer append(String key, String value) {
        return redisTemplate.opsForValue().append(key, value);
    }

    /**
     * 获取值
     *
     * @param key 键
     * @return 值
     */
    @Override
    public Object get(String key) {
        return key == null ? null : redisTemplate.opsForValue().get(key);
    }

    /**
     * 获取所有(一个或多个)给定key的值
     *
     * @param keyList (一个或多个)给定key
     * @return 一个包含所有给定key的值的列表
     */
    @Override
    public List<Object> multiGet(List<String> keyList) {
        return redisTemplate.opsForValue().multiGet(keyList);
    }

    /**
     * 返回给定key的旧值。当key没有旧值时，即key不存在时，返回null
     *
     * @param key   键
     * @param value 新值
     * @return 旧值
     */
    @Override
    public Object getAndSet(String key, String value) {
        return redisTemplate.opsForValue().getAndSet(key, value);
    }

    /**
     * 获取key中字符串的长度
     *
     * @param key 键
     * @return key中字符串的长度
     */
    @Override
    public long size(String key) {
        return redisTemplate.opsForValue().size(key);
    }

    /**
     * 将key中储存的数字值增delta
     * 如果key不存在，那么key的值会先被初始化为0，然后再执行incr操作
     * 如果值包含错误的类型，或字符串类型的值不能表示为数字，那么返回一个错误
     *
     * @param key   键
     * @param delta 递增因子
     * @return 执行incr命令之后key的值
     */
    @Override
    public long increment(String key, long delta) {
        if (delta < 0) {
            throw new RuntimeException("递增因子必须大于0");
        }
        return redisTemplate.opsForValue().increment(key, delta);
    }

    /**
     * 将key中储存的数字值减delta
     * 如果key不存在，那么key的值会先被初始化为0，然后再执行decr操作
     * 如果值包含错误的类型，或字符串类型的值不能表示为数字，那么返回一个错误
     *
     * @param key   键
     * @param delta 递减因子
     * @return 执行decr命令之后key的值
     */
    @Override
    public long decrement(String key, long delta) {
        if (delta < 0) {
            throw new RuntimeException("递减因子必须大于0");
        }
        return redisTemplate.opsForValue().decrement(key, delta);
    }
    // ===== ops for string end =====


    // ===== ops for list begin =====

    /**
     * 返回列表中指定区间内的元素，区间以偏移量START和END指定
     * 其中0表示列表的第一个元素，1表示列表的第二个元素，以此类推
     * 也可以使用负数下标，以-1表示列表的最后一个元素，-2表示列表的倒数第二个元素，以此类推
     *
     * @param key   键
     * @param start 区间开始([)
     * @param end   区间结束(])
     * @return 一个列表，包含指定区间内的元素
     */
    @Override
    public List<Object> lRange(String key, long start, long end) {
        return redisTemplate.opsForList().range(key, start, end);
    }

    /**
     * 对一个列表进行修剪(trim)，就是说，让列表只保留指定区间内的元素，不在指定区间之内的元素都将被删除
     * 下标0表示列表的第一个元素，以1表示列表的第二个元素，以此类推
     * 也可以使用负数下标，以-1表示列表的最后一个元素，-2表示列表的倒数第二个元素，以此类推
     * 举个例子: 有个list的value为["value1", "value2", "value3"]
     * 执行trim(key, 1, -1)后list的value会变成["value2", "value3"]
     *
     * @param key   键
     * @param start 区间开始([)
     * @param end   区间结束(])
     * @return true: 修剪成功   false: 修剪失败
     */
    @Override
    public boolean lTrim(String key, long start, long end) {
        try {
            redisTemplate.opsForList().trim(key, start, end);
            return true;
        } catch (Exception e) {
            logger.error("list trim error: " + e.getMessage());
            return false;
        }
    }

    /**
     * 通过索引获取列表中的元素
     * 也可以使用负数下标，以-1表示列表的最后一个元素，-2表示列表的倒数第二个元素，以此类推
     *
     * @param key   键
     * @param index 索引
     * @return 列表中下标为指定索引值的元素。如果指定索引值不在列表的区间范围内，返回null
     */
    @Override
    public Object lIndex(String key, long index) {
        return redisTemplate.opsForList().index(key, index);
    }

    /**
     * 通过索引来设置元素的值
     * 当索引参数超出范围，或对一个空列表进行LSET时，会返回一个错误
     * 对空列表进行LSET时，会报如下错误:
     * redis.clients.jedis.exceptions.JedisDataException: ERR no such key
     * 当索引参数超出范围，会报如下错误:
     * redis.clients.jedis.exceptions.JedisDataException: ERR index out of range
     *
     * @param key   键
     * @param index 索引
     * @param value 值
     * @return true:设置成功  false:设置失败
     */
    @Override
    public boolean lSet(String key, long index, Object value) {
        try {
            redisTemplate.opsForList().set(key, index, value);
            return true;
        } catch (Exception e) {
            logger.error("list set error: " + e.getMessage());
            return false;
        }
    }

    /**
     * 根据参数COUNT的值，移除列表中与参数VALUE相等的元素
     * count > 0 : 从表头开始向表尾搜索，移除与VALUE相等的元素，数量为COUNT
     * count < 0 : 从表尾开始向表头搜索，移除与VALUE相等的元素，数量为COUNT的绝对值
     * count = 0 : 移除表中所有与VALUE相等的值
     *
     * @param key   键
     * @param count 移除元素的数量
     * @param value 值
     * @return 被移除元素的数量。列表不存在时返回0
     */
    @Override
    public long lRemove(String key, long count, Object value) {
        return redisTemplate.opsForList().remove(key, count, value);
    }

    /**
     * 将值插入到列表的尾部(最右边)
     * 如果列表不存在，一个空列表会被创建并执行RPUSH操作
     * 当列表存在但不是列表类型时，返回一个错误
     * 会报下面这个错误:
     * redis.clients.jedis.exceptions.JedisDataException:
     * WRONGTYPE Operation against a key holding the wrong kind of value
     *
     * @param key   键
     * @param value 值
     * @return 执行RPUSH操作后，列表的长度
     */
    @Override
    public long lRightPush(String key, Object value) {
        return redisTemplate.opsForList().rightPush(key, value);
    }

    /**
     * 将值插入到列表中第一次出现支点(pivot)的右侧
     * 举个例子: list.value为 ["value1", "value2", "value3", "value4", "value2"]
     * 调用lRightPush(key, "value2", "newValue")后
     * list.value会变成 ["value1", "value2", "newValue", "value3", "value4", "value2"]
     *
     * @param key   键
     * @param pivot 支点
     * @param value 值
     * @return 执行RPUSH操作后，列表的长度
     */
    @Override
    public long lRightPush(String key, Object pivot, Object value) {
        return redisTemplate.opsForList().rightPush(key, pivot, value);
    }

    /**
     * 批量rpush
     * 将多个值插入到列表的尾部(最右边)
     *
     * @param key    键
     * @param values 多个值
     * @return 执行RPUSH操作后，列表的长度
     */
    @Override
    public long lRightPushAll(String key, Object... values) {
        return redisTemplate.opsForList().rightPushAll(key, values);
    }

    /**
     * 将一个值插入到已存在的列表尾部(最右边)
     * 如果列表不存在，操作无效。
     *
     * @param key   键
     * @param value 值
     * @return 执行rpushx操作后，列表的长度。如果key对应的列表不存在，返回0
     */
    @Override
    public long lRightPushIfPresent(String key, Object value) {
        return redisTemplate.opsForList().rightPushIfPresent(key, value);
    }

    /**
     * 移除列表的最后一个元素，返回值为移除的元素
     *
     * @param key 键
     * @return 移除的元素
     */
    @Override
    public Object lRightPop(String key) {
        return redisTemplate.opsForList().rightPop(key);
    }

    /**
     * 移出并获取列表的最后一个元素， 如果列表没有元素会阻塞列表直到等待超时或发现可弹出元素为止
     *
     * @param key     键
     * @param timeout 超时时间
     * @param unit    时间颗粒度转换单元
     * @return 假如在指定时间内没有任何元素被弹出，则返回null。反之，返回列表的最后一个元素
     */
    @Override
    public Object lRightPop(String key, long timeout, TimeUnit unit) {
        return redisTemplate.opsForList().rightPop(key, timeout, unit);
    }

    /**
     * 移除列表的最后一个元素，并将该元素添加到另一个列表的头部并返回
     *
     * @param sourceKey      源key
     * @param destinationKey 目标key
     * @return 被弹出的元素
     */
    @Override
    public Object lRightPopAndLeftPush(String sourceKey, String destinationKey) {
        return redisTemplate.opsForList().rightPopAndLeftPush(sourceKey, destinationKey);
    }

    /**
     * 从列表中取出最后一个元素，并插入到另外一个列表的头部； 如果列表没有元素会阻塞列表直到等待超时或发现可弹出元素为止
     *
     * @param sourceKey      源key
     * @param destinationKey 目标key
     * @param timeout        超时时间
     * @param unit           时间颗粒度转换单元
     * @return 假如在指定时间内没有任何元素被弹出，则返回null。反之，返回列表的最后一个元素
     */
    @Override
    public Object lRightPopAndLeftPush(String sourceKey, String destinationKey,
                                       long timeout, TimeUnit unit) {
        return redisTemplate.opsForList().rightPopAndLeftPush(sourceKey, destinationKey, timeout, unit);
    }

    /**
     * 将值插入到列表的头部(最左边)
     * 如果列表不存在，一个空列表会被创建并执行LPUSH操作
     * 当列表存在但不是列表类型时，返回一个错误
     *
     * @param key   键
     * @param value 值
     * @return 执行LPUSH操作后，列表的长度
     */
    @Override
    public long lLeftPush(String key, Object value) {
        return redisTemplate.opsForList().leftPush(key, value);
    }

    /**
     * 将值插入到列表中第一次出现支点(pivot)的左侧
     * 举个例子: list.value为 ["value1", "value2", "value3", "value4", "value2"]
     * 调用lLightPush(key, "value2", "newValue")后
     * list.value会变成 ["value1", "newValue", "value2", "value3", "value4", "value2"]
     *
     * @param key   键
     * @param pivot 支点
     * @param value 值
     * @return 执行LPUSH操作后，列表的长度
     */
    @Override
    public long lLeftPush(String key, Object pivot, Object value) {
        return redisTemplate.opsForList().leftPush(key, pivot, value);
    }

    /**
     * 批量lpush
     * 将多个值插入到列表头部(最左边)
     *
     * @param key    键
     * @param values 多个值
     * @return 执行LPUSH操作后，列表的长度
     */
    @Override
    public long lLeftPushAll(String key, Object... values) {
        return redisTemplate.opsForList().leftPushAll(key, values);
    }

    /**
     * 将一个值插入到已存在的列表头部(最左边)
     * 如果列表不存在，操作无效。
     *
     * @param key   键
     * @param value 值
     * @return 执行lpushx操作后，列表的长度。如果key对应的列表不存在，返回0
     */
    @Override
    public long lLeftPushIfPresent(String key, Object value) {
        return redisTemplate.opsForList().leftPushIfPresent(key, value);
    }

    /**
     * 移除并返回列表的第一个元素
     *
     * @param key 键
     * @return 移除的元素
     */
    @Override
    public Object lLeftPop(String key) {
        return redisTemplate.opsForList().leftPop(key);
    }

    /**
     * 移出并获取列表的第一个元素， 如果列表没有元素会阻塞列表直到等待超时或发现可弹出元素为止
     *
     * @param key     键
     * @param timeout 超时时间
     * @param unit    时间颗粒度转换单元
     * @return 假如在指定时间内没有任何元素被弹出，则返回null。反之，返回列表的第一个元素
     */
    @Override
    public Object lLeftPop(String key, long timeout, TimeUnit unit) {
        return redisTemplate.opsForList().leftPop(key, timeout, unit);
    }
    // ===== ops for list end =====


    // ===== ops for set begin =====

    /**
     * 将一个或多个成员元素加入到集合中，已经存在于集合的成员元素将被忽略
     * 假如集合key不存在，则创建一个只包含添加的元素作成员的集合
     * 当集合key不是集合类型时，返回如下一个错误:
     * redis.clients.jedis.exceptions.JedisDataException: WRONGTYPE Operation against a key holding the wrong kind of value
     *
     * @param key    键
     * @param values 一个或多个成员元素
     * @return 被添加到集合中的新元素的数量，不包括被忽略的元素
     */
    @Override
    public long sAdd(String key, Object... values) {
        return redisTemplate.opsForSet().add(key, values);
    }

    /**
     * 返回集合中元素的数量
     * redis命令: Scard
     *
     * @param key 键
     * @return 集合中元素的数量
     */
    @Override
    public long sSize(String key) {
        return redisTemplate.opsForSet().size(key);
    }

    /**
     * 返回给定集合之间的差集。不存在的集合key将视为空集
     * 差集的结果来自前面的FIRST_KEY,而不是后面的OTHER_KEY，也不是整个FIRST_KEY OTHER_KEY的差集
     * 举个例子:
     * key对应的value为["a", "b"]
     * otherKey对应的value为["b", "c"]
     * sdiff key otherKey的结果为["a"]
     * sdiff otherKey key的结果为["c"]
     *
     * @param key      第一个键，也是产生差值结果的key
     * @param otherKey 第二个键
     * @return 包含差集成员的列表
     */
    @Override
    public Set<Object> sDifference(String key, String otherKey) {
        return redisTemplate.opsForSet().difference(key, otherKey);
    }

    /**
     * 返回给定集合之间的差集。不存在的集合key将视为空集
     * 差集的结果来自前面的FIRST_KEY,而不是后面的OTHER_KEY1，也不是整个FIRST_KEY OTHER_KEY1..OTHER_KEYN的差集
     * 举个例子:
     * key对应的value为["a", "b", "c", "d"]
     * otherKey1对应的value为["c"]
     * otherKey2对应的value为["a", "c", "e"]
     * sdiff key otherKey1 otherKey2的结果为["b", "d"]
     *
     * @param key       第一个键，也是产生差值结果的key
     * @param otherKeys 其他键
     * @return 包含差集成员的列表
     */
    @Override
    public Set<Object> sDifference(String key, Collection<String> otherKeys) {
        return redisTemplate.opsForSet().difference(key, otherKeys);
    }

    /**
     * 将给定集合之间的差集存储在指定的集合(destination)中
     * 如果指定的集合key已存在，则会被覆盖
     *
     * @param key      第一个键，也是产生差值结果的key
     * @param otherKey 第二个键
     * @param destKey  destination
     * @return 结果集中的元素数量
     */
    @Override
    public long sDifferenceAndStore(String key, String otherKey, String destKey) {
        return redisTemplate.opsForSet().differenceAndStore(key, otherKey, destKey);
    }

    /**
     * 将给定集合之间的差集存储在指定的集合(destination)中
     * 如果指定的集合key已存在，则会被覆盖
     *
     * @param key       第一个键，也是产生差值结果的key
     * @param otherKeys 其他键
     * @param destKey   destination
     * @return 结果集中的元素数量
     */
    @Override
    public long sDifferenceAndStore(String key, Collection<String> otherKeys, String destKey) {
        return redisTemplate.opsForSet().differenceAndStore(key, otherKeys, destKey);
    }

    /**
     * 返回给定所有给定集合的交集
     * 不存在的集合key被视为空集
     * 当给定集合当中有一个空集时，结果也为空集(根据集合运算定律)
     *
     * @param key      第一个键
     * @param otherKey 第二个键
     * @return 交集成员的列表
     */
    @Override
    public Set<Object> sIntersect(String key, String otherKey) {
        return redisTemplate.opsForSet().intersect(key, otherKey);
    }

    /**
     * 返回给定所有给定集合的交集
     * 不存在的集合key被视为空集
     * 当给定集合当中有一个空集时，结果也为空集(根据集合运算定律)
     *
     * @param key       第一个键
     * @param otherKeys 其他键
     * @return 交集成员的列表
     */
    @Override
    public Set<Object> sIntersect(String key, Collection<String> otherKeys) {
        return redisTemplate.opsForSet().intersect(key, otherKeys);
    }

    /**
     * 将给定集合之间的交集存储在指定的集合中
     * 如果指定的集合已经存在，则将其覆盖
     *
     * @param key      第一个键
     * @param otherKey 第二个键
     * @param destKey  destination
     * @return 存储交集的集合的元素数量
     */
    @Override
    public long sIntersectAndStore(String key, String otherKey, String destKey) {
        return redisTemplate.opsForSet().intersectAndStore(key, otherKey, destKey);
    }

    /**
     * 将给定集合之间的交集存储在指定的集合中
     * 如果指定的集合已经存在，则将其覆盖
     *
     * @param key       第一个键
     * @param otherKeys 其他键
     * @param destKey   destination
     * @return 存储交集的集合的元素数量
     */
    @Override
    public long sIntersectAndStore(String key, Collection<String> otherKeys, String destKey) {
        return redisTemplate.opsForSet().intersectAndStore(key, otherKeys, destKey);
    }

    /**
     * 判断成员元素是否是集合的成员
     *
     * @param key    键
     * @param member 成员元素
     * @return true: 成员元素是集合的成员   false: 成员元素不是集合的成员 或 key不存在
     */
    @Override
    public boolean sIsMember(String key, Object member) {
        return redisTemplate.opsForSet().isMember(key, member);
    }

    /**
     * 返回集合中的所有的成员
     * 不存在的集合 key 被视为空集合
     *
     * @param key 键
     * @return 集合中的所有成员
     */
    @Override
    public Set<Object> sMembers(String key) {
        return redisTemplate.opsForSet().members(key);
    }

    /**
     * 将指定成员member元素从source集合移动到destination集合
     * smove是原子性操作
     * 如果source集合不存在或不包含指定的member元素，则不执行任何操作
     * 否则，member元素从source集合中被移除，并添加到destination集合中去
     * 当destination集合已经包含member元素时，只是简单地将source集合中的member元素删除
     * 当source或destination不是集合类型时，返回一个错误
     *
     * @param key     source
     * @param value   成员member元素
     * @param destKey destination
     * @return true: 成员元素被成功移除  false: 如果成员元素不是source集合的成员，并且没有任何操作对destination集合执行
     */
    @Override
    public boolean sMove(String key, Object value, String destKey) {
        return redisTemplate.opsForSet().move(key, value, destKey);
    }

    /**
     * 移除集合中的指定key的一个随机元素
     * 移除后会返回移除的元素
     * 该命令类似srandmember命令，但spop将随机元素从集合中移除并返回，而srandmember则仅仅返回随机元素，而不对集合进行任何改动
     *
     * @param key 键
     * @return 被移除的随机元素。当集合不存在或是空集时，返回null
     */
    @Override
    public Object sPop(String key) {
        return redisTemplate.opsForSet().pop(key);
    }

    /**
     * 移除集合中的指定key的多个随机元素
     * 移除后会返回移除的元素
     * 该命令类似srandmember命令，但spop将随机元素从集合中移除并返回，而srandmember则仅仅返回随机元素，而不对集合进行任何改动
     *
     * @param key   键
     * @param count 移除数量
     * @return 被移除的随机元素。当集合不存在或是空集时，返回null
     */
    @Override
    public List<Object> sPop(String key, long count) {
        return redisTemplate.opsForSet().pop(key, count);
    }
    // ===== ops for set end =====
}
