package com.bytter.finance.domain.provide.service;

import com.bytter.core.base.BaseDao;
import com.bytter.core.base.BaseService;
import com.bytter.finance.domain.provide.dao.CmsProvideInterestTempDao;
import com.bytter.finance.domain.provide.entity.CmsProvideInterestTemp;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author huangyl
 * @version 1.0
 * @Title: Service
 * @Description: 借款利息预计明细暂存表
 * @date 2017-08-01 09:58:43
 */
@Service("CmsProvideInterestTempService")
@Transactional
public class CmsProvideInterestTempService extends BaseService<CmsProvideInterestTemp, String> {

    @Autowired
    private CmsProvideInterestTempDao provideInterestTempDao;

    @Override
    public BaseDao<CmsProvideInterestTemp, String> getEntityDao() {
        return provideInterestTempDao;
    }

    /**
     * 根据单据号删除暂存表数据
     *
     * @param billCode 单据号
     */
    public void deleteBySendBillCode(String billCode) {
        provideInterestTempDao.deleteBySendBillCode(billCode);
    }

    public List<CmsProvideInterestTemp> getListByBillCode(String sendCode) {
        return provideInterestTempDao.getListByBillCode(sendCode);
    }
}
