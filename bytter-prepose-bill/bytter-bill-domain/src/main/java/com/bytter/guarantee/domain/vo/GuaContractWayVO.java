package com.bytter.guarantee.domain.vo;
import com.bytter.core.annotation.Dict;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;
/**
 * @author luxy
 * @Title: GuaContractWayVO
 * @ProjectName T2_IDEA
 * @Description: 担保合同关联担保方式
 * @date 2019/7/20 10:15
 */
public class GuaContractWayVO implements Serializable {
    private String id;
    /** 担保合同id */
    private String guaId;
    /** 担保方式id */
    @Dict(tableName = "GUA_WAY", valueFieldName = "id", showFiledName = "NAME", inject = "name")
    private String wayCodes;
    /** 担保金额 */
    private BigDecimal money;
    /** 担保方式code */
    private String guaWayCode;
    /** 担保方式名称 */
    private String guaWayName;
    /** 关联抵质押物id */
    private String pledgeIds;
    /** 抵押物id */
    private String pDids;
    /** 质押物id */
    private String pZids;
    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }
    public String getGuaId() {
        return guaId;
    }
    public void setGuaId(String guaId) {
        this.guaId = guaId;
    }
    public String getWayCodes() {
        return wayCodes;
    }
    public void setWayCodes(String wayCodes) {
        this.wayCodes = wayCodes;
    }
    public BigDecimal getMoney() {
        return money;
    }
    public void setMoney(BigDecimal money) {
        this.money = money;
    }
    public String getPledgeIds() {
        return pledgeIds;
    }
    public void setPledgeIds(String pledgeIds) {
        this.pledgeIds = pledgeIds;
    }
    public String getGuaWayCode() {
        return guaWayCode;
    }
    public void setGuaWayCode(String guaWayCode) {
        this.guaWayCode = guaWayCode;
    }
    public String getpDids() {
        return pDids;
    }
    public void setpDids(String pDids) {
        this.pDids = pDids;
    }
    public String getpZids() {
        return pZids;
    }
    public void setpZids(String pZids) {
        this.pZids = pZids;
    }
    public String getGuaWayName() {
        return guaWayName;
    }
    public void setGuaWayName(String guaWayName) {
        this.guaWayName = guaWayName;
    }
}
