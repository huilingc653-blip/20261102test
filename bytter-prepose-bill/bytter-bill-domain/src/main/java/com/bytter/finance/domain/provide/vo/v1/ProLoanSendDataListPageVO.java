package com.bytter.finance.domain.provide.vo.v1;

import com.bytter.biz.bdlink.api.dto.BisAccDtlDTO;
import com.bytter.system.api.dto.BtBankAccDTO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel("查询借款发放完成的数据")
public class ProLoanSendDataListPageVO {

    @ApiModelProperty("id")
    private String id;

    @ApiModelProperty("银行账号流水")
    private BisAccDtlDTO bisDTO;

    @ApiModelProperty("账户信息")
    private BtBankAccDTO bankAccDTO;

    @ApiModelProperty("业务名称")
    private String bankTypeName;
}
