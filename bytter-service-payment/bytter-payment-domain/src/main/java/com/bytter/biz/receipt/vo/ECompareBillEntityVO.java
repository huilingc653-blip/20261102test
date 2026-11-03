package com.bytter.biz.receipt.vo;

import com.bytter.biz.receipt.entity.EAcceptBill;
import com.bytter.biz.receipt.entity.ECompareBill;
import io.swagger.annotations.ApiModel;
import lombok.Data;

@Data
@ApiModel("对账单电子回单实体类")
public class ECompareBillEntityVO {

    private ECompareBill eCompareBillPage;

}
