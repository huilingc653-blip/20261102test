package com.bytter.finance.domain.precative.vo;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 委托贷款录入页面配置数据
 */
@Data
public class CmsPrecativeLoanInfoPageVo implements Serializable {

    private String budgetSel;
    private String endorseItem;
    private Date opDate;
    private String type;
    private Date planStartTime;
    private Date planEndTime;
}
