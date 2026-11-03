package com.bytter.biz.erp.service;

import com.bytter.biz.erp.dao.ErpNisWageDetailDao;
import com.bytter.biz.erp.entity.NisWageDetail;
import com.bytter.core.base.BaseDao;
import com.bytter.core.base.BaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 代发工资明细表实现类
 *
 * @author fangcf
 * @date 2026-05-13
 */
@Service
public class ErpNisWageDetailService extends BaseService<NisWageDetail, String> {
    @Autowired
    private ErpNisWageDetailDao erpNisWageDetailDao;

    @Override
    public BaseDao getEntityDao() {
        return erpNisWageDetailDao;
    }

    public NisWageDetail getByDtlNo(String tenantId, String wageDtlNo) {
        return erpNisWageDetailDao.getOne(tenantId, wageDtlNo);
    }
}
