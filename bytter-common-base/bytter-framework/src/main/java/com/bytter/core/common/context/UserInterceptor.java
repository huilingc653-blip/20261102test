package com.bytter.core.common.context;

import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.Set;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.collections.MapUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.lang.Nullable;
import org.springframework.web.servlet.HandlerInterceptor;
import com.bytter.cloud.common.core.util.JsonUtil;
import com.bytter.core.base.entity.ShiroUser;
import com.bytter.core.cache.redis.core.RedisCacheKey;
import com.bytter.core.common.exception.BusinessException;
import com.bytter.core.enums.XssCodeEnum;
import com.bytter.core.utils.JsonUtils;
import com.bytter.core.utils.StringUtils;
import cn.hutool.core.util.StrUtil;
import cn.hutool.core.util.URLUtil;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class UserInterceptor implements HandlerInterceptor {

	@Autowired
    private RedisTemplate<String, Object> redisTemplate;



    private static final String X_CLIENT_TOKEN_USER = "x-client-token-user";

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
		// 先清理一下当前上下文，防止污染
		UserContextHelper.helper().removeUser();
        //从网关获取并校验,通过校验就可信任x-client-token-user中的信息 encode防止中文乱码
        String tokenUser = request.getHeader(X_CLIENT_TOKEN_USER);
        String decodeUser = URLUtil.decode(tokenUser, StandardCharsets.UTF_8);
        log.info("获取用户信息：{}", decodeUser);
        if (StringUtils.isBlank(decodeUser)) {
            log.warn("未获取到用户信息{}", request.getRequestURI());
        } else {
       		// checkXss(request);
			// log.info("获取用户信息：{}", decodeUser);
            ShiroUser shiroUser = JsonUtils.parseObject(decodeUser, ShiroUser.class);

			String hashKey = RedisCacheKey.makeRedisKey("bytter-system", RedisCacheKey.USER_SYS_SKIN);
			Object sysSkin = redisTemplate.opsForHash().get(hashKey, shiroUser.getId());
			shiroUser.setSysSkin(StrUtil.toStringOrNull(sysSkin));
			UserContextHelper.helper().setUser(shiroUser);
        }
        return true;
    }

    public Boolean checkXss(HttpServletRequest serverHttpRequest) throws UnsupportedEncodingException {
    	log.debug("----自定义防XSS攻击网关全局过滤器生效----");
//	    String method = serverHttpRequest.getMethod();//POST/GET
	    String uri = serverHttpRequest.getRequestURI();
	    if(uri.indexOf(".")>-1) {//请求的是资源文件 或.css或.js或.png等全部放行
        	return true;
        }
	    Map<String, String[]> a = serverHttpRequest.getParameterMap();
	    if(checkValue(a)) {
	    	log.debug("原请求参数为：{}", JsonUtil.toJson(a));
	    	throw new BusinessException(XssCodeEnum.XSS_SQL_CHECK_EXCEPTION.getMsg());
	    }
	    return true;
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, @Nullable Exception ex) throws Exception {
        UserContextHelper.helper().removeUser();
    }


    public boolean checkValue(Map<String, String[]> map) throws UnsupportedEncodingException {
    	if(!MapUtils.isEmpty(map)) {
    		Set<String> keys = map.keySet();
        	for(String key : keys){
        		if(XssCleanRuleUtils.xssClean(key) || XssCleanRuleUtils.cleanSqlKeyWords(key)) {
        			 return true;
        		}
        	    String[] b = map.get(key);
        	    if(b.length>0) {
        	    	for(String value : b) {
        	    		if(XssCleanRuleUtils.xssClean(value) || XssCleanRuleUtils.cleanSqlKeyWords(value)) {
               			 return true;
        	    		}
        	    	}
        	    }
        	}
    	}
    	return false;
    }

}
