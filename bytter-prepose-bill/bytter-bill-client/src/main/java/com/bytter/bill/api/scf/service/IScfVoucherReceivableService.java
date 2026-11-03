package com.bytter.bill.api.scf.service;

/**
 * @author ztd
 * @description TODO
 * @since 2026/8/25
 */
public interface IScfVoucherReceivableService {
    /**
     * 应收债权凭证转让提交：根据凭证id集合查询转让信息并组装，调用债权凭证转让接口发起提交（写入转让信息并发起转让审批流）
     *
     * @param voucherIds 应收凭证id集合（逗号分隔）
     */
    void submitTransfer(String voucherIds);
}
