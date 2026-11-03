package com.bytter.biz.inneraccount.vo.v1;

import com.bytter.biz.bdlink.api.dto.BisAccDtlDTO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@ApiModel("查询银行账号明细补录详情返回参数")
@Data
public class BisAccDtlMakeupPageVO {

    @ApiModelProperty("补录详情信息")
    private BisAccDtlDTO btBankAccDtlPage;

}
