package com.bytter.biz.erp.service;

import com.bytter.biz.erp.dao.ErpBisAccDtlDao;
import com.bytter.biz.erp.entity.BisAccDtl;
import com.bytter.biz.erp.vo.request.BankAccDtlQueryReqVo;
import com.bytter.core.base.BaseDao;
import com.bytter.core.base.BaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 银行明细查询
 *
 * @author fangcf
 * @date 2026-05-11
 */
@Service("erpBisAccDtlService")
public class ErpBisAccDtlService extends BaseService<BisAccDtl, String> {
    @Autowired
    private ErpBisAccDtlDao erpBisAccDtlDao;

    @Override
    public BaseDao getEntityDao() {
        return erpBisAccDtlDao;
    }

    /**
     * 根据条件查询银行明细
     *
     * @param reqVo 过滤条件vo
     * @return List<BisAccDtl> 银行明细
     */
    public List<BisAccDtl> listBisAccDtl(BankAccDtlQueryReqVo reqVo) {
        return erpBisAccDtlDao.listBisAccDtl(reqVo);
    }
}
