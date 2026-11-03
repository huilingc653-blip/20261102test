package com.bytter.biz.inneraccount.vo.v1;

import com.bytter.system.api.dto.BtBankAccDTO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

@ApiModel("银行账户变更查询数据")
@Data
public class BtBankAccRevPageVO implements Serializable {

    @ApiModelProperty("银行账户信息")
    private BtBankAccDTO btBankAccAppPage;

    @ApiModelProperty("业务类型")
    private String busType;

    @ApiModelProperty("境内境外标识")
    private String isDomesticBankType;

    @ApiModelProperty("币别")
    private String curId;

    @ApiModelProperty("账户组名称")
    private String fcAccGroupName;

    private String bankAccDesc;

    @ApiModelProperty("查询数据来源app为账号申请表，acc为银行账号表")
    private String isFrom;

    @ApiModelProperty("是否允许变更为保证金户")
    private String isAllowedChangeDeposit;
}
