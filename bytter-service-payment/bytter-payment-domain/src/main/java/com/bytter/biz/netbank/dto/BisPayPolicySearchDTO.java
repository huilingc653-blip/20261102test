package com.bytter.biz.netbank.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

import java.io.Serializable;

/**
 * @author huangx
 * @version 1.0
 * @Title: VO
 * @Description: 自动支付策略表
 * @date 2020-04-17 14:38:52
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
@ApiModel("自动支付策略表")
public class BisPayPolicySearchDTO implements Serializable {

    @ApiModelProperty("单位")
    private String corpId;

    @ApiModelProperty("业务类别")
    private String busTypeName;

    @ApiModelProperty("不支付收款人名称")
    private String noPayeeName;

    @ApiModelProperty("支付收款人名称")
    private String payeeName;

    @ApiModelProperty("共享业务类别")
    private String shareBusTypeName;

    @ApiModelProperty("币别ID")
    private String curId;
}
