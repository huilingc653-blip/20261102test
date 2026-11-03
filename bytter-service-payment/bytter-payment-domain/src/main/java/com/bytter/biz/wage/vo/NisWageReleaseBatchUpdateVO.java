package com.bytter.biz.wage.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 批量设置VO
 *
 * @author loveCamille
 */
@ApiModel("批量设置VO")
@Getter
@Setter
public class NisWageReleaseBatchUpdateVO implements Serializable {
    @ApiModelProperty("多个ids以,分割")
    private String ids;
    @ApiModelProperty("单个id")
    private String id;
    @ApiModelProperty("批量设置类型不能为空")
    @NotNull(message = "批量设置类型不能为空")
    private Integer type;

    @ApiModelProperty("amount")
    private BigDecimal amount;
    @ApiModelProperty("ratio")
    private BigDecimal ratio;
    @ApiModelProperty("releaseAmount")
    private BigDecimal releaseAmount;

}
