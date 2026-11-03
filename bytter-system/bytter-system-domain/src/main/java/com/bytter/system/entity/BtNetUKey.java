package com.bytter.system.entity;
import com.bytter.core.base.entity.IdEntity;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.Date;
@Entity
@Table(name = "BT_NET_UKEY")
public class BtNetUKey extends IdEntity implements Serializable {
    /**Ukey编号*/
    private String ukeyNo;
    /**单位ID*/
    private String corpId;
    /**银行类型*/
    private String bankTypeId;
    /**银行权限*/
    private String bankLimit;
    /**个人权限*/
    private String personalLimit;
    /**保管人*/
    private String custodianId;
    /**Ukey用户*/
    private String ukeyUserId;
    /**网银登录名*/
    private String ebankLoginName;
    /**监交人*/
    private String supervisorId;
    /**启用日期*/
    private Date openDate;
    /**到期日期*/
    private Date expireDate;
    /**附件ID*/
    private String attachmentId;
    /**创建人登录名称*/
    private String createBy;
    /**创建日期*/
    private Date createDate;
    /**更新人登录名称*/
    private String updateBy;
    /**更新日期*/
    private Date updateDate;
    private String isHeadCustodian;

    /**网银登陆账号*/
    private String loginAccount;
    /**机器号/IP地址*/
    private String ipAddress;
    /**端口号*/
    private String port;
    /**密码设备号*/
    private String passwordNo;
    /**密码组*/
    private String cipherSuite;

    @Column(name ="login_account")
    public String getLoginAccount() {
        return loginAccount;
    }

    public void setLoginAccount(String loginAccount) {
        this.loginAccount = loginAccount;
    }
    @Column(name ="ip_address")
    public String getIpAddress() {
        return ipAddress;
    }

    public void setIpAddress(String ipAddress) {
        this.ipAddress = ipAddress;
    }

    @Column(name ="port")
    public String getPort() {
        return port;
    }

    public void setPort(String port) {
        this.port = port;
    }

    @Column(name ="password_no")
    public String getPasswordNo() {
        return passwordNo;
    }

    public void setPasswordNo(String passwordNo) {
        this.passwordNo = passwordNo;
    }

    @Column(name ="cipher_suite")
    public String getCipherSuite() {
        return cipherSuite;
    }

    public void setCipherSuite(String cipherSuite) {
        this.cipherSuite = cipherSuite;
    }

    @Column(name ="IS_HEAD_CUSTODIAN")
    public String getIsHeadCustodian() {
        return isHeadCustodian;
    }

    public void setIsHeadCustodian(String isHeadCustodian) {
        this.isHeadCustodian = isHeadCustodian;
    }

    @Column(name ="UKEY_NO",nullable=true,length=60)
    public String getUkeyNo() {
        return ukeyNo;
    }
    public void setUkeyNo(String ukeyNo) {
        this.ukeyNo = ukeyNo;
    }
    @Column(name ="CORP_ID",nullable=true,length=32)
    public String getCorpId() {
        return corpId;
    }
    public void setCorpId(String corpId) {
        this.corpId = corpId;
    }
    @Column(name ="BANK_TYPE_ID",nullable=true,length=32)
    public String getBankTypeId() {
        return bankTypeId;
    }
    public void setBankTypeId(String bankTypeId) {
        this.bankTypeId = bankTypeId;
    }
    @Column(name ="BANK_LIMIT",nullable=true,length=1)
    public String getBankLimit() {
        return bankLimit;
    }
    public void setBankLimit(String bankLimit) {
        this.bankLimit = bankLimit;
    }
    @Column(name ="PERSONAL_LIMIT",nullable=true,length=1)
    public String getPersonalLimit() {
        return personalLimit;
    }
    public void setPersonalLimit(String personalLimit) {
        this.personalLimit = personalLimit;
    }
    @Column(name ="CUSTODIAN_ID",nullable=true,length=32)
    public String getCustodianId() {
        return custodianId;
    }
    public void setCustodianId(String custodianId) {
        this.custodianId = custodianId;
    }
    @Column(name ="UKEY_USER_ID",nullable=true,length=32)
    public String getUkeyUserId() {
        return ukeyUserId;
    }
    public void setUkeyUserId(String ukeyUserId) {
        this.ukeyUserId = ukeyUserId;
    }
    @Column(name ="SUPERVISOR_ID",nullable=true,length=32)
    public String getSupervisorId() {
        return supervisorId;
    }
    public void setSupervisorId(String supervisorId) {
        this.supervisorId = supervisorId;
    }
    @Column(name ="OPEN_DATE",nullable=true)
    public Date getOpenDate() {
        return openDate;
    }
    public void setOpenDate(Date openDate) {
        this.openDate = openDate;
    }
    @Column(name ="EXPIRE_DATE",nullable=true)
    public Date getExpireDate() {
        return expireDate;
    }
    public void setExpireDate(Date expireDate) {
        this.expireDate = expireDate;
    }
    @Column(name ="ATTACHMENT_ID",nullable=true,length=32)
    public String getAttachmentId() {
        return attachmentId;
    }
    public void setAttachmentId(String attachmentId) {
        this.attachmentId = attachmentId;
    }
    @Column(name ="CREATE_BY",nullable=true,length=50)
    public String getCreateBy() {
        return createBy;
    }
    public void setCreateBy(String createBy) {
        this.createBy = createBy;
    }
    @Column(name ="CREATE_DATE",nullable=true)
    public Date getCreateDate() {
        return createDate;
    }
    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }
    @Column(name ="UPDATE_BY",nullable=true,length=50)
    public String getUpdateBy() {
        return updateBy;
    }
    public void setUpdateBy(String updateBy) {
        this.updateBy = updateBy;
    }
    @Column(name ="UPDATE_DATE",nullable=true)
    public Date getUpdateDate() {
        return updateDate;
    }
    public void setUpdateDate(Date updateDate) {
        this.updateDate = updateDate;
    }

    @Column(name ="EBANK_LOGIN_NAME",nullable=true)
    public String getEbankLoginName() {
        return ebankLoginName;
    }

    public void setEbankLoginName(String ebankLoginName) {
        this.ebankLoginName = ebankLoginName;
    }
}
