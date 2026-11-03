package com.bytter.system.mobile.controller;

import cn.hutool.crypto.digest.DigestUtil;
import com.bytter.core.base.BaseController;
import com.bytter.core.common.exception.H5ConfigException;
import com.bytter.core.utils.EmptyUtils;
import com.bytter.core.utils.EncryptionUtils;
import com.bytter.core.utils.JsonUtils;
import com.bytter.core.utils.StringUtils;
import com.bytter.system.api.*;
import com.bytter.system.api.constant.ConfigConstant;
import com.bytter.system.api.dto.AccountMappingDTO;
import com.bytter.system.constants.AppH5Constants;
import com.bytter.system.entity.User;
import com.bytter.system.service.H5DingTalkService;
import com.bytter.system.service.H5FeishuService;
import com.bytter.system.service.UserService;
import com.bytter.third.service.CorpWxClient;
import com.bytter.third.service.ThirdProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @author luxy
 * @Title: H5LoginController
 * @ProjectName T3.4
 * @Description: H5登录控制层
 * @date 2020/8/28 16:39
 */
@Controller
@RequestMapping("/mobile/h5/login")
public class H5LoginController extends BaseController {

    @Autowired
    private H5DingTalkService dingService;

    @Autowired
    private IAccountMappingService accountMapService;

    @Autowired
    private UserService userService;

    @Autowired
    private IOauthService oauthService;

    @Autowired
    private CorpWxClient weiXinService;

    @Resource
    private IAppH5Service appH5Service;

    @Autowired
    private IDingTalkPropertiesService dingTalkPropertiesService;

    @Autowired
    private IFeishuPropertiesService feishuPropertiesService;

    @Autowired
    private H5FeishuService feishuService;

    @Autowired
    private IConfigService configServiceI;

    @Autowired
    private ThirdProperties thirdProperties;

    /**
     * 钉钉内部应用入口
     *
     * @return 重定向地址
     * @throws Exception 异常
     */
    @RequestMapping("/dingtalk")
    public String dingtalk() throws Exception {
        try {
            Map<String, Object> dataMap = new HashMap<>();
            dataMap.put("corpId", dingTalkPropertiesService.getCorpId());
            dataMap.put("bfsBaseUrl", thirdProperties.getTrustedHost());
            // 客户端类型，企业微信:0;钉钉:1;
            dataMap.put("clientType", AppH5Constants.ClientType.DING_TALK);
            // 要加密的数据
            String json = JsonUtils.toJSONString(dataMap);
            // 加密干扰串
            String ivString = EncryptionUtils.getRandomString(16);
            // 得到加密后的数据
            String encryptedData = EncryptionUtils.aesCbcEncrypt(json, AppH5Constants.AES_KEY, ivString, StandardCharsets.UTF_8.name());
            StringBuilder redirectUrl = new StringBuilder();
            redirectUrl.append(thirdProperties.getTrustedHost()).append(thirdProperties.getHomePage());
            redirectUrl.append("data=").append(EncryptionUtils.encodeUrl(encryptedData));
            redirectUrl.append("&is=").append(EncryptionUtils.encodeUrl(ivString));
            return String.format("redirect:%s", redirectUrl.toString());
        } catch (Exception e) {
            return h5ConfigErrorPage();
        }
    }

    /**
     * H5配置错误提示页面
     *
     * @return H5配置错误提示页面
     */
    private String h5ConfigErrorPage() {
        return "forward:/h5ConfigError";
    }

    /**
     * 飞书内部应用入口
     *
     * @return 重定向地址
     */
    @RequestMapping("/feishu")
    public String feishu() {
        try {
            Map<String, Object> dataMap = new HashMap<>();
            dataMap.put("bfsBaseUrl", thirdProperties.getTrustedHost());
            // 客户端类型，企业微信:0;钉钉:1;
            dataMap.put("clientType", AppH5Constants.ClientType.FEISHU);
            dataMap.put("appId", feishuPropertiesService.getAgentId());//应用ID
            // 要加密的数据
            String json = JsonUtils.toJSONString(dataMap);
            // 加密干扰串
            String ivString = EncryptionUtils.getRandomString(16);
            // 得到加密后的数据
            String encryptedData = EncryptionUtils.aesCbcEncrypt(json, AppH5Constants.AES_KEY, ivString, StandardCharsets.UTF_8.name());
            StringBuilder redirectUrl = new StringBuilder();
            redirectUrl.append(thirdProperties.getTrustedHost()).append(thirdProperties.getHomePage());
            redirectUrl.append("data=").append(EncryptionUtils.encodeUrl(encryptedData));
            redirectUrl.append("&is=").append(EncryptionUtils.encodeUrl(ivString));
            return String.format("redirect:%s", redirectUrl);

        } catch (Exception e) {
            return h5ConfigErrorPage();
        }
    }

