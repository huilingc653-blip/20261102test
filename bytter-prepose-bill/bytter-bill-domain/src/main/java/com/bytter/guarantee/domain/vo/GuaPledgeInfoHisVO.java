package com.bytter.guarantee.domain.vo;
import java.io.Serializable;
import java.math.BigDecimal;
import com.bytter.core.annotation.BtCompositeSearch;
import com.bytter.core.annotation.Dict;
import com.bytter.core.bpm.IBusinessKey;
import com.bytter.poi.handler.ExcelModel;
/**
 * 
 * @author luxy
 * @version 1.0
 * @Title:GuaPledgeInfoHisVO
 * @Description:
 * @date 2018年12月12日 下午5:50:50
 */
public class GuaPledgeInfoHisVO extends ExcelModel implements Serializable, IBusinessKey {
    private static final long serialVersionUID = -1133641115504256759L;
    private String id;
    /** 抵质押物ID */
    @BtCompositeSearch(fieldName = "gh.pledge_id")
    private String pledgeId;
    /** 单位代码 */
//    @Corp(inject = "corpName")
    private String corpId;
    /** 单位名称 */
    private String corpName;
    /** 抵质押物编号 */
    private String pledgeNo;
    /** 抵质押物性质 */
    private String pledgeAttr;
    /** 抵质押物类型 */
    @Dict(tableName = "GUA_PLEDGE_TYPE", valueFieldName = "id", showFiledName = "TYPE_NAME", inject = "typeName")
    private String pledgeType;
    /** 抵质押物类型名称 */
    private String typeName;
    /** 抵质押物名称 */
    private String pledgeName;
    /** 开始日期 */
    private java.util.Date startDate;
    /** 结束日期 */
    private java.util.Date endDate;
    /** 币别id */
//    @Currency(inject = "curName")
    private String curId;
    /** 币别名称 */
    private String curName;
    /** 评估价值 */
    private BigDecimal pledgeMoney;
    /** 出质人 */
    private String pledge;
    /** 质押权人 */
    private String pledgee;
    /** 资产状况 */
    private String assetState;
    /** 资产产权证号 */
    private String assetNumber;
    /** 土地面积 */
    private BigDecimal buildArea;
    /** 容积率建筑面积 */
    private BigDecimal buildAreaRate;
    /** 填报单位id */
//    @Corp(inject = "regCorpName")
    private String regCorp;
    /** 填报单位名称 */
    private String regCorpName;
    /** 登记日期 */
    private java.util.Date regDate;
    /** 工商登记单号 */
    private String regNumber;
    /** 抵质押折旧 */
    private BigDecimal pledgeDepreciation;
    /** 抵质押原值 */
    private BigDecimal pledgeInitMoney;
    /** 折旧率 */
    private BigDecimal depreciationRate;
    /** 评估机构 */
    private String evaluationDep;
    /** 数量 */
    private BigDecimal amount;
    /** 规格 */
    private String specification;
    /** 存放地点 */
    private String place;
    /** 保管人 */
    private String keeper;
    /** 调查人 */
    private String investigators;
    /** 状态 */
    private Integer status;
    /** 创建日期 */
    private java.util.Date createDate;
    /** 创建人登录名称 */
    private String createBy;
    /** 更新日期 */
    private java.util.Date updateDate;
    /** 更新人登录名称 */
    private String updateBy;
    /** 附件ID */
    private String attachmentId;
    /** 在途占用金额 */
    private BigDecimal freezeMoney;
    /** 已占用金额 */
    private BigDecimal occupyMoney;
    /** 抵押机构 */
    private String pledgeOrg;
    /** 评估净值 */
    private BigDecimal pledgeNetWorth;
    /** 土地证号 */
    private String landCardNo;
    /** 抵押面积 */
    private BigDecimal pledgeArea;
    /** 宗地号 */
    private String landNo;
    /** 宗地面积 */
    private BigDecimal landArea;
    /** 备注 */
    private String rmk;
    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
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
    public java.util.Date getStartDate() {
        return startDate;
    }
    public void setStartDate(java.util.Date startDate) {
        this.startDate = startDate;
    }
    public java.util.Date getEndDate() {
        return endDate;
    }
    public void setEndDate(java.util.Date endDate) {
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
    public BigDecimal getPledgeMoney() {
        return pledgeMoney;
    }
    public void setPledgeMoney(BigDecimal pledgeMoney) {
        this.pledgeMoney = pledgeMoney;
    }
    public String getPledge() {
        return pledge;
    }
    public void setPledge(String pledge) {
        this.pledge = pledge;
    }
    public String getPledgee() {
        return pledgee;
    }
    public void setPledgee(String pledgee) {
        this.pledgee = pledgee;
    }
    public String getAssetState() {
        return assetState;
    }
    public void setAssetState(String assetState) {
        this.assetState = assetState;
    }
    public String getAssetNumber() {
        return assetNumber;
    }
    public void setAssetNumber(String assetNumber) {
        this.assetNumber = assetNumber;
    }
    public BigDecimal getBuildArea() {
        return buildArea;
    }
    public void setBuildArea(BigDecimal buildArea) {
        this.buildArea = buildArea;
    }
    public BigDecimal getBuildAreaRate() {
        return buildAreaRate;
    }
    public void setBuildAreaRate(BigDecimal buildAreaRate) {
        this.buildAreaRate = buildAreaRate;
    }
    public String getRegCorp() {
        return regCorp;
    }
    public void setRegCorp(String regCorp) {
        this.regCorp = regCorp;
    }
    public String getRegCorpName() {
        return regCorpName;
    }
    public void setRegCorpName(String regCorpName) {
        this.regCorpName = regCorpName;
    }
    public java.util.Date getRegDate() {
        return regDate;
    }
    public void setRegDate(java.util.Date regDate) {
        this.regDate = regDate;
    }
    public String getRegNumber() {
        return regNumber;
    }
    public void setRegNumber(String regNumber) {
        this.regNumber = regNumber;
    }
    public BigDecimal getPledgeDepreciation() {
        return pledgeDepreciation;
    }
    public void setPledgeDepreciation(BigDecimal pledgeDepreciation) {
        this.pledgeDepreciation = pledgeDepreciation;
    }
    public BigDecimal getPledgeInitMoney() {
        return pledgeInitMoney;
    }
    public void setPledgeInitMoney(BigDecimal pledgeInitMoney) {
        this.pledgeInitMoney = pledgeInitMoney;
    }
    public BigDecimal getDepreciationRate() {
        return depreciationRate;
    }
    public void setDepreciationRate(BigDecimal depreciationRate) {
        this.depreciationRate = depreciationRate;
    }
    public String getEvaluationDep() {
        return evaluationDep;
    }
    public void setEvaluationDep(String evaluationDep) {
        this.evaluationDep = evaluationDep;
    }
    public BigDecimal getAmount() {
        return amount;
    }
    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }
    public String getSpecification() {
        return specification;
    }
    public void setSpecification(String specification) {
        this.specification = specification;
    }
    public String getPlace() {
        return place;
    }
    public void setPlace(String place) {
        this.place = place;
    }
    public String getKeeper() {
        return keeper;
    }
    public void setKeeper(String keeper) {
        this.keeper = keeper;
    }
    public String getInvestigators() {
        return investigators;
    }
    public void setInvestigators(String investigators) {
        this.investigators = investigators;
    }
    public Integer getStatus() {
        return status;
    }
    public void setStatus(Integer status) {
        this.status = status;
    }
    public java.util.Date getCreateDate() {
        return createDate;
    }
    public void setCreateDate(java.util.Date createDate) {
        this.createDate = createDate;
    }
    public String getCreateBy() {
        return createBy;
    }
    public void setCreateBy(String createBy) {
        this.createBy = createBy;
    }
    public java.util.Date getUpdateDate() {
        return updateDate;
    }
    public void setUpdateDate(java.util.Date updateDate) {
        this.updateDate = updateDate;
    }
    public String getUpdateBy() {
        return updateBy;
    }
    public void setUpdateBy(String updateBy) {
        this.updateBy = updateBy;
    }
    public String getAttachmentId() {
        return attachmentId;
    }
    public void setAttachmentId(String attachmentId) {
        this.attachmentId = attachmentId;
    }
    public BigDecimal getFreezeMoney() {
        return freezeMoney;
    }
    public void setFreezeMoney(BigDecimal freezeMoney) {
        this.freezeMoney = freezeMoney;
    }
    public BigDecimal getOccupyMoney() {
        return occupyMoney;
    }
    public void setOccupyMoney(BigDecimal occupyMoney) {
        this.occupyMoney = occupyMoney;
    }
    public static long getSerialVersionUID() {
        return serialVersionUID;
    }
    public String getPledgeOrg() {
        return pledgeOrg;
    }
    public void setPledgeOrg(String pledgeOrg) {
        this.pledgeOrg = pledgeOrg;
    }
    public BigDecimal getPledgeNetWorth() {
        return pledgeNetWorth;
    }
    public void setPledgeNetWorth(BigDecimal pledgeNetWorth) {
        this.pledgeNetWorth = pledgeNetWorth;
    }
    public String getLandCardNo() {
        return landCardNo;
    }
    public void setLandCardNo(String landCardNo) {
        this.landCardNo = landCardNo;
    }
    public BigDecimal getPledgeArea() {
        return pledgeArea;
    }
    public void setPledgeArea(BigDecimal pledgeArea) {
        this.pledgeArea = pledgeArea;
    }
    public String getLandNo() {
        return landNo;
    }
    public void setLandNo(String landNo) {
        this.landNo = landNo;
    }
    public BigDecimal getLandArea() {
        return landArea;
    }
    public void setLandArea(BigDecimal landArea) {
        this.landArea = landArea;
    }
    public String getRmk() {
        return rmk;
    }
    public void setRmk(String rmk) {
        this.rmk = rmk;
    }
    @Override
    public String getBusinessKey() {
        // TODO Auto-generated method stub
        return getId();
    }
}
