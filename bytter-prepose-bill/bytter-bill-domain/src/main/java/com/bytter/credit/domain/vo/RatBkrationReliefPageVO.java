package com.bytter.credit.domain.vo;
import java.io.Serializable;
import java.util.List;
/**
 * 授信调剂页面对象
 *
 * @author liyh
 */
public class RatBkrationReliefPageVO implements Serializable {
    private String id;
    /**
     * 分配编号
     */
    private String refCode;
    private String ratCode;
    /**
     * 单位代码
     */
    private String corpId;
    /**
     * 单位代码
     */
    private String corpName;
    /**
     * 授信种类
     */
    private String ratType;
    /**
     * 银行类型
     */
    private String bankType;
    /**
     * 银行编号
     */
    private String bankCode;
    /**
     * 银行编号
     */
    private String bankName;
    /**
     * 授信币种
     */
    private String curCode;
    /**
     * 授信币种
     */
    private String curName;
    /**
     * 授信合同号
     */
    private String contractNo;
    /**
     * 调剂品种标识
     */
    private String reliefItemFlag;
    private String reliefItemCode;
    private String reliefItemName;
    /**
     * 调剂单位
     */
    private String reliefCorp;
    /**
     * 调剂单位名称
     */
    private String reliefCorpName;
    /**
     * 调剂额度编号
     */
    private String reliefCode;
    /**
     * 被调剂单位
     */
    private String byReliefCorp;
    /**
     * 被调剂单位名称
     */
    private String byReliefCorpName;
    /**
     * 调剂额度编号
     */
    private String byReliefCode;
    /**
     * 被调剂品种类型
     */
    private String byReliefItemFlag;
    private String byReliefItemCode;
    private String byReliefItemName;
    /**
     * 被调剂金额
     */
    private java.math.BigDecimal reliefMoney;
    /**
     * 被调剂敞口金额
     */
    private java.math.BigDecimal reliefOpenMoney;
    /**
     * 状态
     */
    private Integer status;
    /**
     * 调剂明细
     */
    private List<RatBkrationReliefDtlPageVO> reliefDtls;
    /**
     * 附件ID
     */
    private String attachmentId;
    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }
    public String getRefCode() {
        return refCode;
    }
    public void setRefCode(String refCode) {
        this.refCode = refCode;
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
    public String getCorpName() {
        return corpName;
    }
    public void setCorpName(String corpName) {
        this.corpName = corpName;
    }
    public String getRatType() {
        return ratType;
    }
    public void setRatType(String ratType) {
        this.ratType = ratType;
    }
    public String getBankType() {
        return bankType;
    }
    public void setBankType(String bankType) {
        this.bankType = bankType;
    }
    public String getBankCode() {
        return bankCode;
    }
    public void setBankCode(String bankCode) {
        this.bankCode = bankCode;
    }
    public String getBankName() {
        return bankName;
    }
    public void setBankName(String bankName) {
        this.bankName = bankName;
    }
    public String getCurCode() {
        return curCode;
    }
    public void setCurCode(String curCode) {
        this.curCode = curCode;
    }
    public String getCurName() {
        return curName;
    }
    public void setCurName(String curName) {
        this.curName = curName;
    }
    public String getContractNo() {
        return contractNo;
    }
    public void setContractNo(String contractNo) {
        this.contractNo = contractNo;
    }
    public String getReliefCorp() {
        return reliefCorp;
    }
    public void setReliefCorp(String reliefCorp) {
        this.reliefCorp = reliefCorp;
    }
    public String getReliefCorpName() {
        return reliefCorpName;
    }
    public void setReliefCorpName(String reliefCorpName) {
        this.reliefCorpName = reliefCorpName;
    }
    public String getReliefCode() {
        return reliefCode;
    }
    public void setReliefCode(String reliefCode) {
        this.reliefCode = reliefCode;
    }
    public String getReliefItemFlag() {
        return reliefItemFlag;
    }
    public void setReliefItemFlag(String reliefItemFlag) {
        this.reliefItemFlag = reliefItemFlag;
    }
    public String getByReliefCorp() {
        return byReliefCorp;
    }
    public void setByReliefCorp(String byReliefCorp) {
        this.byReliefCorp = byReliefCorp;
    }
    public String getByReliefCorpName() {
        return byReliefCorpName;
    }
    public void setByReliefCorpName(String byReliefCorpName) {
        this.byReliefCorpName = byReliefCorpName;
    }
    public String getByReliefCode() {
        return byReliefCode;
    }
    public void setByReliefCode(String byReliefCode) {
        this.byReliefCode = byReliefCode;
    }
    public String getByReliefItemFlag() {
        return byReliefItemFlag;
    }
    public void setByReliefItemFlag(String byReliefItemFlag) {
        this.byReliefItemFlag = byReliefItemFlag;
    }
    public java.math.BigDecimal getReliefMoney() {
        return reliefMoney;
    }
    public void setReliefMoney(java.math.BigDecimal reliefMoney) {
        this.reliefMoney = reliefMoney;
    }
    public java.math.BigDecimal getReliefOpenMoney() {
        return reliefOpenMoney;
    }
    public void setReliefOpenMoney(java.math.BigDecimal reliefOpenMoney) {
        this.reliefOpenMoney = reliefOpenMoney;
    }
    public Integer getStatus() {
        return status;
    }
    public void setStatus(Integer status) {
        this.status = status;
    }
    public String getReliefItemCode() {
        return reliefItemCode;
    }
    public void setReliefItemCode(String reliefItemCode) {
        this.reliefItemCode = reliefItemCode;
    }
    public String getByReliefItemCode() {
        return byReliefItemCode;
    }
    public void setByReliefItemCode(String byReliefItemCode) {
        this.byReliefItemCode = byReliefItemCode;
    }
    public List<RatBkrationReliefDtlPageVO> getReliefDtls() {
        return reliefDtls;
    }
    public void setReliefDtls(List<RatBkrationReliefDtlPageVO> reliefDtls) {
        this.reliefDtls = reliefDtls;
    }
    public String getReliefItemName() {
        return reliefItemName;
    }
    public void setReliefItemName(String reliefItemName) {
        this.reliefItemName = reliefItemName;
    }
    public String getByReliefItemName() {
        return byReliefItemName;
    }
    public void setByReliefItemName(String byReliefItemName) {
        this.byReliefItemName = byReliefItemName;
    }
    public String getAttachmentId() {
        return attachmentId;
    }
    public void setAttachmentId(String attachmentId) {
        this.attachmentId = attachmentId;
    }
}
