package com.bytter.cloud.auth.controller;

import cn.hutool.core.net.URLEncodeUtil;
import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import cn.hutool.jwt.JWT;
import cn.hutool.jwt.JWTUtil;
import com.bytter.cloud.auth.authorization.oauth2.sso.base.SsoRestUtil;
import com.bytter.cloud.auth.authorization.oauth2.sso.base.SsoTenant;
import com.bytter.cloud.auth.authorization.oauth2.sso.dsl.SsoDslConfig;
import com.bytter.cloud.auth.authorization.oauth2.sso.shdq.SsoTenantProperties;
import com.bytter.core.cache.redis.core.RedisCacheKey;
import com.bytter.core.constant.CommonConstant;
import com.bytter.system.api.config.SM2ManagerService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.annotation.Resource;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;

/**
 * 大森林SSO单点登陆控制器。
 *
 * @author zhaohc
 * @date 2024/4/29 9:35
 */
@Slf4j
@Validated
@Controller
@RequestMapping("/sso")
public class SsoLoginController {

    @Value("${bytter.gateway.url}")
    private String gatewayUrl;
    @Value("${sso.service.client_id}")
    private String clientId;
    @Value("${sso.service.client_secret}")
    private String clientSecret;

    @Resource
    private SsoDslConfig ssoDslConfig;
    @Resource
    private SsoTenantProperties ssoTenantProperties;
    @Resource
    private SsoRestUtil ssoRestUtil;
    @Autowired
    private SM2ManagerService sm2ManagerService;
    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    @GetMapping("/dsl/login")
    public String dslLoginTicket(HttpServletRequest request, HttpServletResponse response, Model model) {
        // 添加标识Cookie，避免登陆失败时未携带退出标识
        Cookie cookie = new Cookie(CommonConstant.SSO_TENANT, SsoTenant.DSL.getTenant());
        cookie.setPath("/");
        response.addCookie(cookie);
        // 获取大森林SSO返回的ticket
        String ticket = request.getParameter("ticket");
        log.info("大森林SSO返回ticket：{}", ticket);
        // 直接以SSO的鉴权方式去调用系统鉴权方法，纳入鉴权体系
        Map<String, Object> params = new HashMap<>();
        params.put("client_id", clientId);
        params.put("client_secret", clientSecret);
        params.put("grant_type", "sso_token");
        params.put("scope", "read");
        params.put("token", ticket);
        params.put("tenant", SsoTenant.DSL.getTenant());
        // 校验成功后，生成登陆token
        // gatewayUrl = "http://192.168.0.111:8080";
        log.info("鉴权请求：【{}】", gatewayUrl + "/oauth/token");
        String result = HttpUtil.post(gatewayUrl + "/oauth/token", params);
        log.info("result===>{}", result);
        // {"success":false,"code":null,"msg":"用户不存在","msgParams":null,"time":"2024-06-06 14:15:16"}
        JSONObject tokenData = JSONUtil.parseObj(result);
        if (!tokenData.getBool("success",true)) {
            // 鉴权失败
            String msg = tokenData.getStr("msg");
            log.error("SSO登陆鉴权失败：【{}】", msg);
            model.addAttribute("errorMsg", msg);
            return "redirect:" + gatewayUrl + "/login?errorMsg=" + URLEncodeUtil.encode(msg);
        }
        // 添加登陆Cookie
        String accessToken = tokenData.getStr("access_token");
        cookie = new Cookie(CommonConstant.ACCESS_TOKEN, accessToken);
        cookie.setPath("/");
        response.addCookie(cookie);
        // 清除锁屏信息
        JWT jwt = JWTUtil.parseToken(accessToken);
        JSONObject payloads = jwt.getPayloads();
        String uid = payloads.getStr("uid");
        String hashKey = RedisCacheKey.makeRedisKey("", RedisCacheKey.LOCK_SCREEN);
        boolean isFlag = redisTemplate.opsForHash().delete(hashKey, uid) > 0;
        log.error("清除用户【{}】的锁屏信息【{}】", uid, isFlag);
        // 跳转到首页
        return "redirect:" + gatewayUrl;
    }

    public static void main(String[] args) {
        String accessToken = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiJ7XCJpZFwiOlwiNDAyODgwNDI1YjZiMGE1ODAxNWI2YjBhYmViMTAwMDBcIixcImxvZ2luTmFtZVwiOlwiYWRtaW5cIixcInVzZXJuYW1lXCI6XCLotoXnuqfnrqHnkIblkZhcIixcImNvcnBJZFwiOlwiMmY1NzIyZDBiNGI2NDM1ODkzNDk4NjM3NmUwMTVkZmVcIixcInRlbmFudElkXCI6XCIwMDBcIixcImFkbWluXCI6ZmFsc2V9IiwidWlkIjoiNDAyODgwNDI1YjZiMGE1ODAxNWI2YjBhYmViMTAwMDAiLCJ1c2VyX25hbWUiOiJhZG1pbiIsInNjb3BlIjpbInJlYWQiXSwiZXhwIjoxNzIxNDE1Mzk5LCJhdXRob3JpdGllcyI6WyJhNGIwM2JlODUwYTQ0OTdhOTdlZDZkMTNlOWViYWNhZiIsImM5MDhkZjU1NmFlNTRmYjE4MjJkOTE2NjU2ZjQ0ZGFkIiwiNDhhOWEyMzhkNTcwNDM0YTg0MDczOTUzZmUwM2Y2MGIiLCI5OWQ5MWNkMjQwNTI0NzU2YjUxZTQ2YzhiN2ExZDNmOCIsIjQwMjg4MDQyNWI5NTE3NzcwMTViOTUyM2UwM2QwMDBhIl0sImp0aSI6ImU4MGRmMDE5LTk0ZGMtNGY1Yi1iYWRhLTYzNjIwNDA1ZGFjNiIsImNsaWVudF9pZCI6InRlc3RfY2xpZW50In0.9qKwXBl4b1Sz3MTYWBJ5GaSNR7ymhhz_heX_p6gKveM";
        JWT jwt = JWTUtil.parseToken(accessToken);
        System.out.println("jwt = " + jwt.getPayloads());
        JSONObject payloads = jwt.getPayloads();
        System.out.println("jwt = " + payloads.getStr("uid"));
    }

}
