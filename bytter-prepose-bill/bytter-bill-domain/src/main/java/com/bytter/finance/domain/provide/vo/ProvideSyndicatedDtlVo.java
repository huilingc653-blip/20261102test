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
 * @Description: 银团贷款还款明细表
 * @date 2022-07-21 15:15:54
 */
@SuppressWarnings("serial")
@ExcelTarget("ProvideSyndicatedDtlVo")
public class ProvideSyndicatedDtlVo extends ExcelModel implements Serializable {
    /**
     * 借款主表id
     */
    private String directFinanceId;
    /**
     * 发放编号
     */
    private String billCode;
    /**
     * 还款明细id
     */
    private String scheduleId;
    /**
     * 还款明细id
     */
    private String scheduleDetailedId;
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
     * 待还款金额
     */
    private BigDecimal surplusMoney;
    /**
     * 本次还款金额
     */
    @Excel(name = "*本次还款金额", isImportField = "true", align = HorizontalAlignment.RIGHT, numFormat = "##########0.00")
    @NumberFormat(pattern = "#,##0.00")
    private BigDecimal repaymentMoney;
    /**
     * 还款本金占比%
     */
    private BigDecimal repaymentMoneyShare;
    /**
     * 还款利息
     */
    @Excel(name = "*还款利息", align = HorizontalAlignment.RIGHT, numFormat = "##########0.00")
    @NumberFormat(pattern = "#,##0.00")
    private BigDecimal repaymentIst;
    /**
     * 前端页面页数
     */
    private String rowNum;
    /**
     * 创建人登录名称
     */
    private String createBy;
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date createDate;
    /**
     * 更新人登录名称
     */
    private String updateBy;
    /**
     * 更新日期
     */
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date updateDate;
    public String getDirectFinanceId() {
        return directFinanceId;
    }
    public void setDirectFinanceId(String directFinanceId) {
        this.directFinanceId = directFinanceId;
    }
    public String getBillCode() {
        return billCode;
    }
    public void setBillCode(String billCode) {
        this.billCode = billCode;
    }
    public String getScheduleId() {
        return scheduleId;
    }
    public void setScheduleId(String scheduleId) {
        this.scheduleId = scheduleId;
    }
    public String getScheduleDetailedId() {
        return scheduleDetailedId;
    }
    public void setScheduleDetailedId(String scheduleDetailedId) {
        this.scheduleDetailedId = scheduleDetailedId;
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
    public BigDecimal getSurplusMoney() {
        return surplusMoney;
    }
    public void setSurplusMoney(BigDecimal surplusMoney) {
        this.surplusMoney = surplusMoney;
    }
    public BigDecimal getRepaymentMoney() {
        return repaymentMoney;
    }
    public void setRepaymentMoney(BigDecimal repaymentMoney) {
        this.repaymentMoney = repaymentMoney;
    }
    public BigDecimal getRepaymentMoneyShare() {
        return repaymentMoneyShare;
    }
    public void setRepaymentMoneyShare(BigDecimal repaymentMoneyShare) {
        this.repaymentMoneyShare = repaymentMoneyShare;
    }
    public BigDecimal getRepaymentIst() {
        return repaymentIst;
    }
    public void setRepaymentIst(BigDecimal repaymentIst) {
        this.repaymentIst = repaymentIst;
    }
    public String getRowNum() {
        return rowNum;
    }
    public void setRowNum(String rowNum) {
        this.rowNum = rowNum;
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
    @Override
    public String getBeanName() {
        return "provideSyndicatedDtlService";
    }
}
