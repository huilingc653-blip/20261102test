package com.bytter.finance.domain.provide.vo;
import com.bytter.poi.annotation.Excel;
import com.bytter.poi.annotation.ExcelTarget;
import com.bytter.poi.handler.ExcelModel;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.springframework.format.annotation.NumberFormat;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
/**
 * @author mof
 * @version 1.0
 * @Title: Vo
 * @Description: 银团明细信息
 * @date 2022-07-21 15:15:54
 */
@SuppressWarnings("serial")
@ExcelTarget("ProvideSyndicatedVo")
public class ProvideSyndicatedVo extends ExcelModel implements Serializable {
    private String id;
    /**
     * 主表id
     */
    private String directFinanceId;
    /**
     * 是否牵头行
     */
    @Excel(name = "*是否牵头行")
    private String leadingBank;
    /**
     * 银行名称
     */
    @Excel(name = "*银行名称", isImportField = "true")
    private String bankName;
    /**
     * 银行开户行号
     */
    @Excel(name = "*银行开户行号", isImportField = "true")
    private String bankCode;
    /**
     * 承贷份额%
     */
    private BigDecimal commitmentShare;
    /**
     * 承贷金额
     */
    @Excel(name = "*承贷金额", align = HorizontalAlignment.RIGHT, numFormat = "##########0.00")
    @NumberFormat(pattern = "#,##0.00")
    private BigDecimal commitmentMoney;
    /** 备注 */
    @Excel(name = "备注")
    private String rmk;
    /** 创建人登录名称 */
    private String createBy;
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date createDate;
    /** 更新人名称 */
    private String updateName;
    /** 更新人登录名称 */
    private String updateBy;
    /** 更新日期 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date updateDate;
    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }
    public String getDirectFinanceId() {
        return directFinanceId;
    }
    public void setDirectFinanceId(String directFinanceId) {
        this.directFinanceId = directFinanceId;
    }
    public String getLeadingBank() {
        return leadingBank;
    }
    public void setLeadingBank(String leadingBank) {
        this.leadingBank = leadingBank;
    }
    public String getBankName() {
        return bankName;
    }
    public void setBankName(String bankName) {
        this.bankName = bankName;
    }
    public String getBankCode() {
        return bankCode;
    }
    public void setBankCode(String bankCode) {
        this.bankCode = bankCode;
    }
    public BigDecimal getCommitmentShare() {
        return commitmentShare;
    }
    public void setCommitmentShare(BigDecimal commitmentShare) {
        this.commitmentShare = commitmentShare;
    }
    public BigDecimal getCommitmentMoney() {
        return commitmentMoney;
    }
    public void setCommitmentMoney(BigDecimal commitmentMoney) {
        this.commitmentMoney = commitmentMoney;
    }
    public String getRmk() {
        return rmk;
    }
    public void setRmk(String rmk) {
        this.rmk = rmk;
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
    public String getUpdateName() {
        return updateName;
    }
    public void setUpdateName(String updateName) {
        this.updateName = updateName;
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
    @Override
    public String getBeanName() {
        return "provideSyndicatedService";
    }
}
