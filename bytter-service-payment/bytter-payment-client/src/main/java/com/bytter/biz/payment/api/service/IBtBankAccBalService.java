package com.bytter.biz.payment.api.service;

import com.bytter.biz.payment.api.dto.BtBankAccBalDTO;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
/**
 * 银行账号余额更新接口类
 *
 * @author huangx
 * @since 2022-06-08 09:49:44
 */
public interface IBtBankAccBalService {
    /**
     * 初始化账户实时余额表
     */
    void initBankAccBal();
    
    public void batchSaveByDTO(List<BtBankAccBalDTO> balList);

    public Map<String, BigDecimal> getBalByAccAndCur(String bankAcc, String cur);
}
