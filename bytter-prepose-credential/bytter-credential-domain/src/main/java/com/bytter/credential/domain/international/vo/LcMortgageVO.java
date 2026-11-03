package com.bytter.credential.domain.international.vo;
import com.bytter.core.annotation.BtCompositeSearch;
import com.bytter.core.bpm.IBusinessKey;
import com.fasterxml.jackson.annotation.JsonFormat;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
/**
 * @Title Entity
 * @Description 信用证抵押融资表
 * @Author liusy
 * @Date 2018-11-28 20:02
 * @Version 1.0
 */
public class LcMortgageVO implements Serializable, IBusinessKey {
    private String id;
    /*单位编号*/
    private String corpId;
    /*交单ID*/
    private String arrivalId;
    /*信用证登记ID*/
    private String lcId;
    /*抵押单号*/
    private String mortgageNo;
    /*抵押金额*/
    private BigDecimal mortgageMoney;
    /*抵押日期*/
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date mortgageDate;
    /*到期日期*/
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date expiryDate;
    /*利率*/
    private BigDecimal interestRate;
    /*备注*/
    private String rmk;
    /*状态*/
    private Integer status;
    /*创建日期*/
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date createDate;
    /*创建人登录名称*/
    private String createBy;
    /*更新日期*/
    private Date updateDate;
    /*更新人登录名称*/
    private String updateBy;
    /*取消日期*/
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date delDate;
    /*取消人登录名称*/
    private String delBy;
    /**
     * 登记日期
     **/
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date lcCreateDate;
    private String corpName;
    /**
     * 信用证币种
     */
//    @Currency(inject = "curName")
    private String curId;
    private String curName;
    @BtCompositeSearch(fieldName = "lc.credit_no")
    private String creditNo;
    private String arrivalNo;
    /**
     * 附件id
     */
    private String attachmentId;
    public String getCreditNo() {
        return creditNo;
    }
    public void setCreditNo(String creditNo) {
        this.creditNo = creditNo;
    }
    public String getArrivalNo() {
        return arrivalNo;
    }
    public void setArrivalNo(String arrivalNo) {
        this.arrivalNo = arrivalNo;
    }
    public String getCurName() {
        return curName;
    }
    public void setCurName(String curName) {
        this.curName = curName;
    }
    public String getCorpName() {
        return corpName;
    }
    public void setCorpName(String corpName) {
        this.corpName = corpName;
    }
    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }
    public String getCorpId() {
        return corpId;
    }
    public void setCorpId(String corpId) {
        this.corpId = corpId;
    }
    public String getArrivalId() {
        return arrivalId;
    }
    public void setArrivalId(String arrivalId) {
        this.arrivalId = arrivalId;
    }
    public String getLcId() {
        return lcId;
    }
    public void setLcId(String lcId) {
        this.lcId = lcId;
    }
    public String getMortgageNo() {
        return mortgageNo;
    }
    public void setMortgageNo(String mortgageNo) {
        this.mortgageNo = mortgageNo;
    }
    public BigDecimal getMortgageMoney() {
        return mortgageMoney;
    }
    public void setMortgageMoney(BigDecimal mortgageMoney) {
        this.mortgageMoney = mortgageMoney;
    }
    public Date getMortgageDate() {
        return mortgageDate;
    }
    public void setMortgageDate(Date mortgageDate) {
        this.mortgageDate = mortgageDate;
    }
    public Date getExpiryDate() {
        return expiryDate;
    }
    public void setExpiryDate(Date expiryDate) {
        this.expiryDate = expiryDate;
    }
    public BigDecimal getInterestRate() {
        return interestRate;
    }
    public void setInterestRate(BigDecimal interestRate) {
        this.interestRate = interestRate;
    }
    public String getRmk() {
        return rmk;
    }
    public void setRmk(String rmk) {
        this.rmk = rmk;
    }
    public Integer getStatus() {
        return status;
    }
    public void setStatus(Integer status) {
        this.status = status;
    }
    public Date getCreateDate() {
        return createDate;
    }
    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }
    public String getCreateBy() {
        return createBy;
    }
    public void setCreateBy(String createBy) {
        this.createBy = createBy;
    }
    public Date getUpdateDate() {
        return updateDate;
    }
    public void setUpdateDate(Date updateDate) {
        this.updateDate = updateDate;
    }
    public String getUpdateBy() {
        return updateBy;
    }
    public void setUpdateBy(String updateBy) {
        this.updateBy = updateBy;
    }
    public Date getDelDate() {
        return delDate;
    }
    public void setDelDate(Date delDate) {
        this.delDate = delDate;
    }
    public String getDelBy() {
        return delBy;
    }
    public void setDelBy(String delBy) {
        this.delBy = delBy;
    }
    public Date getLcCreateDate() {
        return lcCreateDate;
    }
    public void setLcCreateDate(Date lcCreateDate) {
        this.lcCreateDate = lcCreateDate;
    }
    public String getAttachmentId() {
        return attachmentId;
    }
    public void setAttachmentId(String attachmentId) {
        this.attachmentId = attachmentId;
    }
    @Override
    public String getBusinessKey() {
        return id;
    }
    public String getCurId() {
        return curId;
    }
    public void setCurId(String curId) {
        this.curId = curId;
    }
}
