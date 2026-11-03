package com.bytter.erp.service;
import com.bytter.erp.dto.BtInputBankInfoDTO;
public interface IBtInputBankInfoService {
    /**
     * 根据联行号，开户行得到联行号信息
     *
     * @param bankCode
     * @param bankName
     * @return
     */
    public BtInputBankInfoDTO getByBankCode(String bankCode, String bankName);
}
