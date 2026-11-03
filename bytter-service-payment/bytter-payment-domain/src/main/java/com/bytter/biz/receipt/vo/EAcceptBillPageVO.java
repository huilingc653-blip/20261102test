package com.bytter.biz.receipt.vo;

import com.bytter.biz.receipt.entity.EAcceptBill;
import io.swagger.annotations.ApiModel;
import lombok.Data;

@Data
@ApiModel("承兑汇票电子回单表详情")
public class EAcceptBillPageVO {

    private String opType;

    private EAcceptBillVO eAcceptBillPage;

    private EVisaTypeVO eVisaTypePage;
}
