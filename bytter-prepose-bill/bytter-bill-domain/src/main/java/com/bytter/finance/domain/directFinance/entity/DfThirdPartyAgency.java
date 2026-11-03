package com.bytter.finance.domain.directFinance.entity;
import com.bytter.core.base.entity.IdEntity;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.io.Serializable;
/**
 * @Title
 * @Description
 * @Author liusy
 * @Date 2019-12-09 16:40
 * @Version 1.0
 */
@Entity
@Table(name = "DF_THIRD_PARTY_AGENCY", schema = "")
@SuppressWarnings("serial")
public class DfThirdPartyAgency extends IdEntity implements Serializable {
    /**
     * 直融业务id
     */
    private String directFinanceId;
    /**
     * 机构类别
     */
    private String agencyType;
    /**
     * 机构名称
     */
    private String agencyName;
    @Column(name = "DIRECT_FINANCE_ID")
    public String getDirectFinanceId() {
        return directFinanceId;
    }
    public void setDirectFinanceId(String directFinanceId) {
        this.directFinanceId = directFinanceId;
    }
    @Column(name = "AGENCY_TYPE")
    public String getAgencyType() {
        return agencyType;
    }
    public void setAgencyType(String agencyType) {
        this.agencyType = agencyType;
    }
    @Column(name = "AGENCY_NAME")
    public String getAgencyName() {
        return agencyName;
    }
    public void setAgencyName(String agencyName) {
        this.agencyName = agencyName;
    }
}
