package com.bytter.finance.domain.provide.service.impl;

import com.bytter.finance.api.IFinancingService;
import com.bytter.finance.domain.provide.dao.FinancingDao;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Service("financingServiceImpl")
@Transactional
@DubboService
public class FinancingServiceImpl implements IFinancingService {

    @Autowired
    private FinancingDao financingDao;

    @Override
    public Map<String, BigDecimal> getSumProvideBal(String corpId, List<String> bankCodes, List<String> bankTypeIds, Date endDate) {
        return financingDao.getSumProvideBal(corpId, bankCodes, bankTypeIds, endDate);
    }

    @Override
    public Map<String, BigDecimal> getSumProvideSendMoney(String corpId, List<String> bankCodes, List<String> bankTypeIds, Date StartDate, Date endDate) {
        return financingDao.getSumProvideSendMoney(corpId, bankCodes, bankTypeIds, StartDate, endDate);
    }

    @Override
    public Map<String, BigDecimal> getAvgProvideSendRate(String corpId, List<String> bankCodes, List<String> bankTypeIds, Date StartDate, Date endDate) {
        return financingDao.getAvgProvideSendRate(corpId, bankCodes, bankTypeIds, StartDate, endDate);
    }

    @Override
    public BigDecimal getProvideBalForReportDate(List<String> corpIds, Date reportDate, String bankTypeId, String bankCode) {
        return financingDao.getProvideBalForReportDate(corpIds, reportDate, bankTypeId, bankCode);
    }
}
