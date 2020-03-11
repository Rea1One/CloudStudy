package com.whu.cloudstudy_server.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.server.standard.ServerEndpointExporter;

/**
 * Author: 郭瑞景
 * Date: 2020-03-09
 * 开启 WebSocket 支持
 */

@Configuration
public class WebSocketConfig {
    /**
     * ServerEndpointExporter 作用:
     * 这个 Bean 会自动注册使用 @ServerEndpoint 注解声明的 websocket endpoint
     *
     * @return
     */
    @Bean
    public ServerEndpointExporter serverEndpointExporter() {
        return new ServerEndpointExporter();
    }
}