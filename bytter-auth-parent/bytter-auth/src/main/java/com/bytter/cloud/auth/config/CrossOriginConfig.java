package com.bytter.cloud.auth.config;
import java.io.IOException;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.context.annotation.Configuration;
@Configuration
public class CrossOriginConfig implements Filter  {
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
    }
    @SuppressWarnings("unused")
    public void doFilter(ServletRequest req, ServletResponse res,
                         FilterChain chain) throws IOException, ServletException {
            HttpServletResponse response = (HttpServletResponse) res;
            HttpServletRequest request = (HttpServletRequest) req;
            //跨域请求，*代表允许全部类型
            response.setHeader("Access-Control-Allow-Origin", "*");
            //允许请求方式
            response.setHeader("Access-Control-Allow-Methods", "POST, GET, OPTIONS, DELETE,PUT");
            //用来指定本次预检请求的有效期，单位为秒，在此期间不用发出另一条预检请求
            response.setHeader("Access-Control-Max-Age", "86400");
            //请求包含的字段内容，如有多个可用哪个逗号分隔如下
            response.setHeader("Access-Control-Allow-Headers", "Content-Type,application/json");
            //访问控制允许凭据，true为允许
            response.setHeader("Access-Control-Allow-Credentials", "true");
            //这里很重要，一定要。否则你post请求就会和我一样的
            String type = request.getMethod(); 
            if(type.toUpperCase().equals("OPTIONS")){
                return;
            }
            chain.doFilter(req, res);
    }
    @Override
    public void destroy() {
    }
}
