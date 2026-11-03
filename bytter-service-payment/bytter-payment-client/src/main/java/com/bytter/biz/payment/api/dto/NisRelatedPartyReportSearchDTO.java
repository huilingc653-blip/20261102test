package com.bytter.biz.payment.api.dto;

import io.swagger.annotations.ApiModel;
import lombok.Data;

import java.io.Serializable;

/**
 *
 */
@ApiModel("NisRelatedPartyReportSearchDTO")
@Data
public class NisRelatedPartyReportSearchDTO implements Serializable {
    private String corpId;
    private String bankAcc;
    private String bankAccName;
    private String oppAccNo;
    private String oppAccName;
    private String bankTypeId;
    private String startTransTime;
    private String endTransTime;
    private String gxBillNo;
    private String remark;
    private String purpose;
    private String postscript;
    private String id;
}
