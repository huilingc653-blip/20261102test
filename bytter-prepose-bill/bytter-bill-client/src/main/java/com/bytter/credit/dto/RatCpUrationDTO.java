package com.bytter.credit.dto;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
/**
 * 企业授信占用DTO
 *
 */
public class RatCpUrationDTO implements Serializable {
    /**
     * 占用ID，可回写给业务表
     */
    private String cpUrationId;
    /**
     * 单位ID(必填)
     */
    private String corpId;
    /**
     * 授信编号(必填)
     */
    private String cpRatCode;
    /**
     * 银行编号
     */
    private String bankCode;
    /**
     * 授信品种(必填)
     */
    private String cpRatItemCode;
    private String cpRatItemName;
    /**
     * 系统授信品种类型(必填)
     */
    private String sysCode;
    /**
     * 业务模块代码(必填)
     */
    private String businessName;
    /**
     * 业务币别(必填)
     */
    private String curCode;
    /**
     * 合同号(手工占用时必填，外部挂接时选填)
     */
    private String contactNo;
    /**
     * 业务ID(必填)
     */
    private String billId;
    /**
     * 业务单号(选填)
     */
    private String billNo;
    /**
     * 占用额度(必填)
     */
    private BigDecimal money;
    /**
     * 开始日期(必填)
     */
    private Date beginDate;
    /**
     * 结束日期(必填)
     */
    private Date endDate;
    /**
     * 占用明细 如果占用单项，不必填写，需填写itemCode 如果占用综合授信，则明细必须填写
     */
    private List<RatUrationDtlDTO> applyDtls;
    /**
     * 释放标识
     */
    private boolean releaseFlag = false;
    /**
     * 是否全部释放
     */
    private boolean allRelease = false;
    /**
     * 获取操作用户
     */
    private String userId;
    /** 业务品种ID */
    private String billName;
    /** 业务品种名称 */
    private String raceName;
    /**
     * 业务金额（必填）
     */
    private BigDecimal billMoney;
    /**
     * 业务币种（手工占用时用此字段）
     */
    private String initCurCode;
    /**
     * 状态
     */
    private Integer status;
    public String getCpUrationId() {
        return cpUrationId;
    }
    public void setCpUrationId(String cpUrationId) {
        this.cpUrationId = cpUrationId;
    }
    public String getCorpId() {
        return corpId;
    }
    public void setCorpId(String corpId) {
        this.corpId = corpId;
    }
    public String getSysCode() {
        return sysCode;
    }
    public void setSysCode(String sysCode) {
        this.sysCode = sysCode;
    }
    public String getBusinessName() {
        return businessName;
    }
    public void setBusinessName(String businessName) {
        this.businessName = businessName;
    }
    public String getCurCode() {
        return curCode;
    }
    public void setCurCode(String curCode) {
        this.curCode = curCode;
    }
    public String getContactNo() {
        return contactNo;
    }
    public void setContactNo(String contactNo) {
        this.contactNo = contactNo;
    }
    public String getBillId() {
        return billId;
    }
    public void setBillId(String billId) {
        this.billId = billId;
    }
    public String getBillNo() {
        return billNo;
    }
    public void setBillNo(String billNo) {
        this.billNo = billNo;
    }
    public BigDecimal getMoney() {
        return money;
    }
    public void setMoney(BigDecimal money) {
        this.money = money;
    }
    public Date getBeginDate() {
        return beginDate;
    }
    public void setBeginDate(Date beginDate) {
        this.beginDate = beginDate;
    }
    public Date getEndDate() {
        return endDate;
    }
    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }
    public List<RatUrationDtlDTO> getApplyDtls() {
        return applyDtls;
    }
    public void setApplyDtls(List<RatUrationDtlDTO> applyDtls) {
        this.applyDtls = applyDtls;
    }
    public String getUserId() {
        return userId;
    }
    public void setUserId(String userId) {
        this.userId = userId;
    }
    public RatCpUrationDTO() {
    }
    public RatCpUrationDTO(String sysCode, String billId) {
        super();
        this.sysCode = sysCode;
        this.billId = billId;
    }
    public boolean isReleaseFlag() {
        return releaseFlag;
    }
    public void setReleaseFlag(boolean releaseFlag) {
        this.releaseFlag = releaseFlag;
    }
    public boolean isAllRelease() {
        return allRelease;
    }
    public void setAllRelease(boolean allRelease) {
        this.allRelease = allRelease;
    }
    public String getBankCode() {
        return bankCode;
    }
    public void setBankCode(String bankCode) {
        this.bankCode = bankCode;
    }
    public BigDecimal getBillMoney() {
        return billMoney;
    }
    public void setBillMoney(BigDecimal billMoney) {
        this.billMoney = billMoney;
    }
    public String getBillName() {
        return billName;
    }
    public void setBillName(String billName) {
        this.billName = billName;
    }
    public String getRaceName() {
        return raceName;
    }
    public void setRaceName(String raceName) {
        this.raceName = raceName;
    }
    public String getInitCurCode() {
        return initCurCode;
    }
    public void setInitCurCode(String initCurCode) {
        this.initCurCode = initCurCode;
    }
    public Integer getStatus() {
        return status;
    }
    public void setStatus(Integer status) {
        this.status = status;
    }
    public String getCpRatCode() {
        return cpRatCode;
    }
    public void setCpRatCode(String cpRatCode) {
        this.cpRatCode = cpRatCode;
    }
    public String getCpRatItemCode() {
        return cpRatItemCode;
    }
    public void setCpRatItemCode(String cpRatItemCode) {
        this.cpRatItemCode = cpRatItemCode;
    }
    public String getCpRatItemName() {
        return cpRatItemName;
    }
    public void setCpRatItemName(String cpRatItemName) {
        this.cpRatItemName = cpRatItemName;
    }
}
