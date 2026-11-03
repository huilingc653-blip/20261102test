package com.bytter.increment.domain.vo.v1;

import com.bytter.increment.domain.vo.FcAgreedDepositVO;
import com.bytter.system.api.dto.FcAccDTO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@ApiModel("协定存款修改登记数据")
@Data
public class FcAgreedDepositUpdateRegVO implements Serializable {

    @ApiModelProperty("协定存款数据")
    private FcAgreedDepositVO fcAgreedDepositPage;

    @ApiModelProperty("日结日期")
    private Date closeDate;

    @ApiModelProperty("账户信息")
    private FcAccDTO fcAccPage;
}
