package com.bytter.finance.domain.loan.vo;

import com.bytter.credit.dto.RatUrationDTO;
import com.bytter.finance.dto.CmsLoanRepayDTO;
import com.bytter.system.api.dto.BtCurrencyDTO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

@Data
@ApiModel(value = "贷款信息账单页面VO", description = "用于返回贷款信息相关页面的数据")
public class CmsLoadInfoBillPageVO {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty("贷款信息主对象")
    private CmsLoanInfoVO cmsLoanInfoPage;

    @ApiModelProperty("贷款金额大写")
    private String loanMoneyUpper;

    @ApiModelProperty("是否计费")
    private Boolean isCharging;

    @ApiModelProperty("是否担保")
    private String isGuarantee;

    @ApiModelProperty("授信占用明细")
    private RatUrationDTO cpURationDTO;

    @ApiModelProperty("自然币种")
    private BtCurrencyDTO cur;

    @ApiModelProperty("贷款发放信息")
    private CmsLoanSendVO cmsLoanSendPage;

    @ApiModelProperty("在途金额")
    private BigDecimal inwayMoney;

    @ApiModelProperty("已还款金额")
    private BigDecimal repayedMoney;

    @ApiModelProperty("参数标志")
    private Boolean param;

    @ApiModelProperty("显示标志")
    private Integer show;

    @ApiModelProperty("贷款还款信息")
    private CmsLoanRepayDTO cmsLoanRepayPage;

    @ApiModelProperty("查看标志")
    private String viewFlag;

    @ApiModelProperty("视图类型")
    private String viewType;
}
