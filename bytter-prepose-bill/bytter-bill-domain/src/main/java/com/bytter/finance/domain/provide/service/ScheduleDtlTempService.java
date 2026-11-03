package com.bytter.finance.domain.provide.service;

import com.bytter.core.base.BaseDao;
import com.bytter.core.base.BaseService;
import com.bytter.finance.domain.provide.dao.ScheduleDtlTempDao;
import com.bytter.finance.domain.provide.entity.ScheduleDtlTemp;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


/**
 * @author huangyl
 * @version 1.0
 * @Title: Service
 * @Description: 还款计划明细暂存表
 * @date 2017-08-01 09:58:43
 */
@Service("scheduleDtlTempService")
@Transactional
public class ScheduleDtlTempService extends BaseService<ScheduleDtlTemp, String> {

    @Autowired
    private ScheduleDtlTempDao dtlTempDao;

    @Override
    public BaseDao<ScheduleDtlTemp, String> getEntityDao() {
        return dtlTempDao;
    }

    /**
     * 删除暂存表数据
     *
     * @param billCode 单据号
     */
    public void deleteBySendBillCode(String billCode) {
        dtlTempDao.deleteBySendBillCode(billCode);
    }

    public List<ScheduleDtlTemp> getListByBillCode(String id) {
        return dtlTempDao.getListByBillCode(id);
    }
    public List<ScheduleDtlTemp> getListByIds(List<String> ids) {
        return dtlTempDao.getListByIds(ids);
    }
}
