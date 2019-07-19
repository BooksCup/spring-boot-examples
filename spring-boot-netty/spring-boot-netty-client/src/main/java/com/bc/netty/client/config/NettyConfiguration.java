package com.bc.netty.client.config;

import com.bc.netty.client.util.NettyBeanScanner;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class NettyConfiguration {
    /**
     * 初始化加载Netty相关bean的配置方法
     *
     * @param basePackage 配置的包名
     * @param clientName  配置的Netty实例对象名
     * @return NettyBeanScanner
     */
    @Bean
    public static NettyBeanScanner initNettyBeanScanner(@Value("${netty.basePackage}") String basePackage,
                                                        @Value("${netty.clientName}") String clientName) {
        // 创建对象
        return new NettyBeanScanner(basePackage, clientName);
    }
}
