package com.bc.netty;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

/**
 * Netty服务端启动类
 *
 * @author zhou
 */
@SpringBootApplication
@ComponentScan({"com.bc.netty.server"})
public class NettyServerApplication {

    /**
     * @param args
     */
    public static void main(String[] args) {
        SpringApplication.run(NettyServerApplication.class);
    }
}
