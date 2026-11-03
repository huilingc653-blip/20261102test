package com.bytter.biz.payment.api.service;

import com.bytter.biz.payment.api.dto.FcBankAccGroupDTO;

import java.util.List;

public interface IFcBankAccGroupService {

    /**
     * 获取有效的系统结算方式
     *
     * @return
     */
    List<FcBankAccGroupDTO> getValidPayType();
}
