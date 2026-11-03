package com.bytter.system.entity;
import com.bytter.core.base.entity.IdEntity;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.Date;
@Entity
@Table(name = "SYS_ACCOUNT_GRANT_HIS")
public class SysAccountGrantHis extends IdEntity {
    private String accountId;
    private Integer accountType;
    private String userId;
    private String corpId;
    private Integer canQueryDetail;
    private Integer canQueryBalance;
    private Integer canTransferAccounts;
    private Date updateDate;
    private String updateBy;
    public SysAccountGrantHis() {
        super();
        // TODO Auto-generated constructor stub
    }
    public SysAccountGrantHis(String accountId, Integer accountType, String userId, String corpId,
                              Integer canQueryDetail, Integer canQueryBalance, Integer canTransferAccounts, Date updateDate,
                              String updateBy) {
        super();
        this.accountId = accountId;
        this.accountType = accountType;
        this.userId = userId;
        this.corpId = corpId;
        this.canQueryDetail = canQueryDetail;
        this.canQueryBalance = canQueryBalance;
        this.canTransferAccounts = canTransferAccounts;
        this.updateDate = updateDate;
        this.updateBy = updateBy;
    }
    @Column(name = "ACCOUNT_ID")
    public String getAccountId() {
        return accountId;
    }
    public void setAccountId(String accountId) {
        this.accountId = accountId;
    }
    @Column(name = "ACCOUNT_TYPE")
    public Integer getAccountType() {
        return accountType;
    }
    public void setAccountType(Integer accountType) {
        this.accountType = accountType;
    }
    @Column(name = "USER_ID")
    public String getUserId() {
        return userId;
    }
    public void setUserId(String userId) {
        this.userId = userId;
    }
    @Column(name = "CORP_ID")
    public String getCorpId() {
        return corpId;
    }
    public void setCorpId(String corpId) {
        this.corpId = corpId;
    }
    @Column(name = "CAN_QUERY_DETAIL")
    public Integer getCanQueryDetail() {
        return canQueryDetail;
    }
    public void setCanQueryDetail(Integer canQueryDetail) {
        this.canQueryDetail = canQueryDetail;
    }
    @Column(name = "CAN_QUERY_BALANCE")
    public Integer getCanQueryBalance() {
        return canQueryBalance;
    }
    public void setCanQueryBalance(Integer canQueryBalance) {
        this.canQueryBalance = canQueryBalance;
    }
    @Column(name = "CAN_TRANSFER_ACCOUNTS")
    public Integer getCanTransferAccounts() {
        return canTransferAccounts;
    }
    public void setCanTransferAccounts(Integer canTransferAccounts) {
        this.canTransferAccounts = canTransferAccounts;
    }
    @Column(name = "UPDATE_DATE")
    public Date getUpdateDate() {
        return updateDate;
    }
    public void setUpdateDate(Date updateDate) {
        this.updateDate = updateDate;
    }
    @Column(name = "UPDATE_BY")
    public String getUpdateBy() {
        return updateBy;
    }
    public void setUpdateBy(String updateBy) {
        this.updateBy = updateBy;
    }
}
