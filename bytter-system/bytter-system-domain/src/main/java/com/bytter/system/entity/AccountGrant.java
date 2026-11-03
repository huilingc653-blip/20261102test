package com.bytter.system.entity;
import com.bytter.core.base.entity.IdEntity;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
/**
 * Created by zhoul on 2017/4/5.
 */
@Entity
@Table(name = "SYS_ACCOUNT_GRANT")
public class AccountGrant extends IdEntity {
    private String accountId;
    private String userId;
    private String corpId;
    private Integer accountType;
    private Integer canQueryDetail;
    private Integer canQueryBalance;
    private Integer canTransferAccounts;
    @Column(name = "ACCOUNT_ID")
    public String getAccountId() {
        return accountId;
    }
    public void setAccountId(String accountId) {
        this.accountId = accountId;
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
    @Column(name = "ACCOUNT_TYPE")
    public Integer getAccountType() {
        return accountType;
    }
    public void setAccountType(Integer accountType) {
        this.accountType = accountType;
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
    @Override
    public String toString() {
        return "AccountGrant{" +
                "accountId='" + accountId + '\'' +
                ", userId='" + userId + '\'' +
                ", corpId='" + corpId + '\'' +
                ", accountType=" + accountType +
                ", canQueryDetail=" + canQueryDetail +
                ", canQueryBalance=" + canQueryBalance +
                ", canTransferAccounts=" + canTransferAccounts +
                '}';
    }
}
