package com.bc.netty;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan({"com.bc.netty.client"})
public class NettyClientApplication {
    /**
     * @param args
     */
    public static void main(String[] args) {
        SpringApplication.run(NettyClientApplication.class);
    }
}
