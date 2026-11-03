package com.bytter.biz.finacial.api.service;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import java.util.Map;
/**
 * 内部账户流水查询
 */
public interface IFcAccDetailQueryService {
    /**
     * 查询内部账号余额列表
     *
     * @param dataTablesInput
     * @return
     */
    public DataTablesOutput getDataTablesUnion(DataTablesInput dataTablesInput, String joinSql, Map<String,Object> params);
}
