package com.bytter.biz.reconcile.service.impl;

import com.bytter.biz.bdlink.api.service.IBisBankMonthStatementDtlService;
import com.bytter.biz.bdlink.vo.BisBankMonthStatementDtlVO;
import com.bytter.biz.reconcile.dao.BisBankMonthStatementDtlDao;
import com.bytter.biz.reconcile.entity.BisBankMonthStatementDtl;
import com.bytter.core.base.BaseDao;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service("bisBankMonthStatementDtlServiceImpl")
@DubboService
@Transactional
public class BisBankMonthStatementDtlServiceImpl implements IBisBankMonthStatementDtlService {
    @Autowired
    private BisBankMonthStatementDtlDao bisBankMonthStatementDtlDao;

    public BaseDao<BisBankMonthStatementDtl, String> getEntityDao() {
        return bisBankMonthStatementDtlDao;
    }
    public DataTablesOutput getDataTablesUnion(DataTablesInput input,StringBuilder commonSql) {
        return bisBankMonthStatementDtlDao.getDataTablesUnion(input, commonSql);
    }
    /**
     * 根据ID获取银行对账单
     *
     * @param id
     * @return
     */
    public BisBankMonthStatementDtlVO getByIdUnion(String id) {
        return bisBankMonthStatementDtlDao.getByIdUnion(id);
    }
    public  List<BisBankMonthStatementDtlVO> listByIds(String ids) {
        return bisBankMonthStatementDtlDao.listByIds(ids);
    }
}
