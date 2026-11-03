package com.bytter.guarantee.dto;


import io.swagger.annotations.ApiModel;
import lombok.Data;

@Data
@ApiModel("释放担保合同操作")
public class GuaContractReleaseDTO {

    private String contractId;

    private String businessId;
}
