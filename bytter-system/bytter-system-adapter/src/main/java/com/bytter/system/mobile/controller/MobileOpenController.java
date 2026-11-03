package com.bytter.system.mobile.controller;


import com.bytter.core.base.BaseController;
import com.bytter.core.utils.AesEcbUtils;
import com.bytter.core.utils.Base64;
import com.bytter.core.utils.JsonUtils;
import com.bytter.core.utils.StringUtils;
import com.bytter.system.api.IOauthService;
import com.bytter.system.api.constant.UserConstant;
import com.bytter.system.entity.User;
import com.bytter.system.service.UserService;
import com.bytter.third.service.ThirdProperties;
import com.fasterxml.jackson.databind.JsonNode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Date;

/**
 * 移动端开放的-控制器
 * <p>
 * 第三方系统通过此接入口，可进入APP-H5端的首页。
 *
 * @history: 1. 2020-03-13  chenlw  对外提供统一的APP-H5接入口
 */
@Controller
@RequestMapping("/mobile/open")
@Slf4j
public class MobileOpenController extends BaseController {


    @Autowired
    private IOauthService oauthService;

    @Autowired
    private UserService userService;


    @Autowired
    private ThirdProperties thirdProperties;

    @GetMapping("/appH5Index")
    public String appH5Index(@RequestParam("data") String data) throws Exception {
        String bfsAccessToken;
        try {
            if (StringUtils.isBlank(data)) {
                throw new Exception("身份验证失败！失败原因：未提交身份验证数据！");
            }
            // 解析获取登录名
            String loginName = getLoginNameFromData(decryptData(data));
            // 检查用户状态
            checkLoginName(loginName);
            bfsAccessToken = oauthService.getAccessTokenId("mobile", "mobile", "read write", loginName);
        } catch (Exception e) {
            // 跳转到身份验证失败页面
            String msg = "Error：" + e.getMessage();
            log.error(msg);
            return String.format("redirect:%s%s?msg=%s", thirdProperties.getTrustedHost(), thirdProperties.getAuthFailPage(), encodeUrl(msg));
            // 开发调试用的URL
            // return "redirect:http://192.168.0.193:8081/t2/appH5/#/pages/authenticationFaild/authenticationFaild?msg=" + encodeUrl(msg);
        }
        // 对参数进行加密：
        // 加密公式：base64Encode(时间戳+参数值)
        String timestamp = String.valueOf((new Date()).getTime());
        String encryptedBfsBaseUrl = encryptValue(timestamp, thirdProperties.getTrustedHost());
        String encryptedBfsAccessToken = encryptValue(timestamp, bfsAccessToken);

        String url = String.format("redirect:%s%s?bfsBaseUrl=%s&bfsAt=%s&timestamp=%s",
                thirdProperties.getTrustedHost(), thirdProperties.getIndexPage(), encryptedBfsBaseUrl, encryptedBfsAccessToken, timestamp);
        // 开发调试使用的URL
        //return String.format("redirect:http://192.168.0.193:8081/t2/appH5/#/pages/appH5Index/appH5Index?bfsBaseUrl=%s&bfsAt=%s&timestamp=%s",
        //       encryptedBfsBaseUrl, encryptedBfsAccessToken, timestamp);
        return url;

    }

    /**
     * 使用密匙解密数据
     *
     * @param encryptedData 加密串
     * @return 解密后的数据
     * @throws Exception 解密失败异常
     */
    private String decryptData(String encryptedData) throws Exception {
        String decryptedResult = null;
        try {
            String charsetName = StandardCharsets.UTF_8.name();
            byte[] decryptedBytes = AesEcbUtils.decrypt(thirdProperties.getDecryptKey().getBytes(charsetName), org.apache.commons.codec.binary.Base64.decodeBase64(encryptedData));
            decryptedResult = new String(decryptedBytes, charsetName);
        } catch (Exception e) {
            log.error("APP-H5接入口解密身份数据失败！", e);
            throw new Exception("APP-H5接入口验证身份数据失败！失败原因：数据可能被篡改！");
        }
        return decryptedResult;
    }

    private String getLoginNameFromData(String jsonData) throws Exception {
        String loginName = null;
        try {
            JsonNode jsonNode = JsonUtils.getJsonNode(jsonData);
            if (jsonNode.get("reqTime") == null) {
                log.error("身份验证验证失败！失败原因：缺失请求时间参数！");
                throw new Exception("身份验证验证失败！失败原因：缺失请求时间参数！");
            }
            if (jsonNode.get("loginName") == null) {
                log.error("身份验证验证失败！失败原因：缺失用户登录名参数！");
                throw new Exception("身份验证验证失败！失败原因：缺失用户登录名参数！");
            }
            long reqTime = jsonNode.get("reqTime").asLong();
            if (System.currentTimeMillis() - reqTime > 30 * 60 * 1000) {
                // 若请求时间超过当前时间30分钟则身份验证失败。
                throw new Exception("身份验证参数验证超时！");
            }
            loginName = jsonNode.get("loginName").asText();
            if (StringUtils.isEmpty(loginName)) {
                throw new Exception("缺失用户登录名参数！");
            }
        } catch (Exception e) {
            log.error("解析身份数据失败：" + e.getMessage(), e);
            throw new Exception("解析身份数据失败！");
        }
        return loginName;
    }

    /**
     * 检查用户名是否合法
     *
     * @param loginName 用户名
     *                                 todo
     */
    public void checkLoginName(String loginName) {
        User userDTO = userService.getByLoginName(loginName);
        if (userDTO != null) {
            Integer userStatus = userDTO.getStatus();
            if (UserConstant.STATUS_LOCK.equals(userStatus)
                    || UserConstant.STATUS_DELETE.equals(userStatus)
                    || UserConstant.STATUS_FORBIDDEN.equals(userStatus)) {
                // 帐号被锁定
                throw new RuntimeException("用户账号" + loginName + "已被锁定！");
            }
        } else {
            throw new RuntimeException("用户" + loginName + "未同步到资金管理系统！");
        }
    }


    /**
     * 参数加密
     * 加密公式：base64Encode(时间戳+参数值)
     *
     * @param timestamp 时间戳
     * @param value     加密参数值
     * @return 加密后的参数值
     * @throws Exception 异常
     */
    private String encryptValue(String timestamp, String value) throws Exception {
        Charset charset = StandardCharsets.UTF_8;
        return Base64.byteArrayToBase64(encodeUrl(timestamp + value).getBytes(charset));
    }

    private String encodeUrl(String value) throws Exception {
        return URLEncoder.encode(value, StandardCharsets.UTF_8.displayName());
    }


}
