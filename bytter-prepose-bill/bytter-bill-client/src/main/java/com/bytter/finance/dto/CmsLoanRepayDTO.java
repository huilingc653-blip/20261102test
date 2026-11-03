package com.bytter.finance.dto;
import lombok.Data;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
@Data
public class CmsLoanRepayDTO implements Serializable {
    private String id;
    private String billCode;
    /**
     * 更新人名称
     */
    private String updateName;
    /**
     * 更新人登录名称
     */
    private String updateBy;
    /**
     * 更新日期
     */
    private Date updateDate;
    /**
     * 单位代码
     */
    private String corpId;
    /**
     * 借款合同号
     */
    private String contractCode;
    /**
     * 贷款发放单号
     */
    private String sendCode;
    /**
     * 还款金额
     */
    private BigDecimal repayMoney;
    /**
     * 还款日期
     */
    private Date repayDate;
    /**
     * 还款利息
     */
    private BigDecimal repayAccrual;
    /**
     * 记账凭证号
     */
    private String voucherNo;
    /**
     * 记账流水号
     */
    private String orderNo;
    /**
     * 借款账号
     */
    private String loanAcc;
    /**
     * 还款账号
     */
    private String repayAcc;
    /**
     * 支付方式
     */
    private String payType;
    /**
     * 附件名称
     */
    private String annexName;
    /**
     * 还款计划
     */
    private String repaymentScheduleId;
    /**
     * 还款方式
     */
    private String repayway;
    /**
     * 还款类型
     */
    private String repayType;
    /**
     * 备注
     */
    private String rmk;
    /**
     * 状态
     */
    private Integer status;
    /**
     * 业务状态
     */
    private String bizStatus;
    /**
     * 附件ID
     */
    private String attachmentId;
    /**
     * 创建人名称
     */
    private String createName;
    /**
     * 创建人登录名称
     */
    private String createBy;
    /**
     * 创建日期
     */
    private Date createDate;
    /**
     * 记账日期
     */
    private Date tallyDate;
    /**
     * 起息日期
     */
    private Date interestStartDate;
    /**
     * 记账员代码
     */
    private String operatorCode;
    /**
     * 待记账提醒标志
     */
    private String remindSign;
}
