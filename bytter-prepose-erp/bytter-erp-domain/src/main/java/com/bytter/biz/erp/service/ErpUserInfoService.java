package com.bytter.biz.erp.service;

import com.bytter.biz.erp.dao.ErpUserInfoDao;
import com.bytter.biz.erp.entity.ErpUserInfo;
import com.bytter.core.base.BaseDao;
import com.bytter.core.base.BaseService;
import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service("erpUserInfoService")
public class ErpUserInfoService extends BaseService<ErpUserInfo, String> {

    @Autowired
    private ErpUserInfoDao erpUserInfoDao;

    @Override
    public BaseDao getEntityDao() {
        return erpUserInfoDao;
    }


    public List<ErpUserInfo> queryList(String tenantId) {
        Criteria criteria = createCriteria();
        criteria.add(Restrictions.eq("tenantId", tenantId));
        List<ErpUserInfo> list = criteria.list();
        return list;
    }
}