    /**
     * 获取飞书应用授权凭证的信息
     *
     * @return 结果map
     */
    @GetMapping("/appAccessToken")
    @ResponseBody
    public Map<String, Object> getAppAccessToken() {
        Map<String, Object> dataMap = new HashMap<>();
        String accessToken = "";
        try {
            accessToken = feishuService.getAppAccessToken();
        } catch (Exception e) {
            dataMap.put("msg", "获取accessToken失败！");
        }
        dataMap.put("accessToken", accessToken);
        dataMap.put("appId", feishuPropertiesService.getAgentId());//应用ID
        return dataMap;
    }

    /**
     * 获取飞书用户的信息
     *
     * @param code 授权码
     * @return 结果map
     * @throws Exception 异常
     */
    @GetMapping("/feishuInfo")
    @ResponseBody
    public Map<String, Object> feishuInfo(String code) throws Exception {
        // 是否需要进入登录页面
        String needLogin = "";
        // 需要加密的数据
        Map<String, Object> dataMap = new HashMap<>();
        String accessToken = "";
        try {
            accessToken = feishuService.getAccessToken(code);
        } catch (Exception e) {
            e.printStackTrace();
            dataMap.put("msg", "获取accessToken失败！");
        }
        if (!StringUtils.isBlank(accessToken)) {
            String userInfo = feishuService.getUserInfo(code, accessToken);
            Map<String, Object> userJson = JsonUtils.json2Map(userInfo);
            String errcode = userJson.get("msg").toString();
            if (errcode.equals("success")) {
                // 请求用户信息成功
                String userId = "";
                if (null != userJson.get("data")) {
                    Map<String, Object> userMap = (LinkedHashMap) userJson.get("data");
                    if (null != userMap.get("user_id")) {
                        userId = userMap.get("user_id").toString();
                    }
                }
                if (EmptyUtils.isEmpty(userId)) {
                    needLogin = AppH5Constants.ClientLoginFlag.NEED_LOGIN;
                    dataMap.put("msg", "账号不存在，请连续飞书管理员开通权限");
                } else {
                    // 飞书用户id
                    dataMap.put("relationUserId", userId);
                    // 校验是否需要执行登录操作
                    String sysUserId = checkIsNeedLogin(userId, AppH5Constants.ClientType.FEISHU);
                    if (StringUtils.isNotEmpty(sysUserId)) {
                        dataMap.put("bfsBaseUrl", thirdProperties.getTrustedHost());
                        needLogin = AppH5Constants.ClientLoginFlag.NO_NEED_LOGIN;
                        User user = userService.get(sysUserId);
                        if (null != user) {
                            String bfsAccessToken = oauthService.getAccessTokenId("mobile", "mobile", "read write", user.getLoginName());
                            dataMap.put("bfsAt", bfsAccessToken);
                        }
                    } else {
                        needLogin = AppH5Constants.ClientLoginFlag.NEED_LOGIN;
                        dataMap.put("msg", "账号未绑定，请登录");
                    }
                }
            } else {
                // 用户信息请求失败
                String errmsg = JsonUtils.json2Map(userInfo).get("msg").toString();
                dataMap.put("msg", errmsg);
            }
        }
        dataMap.put("needLogin", needLogin);
        // 是否只展示报表页面，值为true为是；false则正常展示菜单。（默认false）
        dataMap.put("onlyShowReportPage", appH5Service.isOnlyShowReportPage());
        // 要加密的数据
        String json = JsonUtils.toJSONString(dataMap);
        // 加密干扰串
        String ivString = EncryptionUtils.getRandomString(16);
        // 得到加密后的数据
        String encryptedData = EncryptionUtils.aesCbcEncrypt(json, AppH5Constants.AES_KEY, ivString, StandardCharsets.UTF_8.name());
        // 传到前端的数据
        Map<String, Object> map = new HashMap<>();
        map.put("data", EncryptionUtils.encodeUrl(encryptedData));
        map.put("is", EncryptionUtils.encodeUrl(ivString));
        return map;
    }

