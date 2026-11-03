package com.bytter.accountReceivable.docManage.vo;

import com.bytter.core.annotation.Dict;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * @Title:
 * @Description:
 * @author yangxd
 * @date 2025-03-26
 * @version 1.0
 *
 */
@Data
public class InvoiceDocVo implements Serializable {

    /**发票代码*/
    private String code;
    /**发票类型*/
    @Dict(dictGroup = "fp.type", inject = "typeName")
    private String type;
    private String typeName;
    /**发票编号*/
    private String number;
    /**票面金额*/
    private BigDecimal parAmt;
    /**不含税金额*/
    private BigDecimal taxincAmt;
    /**校验码*/
    private String checkCode;
    /**开票日期*/
    private Date draDate;
    /**挂接业务Id*/
    private String  billId;
    /**挂接业务编码*/
    private String  billNo;
}
