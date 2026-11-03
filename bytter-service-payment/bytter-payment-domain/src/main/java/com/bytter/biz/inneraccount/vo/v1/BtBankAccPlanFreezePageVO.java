package com.bytter.biz.inneraccount.vo.v1;

import com.bytter.biz.inneraccount.vo.BtBankAccFreezeVO;
import com.bytter.biz.inneraccount.vo.BtBankAccPlanFreezeVO;
import com.bytter.system.api.dto.BtBankAccDTO;
import com.bytter.system.api.dto.BtBankAccFreezeDTO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

@ApiModel("银行账户计划冻结查询数据")
@Data
public class BtBankAccPlanFreezePageVO implements Serializable {

    @ApiModelProperty("银行账户信息")
    private BtBankAccDTO accVO;

    @ApiModelProperty("账户冻结申请信息")
    private BtBankAccPlanFreezeVO freezeVO;
}
