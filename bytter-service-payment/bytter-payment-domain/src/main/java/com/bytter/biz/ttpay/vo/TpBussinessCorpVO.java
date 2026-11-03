package com.bytter.biz.ttpay.vo;
import com.bytter.core.annotation.Corp;
public class TpBussinessCorpVO implements java.io.Serializable {
    private String id;
    private String businessId;
    /**
     * 单位ID
     */
    private String corpId;
    private String corpName;
    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }
    public String getBusinessId() {
        return businessId;
    }
    public void setBusinessId(String businessId) {
        this.businessId = businessId;
    }
    public String getCorpId() {
        return corpId;
    }
    public void setCorpId(String corpId) {
        this.corpId = corpId;
    }
    public String getCorpName() {
        return corpName;
    }
    public void setCorpName(String corpName) {
        this.corpName = corpName;
    }
}
