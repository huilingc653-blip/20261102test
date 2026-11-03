package com.bytter.biz.receipt.vo;

import com.bytter.biz.receipt.entity.ELoanBill;
import io.swagger.annotations.ApiModel;
import lombok.Data;

@Data
@ApiModel("贷款电子回单表")
public class ELoanBillEntityVO {

    private ELoanBill eLoanBillPage;

}
