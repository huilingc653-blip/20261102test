package com.bytter.guarantee.domain.vo;
import com.bytter.core.annotation.BtCompositeSearch;
import com.bytter.core.annotation.Dict;
import com.bytter.core.bpm.IBusinessKey;
import java.io.Serializable;
import java.math.BigDecimal;
/**
 * 担保方式维护
 */
public class GuaWayVO implements Serializable, IBusinessKey {
    /** id **/
    @Dict(tableName = "GUA_WAY", valueFieldName = "id", showFiledName = "NAME", inject = "name")
    private String id;
    /** 类型编码 */
    @BtCompositeSearch(fieldName = "g.code")
    private String code;
    /** 担保方式名称 */
    @BtCompositeSearch(fieldName = "g.name")
    private String name;
    /** 担保方式名称 */
    private String guaWayName;
    /** 是否抵质押 **/
    @BtCompositeSearch(fieldName = "g.is_pledge")
    private String isPledge;
    @BtCompositeSearch(fieldName = "g.is_pledge")
    private String isPledgeName;
    /** 抵质押性质 **/
    @BtCompositeSearch(fieldName = "g.pledge_property")
    private String pledgeProperty;
    @BtCompositeSearch(fieldName = "g.pledge_property")
    private String pledgePropertyName;
    /** 担保金额 */
    private BigDecimal money;
    /** 备注 **/
    private String rmk;
    @Override
    public String getBusinessKey() {
        return getId();
    }
    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }
    public String getCode() {
        return code;
    }
    public void setCode(String code) {
        this.code = code;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getIsPledge() {
        return isPledge;
    }
    public void setIsPledge(String isPledge) {
        this.isPledge = isPledge;
    }
    public String getPledgeProperty() {
        return pledgeProperty;
    }
    public void setPledgeProperty(String pledgeProperty) {
        this.pledgeProperty = pledgeProperty;
    }
    public BigDecimal getMoney() {
        return money;
    }
    public void setMoney(BigDecimal money) {
        this.money = money;
    }
    public String getRmk() {
        return rmk;
    }
    public void setRmk(String rmk) {
        this.rmk = rmk;
    }
    public String getGuaWayName() {
        return guaWayName;
    }
    public void setGuaWayName(String guaWayName) {
        this.guaWayName = guaWayName;
    }

    public String getIsPledgeName() {
        return isPledgeName;
    }

    public void setIsPledgeName(String isPledgeName) {
        this.isPledgeName = isPledgeName;
    }

    public String getPledgePropertyName() {
        return pledgePropertyName;
    }

    public void setPledgePropertyName(String pledgePropertyName) {
        this.pledgePropertyName = pledgePropertyName;
    }
}
