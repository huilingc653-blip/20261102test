package com.bytter.system.vo;
import com.bytter.core.annotation.BankType;
import com.bytter.core.annotation.BtCompositeSearch;
import com.bytter.core.annotation.Corp;
import com.bytter.core.annotation.User;
import com.bytter.poi.annotation.Excel;
import com.bytter.poi.annotation.ExcelTarget;
import com.bytter.poi.handler.ExcelModel;
import com.fasterxml.jackson.annotation.JsonFormat;
import java.io.Serializable;
import java.util.Date;
@ExcelTarget("BtNetUKeyVO")
public class BtNetUKeyVO extends ExcelModel implements Serializable {
    private String id;
    /**Ukey编号*/
    @BtCompositeSearch(fieldName = "b.ukey_no")
    @Excel(name = "*U-key编号")
    private String ukeyNo;
    /**单位ID*/
    @BtCompositeSearch(fieldName = "b.corp_id")
    @Corp(inject="corpName")
    private String corpId;
    @Excel(name = "*单位名称")
    private String corpName;
    /**银行类型*/
    @BtCompositeSearch(fieldName = "b.bank_type_id")
    @BankType(inject = "bankTypeName")
    private String bankTypeId;
    private String bankTypeName;
    /**银行权限*/
    @BtCompositeSearch(fieldName = "b.bank_limit")
    private String bankLimit;

    @Excel(name = "*银行权限")
    private String bankLimitName;
    /**个人权限*/
    @BtCompositeSearch(fieldName = "b.personal_limit")
    private String personalLimit;

    @Excel(name = "*个人权限")
    private String personalLimitName;
    /**保管人*/
    @BtCompositeSearch(fieldName = "b.custodian_id")
    @User(inject = "custodianName",field="username")
    private String custodianId;
    @Excel(name = "*保管人")
    private String custodianName;
    /**Ukey用户*/
    @BtCompositeSearch(fieldName = "b.ukey_user_id")
//    @User(inject = "ukeyUserName",field="username")
    private String ukeyUserId;
    @Excel(name = "U-key用户")
    @BtCompositeSearch(fieldName = "b.ukey_user_id")
    private String ukeyUserName;
    @Excel(name = "网银登录名")
    @BtCompositeSearch(fieldName = "b.ebank_login_name")
    private String ebankLoginName;
    /**监交人*/
    @BtCompositeSearch(fieldName = "b.supervisor_id")
    @User(inject = "supervisorName",field="username")
    private String supervisorId;
    @Excel(name = "监交人")
    private String supervisorName;
    /**启用日期*/
    @BtCompositeSearch(fieldName = "b.open_date")
    @Excel(name = "*启用日期")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date openDate;
    /**到期日期*/
    @BtCompositeSearch(fieldName = "b.expire_date")
    @Excel(name = "*到期日期")
    @JsonFormat(pattern = "yyyy-MM-dd")
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
    /**银行账号*/
    @BtCompositeSearch(fieldName = "c.bank_acc")
    @Excel(name = "*银行账号")
    private String bankAcc;
    /**账号名称*/
    @BtCompositeSearch(fieldName = "c.acc_name")
    private String accName;
    /**开户行*/
    @Excel(name = "*开户行名称")
    @BtCompositeSearch(fieldName = "c.bank_name")
    private String bankName;

    private String curName;
    /**
     * 银行账号集合
     */
    private String bankAccIds;
    /**
     * 是否总部保管
     */
    @BtCompositeSearch(fieldName = "b.is_head_custodian")
    private String isHeadCustodian;
    @Excel(name = "*是否总部保管")
    private String isHeadCustodianName;

    private String uKeyNoCheck;

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

    public String getLoginAccount() {
        return loginAccount;
    }

    public void setLoginAccount(String loginAccount) {
        this.loginAccount = loginAccount;
    }

    public String getIpAddress() {
        return ipAddress;
    }

    public void setIpAddress(String ipAddress) {
        this.ipAddress = ipAddress;
    }

    public String getPort() {
        return port;
    }

    public void setPort(String port) {
        this.port = port;
    }

    public String getPasswordNo() {
        return passwordNo;
    }

    public void setPasswordNo(String passwordNo) {
        this.passwordNo = passwordNo;
    }

    public String getCipherSuite() {
        return cipherSuite;
    }

    public void setCipherSuite(String cipherSuite) {
        this.cipherSuite = cipherSuite;
    }

    public String getuKeyNoCheck() {
        return uKeyNoCheck;
    }

    public void setuKeyNoCheck(String uKeyNoCheck) {
        this.uKeyNoCheck = uKeyNoCheck;
    }

