package com.bytter.system.api;
import java.math.BigDecimal;
import java.util.Date;
/**
 * 融资内部账号相关接口
 *
 * @author xiay
 */
public interface ICmsFcAccService {
    /**
     * 更新贷款账号展期信息
     * @param loanAcc
     * @param deferredDate
     * @param deferredRate
     */
    public void updateLoanAccDeferredDate(String loanAcc, Date deferredDate, BigDecimal deferredRate);
}
