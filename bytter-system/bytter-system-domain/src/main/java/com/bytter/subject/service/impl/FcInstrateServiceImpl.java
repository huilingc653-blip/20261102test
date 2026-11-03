package com.bytter.subject.service.impl;


import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.utils.EmptyUtils;
import com.bytter.subject.service.FcInstrateService;
import com.bytter.system.api.IFcInstrateService;
import com.bytter.system.pricing.dto.FcInstrateDTO;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

@Service("fcInstrateServiceImpl")
@Transactional
@DubboService
public class FcInstrateServiceImpl implements IFcInstrateService {
    @Autowired
    private FcInstrateService fcInstrateService;
    @Override
    public BigDecimal getFcInstrateByObjectId(String objectId,String curId) {
        String instrtType = "1";
        String endUnit = "N";
        FcInstrateDTO lastInstrate = fcInstrateService.getLastInstrate(objectId, curId, instrtType, endUnit);
        if(!EmptyUtils.isEmpty(lastInstrate)){
            return lastInstrate.getInterestRate();
        }
        return null;
    }

    @Override
    public void deleteEntity(FcInstrateDTO entity) {
        fcInstrateService.deleteEntity(entity);
    }

    @Override
    public void delete(String id) {
        fcInstrateService.delete(id);
    }

    @Override
    public void save(FcInstrateDTO entity, String loginName) {
        fcInstrateService.save(entity, loginName);
    }

    @Override
    public void update(FcInstrateDTO entity) {
        fcInstrateService.update(entity);
    }

    @Override
    public void saveOrUpdate(FcInstrateDTO entity) {
        fcInstrateService.saveOrUpdate(entity);
    }

    @Override
    public DataTablesOutput fcInstrateList(DataTablesInput dataTablesInput) {
        return fcInstrateService.fcInstrateList(dataTablesInput);
    }

    @Override
    public FcInstrateDTO getfcInstrate(String id) {
        return fcInstrateService.getfcInstrate(id);
    }

    @Override
    public void deleteFcInstrate(String id) {
        fcInstrateService.deleteFcInstrate(id);
    }

    @Override
    public FcInstrateDTO getLastInstrate(String subjectCode, String curCode, String instrtType, String endUnit) {
        return fcInstrateService.getLastInstrate(subjectCode,  curCode,  instrtType,  endUnit);
    }

}
