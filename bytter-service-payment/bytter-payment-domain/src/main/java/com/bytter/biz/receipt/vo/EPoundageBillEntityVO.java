package com.bytter.biz.receipt.vo;

import com.bytter.biz.receipt.entity.EPoundageBill;
import io.swagger.annotations.ApiModel;
import lombok.Data;

@Data
@ApiModel("手续费电子回单表详情")
public class EPoundageBillEntityVO {

    private EPoundageBill ePoundageBillPage;

}
