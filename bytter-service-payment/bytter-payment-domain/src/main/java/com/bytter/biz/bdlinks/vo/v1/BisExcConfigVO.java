package com.bytter.biz.bdlinks.vo.v1;

import com.bytter.biz.bdlinks.vo.BisExcVO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

@Data
@ApiModel("付款指令数据查询")
public class BisExcConfigVO implements Serializable {

    @ApiModelProperty("付款指令页面信息")
    private String isMathcFailReason;

    @ApiModelProperty("开始日期")
    private String startDate;

}
