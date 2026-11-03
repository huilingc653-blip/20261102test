package com.bytter.cloud.gateway.filter;

import cn.hutool.core.util.ObjectUtil;
import com.bytter.cloud.gateway.auth.service.IAuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.Ordered;
import org.springframework.http.HttpCookie;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.PathContainer;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.util.MultiValueMap;
import org.springframework.util.StringUtils;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 关闭渗透测试涉及的维护、接口文档及暂不开放的字典入口。
 * 使用 WebFilter，同时覆盖网关自身接口及转发请求，不受业务匿名白名单影响。
 *
 * @author laozhao
 * @since 2026-10-15
 */
@Component
public class RestrictedEndpointWebFilter implements WebFilter, Ordered {

    private static final String[] RESTRICTED_PATHS = {
            "/**/actuator/**", "/**/swagger-resources/**",
            "/**/v2/api-docs/**", "/**/v3/api-docs/**", "/api-docs-*/**",
            "/swagger-ui.html", "/swagger-ui/**", "/doc.html",
            "/dict/**", "/v1-system/dict/**"
    };

    private static final String CLIENT_TOKEN = "Access-Token";
    private static final String APP_TOKEN = "Authorization";
    private static final String ACCESS_TOKEN = "access_token";

    @Autowired
    private IAuthService authService;

    private final AntPathMatcher pathMatcher = new AntPathMatcher();

    /**
     * 在进入网关自身处理器或业务路由前拒绝关闭的入口。
     *
     * @param exchange 当前请求和响应
     * @param chain 后续请求处理链
     * @return 拒绝响应的完成信号，或后续处理结果
     */
    @Override
    public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {
        // 按实际路由语义解码路径并去除矩阵参数，避免编码或分号绕过路径限制。
        String path = exchange.getRequest().getPath().pathWithinApplication().elements().stream()
                .map(element -> element instanceof PathContainer.PathSegment
                        ? ((PathContainer.PathSegment) element).valueToMatch() : element.value())
                .collect(Collectors.joining());
        path = StringUtils.cleanPath(path).replaceAll("/{2,}", "/");
        for (String pattern : RESTRICTED_PATHS) {
            if (pathMatcher.match(pattern, path)) {
                // 字典入口等业务数据面，仅对已登录（token 有效）用户放开；
                // 未登录或 token 无效仍拦截，避免扩大匿名可达面。
                if (isAuthenticated(exchange)) {
                    return chain.filter(exchange);
                }
                exchange.getResponse().setStatusCode(HttpStatus.FORBIDDEN);
                return exchange.getResponse().setComplete();
            }
        }
        return chain.filter(exchange);
    }

    /**
     * 判断当前请求是否携带有效登录态。
     * token 取值顺序与 AccessGatewayFilter 保持一致：
     * Cookie(Access-Token) -> Header(Authorization) -> 查询参数(access_token)。
     *
     * @param exchange 当前请求和响应
     * @return true 表示已登录
     */
    private boolean isAuthenticated(ServerWebExchange exchange) {
        String token = "";
        MultiValueMap<String, HttpCookie> cookies = exchange.getRequest().getCookies();
        List<HttpCookie> httpCookies = cookies.get(CLIENT_TOKEN);
        if (ObjectUtil.isNotEmpty(httpCookies)) {
            HttpCookie httpCookie = httpCookies.get(0);
            token = CLIENT_TOKEN.equalsIgnoreCase(httpCookie.getName()) ? httpCookie.getValue() : "";
        } else {
            HttpHeaders headers = exchange.getRequest().getHeaders();
            token = headers.getFirst(APP_TOKEN);
            if (StringUtils.isEmpty(token)) {
                token = exchange.getRequest().getQueryParams().getFirst(ACCESS_TOKEN);
            }
        }
        if (StringUtils.isEmpty(token)) {
            return false;
        }
        return authService.checkToken(token);
    }

    /**
     * 保证入口关闭检查在其他请求处理之前执行。
     *
     * @return WebFilter 的最高优先级，数值越小越先执行
     */
    @Override
    public int getOrder() {
        return Ordered.HIGHEST_PRECEDENCE;
    }
}
