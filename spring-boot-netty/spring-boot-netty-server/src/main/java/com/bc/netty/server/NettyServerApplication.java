package com.bc.netty.server;

import com.bc.netty.server.listener.NettyServerListener;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import javax.annotation.Resource;

/**
 * 启动类
 *
 * @author zhou
 */
@SpringBootApplication
public class NettyServerApplication implements CommandLineRunner {
    @Resource
    private NettyServerListener nettyServerListener;

    public static void main(String[] args) {
        SpringApplication.run(NettyServerApplication.class, args);
    }

    @Override
    public void run(String... args) {
        nettyServerListener.start();
    }
}
