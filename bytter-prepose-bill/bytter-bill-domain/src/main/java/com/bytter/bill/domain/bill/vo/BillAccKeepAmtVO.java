package com.bytter.bill.domain.bill.vo;

import com.bytter.poi.annotation.Excel;
import com.bytter.poi.handler.ExcelModel;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
 * Description: 银承账号留存金额
 * Company: 深圳市拜特科技股份有限公司
 * Copyright: Copyright (c) 2025
 *
 * @author ZRS
 * @date 2025/3/26
 */
@Data
public class BillAccKeepAmtVO extends ExcelModel implements java.io.Serializable{
    private static final long serialVersionUID = 1L;
    /** ID */
    private String id;
    /** 单位ID */
    private String corpId;

    /** 单位名称 */
    private String corpName;

    /** 账号 */
    @Excel(name = "*银行账号")
    private String bankAcc;

    /** 户名 */
    private String accName;

    /** 银行类型 */
    private String bankType;

    /** 银行类型名称 */
    private String bankTypeName;

    /** 开户行号 */
    private String bankCode;

    /** 开户行名 */
    private String bankName;

    /** 留存金额 */
    @Excel(name = "*留底金额")
    private BigDecimal keepAmt;
    private BigDecimal keepAmtStr;

    /** 状态：1正常；0禁用 */
    private String status;

    private Date createDate;

    private String createBy;

    private Date updateDate;

    private String updateBy;

    private String ids;

    public String getBeanName() {
        return "billAccKeepAmtService";
    }
}
