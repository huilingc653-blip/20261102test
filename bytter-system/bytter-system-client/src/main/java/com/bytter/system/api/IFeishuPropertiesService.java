package com.bytter.system.api;
/**
 * 飞书-配置接口
 *
 * @author liyh  2022-11-03
 * @since 3.4
 */
public interface IFeishuPropertiesService {
    /**
     * 获取飞书应用的appSecret
     *
     * @return 飞书应用的appSecret
     */
    String getAppSecret();
    /**
     * 获取飞书应用的AgentId
     *
     * @return 飞书应用的AgentId
     */
    String getAgentId();
    /**
     * 是否推送消息到飞书
     *
     * @return 是否推送消息到飞书
     */
    boolean getPushMessageSwitch();
    /**
     * 是否推送审批办结消息给审批发起人
     *
     * @return 是否推送审批办结消息给审批发起人
     */
    boolean isPushApprovalCompletedMsgToApplicant();
}
