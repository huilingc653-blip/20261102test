package com.bytter.biz.erp.service;

import cn.hutool.core.collection.CollectionUtil;
import com.bytter.biz.erp.dao.BisServerParamDao;
import com.bytter.biz.erp.entity.BisServerParam;
import com.bytter.biz.erp.entity.ErpUserInfo;
import com.bytter.core.base.BaseDao;
import com.bytter.core.base.BaseService;
import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

@Service("bisServerParamService")
public class BisServerParamService extends BaseService<BisServerParam, String> {

    @Autowired
    private BisServerParamDao bisServerParamDao;

    @Override
    public BaseDao<BisServerParam, String> getEntityDao() {
        return bisServerParamDao;
    }

    public List<BisServerParam> listParams() {
        Criteria criteria = createCriteria();
        criteria.add(Restrictions.in("paramType", "com", "erp"));
        List<BisServerParam> list = criteria.list();
        if (CollectionUtil.isEmpty(list)){
           return Collections.EMPTY_LIST;
        }
        return list;
    }
}
