package com.bytter.bill.domain.bill.vo.v1;

import com.bytter.credit.dto.RatUrationDTO;
import com.bytter.finance.domain.loan.vo.CmsLoanInfoVO;
import com.bytter.finance.domain.loan.vo.CmsLoanSendVO;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class CmsLoanSendDetailVO {
    private RatUrationDTO cpURationDTO;
    private String isGuarante;
    private CmsLoanSendVO cmsLoanSendPage;
    private BigDecimal inwayMoney;// 在途金额
    private BigDecimal repayedMoney;// 已还款金额
    private Boolean isCharging;
    private String taskId;
    private CmsLoanInfoVO cmsLoanInfo;
    private String loanMoneyUpper;

    private Boolean param;
    private CmsLoanInfoVO cmsLoanInfoPage;
    private Integer   show;
    private String    viewFlag;
    private String forwardUrl;
    private String viewType;
}
