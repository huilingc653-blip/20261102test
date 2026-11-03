package com.bytter.cloud.auth.authorization.oauth2.enhancer;


import cn.hutool.core.convert.Convert;
import com.bytter.cloud.auth.authorization.persistence.AuthUser;
import com.bytter.core.base.entity.ShiroUser;
import com.bytter.core.utils.JsonUtils;
import com.google.common.collect.Maps;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.common.DefaultOAuth2AccessToken;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.token.TokenEnhancer;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import java.util.Enumeration;
import java.util.Map;

/**
 * 自定义token携带内容
 *
 * @author zhoul 2021-1-26
 * @since 1.0.0
 */
public class CustomTokenEnhancer implements TokenEnhancer {
    
    private static final String DEFAULT_UNKNOWN_IP = "unknown";
    
    private static final String DEFAULT_SPLIT_IP = ",";

    @Override
    public OAuth2AccessToken enhance(OAuth2AccessToken accessToken, OAuth2Authentication authentication) {
        Map<String, Object> additionalInfo = Maps.newHashMap();
        if (authentication.getUserAuthentication()!=null && authentication.getUserAuthentication().getPrincipal() instanceof AuthUser) {
            AuthUser authUser = (AuthUser) authentication.getUserAuthentication().getPrincipal();
            ShiroUser cutUser = authUser2ShiroUser(authUser);
            //用户信息通过网关像下游传递
            additionalInfo.put("sub", JsonUtils.toJSONString(cutUser));
            additionalInfo.put("uid", cutUser.getId());
            additionalInfo.put("tenantId", cutUser.getTenantId());
        }
        ((DefaultOAuth2AccessToken) accessToken).setAdditionalInformation(additionalInfo);
        //设置全局authentication
        SecurityContext context = SecurityContextHolder.createEmptyContext();
		context.setAuthentication(authentication);
		SecurityContextHolder.setContext(context);
        return accessToken;
    }

    /**
     * 需要在设置其他值 减少token体积
     * @param user
     * @return
     */
    private ShiroUser authUser2ShiroUser(AuthUser user) {
        ShiroUser currentUser = new ShiroUser();
        currentUser.setId(user.getCurrentUserId());
        currentUser.setCorpId(user.getCorpId());
        currentUser.setLoginName(user.getLoginName());
        currentUser.setUsername(user.getName());
        Map<String, Object> userDetailMap = user.getUserDetailMap();
        currentUser.setUserCadn(Convert.toStr(userDetailMap.get("userCadn"), ""));
        currentUser.setTenantId(user.getTenantId());
        return currentUser;
    }
    
    /**
     * 获取ip
     *
     * @param request 请求对象
     * @return ip地址
     */
    private String getIpAddress(HttpServletRequest request) {
        final String[] headers = new String[] {
                "x-forwarded-for", "X-Forwarded-For", "X-Real-IP",
                "Proxy-Client-IP", "WL-Proxy-Client-IP",
                "HTTP_CLIENT_IP", "HTTP_X_FORWARDED_FOR"
        };
        return this.getIps(request, headers);
    }
    
    /**
     * 获取ip
     *
     * @param request 请求对象
     * @param headers 请求头列表
     * @return ip地址
     */
    private String getIps(HttpServletRequest request, String... headers) {
        String ip = null;
        if (headers != null && headers.length != 0) {
            Enumeration<String> headerList;
            for (String header: headers) {
                if (StringUtils.isEmpty(header)) {
                    continue;
                }
                headerList = request.getHeaders(header);
                if (headerList == null || !headerList.hasMoreElements()) {
                    continue;
                }
                ip = headerList.nextElement();
                if (ip == null || ip.length() == 0 || DEFAULT_UNKNOWN_IP.equalsIgnoreCase(ip)) {
                    continue;
                } else {
                    ip = this.splitIp(ip, DEFAULT_SPLIT_IP, 0);
                    break;
                }
            }
        }
        if (ip == null || ip.length() == 0 || DEFAULT_UNKNOWN_IP.equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }
        if (ip == null || ip.length() == 0 || DEFAULT_UNKNOWN_IP.equalsIgnoreCase(ip)) {
            return null;
        }
        return ip;
    }
    
    /**
     * 获取跟个后的ip
     *
     * @param ips ip组合字符串
     * @param split 分隔符
     * @param index 第几个
     * @return 返回ip
     */
    private String splitIp(String ips, String split, int index) {
        if (ips == null || ips.equalsIgnoreCase("")) {
            return ips;
        }
        if (ips.indexOf(split) != -1) {
            String[] ipArray = ips.split(split);
            return ipArray[index];
        } else {
            return ips;
        }
    }
}
