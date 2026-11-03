package com.bytter.biz.inneraccount.vo.v1;

import com.bytter.biz.finacial.api.vo.FcAccDetailBudgetMatchVO;
import com.bytter.system.api.vo.FcAccVO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@ApiModel("内部账户流水资金计划匹配信息")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class FcAccDetailBudgetMatchPageVO implements Serializable {
    @ApiModelProperty("执行系统名称")
    private String processSysname;
    @ApiModelProperty("业务名称")
    private String businessName;
    @ApiModelProperty("bill订单ID")
    private String billNo;
    @ApiModelProperty("详细资金匹配信息")
    private FcAccDetailBudgetMatchVO fcAccDetailBudgetMatchPage;
}
