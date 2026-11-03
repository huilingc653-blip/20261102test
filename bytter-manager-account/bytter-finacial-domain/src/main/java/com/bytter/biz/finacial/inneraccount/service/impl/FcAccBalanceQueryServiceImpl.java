package com.bytter.biz.finacial.inneraccount.service.impl;

import com.bytter.biz.finacial.api.service.IFcAccBalanceQueryService;
import com.bytter.biz.finacial.api.vo.FcAccBalanceQueryVO;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;

import java.util.Map;

public class FcAccBalanceQueryServiceImpl implements IFcAccBalanceQueryService {
    @Override
    public DataTablesOutput getDataTablesUnion(DataTablesInput dataTablesInput, String userId) {
        return null;
    }

    @Override
    public DataTablesOutput getDataTablesUnionForPool(DataTablesInput dataTablesInput, String userId) {
        return null;
    }

    @Override
    public DataTablesOutput getAccountDtl(DataTablesInput dataTablesInput, String account, String userId) {
        return null;
    }

    @Override
    public void balanceLink(DataTablesOutput output, StringBuilder joinSql, Map<String, Object> params) {

    }

    @Override
    public FcAccBalanceQueryVO getFcAccBalByAccount(String account) {
        return null;
    }
}
