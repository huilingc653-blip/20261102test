package com.bytter.increment.domain.vo.v1;

import com.bytter.increment.domain.vo.FcAgreedDepositVO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

@ApiModel("协定存款登记查询数据")
@Data
public class FcAgreedDepositPageVO implements Serializable {

    @ApiModelProperty("主键ID")
    private String id;

    @ApiModelProperty("日期")
    private String dateStr;

    @ApiModelProperty("操作类型")
    private String opType;

    @ApiModelProperty("业务类型")
    private String busType;

    @ApiModelProperty("协定存款数据")
    private FcAgreedDepositVO fcAgreedDepositPage;

}
