package com.bytter.cloud.gateway.filter;


import cn.hutool.core.util.URLUtil;
import com.bytter.cloud.gateway.auth.service.IAuthService;
import com.bytter.cloud.gateway.context.GatewayCodeEnum;
import com.bytter.cloud.gateway.exception.AuthenticationException;
import com.bytter.cloud.gateway.exception.TokenException;
import io.jsonwebtoken.Claims;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.http.HttpCookie;
import org.springframework.http.HttpMethod;
import org.springframework.http.server.PathContainer;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.util.StringUtils;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.stream.Collectors;

/**
 * 请求url权限校验
 *
 * @author zhoul 2021-1-19
 * @since 1.0.0
 */
@Slf4j
@Component
public class AccessGatewayFilter implements GlobalFilter {

    private static final String X_CLIENT_TOKEN_USER = "x-client-token-user";
    private static final String TENANT_HEADER = "X-Tenant-ID";

    private static final String CLIENT_TOKEN = "Access-Token";
    private static final String APP_TOKEN = "Authorization";
    private static final String ACCESS_TOKEN = "access_token";

    private static final String LOGIN_LANGUAGE_URL = "/lang/login";
    private static final String SYSTEM_LOGIN_LANGUAGE_URL = "/v1-system/lang/login";
    private final AntPathMatcher pathMatcher = new AntPathMatcher();

    private String[] staticResource = {"/static"};

    @Autowired
    private IAuthService authService;

    public boolean matchStaticResource(String uri) {
		for(String w:staticResource) {
			if(uri.equals(w) || uri.startsWith(w + "/")) {
				return true;
			}
		}
		return false;
	}
    /**
     * 1.首先白名单过滤
     * 2.前端forward请求与ajax请求逻辑
     *
     * @param exchange 通道
     * @param chain    链条
     * @return 执行链
     */
    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        ServerHttpRequest request = exchange.getRequest();
        // 与服务端路径匹配保持一致，避免编码、矩阵参数或路径回退绕过入口鉴权。
        String url = request.getPath().pathWithinApplication().elements().stream()
                .map(element -> element instanceof PathContainer.PathSegment
                        ? ((PathContainer.PathSegment) element).valueToMatch() : element.value())
                .collect(Collectors.joining());
        url = StringUtils.cleanPath(url).replaceAll("/{2,}", "/");
        ServerHttpRequest.Builder builder = request.mutate();
        log.info("请求URL：{}", url);
        //静态资源过滤
        if(matchStaticResource(url)) {
        	return chain.filter(exchange.mutate().request(builder.build()).build());
        }
        // 登录页只公开固定文本入口；其余语言资源必须登录，不允许旧白名单中的 /lang 绕过检查。
        boolean languageRequest = pathMatcher.match("/lang/**", url)
                || pathMatcher.match("/v1-system/lang/**", url);
        boolean publicLoginLanguage = HttpMethod.GET.equals(request.getMethod())
                && (LOGIN_LANGUAGE_URL.equals(url) || SYSTEM_LOGIN_LANGUAGE_URL.equals(url));
        if (publicLoginLanguage || (!languageRequest && authService.ignoreAuthentication(url))) {
            return chain.filter(exchange.mutate().request(builder.build()).build());
        }
        String token = resolveToken(request);
        //如果为空重定向到登陆页面
        if (StringUtils.isEmpty(token)) {
            throw new TokenException(GatewayCodeEnum.TOKEN_EX_FAIL);
        }
        // 验证token是否存在及过期
        if (!authService.checkToken(token)) {
            throw new TokenException(GatewayCodeEnum.TOKEN_EX_FAIL);
        }
        Claims claims = authService.getJwt(token).getBody();
        if (claims.getExpiration().before(new Date())) {
            throw new TokenException(GatewayCodeEnum.TOKEN_EX_FAIL);
        }
        // 语言资源供所有已登录用户加载，不要求额外业务权限；其他请求保留原有功能鉴权。
        if (!languageRequest) {
            boolean permission = authService.hasPermissionV1((String) claims.get("uid"), url, request.getMethodValue(),(String)claims.get("tenantId"));
            log.info("请求URL：{} 鉴权结果：{}，请求租户：{}", url, permission, claims.get("tenantId"));
            if(!permission){
                throw new AuthenticationException(GatewayCodeEnum.COMMON_AUTH_LIMIT);
            }
        }
        builder.header(X_CLIENT_TOKEN_USER, URLUtil.encode(claims.getSubject(), StandardCharsets.UTF_8));
        builder.header(TENANT_HEADER, (String) claims.get("tenantId"));
        return chain.filter(exchange.mutate().request(builder.build()).build());
    }

    /**
     * 选取登录凭据，两种传递方式并存时必须一致，避免混用不同账号身份。
     *
     * @param request 当前受保护接口请求
     * @return 去除 Bearer 前缀后的 token；无凭据时返回空字符串
     * @throws TokenException Cookie 与请求头的登录凭据不一致
     */
    private String resolveToken(ServerHttpRequest request) {
        HttpCookie cookie = request.getCookies().getFirst(CLIENT_TOKEN);
        String cookieToken = normalizeToken(cookie == null ? null : cookie.getValue());
        String headerToken = normalizeToken(request.getHeaders().getFirst(APP_TOKEN));
        if (StringUtils.hasText(cookieToken) && StringUtils.hasText(headerToken)
                && !cookieToken.equals(headerToken)) {
            throw new TokenException(GatewayCodeEnum.TOKEN_CONFLICT);
        }
        if (StringUtils.hasText(cookieToken)) {
            return cookieToken;
        }
        if (StringUtils.hasText(headerToken)) {
            return headerToken;
        }
        // 保留无 Cookie、无请求头时使用 access_token 参数的兼容方式。
        return normalizeToken(request.getQueryParams().getFirst(ACCESS_TOKEN));
    }

    /**
     * 统一凭据表示方式，防止同一 token 因 Bearer 前缀或首尾空白被误判为不同。
     *
     * @param value Cookie、请求头或请求参数中的凭据
     * @return 标准化后的 token；输入为空时返回空字符串
     */
    private String normalizeToken(String value) {
        if (!StringUtils.hasText(value)) {
            return "";
        }
        String token = value.trim();
        String bearerPrefix = "Bearer ";
        if (token.regionMatches(true, 0, bearerPrefix, 0, bearerPrefix.length())) {
            token = token.substring(bearerPrefix.length()).trim();
        }
        return token;
    }

}
