package com.bytter.core.utils;
import org.springframework.web.context.ContextLoader;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
/**
 * 下文工具类
 *
 * @author zhoul
 * @since 2012-12-15
 */
public class ContextHolderUtils {
    private static ServletContext servletContext = null;
    /**
     * SpringMvc下获取request
     *
     * @return {@link HttpServletRequest}
     */
    public static HttpServletRequest getRequest() {
        return ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
    }
    /**
     * SpringMvc下获取session
     *
     * @return {@link HttpSession}
     */
    public static HttpSession getSession() {
        return getRequest().getSession();
    }
    /**
     * 获取应用的ServletContext
     *
     * @return ServletContext
     */
    public static ServletContext getServletContext() {
        if (null == servletContext) {
            WebApplicationContext webApplicationContext = ContextLoader.getCurrentWebApplicationContext();
            servletContext = webApplicationContext.getServletContext();
        }
        return servletContext;
    }
}
