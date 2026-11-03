package com.bytter.biz.inneraccount.vo.v1;

import com.bytter.system.api.vo.FcAccVO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;

@ApiModel("查询账号操作的请求参数")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class FcAccOperatorVO implements Serializable {
    @ApiModelProperty("账户ID")
    private String id;
    @ApiModelProperty("账户类别")
    private String accTypeCode;

    @ApiModelProperty("操作类型 add/update")
    private String type;

    @ApiModelProperty("币别")
    private String curId;

    @ApiModelProperty("网点")
    private String netId;

    @ApiModelProperty("账号操作类型 改户时为change")
    private String opType;

    @ApiModelProperty("资金池标识")
    private String poolSign;


    @ApiModelProperty("账户启用日期")
    private String accStartDate;

    @ApiModelProperty("利率")
    private BigDecimal InterestRate;

    @ApiModelProperty("账户相关信息")
    private FcAccVO fcAccPage;

}
