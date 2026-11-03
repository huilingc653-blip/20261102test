package com.bytter.credit.domain.service.impl;

import com.bytter.credit.api.ICreditLimitService;
import com.bytter.credit.domain.dao.CreditLimitDao;
import com.bytter.credit.dto.RatBkrationDTO;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@Service("creditLimitServiceImpl")
@Transactional
@DubboService
public class CreditLimitServiceImpl implements ICreditLimitService {

    @Autowired
    private CreditLimitDao creditLimitDao;

    @Override
    public List<RatBkrationDTO> countCreditExposureLimitByBankType(String corpId, String scoringEndDate, List<String> bankTypeList) {
        return creditLimitDao.countCreditExposureLimitByBankType(corpId, scoringEndDate, bankTypeList);
    }

    @Override
    public List<RatBkrationDTO> countCreditExposureLimitByBankCode(String corpId, String scoringEndDate, List<String> bankCodeList) {
        return creditLimitDao.countCreditExposureLimitByBankCode(corpId, scoringEndDate, bankCodeList);
    }

    @Override
    public BigDecimal getSumCreditBalForReportDate(List<String> corpIds, Date scoringEndDate, String bankTypeId, String bankCode) {
        return creditLimitDao.getSumCreditBalForReportDate(corpIds, scoringEndDate, bankTypeId, bankCode);
    }
}
