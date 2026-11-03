package com.bytter.biz.netbank.vo;

import com.bytter.core.annotation.BtCompositeSearch;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * Description: 关联方报表
 * Company: 深圳市拜特科技股份有限公司
 * Copyright: Copyright (c) 2025
 *
 * @author bytter
 * @date 2025/10/20
 */
@Data
public class NisRelatedPartyReportVO implements Serializable {
    private Integer indexNo;
    private String id;
    @BtCompositeSearch(fieldName = "t.corpId")
    private String corpId;
    private String corpName;
    @BtCompositeSearch(fieldName = "t.bankAcc")
    private String bankAcc;
    @BtCompositeSearch(fieldName = "t.bankAccName")
    private String bankAccName;
    private String bankName;
    @BtCompositeSearch(fieldName = "t.oppAccNo")
    private String oppAccNo;
    @BtCompositeSearch(fieldName = "t.oppAccName")
    private String oppAccName;
    private String oppAccBank;
    private Date transTime;
    private String cdSign;
    @BtCompositeSearch(fieldName = "t.amt")
    private BigDecimal amt;
    @BtCompositeSearch(fieldName = "t.gxBillNo")
    private String gxBillNo;
    private String createUserName;
    @BtCompositeSearch(fieldName = "t.remark")
    private String remark;
    @BtCompositeSearch(fieldName = "t.purpose")
    private String purpose;
    @BtCompositeSearch(fieldName = "t.postscript")
    private String postscript;
    @BtCompositeSearch(fieldName = "t.bankTypeId")
    private String bankTypeId;
    @BtCompositeSearch(fieldName = "t.bankSerialNo")
    private String bankSerialNo;
}
