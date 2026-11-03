package com.bytter.biz.netbank.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

import java.io.Serializable;
import java.util.List;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
@ApiModel("获取自动支付策略表详情信息")
public class BisPayPolicyDetailVO implements Serializable {

    @ApiModelProperty("自动支付策略表详情信息")
  private BisPayPolicyVO bisPayPolicyPage;
    @ApiModelProperty("自动支付优先策略")
    private   List<BisPayPolicySortVo> sort;
    @ApiModelProperty("支付结算参数配置：autoPayerAccNum-自动支付账号数量")
    private String   autoPayerAccNum;

}