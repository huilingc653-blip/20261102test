package com.bytter.system.api.dto;
import java.io.Serializable;
/**
 * Created by zuoy on 2017/5/24.
 */
public class BtPurposeDTO implements Serializable {
    /**
     * 单位ID
     */
    private String corpId;
    /**
     * 用途
     */
    private String purpose;
    /**
     * 所属类别
     */
    private String sysBusType;
    public String getCorpId() {
        return corpId;
    }
    public void setCorpId(String corpId) {
        this.corpId = corpId;
    }
    public String getPurpose() {
        return purpose;
    }
    public void setPurpose(String purpose) {
        this.purpose = purpose;
    }
    public String getSysBusType() {
        return sysBusType;
    }
    public void setSysBusType(String sysBusType) {
        this.sysBusType = sysBusType;
    }
}
