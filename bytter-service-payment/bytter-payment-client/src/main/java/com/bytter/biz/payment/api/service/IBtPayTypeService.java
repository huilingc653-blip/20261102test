package com.bytter.biz.payment.api.service;

import com.bytter.biz.payment.api.dto.BtPayTypeDTO;

/**
 * 系统结算方式服务。
 *
 * @author zhaohc
 * @since 2026/4/10 10:46
 */
public interface IBtPayTypeService {

    /**
     * 根据名称获取系统结算方式。
     *
     * @param voucherName 结算方式名称。
     * @return 结算方式。
     */
    BtPayTypeDTO getPayTypeByName(String voucherName);
}
