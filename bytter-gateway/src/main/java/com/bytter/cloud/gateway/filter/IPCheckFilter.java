package com.bytter.cloud.gateway.filter;

import com.bytter.cloud.gateway.auth.service.IAuthService;
import com.bytter.cloud.gateway.context.GatewayCodeEnum;
import com.bytter.cloud.gateway.exception.IpCheckException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.net.InetAddress;
import java.net.InetSocketAddress;

/**
 * IP白名单过滤器
 * 第一优先级
 */
@Component
@Slf4j
public class IPCheckFilter implements GlobalFilter , Ordered {

    private static final String HEADER_X_FORWARDED_FOR = "x-forwarded-for";
    private static final String HEADER_PROXY_CLIENT_IP = "Proxy-Client-IP";
    private static final String HEADER_WL_PROXY_CLIENT_IP = "WL-Proxy-Client-IP";

    private static final String X_REAL_IP = "X-Real-IP";

    private static final String UNKNOWN = "unknown";
    private static final String LOCALHOST = "127.0.0.1";

    private static final String LOCAL = "localhost";
    private static final String SEPARATOR = ",";

    @Autowired
    private IAuthService authService;

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        String realIpAddress = getRealIpAddress(exchange.getRequest());
        if (LOCALHOST.equalsIgnoreCase(realIpAddress) || LOCAL.equalsIgnoreCase(realIpAddress)) {
            return chain.filter(exchange);
        }
        if (authService.checkIp(realIpAddress)) {
            return chain.filter(exchange);
        }else {
            throw new IpCheckException(GatewayCodeEnum.IP_CHECK_FAIL);
        }
    }

    @Override
    public int getOrder() {
        return Ordered.HIGHEST_PRECEDENCE;
    }


    /**
     * 获取真实客户端IP
     */
    public static String getRealIpAddress(ServerHttpRequest serverHttpRequest) {
        String ipAddress;
        try {
            ipAddress = serverHttpRequest.getHeaders().getFirst(HEADER_X_FORWARDED_FOR);
            if (StringUtils.isEmpty(ipAddress) || UNKNOWN.equalsIgnoreCase(ipAddress)) {
                ipAddress = serverHttpRequest.getHeaders().getFirst(HEADER_PROXY_CLIENT_IP);
            }
            if (StringUtils.isEmpty(ipAddress) || UNKNOWN.equalsIgnoreCase(ipAddress)) {
                ipAddress = serverHttpRequest.getHeaders().getFirst(HEADER_WL_PROXY_CLIENT_IP);
            }
            if (StringUtils.isEmpty(ipAddress) || UNKNOWN.equalsIgnoreCase(ipAddress)) {
                ipAddress = serverHttpRequest.getHeaders().getFirst(X_REAL_IP);
            }
            // 2.如果没有转发的ip，则取当前通信的请求端的ip
            if (StringUtils.isEmpty(ipAddress) || UNKNOWN.equalsIgnoreCase(ipAddress)) {
                InetSocketAddress inetSocketAddress = serverHttpRequest.getRemoteAddress();
                if(inetSocketAddress != null) {
                    ipAddress = inetSocketAddress.getAddress().getHostAddress();
                }
               if(LOCALHOST.equals(ipAddress) || "0:0:0:0:0:0:0:1".equalsIgnoreCase(ipAddress)){
                   InetAddress inet = InetAddress.getLocalHost();
                   ipAddress= inet.getHostAddress();
                }
            }
            // 对于通过多个代理的情况，第一个IP为客户端真实IP,多个IP按照','分割
            if (ipAddress != null) {
                ipAddress = ipAddress.split(SEPARATOR)[0].trim();
            }
        } catch (Exception e) {
            ipAddress = "";
        }
        log.info("获取到客户端ip:{}", ipAddress);
        return ipAddress == null ? "" : ipAddress;
    }


}
