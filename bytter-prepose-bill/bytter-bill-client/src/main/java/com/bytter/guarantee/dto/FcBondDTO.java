package com.bytter.guarantee.dto;
import com.bytter.core.annotation.Corp;
import com.bytter.core.annotation.Currency;
import com.bytter.core.annotation.Dict;
import com.fasterxml.jackson.annotation.JsonFormat;
import java.io.Serializable;
import java.math.BigDecimal;
/**
 * Title: FcBondDTO Description: TODO
 * 
 * @author luxy
 * @since 2019/4/15 17:01
 */
public class FcBondDTO implements Serializable {
    private String id;
    /** 业务单号 */
    private String billNo;
    /** 单位ID */
    @Corp(inject = "corpName")
    private String corpId;
    /** 单位名称 */
    private String corpName;
    /** 银行账号ID */
    private String bankAccId;
    /** 存入银行 */
    private String bankName;
    /** 存入账户 */
    private String bankAcc;
    /** 账户名称 */
    private String accName;
    /** 存入日期 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    private java.util.Date startDate;
    /** 是否已付款 */
    @Dict(dictGroup = "sf_yn", inject = "isPayedName")
    private String isPayed;
    private String isPayedName;
    /** 保证金金额 */
    private BigDecimal bondMoney;
    /** 币别 */
    @Currency(inject = "curName")
    private String curId;
    /** 币别名称 */
    private String curName;
    /** 备注 */
    private String remark;
    /** 状态 */
    private Integer status;
    /** 创建人登录名称 */
    private String createBy;
    /** 录入日期 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    /** 创建日期 */
    private java.util.Date createDate;
    /** 更新人登录名称 */
    private String updateBy;
    /** 更新日期 */
    private java.util.Date updateDate;
    /** 附件ID */
    private String attachmentId;
    /** 保证金账户余额 */
    private BigDecimal balMoney;
    /** 可用余额 */
    private BigDecimal availMoney;
    public String getAttachmentId() {
        return attachmentId;
    }
    public void setAttachmentId(String attachmentId) {
        this.attachmentId = attachmentId;
    }
    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }
    public String getBillNo() {
        return billNo;
    }
    public void setBillNo(String billNo) {
        this.billNo = billNo;
    }
    public String getCorpId() {
        return corpId;
    }
    public void setCorpId(String corpId) {
        this.corpId = corpId;
    }
    public String getCorpName() {
        return corpName;
    }
    public void setCorpName(String corpName) {
        this.corpName = corpName;
    }
    public String getBankAccId() {
        return bankAccId;
    }
    public void setBankAccId(String bankAccId) {
        this.bankAccId = bankAccId;
    }
    public String getBankName() {
        return bankName;
    }
    public void setBankName(String bankName) {
        this.bankName = bankName;
    }
    public String getBankAcc() {
        return bankAcc;
    }
    public void setBankAcc(String bankAcc) {
        this.bankAcc = bankAcc;
    }
    public String getAccName() {
        return accName;
    }
    public void setAccName(String accName) {
        this.accName = accName;
    }
    public java.util.Date getStartDate() {
        return startDate;
    }
    public void setStartDate(java.util.Date startDate) {
        this.startDate = startDate;
    }
    public String getIsPayed() {
        return isPayed;
    }
    public void setIsPayed(String isPayed) {
        this.isPayed = isPayed;
    }
    public BigDecimal getBondMoney() {
        return bondMoney;
    }
    public void setBondMoney(BigDecimal bondMoney) {
        this.bondMoney = bondMoney;
    }
    public String getRemark() {
        return remark;
    }
    public void setRemark(String remark) {
        this.remark = remark;
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
    public java.util.Date getCreateDate() {
        return createDate;
    }
    public void setCreateDate(java.util.Date createDate) {
        this.createDate = createDate;
    }
    public String getUpdateBy() {
        return updateBy;
    }
    public void setUpdateBy(String updateBy) {
        this.updateBy = updateBy;
    }
    public java.util.Date getUpdateDate() {
        return updateDate;
    }
    public void setUpdateDate(java.util.Date updateDate) {
        this.updateDate = updateDate;
    }
    public String getIsPayedName() {
        return isPayedName;
    }
    public void setIsPayedName(String isPayedName) {
        this.isPayedName = isPayedName;
    }
    public String getCurId() {
        return curId;
    }
    public void setCurId(String curId) {
        this.curId = curId;
    }
    public String getCurName() {
        return curName;
    }
    public void setCurName(String curName) {
        this.curName = curName;
    }
    public BigDecimal getBalMoney() {
        return balMoney;
    }
    public void setBalMoney(BigDecimal balMoney) {
        this.balMoney = balMoney;
    }
    public BigDecimal getAvailMoney() {
        return availMoney;
    }
    public void setAvailMoney(BigDecimal availMoney) {
        this.availMoney = availMoney;
    }
}
