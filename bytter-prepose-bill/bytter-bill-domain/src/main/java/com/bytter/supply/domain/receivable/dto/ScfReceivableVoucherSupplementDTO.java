package com.bytter.supply.domain.receivable.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * @author ztd
 * @version 1.0
 * @Title: ScfReceivableVoucherSupplementDTO
 * @Description: 应收债权凭证补录DTO
 * @since 2026/7/9
 */
@Data
@ApiModel(description = "应收债权凭证补录请求DTO")
public class ScfReceivableVoucherSupplementDTO {

    @ApiModelProperty(value = "主键ID", required = true)
    private String id;

    @ApiModelProperty(value = "凭证编号")
    private String voucherNo;

    @ApiModelProperty(value = "上级凭证编号")
    private String forehandVoucherCode;
}
