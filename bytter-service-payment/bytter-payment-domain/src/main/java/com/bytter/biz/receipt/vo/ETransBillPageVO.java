package com.bytter.biz.receipt.vo;

import io.swagger.annotations.ApiModel;
import lombok.Data;

@Data
@ApiModel("账户转换电子回单表详情")
public class ETransBillPageVO {

    private String opType;

    private ETransBillVO eTransBillPage;

    private EVisaTypeVO eVisaTypePage;
}