    /**
     * 获取钉钉用户的信息
     *
     * @param code 授权码
     * @return 结果map
     * @throws Exception 异常
     */
    @GetMapping("/dingTalkInfo")
    @ResponseBody
    public Map<String, Object> dingTalkInfo(String code) throws Exception {
        // 是否需要进入登录页面
        String needLogin = "";
        // 需要加密的数据
        Map<String, Object> dataMap = new HashMap<>();
        String accessToken = "";
        try {
            accessToken = dingService.getAccessToken();
        } catch (Exception e) {
            dataMap.put("msg", "获取accessToken失败！");
        }
        if (!StringUtils.isBlank(accessToken)) {
            String userInfo = dingService.getUserInfo(code, accessToken);
            String errcode = JsonUtils.json2Map(userInfo).get("errcode").toString();
            if (errcode.equals("0")) {
                // 请求用户信息成功
                Map<String, Object> userMap = JsonUtils.json2Map(userInfo);
                String userId = userMap.get("userid").toString();
                // 钉钉用户id
                dataMap.put("relationUserId", userId);
                // 校验是否需要执行登录操作
                String sysUserId = checkIsNeedLogin(userId, AppH5Constants.ClientType.DING_TALK);
                if (StringUtils.isNotEmpty(sysUserId)) {
                    dataMap.put("bfsBaseUrl", thirdProperties.getTrustedHost());
                    needLogin = AppH5Constants.ClientLoginFlag.NO_NEED_LOGIN;
                    User user = userService.get(sysUserId);
                    if (null != user) {
                        String bfsAccessToken = oauthService.getAccessTokenId("mobile", "mobile", "read write", user.getLoginName());
                        dataMap.put("bfsAt", bfsAccessToken);
                    }
                } else {
                    needLogin = AppH5Constants.ClientLoginFlag.NEED_LOGIN;
                    dataMap.put("msg", "账号未绑定，请登录");
                }
            } else {
                // 用户信息请求失败
                String errmsg = JsonUtils.json2Map(userInfo).get("errmsg").toString();
                dataMap.put("msg", errmsg);
            }
        }
        dataMap.put("needLogin", needLogin);
        // 是否只展示报表页面，值为true为是；false则正常展示菜单。（默认false）
        dataMap.put("onlyShowReportPage", appH5Service.isOnlyShowReportPage());
        // 要加密的数据
        String json = JsonUtils.toJSONString(dataMap);
        // 加密干扰串
        String ivString = EncryptionUtils.getRandomString(16);
        // 得到加密后的数据
        String encryptedData = EncryptionUtils.aesCbcEncrypt(json, AppH5Constants.AES_KEY, ivString, StandardCharsets.UTF_8.name());
        // 传到前端的数据
        Map<String, Object> map = new HashMap<>();
        map.put("data", EncryptionUtils.encodeUrl(encryptedData));
        map.put("is", EncryptionUtils.encodeUrl(ivString));
        return map;
    }

    /**
     * 企业微信 - 待审批 H5详情页面入口
     *
     * @param code   授权码
     * @param taskId 审批任务ID
     * @return 重定向地址
     */
    @RequestMapping("/weixin/h5TodoApproveDetailPage")
    public String h5TodoApproveDetailPageEntry(String code, String taskId) throws Exception {
        String weixinUserId = null;
        Map<String, Object> dataMap = new HashMap<>(10);
        try {
            weixinUserId = weiXinService.getUserId(code);
            // 获取企业微信UserID绑定的资金系统账号
            String sysUserId = checkIsNeedLogin(weixinUserId, AppH5Constants.ClientType.CORP_WEI_XIN);
            if (StringUtils.isNotEmpty(sysUserId)) {
                User user = userService.get(sysUserId);
                if (null != user) {
                    String bfsAccessToken = oauthService.getAccessTokenId("mobile", "mobile", "read write", user.getLoginName());
                    dataMap.put("bfsAt", bfsAccessToken);
                } else {
                    return String.format("redirect:%s", appH5Service.getAuthenticationFailPageUrl("当前用户未绑定资金系统账号！请重新登录！"));
                }
            } else {
                return String.format("redirect:%s", appH5Service.getAuthenticationFailPageUrl("当前用户未绑定资金系统账号！请重新登录！"));
            }
        } catch (H5ConfigException ex1) {
            return h5ConfigErrorPage();
        } catch (Exception e) {
            return String.format("redirect:%s", appH5Service.getAuthenticationFailPageUrl(e.getMessage()));
        }
        dataMap.put("bfsBaseUrl", thirdProperties.getTrustedHost());
        // 客户端类型，企业微信:0;钉钉:1;
        dataMap.put("clientType", AppH5Constants.ClientType.CORP_WEI_XIN);
        // 进入H5页面标识，值为空空则进入首页
        dataMap.put("toPageType", AppH5Constants.ToPageType.TODO_APPROVE_DETAIL_PAGE);
        // 业务参数Map
        Map<String, Object> businessParams = new HashMap<>(5);
        businessParams.put("taskId", taskId);
        dataMap.put("businessParams", businessParams);
        // 要加密的数据
        String json = JsonUtils.toJSONString(dataMap);
        // 加密干扰串
        String ivString = EncryptionUtils.getRandomString(16);
        // 得到加密后的数据
        String encryptedData = EncryptionUtils.aesCbcEncrypt(json, AppH5Constants.AES_KEY, ivString, StandardCharsets.UTF_8.name());
        StringBuilder redirectUrl = new StringBuilder();
        redirectUrl.append(thirdProperties.getTrustedHost()).append("/appH5/#/pages/launchPage/launchPage?");
        redirectUrl.append("data=").append(EncryptionUtils.encodeUrl(encryptedData));
        redirectUrl.append("&is=").append(EncryptionUtils.encodeUrl(ivString));
        return String.format("redirect:%s", redirectUrl.toString());
    }

