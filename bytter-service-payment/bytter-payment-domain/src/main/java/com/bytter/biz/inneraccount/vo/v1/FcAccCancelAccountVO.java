package com.bytter.biz.inneraccount.vo.v1;

import com.bytter.biz.inneraccount.vo.FcCancelAccountVO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
*@Description 注销申请查看数据
*@Author zhukr
*@Date 2026/1/6
*/

@ApiModel("账号复核-注销申请查看页面所需数据")
@Data
public class FcAccCancelAccountVO implements Serializable {

    @ApiModelProperty("账号撤销相关信息")
    private FcCancelAccountVO listCancelVOPage;

    @ApiModelProperty("账务凭证分录明细记录条数")
    private Integer size;
}
