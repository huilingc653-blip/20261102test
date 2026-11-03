package com.bytter.increment.domain.vo.v1;

import com.bytter.increment.domain.vo.BisFinanceExcVO;
import com.bytter.increment.domain.vo.FcAgreedDepositVO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

@ApiModel("银行理财指令详情页")
@Data
public class BisFinanceExcPageVO implements Serializable {

    @ApiModelProperty("主键ID")
    private BisFinanceExcVO bisFinanceExcPage;

    @ApiModelProperty("业务类型")
    private String busType;

}
