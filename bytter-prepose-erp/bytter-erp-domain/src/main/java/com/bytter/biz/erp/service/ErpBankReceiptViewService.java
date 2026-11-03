package com.bytter.biz.erp.service;

import com.bytter.biz.erp.dao.ErpBankReceiptViewDao;
import com.bytter.biz.erp.entity.ErpBankReceiptImgView;
import com.bytter.biz.erp.entity.ErpBankReceiptView;
import com.bytter.biz.erp.vo.request.BankReceiptQueryReqVo;
import com.bytter.core.base.BaseDao;
import com.bytter.core.base.BaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 电子回单查询
 *
 * @author fangcf
 * @date 2026-05-12
 */
@Service("erpBankReceiptViewService")
public class ErpBankReceiptViewService extends BaseService<ErpBankReceiptView, String> {
    @Autowired
    private ErpBankReceiptViewDao erpBankReceiptViewDao;

    @Override
    public BaseDao getEntityDao() {
        return erpBankReceiptViewDao;
    }

    /**
     * 根据条件查询电子回单
     *
     * @param reqVo 过滤条件vo
     * @return List<ErpBankReceiptView> 电子回单
     */
    public List<ErpBankReceiptView> listBankReceipt(BankReceiptQueryReqVo reqVo) {
        return erpBankReceiptViewDao.listBankReceipt(reqVo);
    }

    /**
     * 根据条件查询电子回单信息
     *
     * @param tenantId 租户id
     * @param serialId 主键id
     * @return List<ErpBankReceiptImgView> 电子回单
     */
    public List<ErpBankReceiptImgView> listBankReceiptImgView(String tenantId, String serialId) {
        return erpBankReceiptViewDao.listBankReceiptImgView(tenantId, serialId);
    }
}
