package com.bytter.biz.receipt.vo;

import com.bytter.biz.receipt.entity.EConsignBill;
import io.swagger.annotations.ApiModel;
import lombok.Data;

@Data
@ApiModel("通知单电子回单表详情")
public class EConsignBillPage {

    private EConsignBill eConsignBillPage;
}
