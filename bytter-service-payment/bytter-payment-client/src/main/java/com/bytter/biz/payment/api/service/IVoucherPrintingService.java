package com.bytter.biz.payment.api.service;

import com.bytter.biz.payment.api.dto.BisExcDTO;
import com.bytter.core.base.entity.ICurrentUser;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;

import java.util.List;
import java.util.Map;

/**
 * 获取凭证打印信息
 */
public interface IVoucherPrintingService {
    Map<String,Object> queryBvPrintVouOpById(String listFields, String dataId, String tableName);
}
