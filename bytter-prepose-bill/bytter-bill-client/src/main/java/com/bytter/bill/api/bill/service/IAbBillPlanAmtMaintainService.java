package com.bytter.bill.api.bill.service;


import com.bytter.bill.api.bill.dto.AbBillPlanAmtMaintainDTO;

import java.util.Date;

/**
 * 银行计划开票金额维护对外接口
 *
 * @author fangcf
 * @date 2025-07-02
 */
public interface IAbBillPlanAmtMaintainService {

    /**
     * 根据单位，银行类型，周开始日，周结束日，获取唯一的一条银行计划开票金额数据
     *
     * @param corpId       单位id
     * @param bankTypeId   银行类型
     * @param creditType   授信类型
     * @param weekStartDay 周开始日
     * @param weekEndDay   周结束日
     */
    AbBillPlanAmtMaintainDTO getOneBy(String corpId, String bankTypeId, String creditType, Date weekStartDay, Date weekEndDay);

    /**
     * 获取2000年-2099年所有周一日期，然后日期字符串拼接
     *
     * @return 所有周一日期字符串
     */
    String getMondays();
}
