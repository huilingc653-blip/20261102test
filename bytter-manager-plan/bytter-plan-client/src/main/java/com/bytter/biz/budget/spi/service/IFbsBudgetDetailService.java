package com.bytter.biz.budget.spi.service;

import com.bytter.biz.budget.spi.dto.FbsBudgetDetailDTO;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.common.datatable.V1DataTablesInput;
import com.bytter.core.common.model.json.AjaxJson;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

/**
 * 预算明细接口
 * @author zuoy
 * @since 2018-03-01
 */
public interface IFbsBudgetDetailService {

    public List<FbsBudgetDetailDTO> getProjectDetailInfo(DataTablesInput input);

}


