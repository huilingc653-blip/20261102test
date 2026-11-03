package com.bytter.biz.foreign.service;


import com.bytter.biz.foreign.dao.FexRateExcDao;
import com.bytter.biz.foreign.entity.FexRateExc;
import com.bytter.core.base.BaseDao;
import com.bytter.core.base.BaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @Title: Service
 * @Description: 外汇牌价查询指令表
 * @author huangx
 * @date 2020-07-16
 *
 */
@Service("fexRateExcService")
@Transactional
public class FexRateExcService extends BaseService<FexRateExc, String> {
    @Autowired
    private FexRateExcDao fexRateExcDao;

    @Override
    public BaseDao<FexRateExc, String> getEntityDao() {
        return fexRateExcDao;
    }

}
