package com.bytter.cloud.gateway.filter;

import cn.hutool.core.util.ObjectUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.HttpMethod;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.util.MultiValueMap;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.net.URI;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * APP请求重定向过滤器。
 *
 * @author zhaohc
 * @date 2024/4/20 16:30
 */
@Slf4j
@Component
public class CustomRewritePathFilter implements GlobalFilter, Ordered {

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        ServerHttpRequest request = exchange.getRequest();
        MultiValueMap<String, String> queryParams = request.getQueryParams();
        String customMethod = queryParams.getFirst("customMethod");
        if (customMethod != null) {
            log.info(" 重定义请求模式：{}，追加Header参数。", customMethod);
            ServerHttpRequest newRequest = request.mutate()
                    .method(Objects.requireNonNull(HttpMethod.resolve(customMethod)))
                    .header("reqterminal","app")
                    .build();
            return chain.filter(exchange.mutate().request(newRequest).build());
        }
        return chain.filter(exchange);
    }

    @Override
    public int getOrder() {
        return Ordered.HIGHEST_PRECEDENCE;
    }

    public static void main(String[] args) {
        System.out.println("HttpMethod.resolve(\"POST\") = " + HttpMethod.resolve("POST"));
    }
}
