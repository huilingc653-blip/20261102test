package com.bytter.biz.inneraccount.vo.v1;


import com.bytter.biz.bdlink.api.dto.BisAccDtlDTO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

@ApiModel("查询银行账号明细详情返回参数")
@Data
public class BisAccDtlPageVO implements Serializable {

    @ApiModelProperty("银行账号明细详情信息")
    private BisAccDtlDTO bisAccDtlPage;

}
