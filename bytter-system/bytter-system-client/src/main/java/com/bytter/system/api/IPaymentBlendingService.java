package com.bytter.system.api;
/**
 * 付款明细勾对接口类
 *
 * @author huangx
 * @since 2019-03-18 09:49:44
 */
public interface IPaymentBlendingService {
    /**
     * 付款明细勾对发送消息
     */
    void sendNotification();
}
