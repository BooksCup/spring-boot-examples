package com.bc.netty.client;

import com.bc.netty.client.handler.NettyClientHandler;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class NettyClient {
    private static final Logger LOGGER = LoggerFactory.getLogger(NettyClient.class);
    private static int retry = 0;
    /**
     * 初始化Bootstrap实例， 此实例是netty客户端应用开发的入口
     */
    private Bootstrap bootstrap;
    /**
     * 工人线程组
     */
    private EventLoopGroup worker;
    /**
     * 远程端口
     */
    private int port;
    /**
     * 远程服务器url
     */
    private String url;
    /**
     * 默认重连机制为10交
     */
    private int MAX_RETRY_TIMES = 10;

    public NettyClient(int port, String url) {
        this.port = port;
        this.url = url;
        bootstrap = new Bootstrap();
        worker = new NioEventLoopGroup();
        bootstrap.group(worker);
        bootstrap.channel(NioSocketChannel.class);
    }

    public void start() {
        LOGGER.info("{} -> [启动连接] {}:{}", this.getClass().getName(), url, port);
        bootstrap.handler(new NettyClientHandler());
        ChannelFuture f = bootstrap.connect(url, port);
        try {
            f.channel().closeFuture().sync();
        } catch (InterruptedException e) {
            retry++;
            if (retry > MAX_RETRY_TIMES) {
                throw new RuntimeException("调用Wrong");
            } else {
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e1) {
                    e1.printStackTrace();
                }
                LOGGER.info("第{}次尝试....失败", retry);
                start();
            }
        }
    }
}
