package com.bytter.biz.foreign.service;

import com.bytter.biz.foreign.dao.FexCustInfoTenantDao;
import com.bytter.biz.foreign.entity.FexCustInfoTenant;
import com.bytter.biz.foreign.vo.FexTranCustInfoVO;
import com.bytter.core.base.BaseDao;
import com.bytter.core.base.BaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 租户交易自定义信息Service。
 */
@Service("fexCustInfoTenantService")
@Transactional
public class FexCustInfoTenantService extends BaseService<FexCustInfoTenant, String> {

    @Autowired
    private FexCustInfoTenantDao fexCustInfoTenantDao;

    @Override
    public BaseDao<FexCustInfoTenant, String> getEntityDao() {
        return fexCustInfoTenantDao;
    }

    public void deleteCustFieldInfoByBusId(String id){
        fexCustInfoTenantDao.deleteCustFieldInfoByBusId(id);
    }

    public List<FexTranCustInfoVO> listCustInfoByBusId(String id){
        return fexCustInfoTenantDao.listCustInfoByBusId(id);
    }

    public List<FexTranCustInfoVO> listCustInfoByBusIdAndAppRange(String busId, String appRange1, String appRange2){
        return fexCustInfoTenantDao.listCustInfoByBusIdAndAppRange(busId, appRange1, appRange2);
    }

    public Boolean getCustInfoByBusFieldId(String fieldId){
        return fexCustInfoTenantDao.getCustInfoByBusFieldId(fieldId);
    }
}
