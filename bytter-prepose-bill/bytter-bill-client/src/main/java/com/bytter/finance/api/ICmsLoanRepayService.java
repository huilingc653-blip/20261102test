package com.bytter.finance.api;

/**
 * @author luoyj
 * @date 2023/11/21.
 */
public interface ICmsLoanRepayService {

    /**
     * 更新还款计划归还标识
     */
    void setRepayMentScheduleIsRepay(String repaymentId, String isRepay);

}
