package com.bytter.biz.inneraccount.vo.v1;

import com.bytter.biz.inneraccount.entity.BtBankAccVirtual;
import com.bytter.system.api.dto.BtBankAccAppDTO;
import com.bytter.system.api.dto.BtBankAccDTO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

@ApiModel("银行账户开户办理查询数据")
@Data
public class BtBankAccDelHandlePageVO implements Serializable {


    @ApiModelProperty("银行账户相关信息")
    private BtBankAccAppDTO btBankAccAppPage;

    @ApiModelProperty("业务类型")
    private String busType;

    @ApiModelProperty("审核ID 审批相关使用")
    private String taskId;
}
