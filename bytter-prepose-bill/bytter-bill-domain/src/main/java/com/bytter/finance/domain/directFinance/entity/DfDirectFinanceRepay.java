package com.bytter.finance.domain.directFinance.entity;
import com.bytter.core.base.entity.IdEntity;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
/**
 * @Title
 * @Description
 * @Author liusy
 * @Date 2019-12-10 10:03
 * @Version 1.0
 */
@Entity
@Table(name = "DF_DIRECT_FINANCE_REPAY", schema = "")
@SuppressWarnings("serial")
public class DfDirectFinanceRepay extends IdEntity implements Serializable {
    /**
     * 流水号
     */
    private String billCode;
    /**
     * 主表id
     */
    private String directFinanceId;
    /**
     * 状态
     */
    private Integer status;
    /**
     * 还款日期
     */
    private Date repayDate;
    /**
     * 还款日期
     */
    private BigDecimal repayMoney;
    /**
     * 利息
     */
    private BigDecimal interest;
    /**
     * 手续费
     */
    private BigDecimal serviceCharge;
    /**
     * 附件id
     */
    private String uploadId;
    /**
     * 单位id
     */
    private String corpId;
    /**
     *
     */
    private String createBy;
    /**
     *
     */
    private Date createDate;
    /**
     *
     */
    private String updateBy;
    /**
     *
     */
    private Date updateDate;
    /**
     * 备注
     */
    private String rmk;
    @Column(name = "BILL_CODE")
    public String getBillCode() {
        return billCode;
    }
    public void setBillCode(String billCode) {
        this.billCode = billCode;
    }
    @Column(name = "DIRECT_FINANCE_ID")
    public String getDirectFinanceId() {
        return directFinanceId;
    }
    public void setDirectFinanceId(String directFinanceId) {
        this.directFinanceId = directFinanceId;
    }
    @Column(name = "status")
    public Integer getStatus() {
        return status;
    }
    public void setStatus(Integer status) {
        this.status = status;
    }
    @Column(name = "REPAY_DATE")
    public Date getRepayDate() {
        return repayDate;
    }
    public void setRepayDate(Date repayDate) {
        this.repayDate = repayDate;
    }
    @Column(name = "REPAY_MONEY")
    public BigDecimal getRepayMoney() {
        return repayMoney;
    }
    public void setRepayMoney(BigDecimal repayMoney) {
        this.repayMoney = repayMoney;
    }
    @Column(name = "INTEREST")
    public BigDecimal getInterest() {
        return interest;
    }
    public void setInterest(BigDecimal interest) {
        this.interest = interest;
    }
    @Column(name = "SERVICE_CHARGE")
    public BigDecimal getServiceCharge() {
        return serviceCharge;
    }
    public void setServiceCharge(BigDecimal serviceCharge) {
        this.serviceCharge = serviceCharge;
    }
    @Column(name = "upload_id")
    public String getUploadId() {
        return uploadId;
    }
    public void setUploadId(String uploadId) {
        this.uploadId = uploadId;
    }
    @Column(name = "corp_id")
    public String getCorpId() {
        return corpId;
    }
    public void setCorpId(String corpId) {
        this.corpId = corpId;
    }
    @Column(name = "CREATE_BY")
    public String getCreateBy() {
        return createBy;
    }
    public void setCreateBy(String createBy) {
        this.createBy = createBy;
    }
    @Column(name = "CREATE_DATE")
    public Date getCreateDate() {
        return createDate;
    }
    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }
    @Column(name = "UPDATE_BY")
    public String getUpdateBy() {
        return updateBy;
    }
    public void setUpdateBy(String updateBy) {
        this.updateBy = updateBy;
    }
    @Column(name = "UPDATE_DATE")
    public Date getUpdateDate() {
        return updateDate;
    }
    public void setUpdateDate(Date updateDate) {
        this.updateDate = updateDate;
    }
    @Column(name = "RMK")
    public String getRmk() {
        return rmk;
    }
    public void setRmk(String rmk) {
        this.rmk = rmk;
    }
}
