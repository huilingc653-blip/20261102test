package com.bytter.biz.creditRating.service;

import com.bytter.biz.creditRating.dao.CrDataSourceDao;
import com.bytter.biz.creditRating.entity.CrDataSource;
import com.bytter.biz.creditRating.vo.CrDataSourceVo;
import com.bytter.core.base.BaseDao;
import com.bytter.core.base.BaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 数据源来源
 */
@Service("crDataSourceService")
@Transactional
public class CrDataSourceService extends BaseService<CrDataSource, String> {

    @Autowired
    private CrDataSourceDao crDataSourceDao;

    @Override
    public BaseDao<CrDataSource, String> getEntityDao() {
        return crDataSourceDao;
    }


    public List<CrDataSourceVo> getData(String dataSourceName) {
        return crDataSourceDao.getData(dataSourceName);
    }
}
