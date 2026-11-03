package com.bytter.guarantee.domain.service.impl;

import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.guarantee.api.ILcGuaranteeBillBondService;
import com.bytter.guarantee.domain.dao.LcGuaranteeBillBondDao;
import com.bytter.guarantee.dto.LcEnrolDTO;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service("lcGuaranteeBillBondServiceImpl")
@DubboService
@Transactional
public class LcGuaranteeBillBondServiceImpl implements ILcGuaranteeBillBondService {

    @Autowired
    private LcGuaranteeBillBondDao lcGuaranteeBillBondDao;

    @Override
    public DataTablesOutput findAbVoucherInfoId(String corpId, String scoringStartDate, String scoringEndDate, String bankType, String bankId) {
        return lcGuaranteeBillBondDao.findAbVoucherInfoId(corpId, scoringStartDate, scoringEndDate, bankType, bankId);
    }

    @Override
    public List<LcEnrolDTO> findLcEnrol(String corpId, String scoringStartDate, String scoringEndDate, String bankType, String bankId) {
        return lcGuaranteeBillBondDao.findLcEnrol(corpId, scoringStartDate, scoringEndDate, bankType, bankId);
    }
}
