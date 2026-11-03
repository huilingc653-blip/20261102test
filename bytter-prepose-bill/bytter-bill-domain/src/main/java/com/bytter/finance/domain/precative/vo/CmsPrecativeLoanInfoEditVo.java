package com.bytter.finance.domain.precative.vo;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 委托贷款修改/打回页面数据
 */
@Data
public class CmsPrecativeLoanInfoEditVo implements Serializable {

    private CmsPrecativeLoanInfoVo precativePage;
    private String endorseItem;
    private String businessName;
    private String processSysname;
    private String budgetSel;
    private Date planStartTime;
    private Date planEndTime;
}
