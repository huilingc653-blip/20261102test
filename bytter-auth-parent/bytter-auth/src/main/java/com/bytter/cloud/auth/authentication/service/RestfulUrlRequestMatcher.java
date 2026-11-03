package com.bytter.cloud.auth.authentication.service;
import com.google.common.base.Objects;
import lombok.Getter;
import org.springframework.http.HttpMethod;
import org.springframework.security.web.servlet.util.matcher.MvcRequestMatcher;
import org.springframework.util.AntPathMatcher;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.handler.HandlerMappingIntrospector;
import javax.servlet.http.HttpServletRequest;
/**
 * Restful URL请求匹配器
 *
 * @author chenlw 2021-04-07
 * @since 1.0.0
 */
@Getter
public class RestfulUrlRequestMatcher extends MvcRequestMatcher {
    /**
     * RESTFUL URL匹配器
     */
    protected static final AntPathMatcher ANTPATHMATCHER = new AntPathMatcher();
    private final String pattern;
    private final String method;
    public RestfulUrlRequestMatcher(HandlerMappingIntrospector introspector, String pattern, String method) {
        super(introspector, pattern);
        this.setMethod(HttpMethod.resolve(method));
        this.pattern = pattern;
        this.method = method;
    }
    @Override
    public boolean matches(HttpServletRequest request) {
        // 重写URL匹配逻辑
        String requestMethod = request.getMethod();
        if (!StringUtils.isEmpty(this.method) && !this.method.equalsIgnoreCase(requestMethod)) {
            return false;
        }
        String requestUrl = request.getServletPath();
        return ANTPATHMATCHER.match(this.pattern, requestUrl);
    }
    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        RestfulUrlRequestMatcher that = (RestfulUrlRequestMatcher) o;
        return Objects.equal(pattern, that.pattern)
                && Objects.equal(method, that.method);
    }
    @Override
    public int hashCode() {
        return Objects.hashCode(pattern, method);
    }
}
