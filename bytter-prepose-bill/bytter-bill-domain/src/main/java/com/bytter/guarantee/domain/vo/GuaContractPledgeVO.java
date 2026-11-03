package com.bytter.guarantee.domain.vo;
import java.math.BigDecimal;
/**
 * @author luxy
 * @Title: GuaContractPledgeVO
 * @ProjectName T2_IDEA
 * @Description: TODO
 * @date 2019/7/23 11:39
 */
public class GuaContractPledgeVO {
    /** 担保合同关联担保方式id */
    private String guaContractWayId;
    /** 抵质押物id */
    private String pledgeId;
    /** 担保占用额度 */
    private BigDecimal guaMoney;
    /** 担保方式id */
    private String guaWayId;
    public String getGuaContractWayId() {
        return guaContractWayId;
    }
    public void setGuaContractWayId(String guaContractWayId) {
        this.guaContractWayId = guaContractWayId;
    }
    public String getPledgeId() {
        return pledgeId;
    }
    public void setPledgeId(String pledgeId) {
        this.pledgeId = pledgeId;
    }
    public BigDecimal getGuaMoney() {
        return guaMoney;
    }
    public void setGuaMoney(BigDecimal guaMoney) {
        this.guaMoney = guaMoney;
    }
    public String getGuaWayId() {
        return guaWayId;
    }
    public void setGuaWayId(String guaWayId) {
        this.guaWayId = guaWayId;
    }
}
