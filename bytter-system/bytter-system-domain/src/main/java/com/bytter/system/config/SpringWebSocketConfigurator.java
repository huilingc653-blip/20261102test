package com.bytter.system.config;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Configuration;

import javax.websocket.server.ServerEndpointConfig;

/**
 * Spring WebSocket 配置。
 *
 * @author zhaohc
 * @since 2026/3/31 18:18
 */
@Configuration
public class SpringWebSocketConfigurator extends ServerEndpointConfig.Configurator implements ApplicationContextAware {

    private static ApplicationContext applicationContext;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        SpringWebSocketConfigurator.applicationContext = applicationContext;
    }

    @Override
    public <T> T getEndpointInstance(Class<T> endpointClass) throws InstantiationException {
        // 从 Spring 容器中获取 Bean，而不是让 WebSocket 容器直接实例化
        return applicationContext.getBean(endpointClass);
    }
}
