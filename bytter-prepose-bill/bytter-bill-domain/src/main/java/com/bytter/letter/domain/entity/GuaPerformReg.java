package com.bytter.letter.domain.entity;

import com.bytter.core.base.entity.IdEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
/**
 * @author fusb
 * @version 1.0
 * @Title GuaPerformReg
 * @Description 保函类型登记实体类
 * @date 2019年1月7日
 */
@Entity
@Table(name = "GUA_PERFORM_REG", schema = "")
@SuppressWarnings("serial")
public class GuaPerformReg extends IdEntity implements java.io.Serializable {
    /**
     * 保函申请ID
     */
    private String guaraId;
    /**
     * 合同编号
     */
    private String contractNo;
    /**
     * 合同签订日期
     */
    private java.util.Date contractDate;
    /**
     * 收款方式
     */
    private String paymentMethod;
    /**
     * 币别名称
     */
    private String curName;
    /**
     * 单位名称
     */
    private String corpName;
    /**
     * 银行名称
     */
    private String bankName;
    /**
     * 收款日期
     */
    private java.util.Date paymentDate;
    /**
     * 保函分数
     */
    private Integer guaNumber;
    /**
     * 备注
     */
    private String rmk;
    /**
     * 附件ID
     */
    private String attachmentId;
    /**
     * 状态
     */
    private Integer status;
    /**
     * 创建日期
     */
    private java.util.Date createDate;
    /**
     * 创建人登录名称
     */
    private String createBy;
    /**
     * 更新日期
     */
    private java.util.Date updateDate;
    /**
     * 更新人登录名称
     */
    private String updateBy;
    /**
     * 取得 java.lang.String
     *
     * @return guaraId 保函申请ID
     */
    @Column(name = "GUARA_ID")
    public String getGuaraId() {
        return guaraId;
    }
    /**
     * 设置 java.lang.String
     *
     * @return guaraId 保函申请ID
     */
    public void setGuaraId(String guaraId) {
        this.guaraId = guaraId;
    }
    /**
     * 取得 java.lang.String
     *
     * @return contractNo 合同编号
     */
    @Column(name = "CONTRACT_NO")
    public String getContractNo() {
        return contractNo;
    }
    /**
     * 设置 java.lang.String
     *
     * @return contractNo 合同编号
     */
    public void setContractNo(String contractNo) {
        this.contractNo = contractNo;
    }
    /**
     * 取得 java.util.Date
     *
     * @return contractDate 合同签订日期
     */
    @Column(name = "CONTRACT_DATE")
    public java.util.Date getContractDate() {
        return contractDate;
    }
    /**
     * 设置 java.util.Date
     *
     * @return contractDate 合同签订日期
     */
    public void setContractDate(java.util.Date contractDate) {
        this.contractDate = contractDate;
    }
    /**
     * 取得 java.lang.String paymentMethod
     *
     * @return paymentMethod 付款方式
     */
    @Column(name = "PAYMENT_METHOD")
    public String getPaymentMethod() {
        return paymentMethod;
    }
    /**
     * 设置 java.lang.String paymentMethod
     *
     * @return paymentMethod 付款方式
     */
    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
    }
    /**
     * 取得 java.util.Date
     *
     * @return paymentDate 收款日期
     */
    @Column(name = "PAYMENT_DATE")
    public java.util.Date getPaymentDate() {
        return paymentDate;
    }
    /**
     * 设置 java.util.Date
     *
     * @return paymentDate 收款日期
     */
    public void setPaymentDate(java.util.Date paymentDate) {
        this.paymentDate = paymentDate;
    }
    /**
     * 取得 java.lang.Integer
     *
     * @return guaNumber 保函份数
     */
    @Column(name = "GUA_NUMBER")
    public Integer getGuaNumber() {
        return guaNumber;
    }
    /**
     * 设置 java.lang.Integer
     *
     * @return guaNumber 保函份数
     */
    public void setGuaNumber(Integer guaNumber) {
        this.guaNumber = guaNumber;
    }
    /**
     * 取得java.lang.String
     *
     * @return rmk 备注
     */
    @Column(name = "RMK")
    public String getRmk() {
        return rmk;
    }
    /**
     * 设置java.lang.String
     *
     * @return rmk 备注
     */
    public void setRmk(String rmk) {
        this.rmk = rmk;
    }
    /**
     * 方法: 取得java.lang.String
     *
     * @return: java.lang.String 附件ID
     */
    @Column(name = "ATTACHMENT_ID")
    public String getAttachmentId() {
        return attachmentId;
    }
    /**
     * 方法: 设置java.lang.String
     *
     * @return: java.lang.String 附件ID
     */
    public void setAttachmentId(String attachmentId) {
        this.attachmentId = attachmentId;
    }
    /**
     * 设置 java.lang.Integer
     *
     * @return status 状态
     */
    @Column(name = "STATUS")
    public Integer getStatus() {
        return status;
    }
    /**
     * 取得java.lang.Integer
     *
     * @return status 状态
     */
    public void setStatus(Integer status) {
        this.status = status;
    }
    /**
     * 方法: 取得java.util.Date
     *
     * @return: java.util.Date 创建日期
     */
    @Column(name = "CREATE_DATE")
    public java.util.Date getCreateDate() {
        return createDate;
    }
    /**
     * 方法: 设置java.util.Date
     *
     * @param: java.util.Date
     * 创建日期
     */
    public void setCreateDate(java.util.Date createDate) {
        this.createDate = createDate;
    }
    /**
     * 方法: 取得java.lang.String
     *
     * @return: java.lang.String 创建人登录名称
     */
    @Column(name = "CREATE_BY")
    public String getCreateBy() {
        return createBy;
    }
    /**
     * 方法: 设置java.lang.String
     *
     * @param: java.lang.String
     * 创建人登录名称
     */
    public void setCreateBy(String createBy) {
        this.createBy = createBy;
    }
    /**
     * 方法: 取得java.lang.String
     *
     * @return: java.lang.String 更新人登录名称
     */
    @Column(name = "UPDATE_BY")
    public String getUpdateBy() {
        return updateBy;
    }
    /**
     * 方法: 设置java.lang.String
     *
     * @param: java.lang.String
     * 更新人
     */
    public void setUpdateBy(String updateBy) {
        this.updateBy = updateBy;
    }
    @Column(name = "CORP_NAME")
    public String getCorpName() {
        return corpName;
    }
    public void setCorpName(String corpName) {
        this.corpName = corpName;
    }
    @Column(name = "BANK_NAME")
    public String getBankName() {
        return bankName;
    }
    public void setBankName(String bankName) {
        this.bankName = bankName;
    }
    @Column(name = "CUR_NAME")
    public String getCurName() {
        return curName;
    }
    public void setCurName(String curName) {
        this.curName = curName;
    }
}
