package com.bc.netty.server.listener;

import com.bc.netty.common.utils.ObjectCodec;
import com.bc.netty.server.adapter.ServerChannelHandlerAdapter;
import com.bc.netty.server.config.NettyServerConfig;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.codec.LengthFieldPrepender;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import io.netty.handler.timeout.IdleStateHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.annotation.PreDestroy;
import javax.annotation.Resource;
import java.util.concurrent.TimeUnit;

/**
 * Netty服务器监听器
 *
 * @author zhou
 */
@Component
public class NettyServerListener {

    /**
     * 日志
     */
    private static final Logger logger = LoggerFactory.getLogger(NettyServerListener.class);

    /**
     * 创建bootstrap
     */
    private ServerBootstrap serverBootstrap = new ServerBootstrap();

    /**
     * boss
     */
    private EventLoopGroup boss = new NioEventLoopGroup();

    /**
     * worker
     */
    private EventLoopGroup worker = new NioEventLoopGroup();

    /**
     * 通道适配器
     */
    @Resource
    private ServerChannelHandlerAdapter channelHandlerAdapter;

    /**
     * netty服务器配置类
     */
    @Resource
    private NettyServerConfig nettyConfig;

    /**
     * 关闭服务器方法
     */
    @PreDestroy
    public void close() {
        logger.info("关闭服务器....");
        // 优雅退出
        boss.shutdownGracefully();
        worker.shutdownGracefully();
    }

    /**
     * 开启及服务线程
     */
    public void start() {
        // 从配置文件中(application.properties)获取服务端监听端口号
        int port = nettyConfig.getPort();
        serverBootstrap.group(boss, worker)
                .channel(NioServerSocketChannel.class)
                .option(ChannelOption.SO_BACKLOG, 100)
                .handler(new LoggingHandler(LogLevel.INFO));
        try {
            //设置事件处理
            serverBootstrap.childHandler(new ChannelInitializer<SocketChannel>() {
                @Override
                protected void initChannel(SocketChannel ch) {
                    ChannelPipeline pipeline = ch.pipeline();
                    // 添加心跳支持
                    pipeline.addLast(new IdleStateHandler(5, 0, 0, TimeUnit.SECONDS));
                    // 基于定长的方式解决粘包/拆包问题
                    pipeline.addLast(new LengthFieldBasedFrameDecoder(nettyConfig.getMaxFrameLength()
                            , 0, 2, 0, 2));
                    pipeline.addLast(new LengthFieldPrepender(2));
                    // 序列化
                    pipeline.addLast(new ObjectCodec());
                    pipeline.addLast(channelHandlerAdapter);
                }
            });
            logger.info("netty服务器在[{}]端口启动监听", port);
            ChannelFuture f = serverBootstrap.bind(port).sync();
            f.channel().closeFuture().sync();
        } catch (InterruptedException e) {
            logger.info("[出现异常] 释放资源");
            boss.shutdownGracefully();
            worker.shutdownGracefully();
        }
    }
}
