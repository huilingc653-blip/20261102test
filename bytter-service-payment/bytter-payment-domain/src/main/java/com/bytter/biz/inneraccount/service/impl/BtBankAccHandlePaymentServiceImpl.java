package com.bytter.biz.inneraccount.service.impl;

import com.bytter.biz.inneraccount.service.BtBankAccHandleService;
import com.bytter.biz.payment.api.dto.BtBankAccDTO;
import com.bytter.biz.payment.api.service.IBtBankAccHandlePaymentService;
import com.bytter.core.base.entity.ICurrentUser;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author huangx
 * @version 1.0
 * @Title: Service
 * @Description: BT_BANK_ACC
 * @date 2022-05-25 17:50:16
 */
@Service
@Transactional
@DubboService
public class BtBankAccHandlePaymentServiceImpl implements IBtBankAccHandlePaymentService {
    @Autowired
    private BtBankAccHandleService btBankAccHandleService;

    @Override
    public BtBankAccDTO saveBtBankAcc(BtBankAccDTO btBankAccDTO, ICurrentUser user) {
        return btBankAccHandleService.saveBtBankAcc(btBankAccDTO, user);
    }
}
