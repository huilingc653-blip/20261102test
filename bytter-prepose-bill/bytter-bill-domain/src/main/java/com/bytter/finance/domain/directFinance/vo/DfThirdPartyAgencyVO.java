package com.bytter.finance.domain.directFinance.vo;
import java.io.Serializable;
/**
 * @Title
 * @Description
 * @Author liusy
 * @Date 2019-12-09 16:40
 * @Version 1.0
 */
public class DfThirdPartyAgencyVO implements Serializable {
    /**
     * 业务主键id
     */
    private String id;
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
    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }
    public String getDirectFinanceId() {
        return directFinanceId;
    }
    public void setDirectFinanceId(String directFinanceId) {
        this.directFinanceId = directFinanceId;
    }
    public String getAgencyType() {
        return agencyType;
    }
    public void setAgencyType(String agencyType) {
        this.agencyType = agencyType;
    }
    public String getAgencyName() {
        return agencyName;
    }
    public void setAgencyName(String agencyName) {
        this.agencyName = agencyName;
    }
}
