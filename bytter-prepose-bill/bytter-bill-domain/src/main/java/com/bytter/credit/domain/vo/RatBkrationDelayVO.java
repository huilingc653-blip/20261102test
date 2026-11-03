package com.bytter.credit.domain.vo;
import java.io.Serializable;
import java.util.Date;
import com.bytter.core.annotation.BtCompositeSearch;
import com.bytter.core.bpm.IBusinessKey;
import com.fasterxml.jackson.annotation.JsonFormat;
/**
 * 授信延期VO
 * 
 * @author liyh
 *
 */
public class RatBkrationDelayVO implements Serializable, IBusinessKey {
    /**
     * 延期ID
     */
    private String id;
    /**
     * 延期Code
     */
    private String delayCode;
    /** 授信编号 */
    private String ratCode;
    /** 单位代码 */
    @BtCompositeSearch(fieldName = "rd.corp_id")
    private String corpId;
    private String corpName;
    /** 授信类型 */
    private String ratType;
    /** 授信开始时间 */
    private Date begindate;
    /** 授信结束时间 */
    private Date enddate;
    /** 延期时间 */
    private Date delayDate;
    /** 延期时间 */
    private String delaySign;
    /** 金额 */
    private java.math.BigDecimal money;
    /** 状态 */
    private Integer status;
    @JsonFormat(pattern = "yyyy-MM-dd")
    @BtCompositeSearch(orderBy = "desc", fieldName = "rd.create_Date")
    private Date createDate;
    @BtCompositeSearch(fieldName = "rd.delay_date")
    private Date startDate1;
    @BtCompositeSearch(fieldName = "rd.delay_date")
    private Date startDate2;
    /** 附件ID */
    private String attachmentId;
    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }
    public String getDelayCode() {
        return delayCode;
    }
    public void setDelayCode(String delayCode) {
        this.delayCode = delayCode;
    }
    public String getRatCode() {
        return ratCode;
    }
    public void setRatCode(String ratCode) {
        this.ratCode = ratCode;
    }
    public String getCorpId() {
        return corpId;
    }
    public void setCorpId(String corpId) {
        this.corpId = corpId;
    }
    public String getRatType() {
        return ratType;
    }
    public void setRatType(String ratType) {
        this.ratType = ratType;
    }
    public Date getBegindate() {
        return begindate;
    }
    public void setBegindate(Date begindate) {
        this.begindate = begindate;
    }
    public Date getEnddate() {
        return enddate;
    }
    public void setEnddate(Date enddate) {
        this.enddate = enddate;
    }
    public Date getDelayDate() {
        return delayDate;
    }
    public void setDelayDate(Date delayDate) {
        this.delayDate = delayDate;
    }
    public String getDelaySign() {
        return delaySign;
    }
    public void setDelaySign(String delaySign) {
        this.delaySign = delaySign;
    }
    public java.math.BigDecimal getMoney() {
        return money;
    }
    public void setMoney(java.math.BigDecimal money) {
        this.money = money;
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
    public Date getStartDate1() {
        return startDate1;
    }
    public void setStartDate1(Date startDate1) {
        this.startDate1 = startDate1;
    }
    public Date getStartDate2() {
        return startDate2;
    }
    public void setStartDate2(Date startDate2) {
        this.startDate2 = startDate2;
    }
    public String getCorpName() {
        return corpName;
    }
    public void setCorpName(String corpName) {
        this.corpName = corpName;
    }
    public String getBusinessKey() {
        return id;
    }
    public String getAttachmentId() {
        return attachmentId;
    }
    public void setAttachmentId(String attachmentId) {
        this.attachmentId = attachmentId;
    }
}
