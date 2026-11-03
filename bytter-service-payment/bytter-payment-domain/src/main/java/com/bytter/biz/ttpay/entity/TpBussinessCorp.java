package com.bytter.biz.ttpay.entity;
import com.bytter.core.base.entity.IdEntity;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
@Entity
@Table(name = "TP_BUSINESS_CORP", schema = "")
@SuppressWarnings("serial")
public class TpBussinessCorp extends IdEntity {
    private String businessId;
    /**
     * 单位ID
     */
    private String corpId;
    @Column(name = "BUSINESS_ID", nullable = false, length = 32)
    public String getBusinessId() {
        return businessId;
    }
    public void setBusinessId(String businessId) {
        this.businessId = businessId;
    }
    @Column(name = "CORP_ID", nullable = false, length = 32)
    public String getCorpId() {
        return corpId;
    }
    public void setCorpId(String corpId) {
        this.corpId = corpId;
    }
}
