package com.bytter.increment.domain.vo.v1;

import com.bytter.increment.domain.vo.ChoBankFinanceVO;
import com.bytter.system.api.dto.CorpDTO;
import com.bytter.biz.bdlink.api.dto.BisAccDtlDTO;
import io.swagger.annotations.ApiModel;
import lombok.Data;

@Data
@ApiModel("流水认领组件返回接口")
public class ChoBankFinancePageVO {

    private CorpDTO corp;

    private String busType;

    private String row;

    private String endDate;

    private String startDate;

    private BisAccDtlDTO dto;

    private ChoBankFinanceVO choBankFinancePage;

}
