package com.bytter.system.api;

import com.bytter.system.api.dto.InputBankInfoDTO;

public interface IBtInputBankInfoService {

    /**
     * 根据联行号，开户行得到联行号信息
     * @param bankCode
     * @param bankName
     * @return
     */
    public InputBankInfoDTO getByBankCodeOrBankName(String bankCode, String bankName);
}
