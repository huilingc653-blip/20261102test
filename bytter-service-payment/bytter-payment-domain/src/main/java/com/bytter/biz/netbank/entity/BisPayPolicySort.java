package com.bytter.biz.netbank.entity;
import com.bytter.core.base.entity.IdEntity;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
/**
 * @Title: Entity
 * @Description: 自动支付优先策略表
 * @author huangx
 * @date 2020-04-17 14:38:52
 * @version 1.0
 *
 */
@Entity
@Table(name = "BIS_PAY_POLICY_SORT", schema = "")
@SuppressWarnings("serial")
public class BisPayPolicySort extends IdEntity implements java.io.Serializable {
    /**策略ID*/
    private String policyId;
    /**优先策略类型 1-同行优先，2-小额优先，3-早制单优先，4-加急支付优先，5-同城优先*/
    private String policyType;
    /**序号*/
    private Integer sort;
    @Column(name ="POLICY_ID",nullable=false)
    public String getPolicyId() {
        return policyId;
    }
    public void setPolicyId(String policyId) {
        this.policyId = policyId;
    }
    @Column(name ="POLICY_TYPE",nullable=false)
    public String getPolicyType() {
        return policyType;
    }
    public void setPolicyType(String policyType) {
        this.policyType = policyType;
    }
    @Column(name ="SORT",nullable=true,length=10)
    public Integer getSort() {
        return sort;
    }
    public void setSort(Integer sort) {
        this.sort = sort;
    }
}
