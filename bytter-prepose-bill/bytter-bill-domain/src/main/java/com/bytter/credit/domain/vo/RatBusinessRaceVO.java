package com.bytter.credit.domain.vo;
import com.bytter.core.annotation.BtCompositeSearch;
import com.bytter.core.annotation.Dict;
import com.bytter.core.bpm.IBusinessKey;
import java.io.Serializable;
import java.math.BigDecimal;
public class RatBusinessRaceVO implements Serializable, IBusinessKey {
    @Dict(tableName = "RAT_BUSINESS_RACE", valueFieldName = "id", showFiledName = "RACE_NAME", inject = "raceName")
    private String id;
    private BigDecimal type;
    /** 授信品种类型id */
    @Dict(tableName = "RAT_ITEM_TYPE", valueFieldName = "id", showFiledName = "RIT_NAME", inject = "ritName")
    private String ritId;
    private String ritName;
    /** 授信品种Id **/
    @Dict(tableName = "RAT_ITEM_CUSTOM", valueFieldName = "id", showFiledName = "NAME", inject = "name")
    private String customId;
    @BtCompositeSearch(fieldName = "c.name")
    private String name;
    @BtCompositeSearch(fieldName = "b.race_name")
    /** 业务名称 **/
    private String raceName;
    /** 业务品种名 用于维护列表显示 **/
    private String busName;
    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }
    public String getRitName() {
        return ritName;
    }
    public void setRitName(String ritName) {
        this.ritName = ritName;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getRaceName() {
        return raceName;
    }
    public void setRaceName(String raceName) {
        this.raceName = raceName;
    }
    public BigDecimal getType() {
        return type;
    }
    public void setType(BigDecimal type) {
        this.type = type;
    }
    @Override
    public String getBusinessKey() {
        return getId();
    }
    public String getCustomId() {
        return customId;
    }
    public void setCustomId(String customId) {
        this.customId = customId;
    }
    public String getBusName() {
        return busName;
    }
    public void setBusName(String busName) {
        this.busName = busName;
    }
    public String getRitId() {
        return ritId;
    }
    public void setRitId(String ritId) {
        this.ritId = ritId;
    }
}
