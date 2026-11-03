package com.bytter.biz.receipt.vo;

import com.bytter.biz.receipt.entity.EPoundageBill;
import com.bytter.biz.receipt.entity.ETransBill;
import io.swagger.annotations.ApiModel;
import lombok.Data;

@Data
@ApiModel("账户转换电子回单表详情")
public class ETransBillEntityVO {

    private ETransBill ePoundageBillPage;

}
