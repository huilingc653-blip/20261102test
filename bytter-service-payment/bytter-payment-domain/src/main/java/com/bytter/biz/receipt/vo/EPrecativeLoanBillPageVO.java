package com.bytter.biz.receipt.vo;

import io.swagger.annotations.ApiModel;
import lombok.Data;

@Data
@ApiModel("委托贷款电子回单表详情页")
public class EPrecativeLoanBillPageVO {

    private String opType;

    private EPrecativeLoanBillVO ePrecativeLoanBillPage;

    private EVisaTypeVO eVisaTypePage;
}
