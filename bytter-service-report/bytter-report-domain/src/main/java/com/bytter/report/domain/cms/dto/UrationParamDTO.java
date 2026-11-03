package com.bytter.report.domain.cms.dto;
import java.io.Serializable;
import java.util.Date;
/**
 * 授信选择调整参数页面
 * 
 * @author liyh
 *
 */
public class UrationParamDTO implements Serializable {
    /**
     * 单位ID(必填)
     */
    private String corpId;
    /**
     * 授信品种类型(必填)
     */
    private String type;
    /**
     * 授信品种
     */
    private String itemCode;
    /**
     * 业务开始日期
     */
    private Date beginDate;
    /**
     * 业务结束日期
     */
    private Date endDate;
    /**
     * 业务开始日期段 最小日期
     */
    private Date gBeginDate;
    /**
     * 业务开始日期 最大日期
     */
    private Date lBeginDate;
    /**
     * 业务结束日期段 最小日期
     */
    private Date gEndDate;
    /**
     * 业务结束日期段 最大日期
     */
    private Date lEndDate;
    /**
     * 银行
     */
    private String bankCode;
    /**
     * 币别
     */
    private String curCode;
    /**
     * 授信编号
     */
    private String ratCode;
    /**
     * 金额单位
     */
    private Integer moneyType;
    /**
     * 折合本位币
     */
    private String isRmbRat;
    /**
     * 状态
     */
    private String status;
    /**
     * 授信银行控制
     */
    private String bankCtlType;
    /**
     * 银行类型
     */
    private String bankPrefix;
    /**
     * 授信模式
     */
    private String ratMode;
    /**
     * 过期标识
     */
    private String isExpire;
    public String getCorpId() {
        return corpId;
    }
    public void setCorpId(String corpId) {
        this.corpId = corpId;
    }
    public String getType() {
        return type;
    }
    public void setType(String type) {
        this.type = type;
    }
    public String getItemCode() {
        return itemCode;
    }
    public void setItemCode(String itemCode) {
        this.itemCode = itemCode;
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
    public String getBankCode() {
        return bankCode;
    }
    public void setBankCode(String bankCode) {
        this.bankCode = bankCode;
    }
    public String getCurCode() {
        return curCode;
    }
    public void setCurCode(String curCode) {
        this.curCode = curCode;
    }
    public String getRatCode() {
        return ratCode;
    }
    public void setRatCode(String ratCode) {
        this.ratCode = ratCode;
    }
    public Integer getMoneyType() {
        return moneyType;
    }
    public void setMoneyType(Integer moneyType) {
        this.moneyType = moneyType;
    }
    public String getIsRmbRat() {
        return isRmbRat;
    }
    public void setIsRmbRat(String isRmbRat) {
        this.isRmbRat = isRmbRat;
    }
    public String getStatus() {
        return status;
    }
    public void setStatus(String status) {
        this.status = status;
    }
    public String getBankCtlType() {
        return bankCtlType;
    }
    public void setBankCtlType(String bankCtlType) {
        this.bankCtlType = bankCtlType;
    }
    public String getBankPrefix() {
        return bankPrefix;
    }
    public void setBankPrefix(String bankPrefix) {
        this.bankPrefix = bankPrefix;
    }
    public Date getgBeginDate() {
        return gBeginDate;
    }
    public void setgBeginDate(Date gBeginDate) {
        this.gBeginDate = gBeginDate;
    }
    public Date getlBeginDate() {
        return lBeginDate;
    }
    public void setlBeginDate(Date lBeginDate) {
        this.lBeginDate = lBeginDate;
    }
    public Date getgEndDate() {
        return gEndDate;
    }
    public void setgEndDate(Date gEndDate) {
        this.gEndDate = gEndDate;
    }
    public Date getlEndDate() {
        return lEndDate;
    }
    public void setlEndDate(Date lEndDate) {
        this.lEndDate = lEndDate;
    }
    public String getRatMode() {
        return ratMode;
    }
    public void setRatMode(String ratMode) {
        this.ratMode = ratMode;
    }
    public String getIsExpire() {
        return isExpire;
    }
    public void setIsExpire(String isExpire) {
        this.isExpire = isExpire;
    }
}
