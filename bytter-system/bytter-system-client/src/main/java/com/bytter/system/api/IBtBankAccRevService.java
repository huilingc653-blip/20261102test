package com.bytter.system.api;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
public interface IBtBankAccRevService {
    DataTablesOutput getDataTablesUnion(DataTablesInput dataTablesInput, String joinSql);
    DataTablesOutput getChangeDataTablesUnion(DataTablesInput dataTablesInput, String joinSql);
    DataTablesOutput dataTableApprove(DataTablesInput dataTablesInput, String joinSql);
}
