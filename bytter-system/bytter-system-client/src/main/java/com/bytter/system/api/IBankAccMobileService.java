package com.bytter.system.api;

import com.bytter.system.api.dto.BtBankAccMobileDTO;

import java.util.List;

/**
 * @author luoyj
 * @date 2024/4/18.
 */
public interface IBankAccMobileService {

    /**
     * 根据单位和时间查询余额
     * @param corpId
     * @param userId
     * @param beginDate
     * @param endDate
     * @param bankAccId
     * @return
     */
    String getBal(String corpId,String userId, String beginDate,String endDate,String bankAccId,String curId);
    /**
     * 查询明细
     * @param corpId
     * @param userId
     * @param beginDate
     * @param endDate
     * @param beginMoney
     * @param endMoney
     * @param oppBankAcc
     * @return
     */
    String getDtl(String corpId,String userId, String beginDate,String endDate,String bankAcc,String beginMoney,String endMoney,String oppBankAcc,String curId);
    /**
     * 获取有权限的银行账号
     * @param userId
     * @param corpId
     * @param grantType
     * @return
     */
    List<BtBankAccMobileDTO> getGrantBankAcc(String userId, String corpId, String grantType);

}
