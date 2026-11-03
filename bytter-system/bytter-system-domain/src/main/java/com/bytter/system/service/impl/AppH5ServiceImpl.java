package com.bytter.system.service.impl;

import com.bytter.core.utils.Base64;
import com.bytter.system.api.IAppH5Service;
import com.bytter.system.api.IOauthService;
import com.bytter.system.constants.AppH5Constants;
import com.bytter.third.service.ThirdProperties;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Date;
/**
 * @author chenlw
 * @since 2020-04-20
 */
@Service("appH5ServiceI")
@DubboService
public class AppH5ServiceImpl implements IAppH5Service {
    @Autowired
    private IOauthService oauthService;

    @Autowired
    private ThirdProperties thirdProperties;
    /**
     * 获取资金管理系统的基础URL
     *
     * @return
     */
    @Override
    public String getBfsBaseUrl() {
        return thirdProperties.getTrustedHost();
    }
    /**
     * 获取进入H5首页的URL
     *
     * @param loginName 用户登录名
     * @return 进入H5首页的URL
     * @throws Exception 异常
     */
    @Override
    public String getAppH5IndexUrlByLoginName(String loginName) throws Exception {
        String bfsAccessToken = oauthService.getAccessTokenId("mobile", "mobile", "read write", loginName);
        return getAppH5IndexUrl(bfsAccessToken);
    }
    @Override
    public String getAppH5IndexUrl(String bfsAccessToken) throws Exception {
        // 对参数进行加密：
        // 加密公式：base64Encode(时间戳+参数值)
        String timestamp = String.valueOf((new Date()).getTime());
        String encryptedBfsBaseUrl = encryptValue(timestamp, getBfsBaseUrl());
        String encryptedBfsAccessToken = encryptValue(timestamp, bfsAccessToken);
        // 进入H5首页允许执行审批操作
        String encryptedOp = encryptValue(timestamp, getAllowedApproveFlag());
        String url = String.format("redirect:%s%s?bfsBaseUrl=%s&bfsAt=%s&op=%s&timestamp=%s",
                getBfsBaseUrl(), thirdProperties.getIndexPage(), encryptedBfsBaseUrl, encryptedBfsAccessToken, encryptedOp, timestamp);
        // 开发调试使用的URL
//        return String.format("redirect:http://192.168.1.123:8080/t2/appH5/#/pages/oldVersionPage/appH5Index/appH5Index?bfsBaseUrl=%s&bfsAt=%s&op=%s&timestamp=%s",
//                encryptedBfsBaseUrl, encryptedBfsAccessToken, encryptedOp, timestamp);
        return url;
    }
    /**
     * 进入H5审批详情页面的基础URL
     *
     * @return 进入H5审批详情页面的基础URL
     */
    @Override
    public String getAppH5todoApproveIndexUrl() {
        return thirdProperties.getTodoApprovePage();
    }

    /**
     * 生成进入H5完成审批页面的URL
     *
     * @return 完成审批页面的URL
     */
    @Override
    public String getAppH5CompletedApprovedPageUrl() {
        StringBuilder url = new StringBuilder();
        return url.append(getBfsBaseUrl()).append(thirdProperties.getCompletedApprovePage()).toString();
    }
    /**
     * 获取进入身份验证失败页面的URL
     *
     * @param msg 失败消息
     * @return 身份验证失败页面的URL
     * @throws Exception 异常
     */
    @Override
    public String getAuthenticationFailPageUrl(String msg) throws Exception {
        // return String.format("redirect:%s%s?msg=%s", bfsBaseUrl, appH5AuthenticationFailUrl, encodeUrl(msg));
        return String.format("%s%s?msg=%s", getBfsBaseUrl(), thirdProperties.getAuthFailPage(), encodeUrl(msg));
    }
    /**
     * 获取 是否开启H5审批页面操作标识
     * - 返回"1"，标识允许H5审批详情页面执行审批操作；
     * - 返回"0"，标识不允许H5审批详情页面执行审批操作；
     *
     * @return H5审批页面操作标识。
     */
    @Override
    public String getIsAllowedApproveFlag() {
        return thirdProperties.isAllowedApprove() ? getAllowedApproveFlag() : getNotAllowedApproveFlag();
    }
    @Override
    public String getAllowedApproveFlag() {
        return AppH5Constants.AllowedApproveFlag.ALLOWED;
    }
    @Override
    public String getNotAllowedApproveFlag() {
        return AppH5Constants.AllowedApproveFlag.NOT_ALLOWED;
    }
    @Override
    public boolean isOnlyShowReportPage() {
        return thirdProperties.isOnlyShowReportPage();
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
