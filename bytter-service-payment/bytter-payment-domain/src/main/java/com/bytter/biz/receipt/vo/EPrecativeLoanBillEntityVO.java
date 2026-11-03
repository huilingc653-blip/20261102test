package com.bytter.biz.receipt.vo;

import com.bytter.biz.receipt.entity.EAcceptBill;
import com.bytter.biz.receipt.entity.EPrecativeLoanBill;
import io.swagger.annotations.ApiModel;
import lombok.Data;

@Data
@ApiModel("委托贷款电子回单表详情")
public class EPrecativeLoanBillEntityVO {

    private EPrecativeLoanBill ePrecativeLoanBillPage;

}
