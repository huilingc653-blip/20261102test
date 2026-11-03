package com.bytter.biz.erp.service;

import com.bytter.biz.erp.dao.ErpBisAccBalDao;
import com.bytter.biz.erp.entity.BisAccBal;
import com.bytter.biz.erp.entity.ErpBisAccBalView;
import com.bytter.biz.erp.vo.request.BankAccBalQueryReqVo;
import com.bytter.core.base.BaseDao;
import com.bytter.core.base.BaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 银行账户余额查询
 *
 * @author fangcf
 * @date 2026-05-11
 */
@Service("erpBisAccBalService")
public class ErpBisAccBalService extends BaseService<BisAccBal, String> {
    @Autowired
    private ErpBisAccBalDao erpBisAccBalDao;

    @Override
    public BaseDao getEntityDao() {
        return erpBisAccBalDao;
    }

    /**
     * 根据条件查询银行账户余额
     *
     * @param reqVo 过滤条件vo
     * @return List<BisAccDtl> 银行明细
     */
    public List<ErpBisAccBalView> listBisAccBal(BankAccBalQueryReqVo reqVo) {
        return erpBisAccBalDao.listBisAccBal(reqVo);
    }
}
