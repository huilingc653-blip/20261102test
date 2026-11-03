package com.bytter.core.common.context;

import cn.hutool.core.util.ObjectUtil;
import com.bytter.core.utils.IpUtil;
import org.slf4j.MDC;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * 自定义日志过滤器。
 *
 * @author zhaohc
 * @since 2026/1/19 18:11
 */
@Component
public class CustomLoggingFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        // 获取客户端IP
        String clientIp = IpUtil.getClientIpAddress(request);

        // 获取服务器IP和主机名
        String serverIp = IpUtil.getServerIpAddress();
        String hostname = IpUtil.getHostname();

        // 将信息放入MDC（Mapped Diagnostic Context）
        MDC.put("clientIp", clientIp);
        MDC.put("serverIp", serverIp);
        MDC.put("hostname", hostname);

        // 可选：添加请求相关信息
        MDC.put("requestPath", request.getRequestURI());
        MDC.put("requestMethod", request.getMethod());

        try {
            filterChain.doFilter(request, response);
        } finally {
            // 请求完成后清除MDC
            MDC.clear();
        }
    }

}
