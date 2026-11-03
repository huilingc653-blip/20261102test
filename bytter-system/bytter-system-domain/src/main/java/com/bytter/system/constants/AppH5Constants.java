package com.bytter.system.constants;
/**
 * APP-H5，相关常量
 *
 * @author chenlw 2020-08-28
 * @since V3.4
 */
public interface AppH5Constants {
    /**
     * 加密密钥。长度16
     */
    String AES_KEY = "Bytter2020062888";
    /**
     * 客户端类型，企业微信:0；钉钉：1；
     */
    interface ClientType {
        String CORP_WEI_XIN = "0";
        String DING_TALK = "1";
        String FEISHU = "2";
    }
    /**
     * 进入H5页面的类型
     */
    interface ToPageType {
        /**
         * 首页
         */
        String INDEX = "index";
        /**
         * 待审批详情页面
         */
        String TODO_APPROVE_DETAIL_PAGE = "todoApproveDetailPage";
    }
    /**
     * APP-H5端 - 登录标识
     * needLogin = 1，表示H5页面要进入登录页面 0--无需登录，直接进入审批首页
     */
    interface ClientLoginFlag {
        String NEED_LOGIN = "1";
        String NO_NEED_LOGIN = "0";
    }
    /**
     * 第三方平台地址
     */
    interface AccessURL {
        String DINGTALK_URL = "https://oapi.dingtalk.com";
        String WEIXIN_URL = "https://qyapi.weixin.qq.com";
        String WEIXIN_OPEN_URL = "https://open.weixin.qq.com";
        String FEISHU_OPEN_URL = "https://open.feishu.cn";
    }
    /**
     * 检验登录状态
     */
    interface LoginStatus {
        String LOGIN_SUCCESS = "1";
        String LOGIN_FAIL = "0";
    }
    /**
     * 获取 是否开启H5审批页面操作标识
     * - 返回"1"，标识允许H5审批详情页面执行审批操作；
     * - 返回"0"，标识不允许H5审批详情页面执行审批操作；
     */
    interface AllowedApproveFlag {
        String ALLOWED = "1";
        String NOT_ALLOWED = "0";
    }
}
