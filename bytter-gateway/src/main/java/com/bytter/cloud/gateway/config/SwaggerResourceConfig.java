package com.bytter.cloud.gateway.config;

import com.alibaba.nacos.shaded.io.grpc.internal.JsonUtil;
import com.bytter.cloud.gateway.util.JsonUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.config.GatewayProperties;
import org.springframework.cloud.gateway.route.RouteDefinition;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import springfox.documentation.swagger.web.SwaggerResource;
import springfox.documentation.swagger.web.SwaggerResourcesProvider;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
@Slf4j
@Configuration
public class SwaggerResourceConfig {

    @Autowired
    private GatewayProperties gatewayProperties;

    @Bean
    @Primary
    public SwaggerResourcesProvider forceSingleServiceProvider() {
        return () -> {
            List<SwaggerResource> resources = new ArrayList<>();
            // 从配置中提取所有 api-docs- 开头的路由
            List<RouteDefinition> routes = gatewayProperties.getRoutes().stream()
                    .filter(route -> route.getId() != null && route.getId().startsWith("api-docs-"))
                    .collect(Collectors.toList());
            // 为每个 api-docs 路由创建 Swagger 资源
            for (RouteDefinition routeDefinition : routes) {
                // 提取服务名称：api-docs-system -> 系统服务
                SwaggerResource resource = getSwaggerResource(routeDefinition);
                resources.add(resource);
            }
            return resources;
        };
    }

    private static SwaggerResource getSwaggerResource(RouteDefinition routeDefinition) {
        String routeId = routeDefinition.getId();
        String url = "/" + routeId + "/v2/api-docs";
        SwaggerResource resource = new SwaggerResource();
        resource.setName(routeDefinition.getUri().toString().replace("lb://", ""));
        resource.setUrl(url);
        resource.setLocation(url);
        resource.setSwaggerVersion("2.0");
        return resource;
    }


}