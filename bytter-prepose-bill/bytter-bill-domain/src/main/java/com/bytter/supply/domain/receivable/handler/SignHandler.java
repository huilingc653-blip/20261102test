package com.bytter.supply.domain.receivable.handler;

import com.bytter.supply.domain.receivable.entity.ScfLoanInfo;
import com.bytter.supply.domain.receivable.entity.ScfReceivablePaymentInfo;
import com.bytter.supply.domain.receivable.entity.ScfReceivableVoucherInfo;

/**
 * @author ztd
 * @version 1.0
 * @Title: SignHandler
 * @Description: 签收处理器接口（策略模式）
 * @since 2026/7/3
 */
public interface SignHandler {

    /**
     * 执行批量签收
     *
     * @param voucherInfo 凭证信息
     */
    void processSign(ScfReceivableVoucherInfo voucherInfo);

    /**
     * 保存融资信息
     *
     * @param loanInfo 融资信息
     */
    void processLoan(ScfLoanInfo loanInfo);

    /**
     * 发起转让
     *
     * @param voucherInfo 凭证信息
     */
    void processTransfer(ScfReceivableVoucherInfo voucherInfo);

    /**
     * 执行收款处理，审批通过后自动生成债权凭证到期收款记录
     *
     * @param paymentInfo 收款记录
     */
    void processPayment(ScfReceivablePaymentInfo paymentInfo);

    /**
     * 获取当前处理器类型（对应OnlineEnum的code）
     *
     * @return 0=线下，1=线上
     */
    String getType();
}