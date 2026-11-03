package com.bytter.system.vo.v1;

import com.bytter.system.vo.BtNetUKeyVO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

@ApiModel("网银信息维护")
@Data
public class BtNetUKeyPageVO implements Serializable {

    @ApiModelProperty("获取网银信息")
    private BtNetUKeyVO vo;

    @ApiModelProperty("银行账户")
    private String bankAcc;
}
