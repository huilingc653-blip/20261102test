package com.bytter.increment.domain.vo.v1;

import com.bytter.biz.bdlink.api.dto.BisAccDtlDTO;
import com.bytter.system.api.dto.CorpDTO;
import io.swagger.annotations.ApiModel;
import lombok.Data;

@Data
@ApiModel("理财产品组件选择相关配置")
public class ChoProductRedeemSelectVO {

    private CorpDTO corp;

    private String endDate;

    private String startDate;

    private BisAccDtlDTO dto;
}
