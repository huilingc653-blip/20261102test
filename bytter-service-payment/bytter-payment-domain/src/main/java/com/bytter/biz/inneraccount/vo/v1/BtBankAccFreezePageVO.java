package com.bytter.biz.inneraccount.vo.v1;

import com.bytter.biz.inneraccount.vo.BtBankAccFreezeVO;
import com.bytter.system.api.dto.BtBankAccDTO;
import com.bytter.system.api.dto.BtBankAccFreezeDTO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

@ApiModel("银行账户冻结查询数据")
@Data
public class BtBankAccFreezePageVO implements Serializable {

    @ApiModelProperty("银行账户信息")
    private BtBankAccDTO accVO;

    @ApiModelProperty("银行账户信息")
    private BtBankAccFreezeVO btBankAccFreezeVO;

    @ApiModelProperty("冻结金额")
    private BigDecimal freezeMoney;

    @ApiModelProperty("冻结原因")
    private String freezeRession;

    @ApiModelProperty("审批环节使用的 taskId")
    private String taskId;

    @ApiModelProperty("mobileApprove使用参数")
    private BtBankAccFreezeDTO freeze;
}
