package com.bytter.increment.domain.service.impl;

import com.bytter.increment.api.dto.ChoDepositRateDTO;
import com.bytter.increment.api.dto.ChoProductReqQueryDTO;
import com.bytter.increment.api.service.IChoDepositRateService;
import com.bytter.increment.domain.dao.ChoDepositRateDao;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@Service("choDepositRateServiceImpl")
@Transactional
@DubboService
public class ChoDepositRateServiceImpl implements IChoDepositRateService {

    @Autowired
    private ChoDepositRateDao choDepositRateDao;

    @Override
    public List<ChoDepositRateDTO> queryDepositList(String corpId, String bankCode, String bankTypeId, String scoringStartDate, String scoringEndDate) {
        return choDepositRateDao.queryDepositList(corpId, bankCode, bankTypeId, scoringStartDate, scoringEndDate);
    }

    @Override
    public List<ChoDepositRateDTO> queryNotChoList(String corpId, String bankCode, String bankTypeId, String scoringStartDate, String scoringEndDate) {
        return choDepositRateDao.queryNotChoList(corpId, bankCode, bankTypeId, scoringStartDate, scoringEndDate);
    }

    @Override
    public List<ChoProductReqQueryDTO> queryChoList(String corpId, String bankCode, String bankTypeId, String scoringStartDate, String scoringEndDate) {
        return choDepositRateDao.queryChoList(corpId, bankCode, bankTypeId, scoringStartDate, scoringEndDate);
    }

    @Override
    public BigDecimal getChoFinanceForReportDate(List<String> corpIds, Date balDate, String bankTypeId, String bankCode) {
        return choDepositRateDao.getChoFinanceForReportDate(corpIds, balDate, bankTypeId, bankCode);
    }

    @Override
    public BigDecimal getChoProductReqForReportDate(List<String> corpIds, Date balDate, String bankTypeId, String bankCode) {
        return choDepositRateDao.getChoProductReqForReportDate(corpIds, balDate, bankTypeId, bankCode);
    }
}
