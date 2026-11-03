package com.bytter.biz.payment.api.service;
import com.bytter.biz.payment.api.dto.BisAccDtlDTO;
/**
 * add by qijh 收款流水认领外汇接口
 */
public interface IFexForeignReqService {
    /**
     * 收款流水认领已确定时外汇修改
     * @param bisAccDtlDTO
     */
    void updateFexCashFlowForSure(BisAccDtlDTO bisAccDtlDTO);
    /**
     * 收款流水认领取消时外汇修改
     * @param bisAccDtlDTO
     */
    void updateFexCashFlowForCancel(BisAccDtlDTO bisAccDtlDTO);
    String getFexCashFlowBillNo(String id);
}
