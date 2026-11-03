package com.bytter.system.api;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
public interface IBtBankAccDelService {
    DataTablesOutput getDataTablesUnion(DataTablesInput dataTablesInput, String joinSql);
}
