package com.bytter.letter.domain.service.impl;

import com.bytter.core.utils.DateUtils;
import com.bytter.letter.api.IGuaGuaranteeService;
import com.bytter.letter.domain.service.GuaGuaranteeInfoService;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.Date;
@Service("guaGuaranteeService")
@DubboService
public class GuaGuaranteeServiceImpl implements IGuaGuaranteeService {
    @Autowired
    private GuaGuaranteeInfoService guaGuaranteeInfoService;
    @Override
    public BigDecimal queryGuaranteeUseRatMoney(String billId, String endDate) {
        Date calEndDate = DateUtils.str2Date(endDate, DateUtils.datetimeFormat);
        return guaGuaranteeInfoService.queryGuaranteeUseRatMoney(billId, calEndDate);
    }
}