    public String getIsHeadCustodian() {
        return isHeadCustodian;
    }

    public void setIsHeadCustodian(String isHeadCustodian) {
        this.isHeadCustodian = isHeadCustodian;
    }

    public String getIsHeadCustodianName() {
        return isHeadCustodianName;
    }

    public void setIsHeadCustodianName(String isHeadCustodianName) {
        this.isHeadCustodianName = isHeadCustodianName;
    }

    public String getCurName() {
        return curName;
    }
    public void setCurName(String curName) {
        this.curName = curName;
    }
    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }
    public String getUkeyNo() {
        return ukeyNo;
    }
    public void setUkeyNo(String ukeyNo) {
        this.ukeyNo = ukeyNo;
    }
    public String getCorpId() {
        return corpId;
    }
    public void setCorpId(String corpId) {
        this.corpId = corpId;
    }
    public String getBankTypeId() {
        return bankTypeId;
    }
    public void setBankTypeId(String bankTypeId) {
        this.bankTypeId = bankTypeId;
    }
    public String getBankLimit() {
        return bankLimit;
    }
    public void setBankLimit(String bankLimit) {
        this.bankLimit = bankLimit;
    }
    public String getPersonalLimit() {
        return personalLimit;
    }
    public void setPersonalLimit(String personalLimit) {
        this.personalLimit = personalLimit;
    }

    public String getBankLimitName() {
        return bankLimitName;
    }

    public void setBankLimitName(String bankLimitName) {
        this.bankLimitName = bankLimitName;
    }

    public String getPersonalLimitName() {
        return personalLimitName;
    }

    public void setPersonalLimitName(String personalLimitName) {
        this.personalLimitName = personalLimitName;
    }

    public String getCustodianId() {
        return custodianId;
    }
    public void setCustodianId(String custodianId) {
        this.custodianId = custodianId;
    }
    public String getUkeyUserId() {
        return ukeyUserId;
    }
    public void setUkeyUserId(String ukeyUserId) {
        this.ukeyUserId = ukeyUserId;
    }
    public String getSupervisorId() {
        return supervisorId;
    }
    public void setSupervisorId(String supervisorId) {
        this.supervisorId = supervisorId;
    }
    public Date getOpenDate() {
        return openDate;
    }
    public void setOpenDate(Date openDate) {
        this.openDate = openDate;
    }
    public Date getExpireDate() {
        return expireDate;
    }
    public void setExpireDate(Date expireDate) {
        this.expireDate = expireDate;
    }
    public String getAttachmentId() {
        return attachmentId;
    }
    public void setAttachmentId(String attachmentId) {
        this.attachmentId = attachmentId;
    }
    public String getCreateBy() {
        return createBy;
    }
    public void setCreateBy(String createBy) {
        this.createBy = createBy;
    }
    public Date getCreateDate() {
        return createDate;
    }
    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }
    public String getUpdateBy() {
        return updateBy;
    }
    public void setUpdateBy(String updateBy) {
        this.updateBy = updateBy;
    }
    public Date getUpdateDate() {
        return updateDate;
    }
    public void setUpdateDate(Date updateDate) {
        this.updateDate = updateDate;
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
    public String getBankName() {
        return bankName;
    }
    public void setBankName(String bankName) {
        this.bankName = bankName;
    }
    public String getCorpName() {
        return corpName;
    }
    public void setCorpName(String corpName) {
        this.corpName = corpName;
    }
    public String getCustodianName() {
        return custodianName;
    }
    public void setCustodianName(String custodianName) {
        this.custodianName = custodianName;
    }
    public String getUkeyUserName() {
        return ukeyUserName;
    }
    public void setUkeyUserName(String ukeyUserName) {
        this.ukeyUserName = ukeyUserName;
    }
    public String getSupervisorName() {
        return supervisorName;
    }
    public void setSupervisorName(String supervisorName) {
        this.supervisorName = supervisorName;
    }
    public String getBankAccIds() {
        return bankAccIds;
    }
    public void setBankAccIds(String bankAccIds) {
        this.bankAccIds = bankAccIds;
    }
    public String getBankTypeName() {
        return bankTypeName;
    }
    public void setBankTypeName(String bankTypeName) {
        this.bankTypeName = bankTypeName;
    }
    @Override
    public String getBeanName() {
        return "btNetUKeyService";
    }

    public String getEbankLoginName() {
        return ebankLoginName;
    }

    public void setEbankLoginName(String ebankLoginName) {
        this.ebankLoginName = ebankLoginName;
    }
}