    /**
     * 企业微信内部应用入口(获取授权码)
     *
     * @return 微信重定向地址
     */
    @RequestMapping("/weixin")
    public String weixin() {
        StringBuilder redirectUrl = new StringBuilder();
        redirectUrl.append(AppH5Constants.AccessURL.WEIXIN_OPEN_URL).append("/connect/oauth2/authorize?appid=" + thirdProperties.getCorpWx().getAppId() + "&")
                .append("redirect_uri=").append(thirdProperties.getTrustedHost() + "/mobile/h5/login/weixinInfo")
                .append("&response_type=code&scope=snsapi_base")
                .append("#wechat_redirect");
        return String.format("redirect:%s", redirectUrl);
    }

    /**
     * 获取微信用户的信息
     *
     * @param request 请求对象
     * @return 重定向地址
     * @throws Exception 异常
     */
    @RequestMapping("/weixinInfo")
    public String weixinInfo(HttpServletRequest request) throws Exception {
        try {
            // 是否需要进入登录页面
            String needLogin = "";
            String code = request.getParameter("code");
            Map<String, Object> dataMap = new HashMap<>();
            if (StringUtils.isNotEmpty(code)) {
                String userInfo = weiXinService.getUserInfo(code);
                if (!StringUtils.isBlank(userInfo)) {
                    Map<String, Object> userMap = JsonUtils.json2Map(userInfo);
                    String errcode = userMap.get("errcode").toString();
                    if (errcode.equals("0")) {
                        String userId = userMap.get("UserId").toString();
                        // 微信用户id
                        dataMap.put("relationUserId", userId);
                        // 校验是否需要执行登录操作
                        String sysUerId = checkIsNeedLogin(userId, AppH5Constants.ClientType.CORP_WEI_XIN);
                        if (StringUtils.isNotEmpty(sysUerId)) {
                            needLogin = AppH5Constants.ClientLoginFlag.NO_NEED_LOGIN;
                            User user = userService.get(sysUerId);
                            if (null != user) {
                                String bfsAccessToken = oauthService.getAccessTokenId("mobile", "mobile", "read write", user.getLoginName());
                                dataMap.put("bfsAt", bfsAccessToken);
                            }
                        } else {
                            needLogin = AppH5Constants.ClientLoginFlag.NEED_LOGIN;
                            dataMap.put("msg", "账号未绑定，请登录");
                        }
                    }
                }
            }
            dataMap.put("bfsBaseUrl", thirdProperties.getTrustedHost());
            dataMap.put("needLogin", needLogin);
            // 是否只展示报表页面，值为true为是；false则正常展示菜单。（默认false）
            dataMap.put("onlyShowReportPage", appH5Service.isOnlyShowReportPage());
            // 客户端类型，企业微信:0;钉钉:1;
            dataMap.put("clientType", AppH5Constants.ClientType.CORP_WEI_XIN);
            // 要加密的数据
            String json = JsonUtils.toJSONString(dataMap);
            // 加密干扰串
            String ivString = EncryptionUtils.getRandomString(16);
            // 得到加密后的数据
            String encryptedData = EncryptionUtils.aesCbcEncrypt(json, AppH5Constants.AES_KEY, ivString, StandardCharsets.UTF_8.name());
            StringBuilder redirectUrl = new StringBuilder();
            redirectUrl.append(thirdProperties.getTrustedHost()).append(thirdProperties.getHomePage());
            redirectUrl.append("data=").append(EncryptionUtils.encodeUrl(encryptedData));
            redirectUrl.append("&is=").append(EncryptionUtils.encodeUrl(ivString));
            return String.format("redirect:%s", redirectUrl);
        } catch (Exception e) {
            return h5ConfigErrorPage();
        }
    }


