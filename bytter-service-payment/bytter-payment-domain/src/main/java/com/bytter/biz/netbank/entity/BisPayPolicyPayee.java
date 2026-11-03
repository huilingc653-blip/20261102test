package com.bytter.biz.netbank.entity;

import com.bytter.core.base.entity.IdEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * @author huangx
 * @version 1.0
 * @Title: Entity
 * @Description: 自动支付策略收款人关联表
 * @date 2020-04-17 14:38:52
 */
@Entity
@Table(name = "BIS_PAY_POLICY_PAYEE", schema = "")
@SuppressWarnings("serial")
public class BisPayPolicyPayee extends IdEntity implements java.io.Serializable {
    /**
     * 策略ID
     */
    private String policyId;
    /**
     * 收款人ID
     */
    private String payeeId;
    /**
     * 收款人账号
     */
    private String payeeAcc;
    /**
     * 收款人户名
     */
    private String payeeName;
    /**
     * 是否支付收款人：1-支付收款人，0-不支付收款人
     */
    private String isPayee;

    @Column(name = "POLICY_ID", nullable = false)
    public String getPolicyId() {
        return policyId;
    }

    public void setPolicyId(String policyId) {
        this.policyId = policyId;
    }

    @Column(name = "PAYEE_ID", nullable = false)
    public String getPayeeId() {
        return payeeId;
    }

    public void setPayeeId(String payeeId) {
        this.payeeId = payeeId;
    }

    @Column(name = "PAYEE_ACC", nullable = false)
    public String getPayeeAcc() {
        return payeeAcc;
    }

    public void setPayeeAcc(String payeeAcc) {
        this.payeeAcc = payeeAcc;
    }

    @Column(name = "PAYEE_NAME", nullable = false)
    public String getPayeeName() {
        return payeeName;
    }

    public void setPayeeName(String payeeName) {
        this.payeeName = payeeName;
    }

    @Column(name = "IS_PAYEE", nullable = false)
    public String getIsPayee() {
        return isPayee;
    }

    public void setIsPayee(String isPayee) {
        this.isPayee = isPayee;
    }
}
