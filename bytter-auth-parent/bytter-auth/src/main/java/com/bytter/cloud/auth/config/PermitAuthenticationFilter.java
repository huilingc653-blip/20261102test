package com.bytter.cloud.auth.config;

import com.bytter.cloud.auth.utils.ThreadLocalRequestUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.http.HttpHeaders;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Set;
/**
 * 鉴权过滤限制
 *
 * @author limm 2022-05-23
 * @since 1.0.0
 */
public class PermitAuthenticationFilter implements Filter {
    private static final String DEFAULT_LOGIN_URL = "/oauth/token";
    
    /**
     * Authorization认证开头是"bearer "
     */
    private static final String BEARER = "Bearer ";
    
    private static final String BASIC = "Basic";
    
    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain)
            throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;
        if (DEFAULT_LOGIN_URL.equals(request.getRequestURI())) {
            String authentication = request.getHeader(HttpHeaders.AUTHORIZATION);
            if (StringUtils.isNotBlank(authentication) && authentication.startsWith(BEARER)) {
                request = new HttpServletRequestWrapper(request) {
                    private Set<String> headerNameSet;
                    
                    @Override
                    public Enumeration<String> getHeaderNames() {
                        if (headerNameSet == null) {
                            headerNameSet = new HashSet<>();
                            Enumeration<String> wrappedHeaderNames = super.getHeaderNames();
                            while (wrappedHeaderNames.hasMoreElements()) {
                                String headerName = wrappedHeaderNames.nextElement();
                                if (!HttpHeaders.AUTHORIZATION.equalsIgnoreCase(headerName)) {
                                    headerNameSet.add(headerName);
                                }
                            }
                        }
                        return Collections.enumeration(headerNameSet);
                    }
                    
                    @Override
                    public Enumeration<String> getHeaders(String name) {
                        if (HttpHeaders.AUTHORIZATION.equalsIgnoreCase(name)) {
                            return Collections.<String>emptyEnumeration();
                        }
                        return super.getHeaders(name);
                    }
                    
                    @Override
                    public String getHeader(String name) {
                        if (HttpHeaders.AUTHORIZATION.equalsIgnoreCase(name)) {
                            return String.format("%s %s", BASIC, "dGVzdF9jbGllbnQ6dGVzdF9zZWNyZXQ");
                        }
                        return super.getHeader(name);
                    }
                };
            }
        }
        try {
            ThreadLocalRequestUtils.set(request);
            filterChain.doFilter(request, response);
        } finally {
            ThreadLocalRequestUtils.remove();
        }
    }
}
