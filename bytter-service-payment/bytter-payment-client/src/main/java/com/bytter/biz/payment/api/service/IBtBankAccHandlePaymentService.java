package com.bytter.biz.payment.api.service;

import com.bytter.biz.payment.api.dto.BtBankAccDTO;
import com.bytter.core.base.entity.ICurrentUser;

/**
 * 银行账号接口类
 *
 * @author huangx
 * @since 2022-06-08 09:49:44
 */
public interface IBtBankAccHandlePaymentService {
    /**
     * 银行账号新增，系统自动新增保证金账户
     *
     * @param btBankAccDTO
     * @throws Exception
     */
    public BtBankAccDTO saveBtBankAcc(BtBankAccDTO btBankAccDTO, ICurrentUser user);
}
