package com.bytter.system.api.dto;
import java.io.Serializable;
/**
 * 账号授权
 *
 * @author zuoy    2017/7/13
 */
public class AccountGrantDTO implements Serializable {
    /**
     * 账户ID
     */
    private String accountId;
    /**
     * 用户ID
     */
    private String userId;
    /**
     * 单位ID
     */
    private String corpId;
    /**
     * 账户类型   1-内部账号  2-银行账号  3-离线账号
     */
    private Integer accountType;
    /**
     * 明细查询
     */
    private Integer canQueryDetail;
    /**
     * 余额查询
     */
    private Integer canQueryBalance;
    /**
     * 转账
     */
    private Integer canTransferAccounts;
    public String getAccountId() {
        return accountId;
    }
    public void setAccountId(String accountId) {
        this.accountId = accountId;
    }
    public String getUserId() {
        return userId;
    }
    public void setUserId(String userId) {
        this.userId = userId;
    }
    public String getCorpId() {
        return corpId;
    }
    public void setCorpId(String corpId) {
        this.corpId = corpId;
    }
    public Integer getAccountType() {
        return accountType;
    }
    public void setAccountType(Integer accountType) {
        this.accountType = accountType;
    }
    public Integer getCanQueryDetail() {
        return canQueryDetail;
    }
    public void setCanQueryDetail(Integer canQueryDetail) {
        this.canQueryDetail = canQueryDetail;
    }
    public Integer getCanQueryBalance() {
        return canQueryBalance;
    }
    public void setCanQueryBalance(Integer canQueryBalance) {
        this.canQueryBalance = canQueryBalance;
    }
    public Integer getCanTransferAccounts() {
        return canTransferAccounts;
    }
    public void setCanTransferAccounts(Integer canTransferAccounts) {
        this.canTransferAccounts = canTransferAccounts;
    }
    @Override
    public String toString() {
        return "AccountGrantDTO{" +
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
