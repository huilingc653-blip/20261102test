package com.bytter.check.service.impl;
import com.bytter.check.service.PaymentBlendingService;
import com.bytter.system.api.IPaymentBlendingService;
import com.bytter.system.api.constant.NotificationConstant;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
/**
 * qijh付款明细勾对接口实现类
 */
@Service("paymentBlendingServiceImpl")
@DubboService
@Transactional
public class PaymentBlendingServiceImpl implements IPaymentBlendingService {
    @Autowired
    private PaymentBlendingService paymentBlendingService;
    @Override
    public void sendNotification() {
        //付款明细勾兑提醒
        paymentBlendingService.run(NotificationConstant.NETBANK.BANKACC_PAYMENTBLENDING);
    }
}
