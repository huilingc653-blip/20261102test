package com.bytter.biz.netbank.entity;
import com.bytter.core.base.entity.IdEntity;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
/**
 * @Title: Entity
 * @Description: 自动支付策略支付账号关联表
 * @author huangx
 * @date 2020-04-17 14:38:52
 * @version 1.0
 *
 */
@Entity
@Table(name = "BIS_PAY_POLICY_PAYERACC", schema = "")
@SuppressWarnings("serial")
public class BisPayPolicyPayerAcc extends IdEntity implements java.io.Serializable {
    /**策略ID*/
    private String policyId;
    /**账号ID*/
    private String accId;
    @Column(name ="POLICY_ID",nullable=false)
    public String getPolicyId() {
        return policyId;
    }
    public void setPolicyId(String policyId) {
        this.policyId = policyId;
    }
    @Column(name ="ACC_ID",nullable=false)
    public String getAccId() {
        return accId;
    }
    public void setAccId(String accId) {
        this.accId = accId;
    }
}
