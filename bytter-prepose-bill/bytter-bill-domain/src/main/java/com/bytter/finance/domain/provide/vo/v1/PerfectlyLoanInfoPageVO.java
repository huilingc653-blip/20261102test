package com.bytter.finance.domain.provide.vo.v1;

import com.bytter.credit.dto.RatUrationDTO;
import com.bytter.finance.domain.basedata.entity.RepayType;
import com.bytter.finance.domain.directFinance.vo.DfIntermediateCostVO;
import com.bytter.finance.domain.provide.entity.ProvideLoanSend;
import com.bytter.finance.domain.provide.vo.LoanInfoVO;
import com.bytter.finance.domain.provide.vo.LoanSendVO;
import com.bytter.guarantee.dto.GuaContractUseDTO;
import com.bytter.system.api.dto.BtCurrencyDTO;
import com.bytter.system.api.dto.ConfigDTO;
import io.swagger.annotations.ApiModel;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@Data
@ApiModel("查看银行借款详情")
public class PerfectlyLoanInfoPageVO {

    private LoanInfoVO loanInfo;

    private Date ProcessDate;

    private String businessName;

    private String budgetSel;

    private String sendItem;

    private String processSysname;

    private List<DfIntermediateCostVO> costVOS;

    private LoanSendVO loanSend;

    private String sendBillCode;

    private BigDecimal repayPassage;

    private BigDecimal repayFinish;

    private String show;

    private String relateRat;

    private String ratBankConfig;

    private String isOpenRat;

    private RatUrationDTO urationDTO;

    private String ratBankCode;

    private BtCurrencyDTO cur;

    private String contractIds;

    private List<String> idList;

    private String guaranteeRmk;

    private String guaWayNames;

    private String guaWay;

    private String contractCode;

    private String viewFlag;

    private String isGuarantee;

    private GuaContractUseDTO guaContractUseDTO;

    private String isHangGuarantee;

    private RepayType repayType;

    private String planId;
    private String planStatus;
    private String type;

    private String closeDay;

    private ProvideLoanSend send;

    private String baseCurId;

    private BigDecimal waitRepayMoney;

    private String naturalSign;

    private String minDate;

    private String    taskId;


}
