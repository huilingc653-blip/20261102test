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
 * 可交债信息VO
 */
public class CmsBondSwapVO extends ExcelModel implements Serializable, IBusinessKey {
    private String id;
    /** 债劵发行表id */
    @BtCompositeSearch(fieldName = "t.issue_id")
    private String issueId;
    @Excel(name = "*债券代码", isImportField = "true")
    private String bondCode;
    /** 换股日 */
    @Excel(name = "*换股日", isImportField = "true")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date swapDate;
    /** 换股价 */
    @Excel(name = "*换股价", isImportField = "true")
    private BigDecimal swapPrice;
    /** 换股数量 */
    @Excel(name = "*换股数量", isImportField = "true")
    private Integer swapAmount;
    /** 累计换股数量 */
    @Excel(name = "*累计换股数量", isImportField = "true")
    private Integer totalSwapAmount;
    /** 累计转股金额 */
    @Excel(name = "*累计换股金额", isImportField = "true")
    private BigDecimal totalSwapMoney;
    /** 交债状态 */
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
    private String saveType;

    public String getSaveType() {
        return saveType;
    }

    public void setSaveType(String saveType) {
        this.saveType = saveType;
    }

    @Override
    public String getBeanName() {
        return "cmsBondSwapService";
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
    public Date getSwapDate() {
        return swapDate;
    }
    public void setSwapDate(Date swapDate) {
        this.swapDate = swapDate;
    }
    public BigDecimal getSwapPrice() {
        return swapPrice;
    }
    public void setSwapPrice(BigDecimal swapPrice) {
        this.swapPrice = swapPrice;
    }
    public Integer getSwapAmount() {
        return swapAmount;
    }
    public void setSwapAmount(Integer swapAmount) {
        this.swapAmount = swapAmount;
    }
    public Integer getTotalSwapAmount() {
        return totalSwapAmount;
    }
    public void setTotalSwapAmount(Integer totalSwapAmount) {
        this.totalSwapAmount = totalSwapAmount;
    }
    public BigDecimal getTotalSwapMoney() {
        return totalSwapMoney;
    }
    public void setTotalSwapMoney(BigDecimal totalSwapMoney) {
        this.totalSwapMoney = totalSwapMoney;
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
