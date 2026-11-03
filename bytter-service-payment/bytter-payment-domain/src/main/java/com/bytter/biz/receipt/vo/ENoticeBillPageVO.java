package com.bytter.biz.receipt.vo;

import io.swagger.annotations.ApiModel;
import lombok.Data;

@Data
@ApiModel("通知单电子回单表详情")
public class ENoticeBillPageVO {


    private EVisaTypeVO eVisaTypePage;

    private ENoticeBillVO eNoticeBillPage;

    private String opType;
}
