package com.bytter.increment.api.service;
import com.bytter.increment.api.dto.ChoBankFinanceDTO;
/**
 * 银行理财交易接口类
 * @author huangx
 * @since 2020-6-4
 */
public interface ICBFService {
    
    ChoBankFinanceDTO getBankFinanceByVoucherNo(String voucherNo);
    
    int updateBankFinanceByVoucherNo(ChoBankFinanceDTO choBankFinanceDTO);
    
    public ChoBankFinanceDTO getChoBankFinanceById(String cbsBillId);
}
