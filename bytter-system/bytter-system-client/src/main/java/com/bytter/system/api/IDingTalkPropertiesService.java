package com.bytter.system.api;
/**
 * 钉钉-配置接口
 *
 * @author chenlw  2020-12-02
 * @since 3.4
 */
public interface IDingTalkPropertiesService {
    /**
     * 获取钉钉应用的appKey
     *
     * @return 钉钉应用的appKey
     */
    String getAppKey();
    /**
     * 获取钉钉应用的appSecret
     *
     * @return 钉钉应用的appSecret
     */
    String getAppSecret();
    /**
     * 获取钉钉应用的AgentId
     *
     * @return 钉钉应用的AgentId
     */
    String getAgentId();
    /**
     * 获取钉钉应用的CorpId
     *
     * @return 钉钉应用的CorpId
     */
    String getCorpId();
    /**
     * 是否推送消息到钉钉
     *
     * @return 是否推送消息到钉钉
     */
    boolean getPushMessageSwitch();
    /**
     * 是否推送审批办结消息给审批发起人
     *
     * @return 是否推送审批办结消息给审批发起人
     */
    boolean isPushApprovalCompletedMsgToApplicant();
}
