package com.bytter.credit.domain.entity;
import com.bytter.core.base.entity.IdEntity;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
@Entity
@Table(name = "RAT_BUSINESS_RACE", schema = "")
public class RatBusinessRace extends IdEntity implements java.io.Serializable {
    /** 授信品种Id **/
    private String customId;
    /** 业务名称 **/
    private String raceName;
    @Column(name = "CUSTOM_ID")
    public String getCustomId() {
        return customId;
    }
    public void setCustomId(String customId) {
        this.customId = customId;
    }
    @Column(name = "RACE_NAME")
    public String getRaceName() {
        return raceName;
    }
    public void setRaceName(String raceName) {
        this.raceName = raceName;
    }
}
