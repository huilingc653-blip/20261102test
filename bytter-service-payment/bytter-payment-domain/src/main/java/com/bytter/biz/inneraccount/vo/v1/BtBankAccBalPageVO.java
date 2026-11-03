package com.bytter.biz.inneraccount.vo.v1;

import com.bytter.biz.bdlink.api.dto.BisAccBalDTO;
import com.bytter.system.api.dto.BtBankAccAppDTO;
import com.bytter.system.api.dto.BtBankAccDTO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

@ApiModel("银行账户余额信息 查询数据")
@Data
public class BtBankAccBalPageVO implements Serializable {

    @ApiModelProperty("银行账户余额信息")
    private BisAccBalDTO bisAccBalPage;

    @ApiModelProperty("业务类型")
    private String busType;

    @ApiModelProperty("审核任务ID")
    private String taskId;
}
