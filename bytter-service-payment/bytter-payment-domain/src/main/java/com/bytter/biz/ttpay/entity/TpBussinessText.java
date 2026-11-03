package com.bytter.biz.ttpay.entity;
import com.bytter.core.base.entity.IdEntity;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.Date;
@Entity
@Table(name = "TP_BUSINESS_TEXT", schema = "")
@SuppressWarnings("serial")
public class TpBussinessText extends IdEntity implements java.io.Serializable {
    //备注
    private String rmk;
    //商户表id
    private String tpBusinessId;
    /**
     * 创建人登录名称
     */
    private String createBy;
    /**
     * 创建日期
     */
    private Date createDate;
    /**
     * 更新人登录名称
     */
    private String updateBy;
    /**
     * 更新日期
     */
    private Date updateDate;
    /**
     * 状态
     */
    private Integer status;
    //支付方式
    private String payType;
    //登录号
    private String loginNo;
    //手机号
    private String phone;
    //邮箱
    private String email;
    //支付类型
    private String payeeMode;
    @Column(name = "RMK", nullable = true, length = 32)
    public String getRmk() {
        return rmk;
    }
    public void setRmk(String rmk) {
        this.rmk = rmk;
    }
    @Column(name = "PAY_TYPE")
    public String getPayType() {
        return payType;
    }
    public void setPayType(String payType) {
        this.payType = payType;
    }
    @Column(name = "TP_BUSINESS_ID", nullable = true, length = 32)
    public String getTpBusinessId() {
        return tpBusinessId;
    }
    public void setTpBusinessId(String tpBusinessId) {
        this.tpBusinessId = tpBusinessId;
    }
    @Column(name = "STATUS")
    public Integer getStatus() {
        return status;
    }
    public void setStatus(Integer status) {
        this.status = status;
    }
    @Column(name = "CREATE_BY", nullable = true, length = 32)
    public String getCreateBy() {
        return createBy;
    }
    public void setCreateBy(String createBy) {
        this.createBy = createBy;
    }
    @Column(name = "CREATE_DATE", nullable = true, length = 32)
    public Date getCreateDate() {
        return createDate;
    }
    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }
    @Column(name = "UPDATE_BY", nullable = true, length = 32)
    public String getUpdateBy() {
        return updateBy;
    }
    public void setUpdateBy(String updateBy) {
        this.updateBy = updateBy;
    }
    @Column(name = "UPDATE_DATE", nullable = true, length = 32)
    public Date getUpdateDate() {
        return updateDate;
    }
    public void setUpdateDate(Date updateDate) {
        this.updateDate = updateDate;
    }
    @Column(name = "PAYEE_MODE")
    public String getPayeeMode() {
        return payeeMode;
    }
    public void setPayeeMode(String payeeMode) {
        this.payeeMode = payeeMode;
    }
    @Column(name = "LOGIN_NO")
    public String getLoginNo() {
        return loginNo;
    }
    public void setLoginNo(String loginNo) {
        this.loginNo = loginNo;
    }
    @Column(name = "PHONE")
    public String getPhone() {
        return phone;
    }
    public void setPhone(String phone) {
        this.phone = phone;
    }
    @Column(name = "EMAIL")
    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }
}
