package com.bc.netty.client.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * netty客户端配置
 *
 * @author zhou
 */
@Component
@ConfigurationProperties(prefix = "netty")
@Data
public class NettyConfig {

    private String url;

    private int port;
}
