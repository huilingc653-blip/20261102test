package com.bytter.finance.domain.precative.vo;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 委托贷款补录页面数据
 */
@Data
public class CmsPrecativeLoanInfoRepairVo implements Serializable {

    private String endorseItem;
    private String businessName;
    private String processSysname;
    private Date opDate;
    private CmsPrecativeLoanInfoVo precativePage;
    private Boolean isNatualCurrency;
    private String conType;
    private String conId;
    private String relateCon;
}
