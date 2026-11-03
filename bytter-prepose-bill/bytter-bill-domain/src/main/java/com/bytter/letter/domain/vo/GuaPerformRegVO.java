package com.bytter.letter.domain.vo;

import com.bytter.core.bpm.IBusinessKey;

import java.io.Serializable;
/**
 *
 * @author yangl
 *
 */
public class GuaPerformRegVO implements Serializable, IBusinessKey {
    private static final long serialVersionUID = 8460373880368343008L;
    private String id;
    /** 保函申请ID */
    private String guaraId;
    /** 合同编号 */
    private String contractNo;
    /** 合同签订日期 */
    private java.util.Date contractDate;
    /** 收款方式 */
    private String paymentMethod;
    /** 收款日期 */
    private java.util.Date paymentDate;
    /** 保函分数 */
    private Integer guaNumber;
    /** 备注 */
    private String rmk;
    /** 附件ID */
    private String attachmentId;
    /** 状态 */
    private Integer status;
    /** 创建日期 */
    private java.util.Date createDate;
    /** 创建人登录名称 */
    private String createBy;
    /** 更新日期 */
    private java.util.Date updateDate;
    /** 更新人登录名称 */
    private String updateBy;
    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }
    public String getGuaraId() {
        return guaraId;
    }
    public void setGuaraId(String guaraId) {
        this.guaraId = guaraId;
    }
    public String getContractNo() {
        return contractNo;
    }
    public void setContractNo(String contractNo) {
        this.contractNo = contractNo;
    }
    public java.util.Date getContractDate() {
        return contractDate;
    }
    public void setContractDate(java.util.Date contractDate) {
        this.contractDate = contractDate;
    }
    public String getPaymentMethod() {
        return paymentMethod;
    }
    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
    }
    public java.util.Date getPaymentDate() {
        return paymentDate;
    }
    public void setPaymentDate(java.util.Date paymentDate) {
        this.paymentDate = paymentDate;
    }
    public Integer getGuaNumber() {
        return guaNumber;
    }
    public void setGuaNumber(Integer guaNumber) {
        this.guaNumber = guaNumber;
    }
    public String getRmk() {
        return rmk;
    }
    public void setRmk(String rmk) {
        this.rmk = rmk;
    }
    public String getAttachmentId() {
        return attachmentId;
    }
    public void setAttachmentId(String attachmentId) {
        this.attachmentId = attachmentId;
    }
    public Integer getStatus() {
        return status;
    }
    public void setStatus(Integer status) {
        this.status = status;
    }
    public java.util.Date getCreateDate() {
        return createDate;
    }
    public void setCreateDate(java.util.Date createDate) {
        this.createDate = createDate;
    }
    public String getCreateBy() {
        return createBy;
    }
    public void setCreateBy(String createBy) {
        this.createBy = createBy;
    }
    public java.util.Date getUpdateDate() {
        return updateDate;
    }
    public void setUpdateDate(java.util.Date updateDate) {
        this.updateDate = updateDate;
    }
    public String getUpdateBy() {
        return updateBy;
    }
    public void setUpdateBy(String updateBy) {
        this.updateBy = updateBy;
    }
    @Override
    public String getBusinessKey() {
        return getId();
    }
}
