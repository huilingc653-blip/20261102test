package com.bytter.cloud.auth.controller;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.bytter.cloud.auth.authorization.oauth2.granter.MobileTokenGranter;
import com.bytter.cloud.common.core.persistence.vo.Result;
import com.bytter.core.cache.redis.core.RedisCacheKey;
import com.bytter.core.common.model.json.AjaxJson;
import com.bytter.core.utils.DateUtils;
import com.bytter.core.utils.RedisUtils;
import com.bytter.core.utils.StringUtils;
import com.bytter.starter.crypto.AESUtil;
import com.bytter.system.api.config.SM2ManagerService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.Cache;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.endpoint.TokenEndpoint;
import org.springframework.security.oauth2.provider.token.ConsumerTokenServices;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.Serializable;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

/**
 * APP鉴权入口。
 *
 * @author zhaohc
 * @date 2023/11/30 17:19
 */
@Controller
@RequestMapping("/oauth2")
public class AppAuthorizeController {

    private static final Logger LOG = LoggerFactory.getLogger(AppAuthorizeController.class);

    @Value("${push.customerPrefix}")
    private String customerPrefix;
    @Value("${app.launchImage}")
    private String launchImage;
    @Value("${app.logoImage}")
    private String logoImage;
    @Value("${app.openTenant:false}")
    private Boolean openTenant;

    @Autowired
    private SM2ManagerService sm2ManagerService;
    @Autowired
    private RedisUtils redisUtils;
    @Autowired
    private ConsumerTokenServices consumerTokenServices;
    private static final String APP_TOKEN = "Authorization";

