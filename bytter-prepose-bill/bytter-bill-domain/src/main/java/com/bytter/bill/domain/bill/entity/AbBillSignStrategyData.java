package com.bytter.bill.domain.bill.entity;
import com.bytter.core.base.entity.IdEntity;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
/**
 * @Title: AbBillSignStrategyData
 * @Description: 电票签收策略数据信息表
 * @author wangzy
 * @date 2020/7/14 11:25
 *
 */
@Entity
@Table(name = "AB_BILL_SIGN_STRATEGY_DATA", schema = "")
@SuppressWarnings("serial")
public class AbBillSignStrategyData extends IdEntity implements java.io.Serializable{
    /**策略表Id*/
    private String ssId;
    /**数据类型字典（BILL_SIGN_STRATEGY_DATA_TYPE）：01出票人，02承兑人，03申请人*/
    private String dataType;
    /**组织标识字典（BILL_SIGN_STRATEGY_ORG_TAG）：01单位，02客户/供应商*/
    private String orgTag;
    /**数据值对应单位或者客户供应商的ID*/
    private String dataVal;
    @Column(name ="SS_ID")
    public String getSsId() {
        return ssId;
    }
    public void setSsId(String ssId) {
        this.ssId = ssId;
    }
    @Column(name ="DATA_TYPE")
    public String getDataType() {
        return dataType;
    }
    public void setDataType(String dataType) {
        this.dataType = dataType;
    }
    @Column(name ="ORG_TAG")
    public String getOrgTag() {
        return orgTag;
    }
    public void setOrgTag(String orgTag) {
        this.orgTag = orgTag;
    }
    @Column(name ="DATA_VAL")
    public String getDataVal() {
        return dataVal;
    }
    public void setDataVal(String dataVal) {
        this.dataVal = dataVal;
    }
}
