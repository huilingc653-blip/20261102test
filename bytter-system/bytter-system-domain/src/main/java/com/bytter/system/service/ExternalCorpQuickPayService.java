package com.bytter.system.service;

import com.bytter.system.dao.ExternalCorpQuickPayDao;
import com.bytter.system.entity.ExternalCorpBankAcc;
import com.bytter.system.entity.ExternalCorpQuickPay;
import com.bytter.system.vo.ExternalCorpBankAccVO;
import com.bytter.system.vo.ExternalCorpQuickPayVO;
import com.bytter.core.base.BaseDao;
import com.bytter.core.base.BaseService;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * 客商快捷支付信息service
 */
@Service("externalCorpQuickPayService")
public class ExternalCorpQuickPayService extends BaseService<ExternalCorpQuickPay, String> {

    @Resource
    private ExternalCorpQuickPayDao externalCorpQuickPayDao;

    @Override
    public BaseDao<ExternalCorpQuickPay, String> getEntityDao() {
        return externalCorpQuickPayDao;
    }

    /**
     * 根据客商ID删除对应的快捷支付信息
     * @param externalCorpId
     */
    public void deleteQuickPayByExternalCorpId(String externalCorpId) {
        this.externalCorpQuickPayDao.deleteQuickPayByExternalCorpId(externalCorpId);
    }

    public List<ExternalCorpQuickPay> listQuickPayByExternalCorpId(String externalCorpId) {
        return this.externalCorpQuickPayDao.listQuickPayByExternalCorpId(externalCorpId);
    }

    public List<ExternalCorpQuickPayVO> listByExternalCorpId(String externalCorpId) {
        return this.externalCorpQuickPayDao.listByExternalCorpId(externalCorpId);
    }

    /**
     * 根据客商ID集合批量查询关联交付产品名称。
     *
     * @param externalCorpIds 客商ID集合
     * @return 客商快捷支付信息集合
     */
    public List<ExternalCorpQuickPayVO> listByExternalCorpIds(List<String> externalCorpIds) {
        return this.externalCorpQuickPayDao.listByExternalCorpIds(externalCorpIds);
    }

    public void save(ExternalCorpQuickPay externalCorpQuickPay) {
        this.saveEntity(externalCorpQuickPay);
    }

    public void update(ExternalCorpQuickPay externalCorpQuickPay) {
        this.updateEntity(externalCorpQuickPay);
    }

    /**
     * 根据客商id获取所有关联产品
     * @param externalId
     * @return
     */
    public List<String> getProductNameByExternalId(String externalId){
        return externalCorpQuickPayDao.getProductNameByExternalId(externalId);
    }

    /**
     * 所有客fps信息组件取数
     */
    public DataTablesOutput fpsExternalBankAccDate(DataTablesInput input) {
        return externalCorpQuickPayDao.fpsExternalBankAccDate(input);
    }

    /**
     * 所有客商payPal信息组件取数
     */
    public DataTablesOutput payPalExternalBankAccDate(DataTablesInput input) {
        return externalCorpQuickPayDao.payPalExternalBankAccDate(input);
    }

    /**
     * 资金收付收款账户类型对应数据
     */
    public List<ExternalCorpQuickPayVO> fexPayPayModelList(String externalId, String proId,String field) {
        return externalCorpQuickPayDao.fexPayPayModelList(externalId,proId,field);
    }
}
