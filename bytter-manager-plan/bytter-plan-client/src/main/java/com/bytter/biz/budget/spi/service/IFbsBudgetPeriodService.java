package com.bytter.biz.budget.spi.service;
import java.util.Date;
import com.bytter.biz.budget.spi.dto.FbsBudgetPeriodDTO;
/**
 * 计划时间接口
 */
public interface IFbsBudgetPeriodService  {
    /**
     * 根据设定时间和周期类型获取当前时间对应的本期周期
     *
     * @param date     时间
     * @param timeType 计划周期类型
     * @return
     */
    FbsBudgetPeriodDTO getCurBudgetPeriodByTimeType(Date date, String timeType);
}
