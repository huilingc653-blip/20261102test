package com.bytter.finance.domain.precative.vo;

import lombok.Data;

import java.io.Serializable;

/**
 * 手机审核页面数据
 */
@Data
public class CmsPrecativeLoanMobileApproveVo implements Serializable {

    private CmsPrecativeLoanInfoVo precativePage;
    private String loanMoneyUpper;
    private String taskId;
    private String pageType;
}
