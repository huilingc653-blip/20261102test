package com.bytter.guarantee.domain.vo;
import com.bytter.core.annotation.BtCompositeSearch;
import com.bytter.core.annotation.Dict;
import com.bytter.core.bpm.IBusinessKey;
import com.bytter.poi.annotation.Excel;
import com.fasterxml.jackson.annotation.JsonFormat;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
/**
 * @author luxy
 * @Title: GuaPledgeOccupyVO
 * @ProjectName T3(3.2)
 * @Description:抵质押物占用VO
 * @date 2020/7/10 16:19
 */
public class GuaPledgeOccupyVO implements Serializable, IBusinessKey {
    private String id;
    /** 系统代码 */
//    @Dict(dictGroup = "pledge.busType", inject = "businessName")
    private String sysCode;
    /** 业务品种名称 */
    private String businessName;
    /** 业务id */
    private String businessId;
    /** 业务编号 */
    private String itemNo;
    /** 抵质押物id */
    private String pledgeId;
    /** 单位ID */
//    @Corp(inject = "corpName")
    private String corpId;
    /** 单位名称 */
    private String corpName;
    /** 抵质押物编号 */
    @BtCompositeSearch(fieldName = "b.pledge_no")
    private String pledgeNo;
    /** 抵质押物性质 */
    @BtCompositeSearch(fieldName = "b.pledge_attr")
    private String pledgeAttr;
    /** 抵质押物类型 */
    @BtCompositeSearch(fieldName = "b.pledge_type")
    @Dict(tableName = "GUA_PLEDGE_TYPE", valueFieldName = "id", showFiledName = "TYPE_NAME", inject = "typeName")
    private String pledgeType;
    @BtCompositeSearch(fieldName = "e.type_name")
    private String typeName;
    /** 抵质押物名称 */
    @BtCompositeSearch(fieldName = "b.pledge_name")
    private String pledgeName;
    /** 业务开始日期 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date startDate;
    /** 业务结束日期 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date endDate;
    /** 币别 */
//    @Currency(inject = "curName")
    @BtCompositeSearch(fieldName = "a.cur_id")
    private String curId;
    /** 币别名称 */
    @Excel(name = "*币别")
    private String curName;
    /** 占用额度 */
    private BigDecimal occupyMoney;
    /** 状态 */
    private Integer status;
    @Override
    public String getBusinessKey() {
        return null;
    }
    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }
    public String getSysCode() {
        return sysCode;
    }
    public void setSysCode(String sysCode) {
        this.sysCode = sysCode;
    }
    public String getBusinessId() {
        return businessId;
    }
    public void setBusinessId(String businessId) {
        this.businessId = businessId;
    }
    public String getItemNo() {
        return itemNo;
    }
    public void setItemNo(String itemNo) {
        this.itemNo = itemNo;
    }
    public String getPledgeId() {
        return pledgeId;
    }
    public void setPledgeId(String pledgeId) {
        this.pledgeId = pledgeId;
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
    public String getPledgeNo() {
        return pledgeNo;
    }
    public void setPledgeNo(String pledgeNo) {
        this.pledgeNo = pledgeNo;
    }
    public String getPledgeAttr() {
        return pledgeAttr;
    }
    public void setPledgeAttr(String pledgeAttr) {
        this.pledgeAttr = pledgeAttr;
    }
    public String getPledgeType() {
        return pledgeType;
    }
    public void setPledgeType(String pledgeType) {
        this.pledgeType = pledgeType;
    }
    public String getTypeName() {
        return typeName;
    }
    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }
    public String getPledgeName() {
        return pledgeName;
    }
    public void setPledgeName(String pledgeName) {
        this.pledgeName = pledgeName;
    }
    public Date getStartDate() {
        return startDate;
    }
    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }
    public Date getEndDate() {
        return endDate;
    }
    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }
    public String getCurId() {
        return curId;
    }
    public void setCurId(String curId) {
        this.curId = curId;
    }
    public String getCurName() {
        return curName;
    }
    public void setCurName(String curName) {
        this.curName = curName;
    }
    public Integer getStatus() {
        return status;
    }
    public void setStatus(Integer status) {
        this.status = status;
    }
    public String getBusinessName() {
        return businessName;
    }
    public void setBusinessName(String businessName) {
        this.businessName = businessName;
    }
    public BigDecimal getOccupyMoney() {
        return occupyMoney;
    }
    public void setOccupyMoney(BigDecimal occupyMoney) {
        this.occupyMoney = occupyMoney;
    }
}
