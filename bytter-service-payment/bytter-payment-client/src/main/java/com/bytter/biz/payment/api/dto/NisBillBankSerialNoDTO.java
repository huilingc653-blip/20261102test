package com.bytter.biz.payment.api.dto;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
public class NisBillBankSerialNoDTO implements Serializable {
    private String billId;//	付款单id
    private String bankSerialNo;//	银行流水号
    private String isFrom;//	来源
    private String dtlId;//明细id
    private Date paymentblendingDate;//勾对日期

}
