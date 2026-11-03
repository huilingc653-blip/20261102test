package com.bytter.cloud.auth.utils;
import javax.servlet.http.HttpServletRequest;
/**
 * bytter 父类 UserDetailsService
 *
 * @author limm 2022-07-18
 * @since 1.0.0
 */
public final class ThreadLocalRequestUtils {
    
    private static final ThreadLocal<HttpServletRequest> THREAD_LOCAL_REQUEST = new ThreadLocal();
    
    public static void set(HttpServletRequest request) {
        THREAD_LOCAL_REQUEST.set(request);
    }
    
    public static HttpServletRequest get() {
        return THREAD_LOCAL_REQUEST.get();
    }
    
    public static void remove() {
        THREAD_LOCAL_REQUEST.remove();
    }
}
