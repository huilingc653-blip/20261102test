package com.bytter.check.vo;

import com.bytter.check.reconcile.vo.BisCheckReasonVO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

@ApiModel("银企对账 对账原因维护数据信息")
@Data
public class BisCheckReasonPageVO implements Serializable {

    @ApiModelProperty("对账原因维护详细数据")
    private BisCheckReasonVO checkReasonVo;

}
