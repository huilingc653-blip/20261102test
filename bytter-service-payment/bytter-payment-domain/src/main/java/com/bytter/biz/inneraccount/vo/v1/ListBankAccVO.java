package com.bytter.biz.inneraccount.vo.v1;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @author zhengzf
 * @Description: 银行账号信息
 * @date 2026-01-04 15:02:30
 */
@ApiModel("银行账号信息")
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class ListBankAccVO implements Serializable {
    @ApiModelProperty("调拨策略ID")
    String gatherPolicyId;
    @ApiModelProperty("父账号ID")
    String prentAccId;
    @ApiModelProperty("父账号标志")
    String parentSign;
    @ApiModelProperty("币别ID")
    String curId;
    //调拨类型：0-定时归集，1-实时归集，2-定时下拨
    @ApiModelProperty("调拨类型：0-定时归集，1-实时归集，2-定时下拨")
    String gatherType;
}
