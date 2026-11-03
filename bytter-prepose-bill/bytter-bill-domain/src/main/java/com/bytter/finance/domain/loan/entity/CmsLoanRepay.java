package com.bytter.finance.domain.loan.entity;
import java.math.BigDecimal;
import java.util.Date;
import java.lang.String;
import java.lang.Double;
import java.lang.Integer;
import java.math.BigDecimal;
import javax.xml.soap.Text;
import java.sql.Blob;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;
import org.hibernate.annotations.GenericGenerator;
import com.bytter.core.base.entity.IdEntity;
/**
 * @Title: Entity
 * @Description: 贷款还款表
 * @author onlineGenerator
 * @date 2017-08-03 10:35:09
 * @version 1.0
 *
 */
@Entity
@Table(name = "CMS_LOAN_REPAY", schema = "")
@SuppressWarnings("serial")
public class CmsLoanRepay extends IdEntity implements java.io.Serializable {
    private String billCode;
    /** 更新人名称 */
    private String updateName;
    /** 更新人登录名称 */
    private String updateBy;
    /** 更新日期 */
    private Date updateDate;
    /** 单位代码 */
    private String corpId;
    /** 借款合同号 */
    private String contractCode;
    /** 贷款发放单号 */
    private String sendCode;
    /** 还款金额 */
    private BigDecimal repayMoney;
    /** 还款日期 */
    private Date repayDate;
    /** 还款利息 */
    private BigDecimal repayAccrual;
    /** 记账凭证号 */
    private String voucherNo;
    /** 记账流水号 */
    private String orderNo;
    /** 借款账号 */
    private String loanAcc;
    /** 还款账号 */
    private String repayAcc;
    /** 支付方式 */
    private String payType;
    /** 附件名称 */
    private String annexName;
    /** 还款计划 */
    private String repaymentScheduleId;
    /** 还款方式 */
    private String repayway;
    /** 还款类型 */
    private String repayType;
    /** 备注 */
    private String rmk;
    /** 状态 */
    private Integer status;
    /** 业务状态 */
    private String bizStatus;
    /** 附件ID */
    private String attachmentId;
    /** 创建人名称 */
    private String createName;
    /** 创建人登录名称 */
    private String createBy;
    /** 创建日期 */
    private Date createDate;
    /** 记账日期 */
    private Date tallyDate;
    /** 起息日期 */
    private Date interestStartDate;
    /** 记账员代码 */
    private String operatorCode;
    @Column(name = "BILL_CODE")
    public String getBillCode() {
        return billCode;
    }
    public void setBillCode(String billCode) {
        this.billCode = billCode;
    }
    /**
     * 方法: 取得java.lang.String
     * 
     * @return: java.lang.String 更新人名称
     */
    @Column(name = "UPDATE_NAME")
    public String getUpdateName() {
        return this.updateName;
    }
    /**
     * 方法: 设置java.lang.String
     * 
     * @param: java.lang.String
     *             更新人名称
     */
    public void setUpdateName(String updateName) {
        this.updateName = updateName;
    }
    /**
     * 方法: 取得java.lang.String
     * 
     * @return: java.lang.String 更新人登录名称
     */
    @Column(name = "UPDATE_BY")
    public String getUpdateBy() {
        return this.updateBy;
    }
    /**
     * 方法: 设置java.lang.String
     * 
     * @param: java.lang.String
     *             更新人登录名称
     */
    public void setUpdateBy(String updateBy) {
        this.updateBy = updateBy;
    }
    /**
     * 方法: 取得java.util.Date
     * 
     * @return: java.util.Date 更新日期
     */
    @Column(name = "UPDATE_DATE")
    public Date getUpdateDate() {
        return this.updateDate;
    }
    /**
     * 方法: 设置java.util.Date
     * 
     * @param: java.util.Date
     *             更新日期
     */
    public void setUpdateDate(Date updateDate) {
        this.updateDate = updateDate;
    }
    /**
     * 方法: 取得java.lang.String
     * 
     * @return: java.lang.String 单位代码
     */
    @Column(name = "CORP_ID")
    public String getCorpId() {
        return this.corpId;
    }
    /**
     * 方法: 设置java.lang.String
     * 
     * @param: java.lang.String
     *             单位代码
     */
    public void setCorpId(String corpId) {
        this.corpId = corpId;
    }
    /**
     * 方法: 取得java.lang.String
     * 
     * @return: java.lang.String 借款合同号
     */
    @Column(name = "CONTRACT_CODE")
    public String getContractCode() {
        return this.contractCode;
    }
    /**
     * 方法: 设置java.lang.String
     * 
     * @param: java.lang.String
     *             借款合同号
     */
    public void setContractCode(String contractCode) {
        this.contractCode = contractCode;
    }
    /**
     * 方法: 取得java.lang.String
     * 
     * @return: java.lang.String 贷款发放单号
     */
    @Column(name = "SEND_CODE")
    public String getSendCode() {
        return this.sendCode;
    }
    /**
     * 方法: 设置java.lang.String
     * 
     * @param: java.lang.String
     *             贷款发放单号
     */
    public void setSendCode(String sendCode) {
        this.sendCode = sendCode;
    }
    /**
     * 方法: 取得java.math.BigDecimal
     * 
     * @return: java.math.BigDecimal 还款金额
     */
    @Column(name = "REPAY_MONEY")
    public BigDecimal getRepayMoney() {
        return this.repayMoney;
    }
    /**
     * 方法: 设置java.math.BigDecimal
     * 
     * @param: java.math.BigDecimal
     *             还款金额
     */
    public void setRepayMoney(BigDecimal repayMoney) {
        this.repayMoney = repayMoney;
    }
    /**
     * 方法: 取得java.util.Date
     * 
     * @return: java.util.Date 还款日期
     */
    @Column(name = "REPAY_DATE")
    public Date getRepayDate() {
        return this.repayDate;
    }
    /**
     * 方法: 设置java.util.Date
     * 
     * @param: java.util.Date
     *             还款日期
     */
    public void setRepayDate(Date repayDate) {
        this.repayDate = repayDate;
    }
    /**
     * 方法: 取得java.math.BigDecimal
     * 
     * @return: java.math.BigDecimal 还款利息
     */
    @Column(name = "REPAY_ACCRUAL")
    public BigDecimal getRepayAccrual() {
        return this.repayAccrual;
    }
    /**
     * 方法: 设置java.math.BigDecimal
     * 
     * @param: java.math.BigDecimal
     *             还款利息
     */
    public void setRepayAccrual(BigDecimal repayAccrual) {
        this.repayAccrual = repayAccrual;
    }
    /**
     * 方法: 取得java.lang.String
     * 
     * @return: java.lang.String 记账凭证号
     */
    @Column(name = "VOUCHER_NO")
    public String getVoucherNo() {
        return this.voucherNo;
    }
    /**
     * 方法: 设置java.lang.String
     * 
     * @param: java.lang.String
     *             记账凭证号
     */
    public void setVoucherNo(String voucherNo) {
        this.voucherNo = voucherNo;
    }
    /**
     * 方法: 取得java.lang.String
     * 
     * @return: java.lang.String 记账流水号
     */
    @Column(name = "ORDER_NO")
    public String getOrderNo() {
        return this.orderNo;
    }
    /**
     * 方法: 设置java.lang.String
     * 
     * @param: java.lang.String
     *             记账流水号
     */
    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }
    /**
     * 方法: 取得java.lang.String
     * 
     * @return: java.lang.String 借款账号
     */
    @Column(name = "LOAN_ACC")
    public String getLoanAcc() {
        return this.loanAcc;
    }
    /**
     * 方法: 设置java.lang.String
     * 
     * @param: java.lang.String
     *             借款账号
     */
    public void setLoanAcc(String loanAcc) {
        this.loanAcc = loanAcc;
    }
    /**
     * 方法: 取得java.lang.String
     * 
     * @return: java.lang.String 还款账号
     */
    @Column(name = "REPAY_ACC")
    public String getRepayAcc() {
        return this.repayAcc;
    }
    /**
     * 方法: 设置java.lang.String
     * 
     * @param: java.lang.String
     *             还款账号
     */
    public void setRepayAcc(String repayAcc) {
        this.repayAcc = repayAcc;
    }
    /**
     * 方法: 取得java.lang.String
     * 
     * @return: java.lang.String 支付方式
     */
    @Column(name = "PAY_TYPE")
    public String getPayType() {
        return this.payType;
    }
    /**
     * 方法: 设置java.lang.String
     * 
     * @param: java.lang.String
     *             支付方式
     */
    public void setPayType(String payType) {
        this.payType = payType;
    }
    /**
     * 方法: 取得java.lang.String
     * 
     * @return: java.lang.String 附件名称
     */
    @Column(name = "ANNEX_NAME")
    public String getAnnexName() {
        return this.annexName;
    }
    /**
     * 方法: 设置java.lang.String
     * 
     * @param: java.lang.String
     *             附件名称
     */
    public void setAnnexName(String annexName) {
        this.annexName = annexName;
    }
    /**
     * 方法: 取得java.lang.String
     * 
     * @return: java.lang.String 还款计划
     */
    @Column(name = "REPAYMENT_SCHEDULE_ID")
    public String getRepaymentScheduleId() {
        return this.repaymentScheduleId;
    }
    /**
     * 方法: 设置java.lang.String
     * 
     * @param: java.lang.String
     *             还款计划
     */
    public void setRepaymentScheduleId(String repaymentScheduleId) {
        this.repaymentScheduleId = repaymentScheduleId;
    }
    /**
     * 方法: 取得java.lang.String
     * 
     * @return: java.lang.String 还款方式
     */
    @Column(name = "REPAYWAY")
    public String getRepayway() {
        return this.repayway;
    }
    /**
     * 方法: 设置java.lang.String
     * 
     * @param: java.lang.String
     *             还款方式
     */
    public void setRepayway(String repayway) {
        this.repayway = repayway;
    }
    @Column(name = "REPAY_TYPE")
    public String getRepayType() {
        return repayType;
    }
    public void setRepayType(String repayType) {
        this.repayType = repayType;
    }
    /**
     * 方法: 取得java.lang.String
     * 
     * @return: java.lang.String 备注
     */
    @Column(name = "RMK")
    public String getRmk() {
        return this.rmk;
    }
    /**
     * 方法: 设置java.lang.String
     * 
     * @param: java.lang.String
     *             备注
     */
    public void setRmk(String rmk) {
        this.rmk = rmk;
    }
    /**
     * 方法: 取得java.lang.Integer
     * 
     * @return: java.lang.Integer 状态
     */
    @Column(name = "STATUS")
    public Integer getStatus() {
        return this.status;
    }
    /**
     * 方法: 设置java.lang.Integer
     * 
     * @param: java.lang.Integer
     *             状态
     */
    public void setStatus(Integer status) {
        this.status = status;
    }
    @Column(name = "BIZ_STATUS")
    public String getBizStatus() {
        return bizStatus;
    }
    public void setBizStatus(String bizStatus) {
        this.bizStatus = bizStatus;
    }
    /**
     * 方法: 取得java.lang.String
     * 
     * @return: java.lang.String 创建人名称
     */
    @Column(name = "CREATE_NAME")
    public String getCreateName() {
        return this.createName;
    }
    /**
     * 方法: 设置java.lang.String
     * 
     * @param: java.lang.String
     *             创建人名称
     */
    public void setCreateName(String createName) {
        this.createName = createName;
    }
    /**
     * 方法: 取得java.lang.String
     * 
     * @return: java.lang.String 创建人登录名称
     */
    @Column(name = "CREATE_BY")
    public String getCreateBy() {
        return this.createBy;
    }
    /**
     * 方法: 设置java.lang.String
     * 
     * @param: java.lang.String
     *             创建人登录名称
     */
    public void setCreateBy(String createBy) {
        this.createBy = createBy;
    }
    /**
     * 方法: 取得java.util.Date
     * 
     * @return: java.util.Date 创建日期
     */
    @Column(name = "CREATE_DATE")
    public Date getCreateDate() {
        return this.createDate;
    }
    /**
     * 方法: 设置java.util.Date
     * 
     * @param: java.util.Date
     *             创建日期
     */
    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }
    @Column(name = "ATTACHMENT_ID")
    public String getAttachmentId() {
        return attachmentId;
    }
    public void setAttachmentId(String attachmentId) {
        this.attachmentId = attachmentId;
    }
    @Column(name = "TALLY_DATE")
    public Date getTallyDate() {
        return tallyDate;
    }
    public void setTallyDate(Date tallyDate) {
        this.tallyDate = tallyDate;
    }
    @Column(name = "INTEREST_START_DATE")
    public Date getInterestStartDate() {
        return interestStartDate;
    }
    public void setInterestStartDate(Date interestStartDate) {
        this.interestStartDate = interestStartDate;
    }
    @Column(name = "OPERATOR_CODE")
    public String getOperatorCode() {
        return operatorCode;
    }
    public void setOperatorCode(String operatorCode) {
        this.operatorCode = operatorCode;
    }
}
