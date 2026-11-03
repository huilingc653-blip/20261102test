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
 * @Description: 银团贷款发放表
 * @date 2022-07-21 15:15:54
 */
@SuppressWarnings("serial")
@ExcelTarget("ProvideSyndicatedSendVo")
public class ProvideSyndicatedSendVo extends ExcelModel implements Serializable {
    /**
     * 借款主表id
     */
    private String directFinanceId;
    /**
     * 发放表id
     */
    private String provideLoanSendId;
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
     * 未发放金额
     */
    private BigDecimal notsendMoney;
    /**
     * 本次放款
     */
    @Excel(name = "*已放款金额", align = HorizontalAlignment.RIGHT, numFormat = "##########0.00")
    @NumberFormat(pattern = "#,##0.00")
    private BigDecimal sendMoney;
    /** 放款占比% */
    private BigDecimal sendMoneyShare;
    /** 创建人登录名称 */
    private String createBy;
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date createDate;
    /** 更新人登录名称 */
    private String updateBy;
    /** 更新日期 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date updateDate;
    public String getDirectFinanceId() {
        return directFinanceId;
    }
    public void setDirectFinanceId(String directFinanceId) {
        this.directFinanceId = directFinanceId;
    }
    public String getProvideLoanSendId() {
        return provideLoanSendId;
    }
    public void setProvideLoanSendId(String provideLoanSendId) {
        this.provideLoanSendId = provideLoanSendId;
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
    public BigDecimal getNotsendMoney() {
        return notsendMoney;
    }
    public void setNotsendMoney(BigDecimal notsendMoney) {
        this.notsendMoney = notsendMoney;
    }
    public BigDecimal getSendMoney() {
        return sendMoney;
    }
    public void setSendMoney(BigDecimal sendMoney) {
        this.sendMoney = sendMoney;
    }
    public BigDecimal getSendMoneyShare() {
        return sendMoneyShare;
    }
    public void setSendMoneyShare(BigDecimal sendMoneyShare) {
        this.sendMoneyShare = sendMoneyShare;
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
        return "provideSyndicatedSendService";
    }
}
