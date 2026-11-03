package com.bytter.biz.netbank.entity;

import com.bytter.core.base.entity.IdEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * @author huangx
 * @version 1.0
 * @Title: Entity
 * @Description: 自动支付策略业务类别关联表
 * @date 2020-04-17 14:38:52
 */
@Entity
@Table(name = "BIS_PAY_POLICY_BUSTYPE", schema = "")
@SuppressWarnings("serial")
public class BisPayPolicyBusType extends IdEntity implements java.io.Serializable {
    /**
     * 策略ID
     */
    private String policyId;
    /**
     * 业务类别ID
     */
    private String busTypeId;
    /**
     * 共享业务类别ID
     */
    private String shareBusTypeId;

    @Column(name = "POLICY_ID", nullable = false)
    public String getPolicyId() {
        return policyId;
    }

    public void setPolicyId(String policyId) {
        this.policyId = policyId;
    }

    @Column(name = "BUS_TYPE_ID", nullable = false)
    public String getBusTypeId() {
        return busTypeId;
    }

    public void setBusTypeId(String busTypeId) {
        this.busTypeId = busTypeId;
    }

    @Column(name = "SHARE_BUS_TYPE_ID", nullable = false)
    public String getShareBusTypeId() {
        return shareBusTypeId;
    }

    public void setShareBusTypeId(String shareBusTypeId) {
        this.shareBusTypeId = shareBusTypeId;
    }
}
