package com.bytter.bill.domain.bill.vo.v1;

import com.bytter.finance.domain.loan.entity.CmsLoanSend;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 添加贷款发放 参数
 */
@Data
public class CmsLoanSendSaveVO implements Serializable {
  private  String sendBillCode;
  private String billCode;
  /** 发放合同 */
  private String contractCode;
  /** 贷款信息编号 */
  private String loanCode;
  /** 单位代码 */
  private String corpId;
  /** 发放金额 */
  private BigDecimal loanMoney;
  /** 贷款账号 */
  private String loanAcc;
  /** 发放日期 */
  private Date loanDate;
  /** 还款日期 */
  private Date repayDate;
  /** 放款利率 */
  private BigDecimal loanRate;
  /** 放款账号 */
  private String provideLoanAcc;
  /** 放款币种 */
  private String curCode;
  /** 逾期利率 */
  private BigDecimal overdueRate;
  /** 展期利率 */
  private BigDecimal deferredRate;
  /** 增值税税率 */
  private BigDecimal texRate;
  /** 往来账号 */
  private String netAcc;
  /** 状态 */
  private Integer status;
  /** 业务状态 */
  private String bizStatus;
  /** 记账凭证号 */
  private String voucherNo;
  /** 记账流水号 */
  private String orderNo;
  /** 记账跨中心凭证号 */
  private String voucherNo1;
  /** 记账跨中心流水号 */
  private String orderNo1;
  /** 打回原因 */
  private String refuseWhy;
  /** 贷款发放方式 */
  private String loanSendOutType;
  /** 浮动方式 */
  private String floatWay;
  /** 点数或浮动比例 */
  private String pointsProportionText;
  /** 点数或者比例 */
  private String pointsProportionSelect;
  /** 利率调整生效日期 */
  private Date rateEntriesEffectDate;
  /** 计息方式 */
  private String interestWay;
  /** 基准利率类型 */
  private String referenceRateType;
  /** 贷款周期 */
  private String repayCycle;
  /** 利率复核生效日期 */
  private Date ratetimeoftakeeffect;
  /** 支付方式 */
  private String payType;
  /** 发放次数 */
  private Integer sendTime;
  /** 借款单位 */
  private String borrowCorpId;
  /** 还款方式 */
  private String repayType;
  /** 协定费率 */
  private BigDecimal clbAgreementRate;
  /** 固定费率 */
  private BigDecimal clbFixedRate;
  /** 借贷关联的借款单号 */
  private String ploadcode;
  /** 附件ID */
  private String attachmentId;
  /** 创建人名称 */
  private String createName;
  /** 创建人登录名称 */
  private String createBy;
  /** 创建日期 */
  private Date createDate;
  /** 更新人名称 */
  private String updateName;
  /** 更新人登录名称 */
  private String updateBy;
  /** 更新日期 */
  private Date updateDate;
  /** 展期到期日期 */
  private Date deferredDate;
  /** 发送标识 */
  private String sendSign;
  /** 还款计划Id */
  private String planId;
  /** 贷款账户名称 */
  private String accName;
  /** 放款账户名称 */
  private String interestAccName;
  /** 记账员代码 */
  private String operatorCode;
}
