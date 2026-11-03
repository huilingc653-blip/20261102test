package com.bytter.system.api;
/**
 * @author chenlw
 * @date 2020/04/20
 */
public interface IAppH5Service {
    /**
     * 获取资金管理系统的基础URL
     *
     * @return 资金管理系统的基础URL
     */
    String getBfsBaseUrl();
    /**
     * 获取进入H5首页的URL
     *
     * @param loginName 用户登录名
     * @return 进入H5首页的URL
     * @throws Exception 异常
     */
    String getAppH5IndexUrlByLoginName(String loginName) throws Exception;
    String getAppH5IndexUrl(String bfsAccessToken) throws Exception;
    /**
     * 进入H5审批详情页面的基础URL
     *
     * @return 进入H5审批详情页面的基础URL
     */
    String getAppH5todoApproveIndexUrl();
    /**
     * 生成进入H5完成审批页面的URL
     *
     * @return 完成审批页面的URL
     */
    String getAppH5CompletedApprovedPageUrl();
    /**
     * 获取进入身份验证失败页面的URL
     *
     * @param msg 失败消息
     * @return 身份验证失败页面的URL
     * @throws Exception 异常
     */
    String getAuthenticationFailPageUrl(String msg) throws Exception;
    String getIsAllowedApproveFlag();
    String getAllowedApproveFlag();
    String getNotAllowedApproveFlag();
    /**
     * 是否只展示报表页面，值为true为是；false则正常展示菜单。（默认false）
     *
     * @return 是否只展示报表页面
     */
    boolean isOnlyShowReportPage();
}
