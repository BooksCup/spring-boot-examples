package com.bc.netty.client.util;

import com.bc.netty.client.entity.MethodInvokeMeta;
import com.bc.netty.client.exception.NoUseableChannel;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentSkipListSet;

public class ChannelUtil {
    private static final Set<Channel> CHANNELS = new ConcurrentSkipListSet<>();
    /**
     * ChannelUtil日志输出
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(ChannelUtil.class);

    private static final Map<String, Object> RESULT_MAP = new ConcurrentHashMap<>();

    private ChannelUtil() {
    }

    public static void calculateResult(String key, Object result) {
        RESULT_MAP.put(key, result);
    }

    public static String getResultKey(String key) {
        return (String) getResult(key);
    }

    public static Object getResult(String key) {
        return RESULT_MAP.get(key);
    }

    /**
     * 注册通道
     *
     * @param channel 通道
     */
    public static void registerChannel(Channel channel) {
        ChannelId id = channel.id();
        LOGGER.info("{} -> [添加通道] {}", ChannelUtil.class.getName(), id);
        CHANNELS.add(channel);
    }

    /**
     * 获取回调结果
     *
     * @param methodInvokeMeta 远程调用方法信息
     * @param key              用于取结果的key值
     */
    public static void remoteCall(MethodInvokeMeta methodInvokeMeta, String key) {
        LOGGER.info("{} -> [远程调用] ", ChannelUtil.class.getName());
        Iterator<Channel> iterator = CHANNELS.iterator();
        Channel channel;
        if (iterator.hasNext()) {
            channel = iterator.next();
        } else {
            LOGGER.error("{} -> [没有活跃的通道] ", ChannelUtil.class);
            throw new NoUseableChannel("没有活跃的通道");
        }
        // 将用于获取结果的key保存,以通道id为键
        String channelID = channel.id().asLongText();
        LOGGER.info("{} -> [保存获取结果的key] key - {} 通道id - {}", ChannelUtil.class, key, channelID);
        RESULT_MAP.put(channelID, key);
        ChannelFuture channelFuture = channel.writeAndFlush(methodInvokeMeta);
//        channelFuture.addListener(ChannelFutureListener.CLOSE);
    }

    public static void remove(Channel channel) {
        CHANNELS.remove(channel);
    }
}
