package com.bytter.biz.receipt.vo;

import com.bytter.biz.receipt.entity.EAcceptBill;
import com.bytter.biz.receipt.entity.ETransBill;
import io.swagger.annotations.ApiModel;
import lombok.Data;

@Data
@ApiModel("承兑汇票电子回单表详情")
public class EAcceptBillEntityVO {

    private EAcceptBill eAcceptBillPage;

}
