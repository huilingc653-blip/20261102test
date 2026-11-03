package com.bytter.biz.inneraccount.vo.v1;

import com.bytter.system.api.dto.BtBankAccAppDTO;
import com.bytter.system.api.dto.BtBankAccDTO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 *@Description 银行账户 查询数据
 *@Author zhukr
 *@Date 2026/1/14
 */

@ApiModel("银行账户 审核查询数据")
@Data
public class BtBankAccApproveViewVO implements Serializable {

    @ApiModelProperty("账户信息")
    private BtBankAccDTO btBankAcc;

    @ApiModelProperty("账户申请信息")
    private BtBankAccAppDTO btBankAccAppPage;

    @ApiModelProperty("业务类型")
    private String busType;

    @ApiModelProperty("审核任务ID")
    private String taskId;
}
