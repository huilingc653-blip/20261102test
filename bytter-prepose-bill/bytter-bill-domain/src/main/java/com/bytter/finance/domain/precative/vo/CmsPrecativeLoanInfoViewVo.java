package com.bytter.finance.domain.precative.vo;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 委托贷款详情页数据
 */
@Data
public class CmsPrecativeLoanInfoViewVo implements Serializable {

    private String infoMoneyUpper;
    private Date opDate;
    private CmsPrecativeLoanInfoVo precativePage;
    private String opType;
    private String status;
    private String endorseItem;
    private String budgetSel;
    private String businessName;
    private String processSysname;
    private String dataType;
}
