package com.bytter.biz.budget.service.impl;
import java.util.Date;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.bytter.biz.budget.service.FbsBudgetPeriodService;
import com.bytter.biz.budget.spi.dto.FbsBudgetPeriodDTO;
import com.bytter.biz.budget.spi.service.IFbsBudgetPeriodService;
import com.bytter.biz.budget.vo.FbsBudgetPeriodVO;
@DubboService
@Service("fbsBudgetPeriodImplService")
@Transactional
public class FbsBudgetPeriodImplService implements IFbsBudgetPeriodService {
    @Autowired
    private FbsBudgetPeriodService fbsBudgetPeriodService;
    /**
     * 根据设定时间和周期类型获取当前时间对应的本期周期
     *
     * @param date     时间
     * @param timeType 计划周期类型
     * @return
     */
    @Override
    public FbsBudgetPeriodDTO getCurBudgetPeriodByTimeType(Date date, String timeType) {
        FbsBudgetPeriodDTO  dto = new FbsBudgetPeriodDTO();
        FbsBudgetPeriodVO vo = fbsBudgetPeriodService.getCurBudgetPeriodByTimeType(date,timeType);
        BeanUtils.copyProperties(vo,dto);
        return dto;
    }
}