    /**
     * 生成随机数
     *
     * @param length
     * @return
     */
    private static String getRandomString(int length) {
        String str = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        Random random = new Random();
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < length; i++) {
            int number = random.nextInt(62);
            sb.append(str.charAt(number));
        }
        return sb.toString();
    }

    /**
     * 登录盐
     *
     * @param request 请求对象
     * @return
     */
    @GetMapping("/randomString")
    @ResponseBody
    public Map<String, String> getRandomString(HttpServletRequest request) {
        Map<String, String> map = new HashMap<>(2);
        String key = getRandomString(16);
        String iv = getRandomString(16);
        map.put("key", key);
        map.put("iv", iv);
        map.put("_allow_once_", StringUtils.getUUId());
        String entryKey = RedisCacheKey.makeRedisKey(RedisCacheKey.SM2_KEY, key);
        redisUtils.put(entryKey, map, 300);
        return map;
    }

    public static void main(String[] args) {
        //{"iv":"naTkQXdddDP05RoZ","key":"bqpJAy9x4nhy5avq","_allow_once_":"7c75abc3668b45c489a2e1b29769b329"}
        String data = "mG6ltMfAWSy/rwjPWJtMPQ==";
        String decrypt = AESUtil.aesCbcDecrypt(data, "bqpJAy9x4nhy5avq", "naTkQXdddDP05RoZ","UTF-8");
        System.out.println("decrypt = " + decrypt);
        System.out.println(StringUtils.getUUId());
    }

    @GetMapping("/check")
    @ResponseBody
    public Object check(HttpServletRequest request, String flag) {
        String requestURL = request.getRequestURL().toString();
        String requestURI = request.getRequestURI();
        requestURL = requestURL.replace(requestURI, "");
        requestURL += request.getContextPath();

        HashMap<String, Object> map = new HashMap<>();
        // 返回时间标识
        map.put("flag", flag);
        map.put("customerPrefix", customerPrefix);
        // 生成当前请求路径
        map.put("pageURL", requestURL);
        // appSvFlag，标识服务器版本标识。
        map.put("appSvFlag", "Bytter");
        // APP登录密码加密标识:新版本默认都要加密；旧版本可不加密。
        map.put("appLIEnFlag", "Bytter");

        // APP启动图片
        if (!StringUtils.isEmpty(launchImage)) {
            map.put("launchImage", launchImage);
        } else {
            map.put("launchImage", requestURL + "/static/images/app/img_bytter_logo.png");
        }
        // APP logo图片
        if (!StringUtils.isEmpty(logoImage)) {
            map.put("logoImage", logoImage);
        } else {
            map.put("logoImage", requestURL + "/static/images/app/logo.png");
        }

        if ( openTenant != null ) {
            map.put("openTenant", openTenant);
        }

        // map.put("launchImage","http://192.168.0.193:8030/t2/static/images/app_start_bg.png");
        // APP-logo图片
        // map.put("logoImage","http://192.168.0.193:8030/t2/static/images/bg/blue.jpg");

        // map.put("logoImage","http://117.141.118.147:8081/btweb/api_img/logo1.png");
        map.put("app_type", "new");
        map.put("auth_type", "cloud");
        return map;
    }

    @Autowired
    private TokenEndpoint tokenEndpoint;
    @Autowired
    private AuthenticationManager authenticationManager;


    @PostMapping("/token")
    @Deprecated
    public Result getToken(HttpServletRequest request, @RequestParam Map<String, String> params) {
        try {
            String username = request.getParameter("username");
            String password = request.getParameter("password");
            if (ObjectUtil.hasEmpty(username,password)){
                return Result.fail();
            }
            UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(username, password);
            Authentication authenticate = authenticationManager.authenticate(token);
            OAuth2AccessToken body = tokenEndpoint.postAccessToken(authenticate, params).getBody();
            Map<String, Object> result = new HashMap<>();
            result.put("access_token", body.getValue());
            result.put("token_type", body.getTokenType());
            result.put("refresh_token", body.getRefreshToken());
            result.put("expires_in", body.getExpiresIn());
            result.put("scope", body.getScope().iterator().next());
            return Result.success(result);
        } catch (HttpRequestMethodNotSupportedException e) {
            e.printStackTrace();
        }
        return Result.fail();
    }

    /**
     * 解析租户编号和秘钥。
     *
     * @param header Basic字符串。
     * @return 租户编号和秘钥。
     */
    public String[] extractAndDecodeHeader(String header) {
        header = header.trim();
        byte[] base64Token = header.substring(6).getBytes(StandardCharsets.UTF_8);
        byte[] decoded;
        try {
            decoded = Base64.getDecoder().decode(base64Token);
        } catch (IllegalArgumentException e) {
            throw new BadCredentialsException(
                    "Failed to decode basic authentication token");
        }
        String token = new String(decoded, StandardCharsets.UTF_8);
        int delim = token.indexOf(":");
        if (delim == -1) {
            throw new BadCredentialsException("Invalid basic authentication token");
        }
        return new String[]{token.substring(0, delim), token.substring(delim + 1)};
    }

    /**
     * 判断是否移动端的人脸登录
     *
     * @param request 请求对象
     * @return 是否移动端的人脸登录
     */
    protected boolean isFaceLogin(HttpServletRequest request) {
        boolean flag = false;
        String password = request.getParameter("password");
        if ("faceLogin".equals(password)) {
            flag = true;
        }
        return flag;
    }


    @RequestMapping(value = "/oauth_login")
    public String oauthLogin(String response_type
            , String scope
            , String state
            , String client_id
            , String client_secret
            , String redirect_uri
            , Model model) {
        model.addAttribute("response_type", response_type);
        model.addAttribute("scope", scope);
        model.addAttribute("client_id", client_id);
        model.addAttribute("client_secret", client_secret);
        model.addAttribute("redirect_uri", redirect_uri);
        model.addAttribute("state", state);
        return "oauth2/oauth_login";
    }


    @RequestMapping(value = "/oauth_approval")
    public String oauthApproval() {
        return "oauth2/oauth_approval";
    }

    @RequestMapping("/logout")
    @ResponseBody
    public Object logout(HttpServletRequest request, HttpServletResponse response) {
        // 退出登陆
        String appToken = request.getHeader(APP_TOKEN);
        LOG.info("【access-token】===>{}", appToken);
        if (ObjectUtil.isNotEmpty(appToken)){
            if (StrUtil.startWithIgnoreCase(appToken,"bearer")){
                appToken = org.apache.commons.lang.StringUtils.substring(appToken, "bearer ".length());
            }
            boolean revoked = consumerTokenServices.revokeToken(appToken);
            LOG.info("APP退出登陆，移除Token===>{}", revoked);
            response.setHeader(APP_TOKEN,"");
        }
        return "success";
    }
}
