package com.bytter.biz.fundwarn.spi.service;

import com.bytter.core.base.entity.ICurrentUser;

/**
* @Description 异常警告调用接口
* @author zhous
* @date 2024/8/22 14:55
*/
public interface IFundWarnExceptionDataService {

    /**
     * 通过群聊机器人发送异常警告信息
     * @param warnContent 警告内容
     * @param sendChannel 发送渠道 all-全部 0-企业微信 1-钉钉
     * @param warnType 异常警告类型 0-应用接口 1-前置机服
     */
    public void sendWarnExceptionDataByGroupChatBot(String warnContent,String sendChannel,String warnType);
}
