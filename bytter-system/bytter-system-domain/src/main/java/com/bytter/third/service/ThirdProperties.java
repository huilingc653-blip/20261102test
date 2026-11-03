package com.bytter.third.service;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
@ConfigurationProperties(prefix = "h5")
@RefreshScope
public class ThirdProperties {

    //信任域名就是三方回调我们服务的域名 有的需要域名认证 线上都需要使用域名
    private String trustedHost;
    //H5回调首页
    private String homePage;

    //也是首页？
    private String indexPage;

    //授权失败跳转页面
    private String authFailPage;

    //待审批页面
    private String todoApprovePage;

    //已完成
    private String completedApprovePage;

    //是否允许审批
    private boolean allowedApprove;

    //只展示报表数据
    private boolean onlyShowReportPage;

    //密钥
    private String decryptKey;

    private CorpWxProperties corpWx;

    private DingDingProperties dd;

    private FeiShuProperties fs;

    @Data
    public static class FeiShuProperties{
        private String agentId;
        private String appId;
        private String appSecret;

        //推送总开关
        private boolean pushSwitch;
        //审批完成是否推送
        private boolean approvalPush;
    }

    @Data
    public static class CorpWxProperties {
        //请求域名
        private String host;
        private String agentId;
        private String appId;
        private String appSecret;

        //推送总开关
        private boolean pushSwitch;
        //审批完成是否推送
        private boolean approvalPush;

    }

    @Data
    public static class DingDingProperties {
        private String agentId;
        private String appId;
        private String appSecret;
        private String corpId;
        //推送总开关
        private boolean pushSwitch;
        //审批完成是否推送
        private boolean approvalPush;

    }
}
