package com.bytter.guarantee.dto;

import io.swagger.annotations.ApiModel;
import lombok.Data;

@Data
@ApiModel("担保合同提交数据")
public class GuaContractSubmitDTO {
    private String businessId;
    private String contractIds;
    private String billCode;
    private String sysCode;
}
