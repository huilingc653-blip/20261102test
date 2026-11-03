package com.bytter.cloud.auth.controller;

import com.bytter.cloud.common.core.persistence.vo.Result;
import com.bytter.core.autoconfig.MessageProducer;
import com.bytter.core.common.context.UserContextHelper;
import com.bytter.core.constant.CommonConstant;
import com.bytter.core.utils.BrowserUtils;
import com.bytter.core.utils.IpUtil;
import com.bytter.system.api.IConfigService;
import com.bytter.system.api.IUserService;
import com.bytter.system.api.config.SM2ManagerService;
import com.bytter.system.api.dto.ConfigDTO;
import com.bytter.system.api.dto.LoginLogDTO;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.oauth2.provider.token.ConsumerTokenServices;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.nio.charset.StandardCharsets;
import java.util.Date;

/**
 * 登录时的token
 *
 * @author zhoul 2021-3-4
 * @since 1.0.0
 */
@Slf4j
@RestController
public class LoginTokenController {

    @Autowired
    private SM2ManagerService sm2ManagerService;
    @Autowired
    private ConsumerTokenServices consumerTokenServices;

    private static final String BEARER = "Authorization ";
    /**
     * jwt token 密钥，主要用于token解析，签名验证
     */
    @Value("${spring.security.oauth2.jwt.signingKey}")
    private String signingKey;

    @Autowired
    private MessageProducer messageProducer;

    @DubboReference
    IUserService userService;

    @DubboReference
    IConfigService configService;

    /**
     * 获取登录时的token
     *
     * @return 登录时的token
     */
    @GetMapping("/oauth/loginToken")
    public Result<String> loginToken() {
        return Result.success(sm2ManagerService.genSM2PukKey());
    }

    @GetMapping("/oauth/loginSuccessLog")
    public Result<String> loginSuccessLog(HttpServletRequest request, HttpServletResponse response) {
    	String access_token = request.getParameter("access_token");
    	Claims claims = getJwt(access_token).getBody();
    	String userid = (String) claims.get("uid");
    	ConfigDTO configDTO =configService.getGlobalConfig("sys", "loginType");
    	//记录登录事件
    	String loginName = "";
        String tenantId = "";
    	LoginLogDTO logLogin = new LoginLogDTO();
    	logLogin.setHost(IpUtil.getClientIpAddress(request));
    	logLogin.setLoginDate(new Date());
    	if(!StringUtils.isBlank(userid)) {
            loginName = (String) claims.get("user_name");
            tenantId = (String) claims.get("tenantId");
    	}
    	logLogin.setLoginName(loginName);
        logLogin.setTenantId(tenantId);
    	logLogin.setReason("登录成功事件");
    	logLogin.setStatus(1);
    	logLogin.setType(Integer.valueOf(!StringUtils.isBlank(configDTO.getValue()) ? configDTO.getValue() : "0"));
    	logLogin.setUserAgent(!StringUtils.isBlank(BrowserUtils.checkBrowse(request)) ? BrowserUtils.checkBrowse(request) : "");
    	userService.saveLoginLog(logLogin);
        return Result.success();
    }

    /**
     * 注销接口，清除当前用户的token
     *
     * @return 注销结果
     */
    @GetMapping("/oauth/logout")
    public Result<String> logOut(HttpServletRequest request, HttpServletResponse response) {
        Cookie[] cookies = request.getCookies();
        for (Cookie cookie : cookies) {
            if (CommonConstant.ACCESS_TOKEN.equals(cookie.getName())) {
                log.info("【access-token】===>{}", cookie.getValue());
                String access_token = cookie.getValue();
            	Claims claims = getJwt(access_token).getBody();
            	String userid = (String) claims.get("uid");
            	ConfigDTO configDTO =configService.getGlobalConfig("sys", "loginType");
            	//记录退出事件
            	String loginName = "";
                String tenantId = "";
            	LoginLogDTO logLogin = new LoginLogDTO();
                logLogin.setHost(IpUtil.getClientIpAddress(request));
            	logLogin.setLogoutDate(new Date());
            	if(!StringUtils.isBlank(userid)) {
            		loginName = (String) claims.get("user_name");
                    tenantId = (String) claims.get("tenantId");
            	}
            	logLogin.setLoginName(loginName);
                logLogin.setTenantId(tenantId);
            	logLogin.setReason("退出成功事件");
            	logLogin.setStatus(1);
            	logLogin.setType(Integer.valueOf(!StringUtils.isBlank(configDTO.getValue()) ? configDTO.getValue() : "0"));
            	logLogin.setUserAgent(!StringUtils.isBlank(BrowserUtils.checkBrowse(request)) ? BrowserUtils.checkBrowse(request) : "");
            	userService.saveLoginLog(logLogin);

                boolean revoked = consumerTokenServices.revokeToken(cookie.getValue());
                Cookie cookie1 = new Cookie("Access-Token", null);
                cookie1.setMaxAge(0);
                cookie1.setPath("/");
                response.addCookie(cookie1);
            }
        }
        // todo 清除用户资源缓存
        UserContextHelper.helper().removeUser();
        return Result.success();
    }

    public Jws<Claims> getJwt(String jwtToken) {
        if (jwtToken.startsWith(BEARER)) {
            jwtToken = StringUtils.substring(jwtToken, BEARER.length());
        }
        // 得到DefaultJwtParser
        return Jwts.parser()
                // 设置签名的秘钥
                .setSigningKey(signingKey.getBytes(StandardCharsets.UTF_8))
                .parseClaimsJws(jwtToken);
    }
}
