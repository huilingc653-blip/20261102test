package com.bytter.biz.foreign.service;

import com.bytter.biz.foreign.dao.FexTranGroupRuleDtlDao;
import com.bytter.biz.foreign.entity.FexTranGroupRuleDtl;
import com.bytter.biz.foreign.vo.FexTranGroupRuleDtlVO;
import com.bytter.core.base.BaseDao;
import com.bytter.core.base.BaseService;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author qinl
 * @version 1.0
 * @description: 外汇交易规则详细
 * @date 2020-02-18
 */
@Service("fexTranGroupRuleDtlService")
@Transactional
public class FexTranGroupRuleDtlService extends BaseService<FexTranGroupRuleDtl, String> {
    @Autowired
    private FexTranGroupRuleDtlDao fexTranGroupRuleDtlDao;

    @Override
    public BaseDao<FexTranGroupRuleDtl, String> getEntityDao() {
        return fexTranGroupRuleDtlDao;
    }

    public DataTablesOutput getDataTable(DataTablesInput dataTablesInput) {
        return fexTranGroupRuleDtlDao.selectDataTable(dataTablesInput);
    }

    /**
     * 根据规则组ID 查询规则组的交易规则列表
     *
     * @param ruleId 规则组id
     * @return List<ExternalCorpBankAccVO>
     */
    public List<FexTranGroupRuleDtlVO> listDetailsById(String ruleId) {
        return this.fexTranGroupRuleDtlDao.selectListById(ruleId);
    }

    /**
     * 根据ID查询 详细
     *
     * @param id 详细id
     * @return ExternalCorpBankAccVO
     */
    public FexTranGroupRuleDtlVO getDetailsById(String id) {
        return this.fexTranGroupRuleDtlDao.getById(id);
    }

    /**
     * 根据规则组ID查询 详细列表
     *
     * @param ruleId 规则组ID
     * @return List<FexTranGroupRuleDtlVO>
     */
    public List<FexTranGroupRuleDtlVO> listByRuleId(String ruleId) {
        return fexTranGroupRuleDtlDao.selectListByRuleId(ruleId);
    }
}
