package com.bytter.cloud.auth.authentication.service;
import com.google.common.base.Objects;
import lombok.Getter;
import org.springframework.http.HttpMethod;
import org.springframework.security.web.servlet.util.matcher.MvcRequestMatcher;
import org.springframework.web.servlet.handler.HandlerMappingIntrospector;
/**
 * mvc请求匹配器
 *
 * @author zhoul 2021-1-26
 * @since 1.0.0
 */
@Getter
public class NewMvcRequestMatcher extends MvcRequestMatcher {
    private final String pattern;
    private final String method;
    public NewMvcRequestMatcher(HandlerMappingIntrospector introspector, String pattern, String method) {
        super(introspector, pattern);
        this.setMethod(HttpMethod.resolve(method));
        this.pattern = pattern;
        this.method = method;
    }
    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        NewMvcRequestMatcher that = (NewMvcRequestMatcher) o;
        return Objects.equal(pattern, that.pattern)
                && Objects.equal(method, that.method);
    }
    @Override
    public int hashCode() {
        return Objects.hashCode(pattern, method);
    }
}
