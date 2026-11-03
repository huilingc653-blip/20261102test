package com.bytter.biz.inneraccount.vo.v1;

import com.bytter.biz.inneraccount.vo.BtBankAccDtlVO;
import com.bytter.biz.inneraccount.vo.BtBankAccFreezeVO;
import com.bytter.system.api.dto.BtBankAccDTO;
import com.bytter.system.api.dto.BtBankAccFreezeDTO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

@ApiModel("银行账户流水根据查询数据")
@Data
public class BtBankAccDtlPageVO implements Serializable {

    @ApiModelProperty("银行账户流水详细信息")
    private BtBankAccDtlVO btBankAccDtlPage;

    @ApiModelProperty("币别名称")
    private String curName;

    @ApiModelProperty("接口代码名称")
    private String bifName;

    @ApiModelProperty("业务类别")
    private String busType;

    @ApiModelProperty("审批id")
    private String taskId;

}
