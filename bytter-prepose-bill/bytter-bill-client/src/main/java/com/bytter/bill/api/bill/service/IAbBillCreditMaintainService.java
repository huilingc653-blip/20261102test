package com.bytter.bill.api.bill.service;


import com.bytter.bill.api.bill.dto.AbBillCreditInfoMaintainDTO;

/**
 * 银行开票授信资料维护对外接口
 *
 * @author fangcf
 * @date 2025-07-02
 */
public interface IAbBillCreditMaintainService {

    /**
     * 根据单位，银行类型，授信类型，获取唯一的一条银行开票授信资料
     *
     * @param corpId     单位id
     * @param bankTypeId 银行类型
     * @param creditType 授信类型
     */
    AbBillCreditInfoMaintainDTO getOneBy(String corpId, String bankTypeId, String creditType);
}
