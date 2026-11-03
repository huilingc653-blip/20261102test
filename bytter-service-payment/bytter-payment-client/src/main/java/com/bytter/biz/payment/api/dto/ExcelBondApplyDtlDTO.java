package com.bytter.biz.payment.api.dto;

import lombok.Data;

@Data
public class ExcelBondApplyDtlDTO {

    private String bondId;

    private String status;

    private String payBillNo;

    private String bursarBillNo;

    private String origNo;

    private String startDate;

    private String endDate;
}
