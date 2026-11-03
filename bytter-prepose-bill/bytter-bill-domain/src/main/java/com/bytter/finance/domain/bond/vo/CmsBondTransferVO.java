package com.bytter.finance.domain.bond.vo;
import com.bytter.core.annotation.BtCompositeSearch;
import com.bytter.core.annotation.Dict;
import com.bytter.core.bpm.IBusinessKey;
import com.bytter.poi.annotation.Excel;
import com.bytter.poi.handler.ExcelModel;
import com.fasterxml.jackson.annotation.JsonFormat;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
/**
 * 可转债信息VO
 */
public class CmsBondTransferVO extends ExcelModel implements Serializable, IBusinessKey {
    private String id;
    /** 债劵发行表id */
    @BtCompositeSearch(fieldName = "t.issue_id")
    private String issueId;
    @Excel(name = "*债券代码", isImportField = "true")
    private String bondCode;
    /** 转股日 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "*转股日", isImportField = "true")
    private Date transferDate;
    /** 转股价 */
    @Excel(name = "*转股价", isImportField = "true")
    private BigDecimal transferPrice;
    /** 转股数量 */
    @Excel(name = "*转股数量", isImportField = "true")
    private Integer transferAmount;
    /** 累计转股数量 */
    @Excel(name = "*累计转股数量", isImportField = "true")
    private Integer totalTransferAmount;
    /** 累计转股金额 */
    @Excel(name = "*累计转股金额", isImportField = "true")
    private BigDecimal totalTransferMoney;
    /** 累计转股比例 */
    @Excel(name = "*累计转股比例", isImportField = "true")
    private BigDecimal totalTransferRate;
    /** 转债状态 */
    @Dict(dictGroup = "AB_BILL_EMITBILL_STATUS", inject = "statusName")
    private Integer status;
    /**
     * 状态名
     */
    private String statusName;
    /** 创建人登录名称 */
    private String createBy;
    /** 创建日期 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date createDate;
    /** 更新人登录名称 */
    private String updateBy;
    /** 更新日期 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date updateDate;
    @Override
    public String getBeanName() {
        return "cmsBondTransferService";
    }
    @Override
    public String getBusinessKey() {
        return id;
    }
    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }
    public String getIssueId() {
        return issueId;
    }
    public void setIssueId(String issueId) {
        this.issueId = issueId;
    }
    public Date getTransferDate() {
        return transferDate;
    }
    public void setTransferDate(Date transferDate) {
        this.transferDate = transferDate;
    }
    public BigDecimal getTransferPrice() {
        return transferPrice;
    }
    public void setTransferPrice(BigDecimal transferPrice) {
        this.transferPrice = transferPrice;
    }
    public Integer getTransferAmount() {
        return transferAmount;
    }
    public void setTransferAmount(Integer transferAmount) {
        this.transferAmount = transferAmount;
    }
    public Integer getTotalTransferAmount() {
        return totalTransferAmount;
    }
    public void setTotalTransferAmount(Integer totalTransferAmount) {
        this.totalTransferAmount = totalTransferAmount;
    }
    public BigDecimal getTotalTransferMoney() {
        return totalTransferMoney;
    }
    public void setTotalTransferMoney(BigDecimal totalTransferMoney) {
        this.totalTransferMoney = totalTransferMoney;
    }
    public BigDecimal getTotalTransferRate() {
        return totalTransferRate;
    }
    public void setTotalTransferRate(BigDecimal totalTransferRate) {
        this.totalTransferRate = totalTransferRate;
    }
    public Integer getStatus() {
        return status;
    }
    public void setStatus(Integer status) {
        this.status = status;
    }
    public String getCreateBy() {
        return createBy;
    }
    public void setCreateBy(String createBy) {
        this.createBy = createBy;
    }
    public Date getCreateDate() {
        return createDate;
    }
    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }
    public String getUpdateBy() {
        return updateBy;
    }
    public void setUpdateBy(String updateBy) {
        this.updateBy = updateBy;
    }
    public Date getUpdateDate() {
        return updateDate;
    }
    public void setUpdateDate(Date updateDate) {
        this.updateDate = updateDate;
    }
    public String getStatusName() {
        return statusName;
    }
    public void setStatusName(String statusName) {
        this.statusName = statusName;
    }
    public String getBondCode() {
        return bondCode;
    }
    public void setBondCode(String bondCode) {
        this.bondCode = bondCode;
    }
}
