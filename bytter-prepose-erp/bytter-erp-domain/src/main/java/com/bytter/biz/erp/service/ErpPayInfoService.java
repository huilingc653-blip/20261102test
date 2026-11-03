package com.bytter.biz.erp.service;

import com.bytter.biz.erp.dao.ErpPayInfoDao;
import com.bytter.biz.erp.entity.ErpPayInfo;
import com.bytter.core.base.BaseDao;
import com.bytter.core.base.BaseService;
import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("erpPayInfoService")
public class ErpPayInfoService extends BaseService<ErpPayInfo, String> {

    @Autowired
    private ErpPayInfoDao erpPayInfoDao;

    @Override
    public BaseDao getEntityDao() {
        return erpPayInfoDao;
    }

    /**
     * 根据erp流水号查询付款单
     *
     * @param serialNoErp
     * @return erp付款单
     */
    public ErpPayInfo getBySerialNoErp(String serialNoErp) {
        Criteria criteria = createCriteria();
        criteria.add(Restrictions.eq("serialNoErp", serialNoErp));
        return (ErpPayInfo) criteria.uniqueResult();
    }
}
