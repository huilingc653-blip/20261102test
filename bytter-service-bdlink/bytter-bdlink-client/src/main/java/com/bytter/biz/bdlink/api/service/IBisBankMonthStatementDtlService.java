package com.bytter.biz.bdlink.api.service;

import com.bytter.biz.bdlink.vo.BisBankMonthStatementDtlVO;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;

import java.util.List;

public interface IBisBankMonthStatementDtlService {
    DataTablesOutput getDataTablesUnion(DataTablesInput input, StringBuilder commonSql) ;
    /**
     * 根据ID获取银行对账单
     *
     * @param id
     * @return
     */
    BisBankMonthStatementDtlVO getByIdUnion(String id) ;
    List<BisBankMonthStatementDtlVO> listByIds(String ids);
}
