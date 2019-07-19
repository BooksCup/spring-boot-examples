package com.bc.netty.client.listener;

import com.bc.netty.client.NettyClient;
import com.bc.netty.client.action.MainAction;
import com.bc.netty.client.config.NettyConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

@Order(0)
@Component
public class NettyClientListener implements CommandLineRunner {
    /**
     * NettyClientListener 日志输出
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(NettyClientListener.class);

    /**
     * netty客户端配置
     */
    @Resource
    private NettyConfig nettyConfig;

    @Resource
    private MainAction mainAction;

    @Override
    public void run(String... args) throws Exception {
        LOGGER.info("{} -> [准备进行与服务端通信]", this.getClass().getName());
        Thread t1 = new Thread(() -> {
            try {
                mainAction.call();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });
        t1.start();
        int port = nettyConfig.getPort();
        String url = nettyConfig.getUrl();
        new NettyClient(port, url).start();
    }
}