    /**
     * 检查用户登录信息
     *
     * @param account        资金系统账号
     * @param password       密码
     * @param relationUserId 钉钉/企业微信账号ID
     * @param clientType     客户端类型：钉钉/企业微信
     * @return map
     * @throws Exception 异常
     */
    @GetMapping("/checkLogin")
    @ResponseBody
    public Map<String, Object> checkLogin(String account, String password, String relationUserId, String clientType) throws Exception {
        // 传到前端的数据
        Map<String, Object> dataMap = new HashMap<>();
        User user = userService.getByLoginName(account);
        String loginStatus;
        Map<String, Object> entryMap = new HashMap<>();
        if (null == user) {
            entryMap.put("msg", "账号或密码错误");
        } else {
            String usePwd = DigestUtil.md5Hex(password + user.getSalt());
            if (!usePwd.equals(user.getPassword())) {
                entryMap.put("msg", "账号或密码错误");
            } else {
                if (checkMapIsExist(user.getId(), clientType)) {//账号已存在映射关系
                    entryMap.put("msg", "账号已绑定其他用户");
                } else {
                    entryMap.put("bfsBaseUrl", thirdProperties.getTrustedHost());
                    loginStatus = AppH5Constants.LoginStatus.LOGIN_SUCCESS;
                    entryMap.put("loginStatus", loginStatus);
                    // 是否只展示报表页面，值为true为是；false则正常展示菜单。（默认false）
                    entryMap.put("onlyShowReportPage", appH5Service.isOnlyShowReportPage());
                    AccountMappingDTO mappingDTO = new AccountMappingDTO();
                    mappingDTO.setSysUserId(user.getId());
                    mappingDTO.setRelationUserId(relationUserId);
                    mappingDTO.setClientType(clientType);
                    accountMapService.saveMapRelation(mappingDTO);
                    String bfsAccessToken = oauthService.getAccessTokenId("mobile", "mobile", "read write", account);
                    entryMap.put("bfsAt", bfsAccessToken);
                }
            }
        }
        // 要加密的数据
        String json = JsonUtils.toJSONString(entryMap);
        // 加密干扰串
        String ivString = EncryptionUtils.getRandomString(16);
        // 得到加密后的数据
        String encryptedData = EncryptionUtils.aesCbcEncrypt(json, AppH5Constants.AES_KEY, ivString, StandardCharsets.UTF_8.name());
        dataMap.put("data", EncryptionUtils.encodeUrl(encryptedData));
        dataMap.put("is", EncryptionUtils.encodeUrl(ivString));
        return dataMap;
    }

    /**
     * 校验是否需要登录操作
     *
     * @param clientType
     * @return
     */
    public String checkIsNeedLogin(String relationUserId, String clientType) {
        String userId = "";
        AccountMappingDTO accountMappingDTO = accountMapService.queryAccountMapping(relationUserId, null, clientType);
        if (null != accountMappingDTO) {
            userId = accountMappingDTO.getSysUserId();
        }
        return userId;
    }

    /**
     * 校验是否存在账号映射关系
     *
     * @param clientType
     * @return
     */
    public Boolean checkMapIsExist(String sysUserId, String clientType) {
        AccountMappingDTO accountMappingDTO = accountMapService.queryAccountMapping(null, sysUserId, clientType);
        if (null != accountMappingDTO) {
            return true;
        }
        return false;
    }

    /**
     * 解除账号绑定
     *
     * @param relationUserId 钉钉/企业微信用户账号ID
     * @param clientType     客户端类型：钉钉/企业微信
     */
    @GetMapping("/unbindAccount")
    @ResponseBody
    public Object unbindAccount(String relationUserId, String clientType) {
        accountMapService.unbindAccount(relationUserId, clientType);
        return success();
    }

    /**
     * 是否人脸登录
     */
    @GetMapping("/isFaceLogin")
    @ResponseBody
    public Map<String, Object> isFaceLogin() {
        Map<String, Object> map = new HashMap<>();
        map.put("isFaceLogin", configServiceI.getGlobalConfig(ConfigConstant.SYS, "faceLogin").getValue());
        return map;
    }
}
