package com.bytter.bill.domain.bill.service.impl;

import com.bytter.bill.api.bill.dto.AbBillPlanAmtMaintainDTO;
import com.bytter.bill.api.bill.service.IAbBillPlanAmtMaintainService;
import com.bytter.bill.domain.bill.dao.AbBillPlanAmtMaintainDao;
import com.bytter.bill.domain.bill.service.AbBillPlanAmtMaintainService;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

/**
 * 银行计划开票金额维护对外接口实现
 *
 * @author fangcf
 * @date 2025-07-02
 */
@Service("abBillPlanAmtMaintainServiceImpl")
@Transactional
@DubboService
public class AbBillPlanAmtMaintainServiceImpl implements IAbBillPlanAmtMaintainService {

    @Autowired
    private AbBillPlanAmtMaintainService abBillPlanAmtMaintainService;
    @Autowired
    private AbBillPlanAmtMaintainDao abBillPlanAmtMaintainDao;

    /**
     * 根据单位，银行类型，周开始日，周结束日，获取唯一的一条银行计划开票金额数据
     *
     * @param corpId       单位id
     * @param bankTypeId   银行类型
     * @param creditType   授信类型
     * @param weekStartDay 周开始日
     * @param weekEndDay   周结束日
     */
    @Override
    public AbBillPlanAmtMaintainDTO getOneBy(String corpId, String bankTypeId, String creditType, Date weekStartDay, Date weekEndDay) {
        return abBillPlanAmtMaintainDao.getOneBy(corpId, bankTypeId, creditType, weekStartDay, weekEndDay);
    }

    /**
     * 获取2000年-2099年所有周一日期，然后日期字符串拼接
     *
     * @return 所有周一日期字符串
     */
    @Override
    public String getMondays() {
        return abBillPlanAmtMaintainService.getMondays();
    }
}
