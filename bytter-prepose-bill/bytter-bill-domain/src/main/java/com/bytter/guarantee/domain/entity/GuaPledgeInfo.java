package com.bytter.guarantee.domain.entity;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import com.bytter.core.base.entity.IdEntity;
import java.math.BigDecimal;
/**
 * @author luxy
 * @version 1.0
 * @Title:GuaPledgeInfo
 * @Description:抵质押物信息表
 * @date 2018年12月11日 上午10:54:45
 */
@Entity
@Table(name = "GUA_PLEDGE_INFO", schema = "")
@SuppressWarnings("serial")
public class GuaPledgeInfo extends IdEntity implements java.io.Serializable {
    /**
     * 单位代码
     */
    private String corpId;
    /**
     * 抵质押物编号
     */
    private String pledgeNo;
    /**
     * 抵质押物性质
     */
    private String pledgeAttr;
    /**
     * 抵质押物类型
     */
    private String pledgeType;
    /**
     * 抵质押物名称
     */
    private String pledgeName;
    /**
     * 开始日期
     */
    private java.util.Date startDate;
    /**
     * 结束日期
     */
    private java.util.Date endDate;
    /**
     * 币别
     */
    private String curId;
    /**
     * 评估价值
     */
    private BigDecimal pledgeMoney;
    /**
     * 出质人
     */
    private String pledge;
    /**
     * 质押权人
     */
    private String pledgee;
    /**
     * 资产状况
     */
    private String assetState;
    /**
     * 资产产权证号
     */
    private String assetNumber;
    /**
     * 土地面积
     */
    private BigDecimal buildArea;
    /**
     * 容积率建筑面积
     */
    private BigDecimal buildAreaRate;
    /**
     * 填报单位名称
     */
    private String regCorp;
    /**
     * 填报单位名称
     */
    private String regCorpName;
    /**
     * 币别名称
     */
    private String curName;
    /**
     * 登记日期
     */
    private java.util.Date regDate;
    /**
     * 工商登记单号
     */
    private String regNumber;
    /**
     * 抵质押折旧
     */
    private BigDecimal pledgeDepreciation;
    /**
     * 抵质押原值
     */
    private BigDecimal pledgeInitMoney;
    /**
     * 折旧率
     */
    private BigDecimal depreciationRate;
    /**
     * 评估机构
     */
    private String evaluationDep;
    /**
     * 数量
     */
    private BigDecimal amount;
    /**
     * 规格
     */
    private String specification;
    /**
     * 存放地点
     */
    private String place;
    /**
     * 保管人
     */
    private String keeper;
    /**
     * 调查人
     */
    private String investigators;
    /**
     * 状态
     */
    private Integer status;
    /**
     * 创建日期
     */
    private java.util.Date createDate;
    /**
     * 创建人登录名称
     */
    private String createBy;
    /**
     * 更新日期
     */
    private java.util.Date updateDate;
    /**
     * 更新人登录名称
     */
    private String updateBy;
    /**
     * 在途占用金额
     */
    private BigDecimal freezeMoney;
    /**
     * 已占用金额
     */
    private BigDecimal occupyMoney;
    /**
     * 附件ID
     */
    private String attachmentId;
    /**
     * 抵押机构
     */
    private String pledgeOrg;
    /**
     * 评估净值
     */
    private BigDecimal pledgeNetWorth;
    /**
     * 土地证号
     */
    private String landCardNo;
    /**
     * 单位名称
     */
    private String corpName;
    /**
     * 抵押面积
     */
    private BigDecimal pledgeArea;
    /**
     * 宗地号
     */
    private String landNo;
    /**
     * 宗地面积
     */
    private BigDecimal landArea;
    /**
     * 备注
     */
    private String rmk;
    /**
     * 方法: 取得java.lang.String
     *
     * @return: java.lang.String 单位代码
     */
    @Column(name = "CORP_ID")
    public String getCorpId() {
        return corpId;
    }
    /**
     * 方法: 设置java.lang.String
     *
     * @param: java.lang.String
     * 单位代码
     */
    public void setCorpId(String corpId) {
        this.corpId = corpId;
    }
    /**
     * 方法: 取得java.lang.String
     *
     * @return: java.lang.String 抵质押物编号
     */
    @Column(name = "PLEDGE_NO")
    public String getPledgeNo() {
        return pledgeNo;
    }
    /**
     * 方法: 设置java.lang.String
     *
     * @param: java.lang.String
     * 抵质押物编号
     */
    public void setPledgeNo(String pledgeNo) {
        this.pledgeNo = pledgeNo;
    }
    /**
     * 方法: 取得java.lang.String
     *
     * @return: java.lang.String 抵质押物性质
     */
    @Column(name = "PLEDGE_ATTR")
    public String getPledgeAttr() {
        return pledgeAttr;
    }
    /**
     * 方法: 设置java.lang.String
     *
     * @param: java.lang.String
     * 抵质押物性质
     */
    public void setPledgeAttr(String pledgeAttr) {
        this.pledgeAttr = pledgeAttr;
    }
    /**
     * 方法: 取得java.lang.String
     *
     * @return: java.lang.String 抵质押物类型
     */
    @Column(name = "PLEDGE_TYPE")
    public String getPledgeType() {
        return pledgeType;
    }
    /**
     * 方法: 设置java.lang.String
     *
     * @param: java.lang.String
     * 抵质押物类型
     */
    public void setPledgeType(String pledgeType) {
        this.pledgeType = pledgeType;
    }
    /**
     * 方法: 取得java.lang.String
     *
     * @return: java.lang.String 抵质押物名称
     */
    @Column(name = "PLEDGE_NAME")
    public String getPledgeName() {
        return pledgeName;
    }
    /**
     * 方法: 设置java.lang.String
     *
     * @param: java.lang.String
     * 抵质押物名称
     */
    public void setPledgeName(String pledgeName) {
        this.pledgeName = pledgeName;
    }
    /**
     * 方法: 取得java.util.Date
     *
     * @return: java.util.Date 开始日期
     */
    @Column(name = "START_DATE")
    public java.util.Date getStartDate() {
        return startDate;
    }
    /**
     * 方法: 设置java.util.Date
     *
     * @param: java.util.Date
     * 开始日期
     */
    public void setStartDate(java.util.Date startDate) {
        this.startDate = startDate;
    }
    /**
     * 方法: 取得java.util.Date
     *
     * @return: java.util.Date 抵质押物结束日期
     */
    @Column(name = "END_DATE")
    public java.util.Date getEndDate() {
        return endDate;
    }
    /**
     * 方法: 设置java.util.Date
     *
     * @param: java.util.Date
     * 抵质押物结束日期
     */
    public void setEndDate(java.util.Date endDate) {
        this.endDate = endDate;
    }
    /**
     * 方法: 取得java.lang.String
     *
     * @return: java.lang.String 币别
     */
    @Column(name = "CUR_ID")
    public String getCurId() {
        return curId;
    }
    /**
     * 方法: 设置java.lang.String
     *
     * @return: java.lang.String 币别
     */
    public void setCurId(String curId) {
        this.curId = curId;
    }
    /**
     * 方法: 取得java.math.BigDecimal
     *
     * @return: java.math.BigDecimal 评估价值
     */
    @Column(name = "PLEDGE_MONEY")
    public BigDecimal getPledgeMoney() {
        return pledgeMoney;
    }
    /**
     * 方法: 设置java.math.BigDecimal
     *
     * @param: java.math.BigDecimal
     * 评估价值
     */
    public void setPledgeMoney(BigDecimal pledgeMoney) {
        this.pledgeMoney = pledgeMoney;
    }
    /**
     * 方法: 取得java.lang.String
     *
     * @return: java.lang.String 出质人
     */
    @Column(name = "PLEDGE")
    public String getPledge() {
        return pledge;
    }
    /**
     * 方法: 设置java.lang.String
     *
     * @return: java.lang.String 出质人
     */
    public void setPledge(String pledge) {
        this.pledge = pledge;
    }
    /**
     * 方法: 取得java.lang.String
     *
     * @return: java.lang.String 质押权人
     */
    @Column(name = "PLEDGEE")
    public String getPledgee() {
        return pledgee;
    }
    /**
     * 方法: 设置java.lang.String
     *
     * @return: java.lang.String 质押权人
     */
    public void setPledgee(String pledgee) {
        this.pledgee = pledgee;
    }
    /**
     * 方法: 取得java.lang.String
     *
     * @return: java.lang.String 资产状况
     */
    @Column(name = "ASSET_STATE")
    public String getAssetState() {
        return assetState;
    }
    /**
     * 方法: 设置java.lang.String
     *
     * @return: java.lang.String 资产状况
     */
    public void setAssetState(String assetState) {
        this.assetState = assetState;
    }
    /**
     * 方法: 取得java.lang.String
     *
     * @return: java.lang.String 资产产权证号
     */
    @Column(name = "ASSET_NUMBER")
    public String getAssetNumber() {
        return assetNumber;
    }
    /**
     * 方法: 设置java.lang.String
     *
     * @return: java.lang.String 资产产权证号
     */
    public void setAssetNumber(String assetNumber) {
        this.assetNumber = assetNumber;
    }
    /**
     * 方法: 取得java.math.BigDecimal
     *
     * @return: java.math.BigDecimal 土地面积
     */
    @Column(name = "BUILD_AREA")
    public BigDecimal getBuildArea() {
        return buildArea;
    }
    /**
     * 方法: 设置java.math.BigDecimal
     *
     * @param: java.math.BigDecimal
     * 土地面积
     */
    public void setBuildArea(BigDecimal buildArea) {
        this.buildArea = buildArea;
    }
    /**
     * 方法: 取得java.math.BigDecimal
     *
     * @return: java.math.BigDecimal 容积率面积
     */
    @Column(name = "BUILD_AREA_RATE")
    public BigDecimal getBuildAreaRate() {
        return buildAreaRate;
    }
    /**
     * 方法: 设置java.math.BigDecimal
     *
     * @param: java.math.BigDecimal
     * 容积率面积
     */
    public void setBuildAreaRate(BigDecimal buildAreaRate) {
        this.buildAreaRate = buildAreaRate;
    }
    /**
     * 方法: 取得java.lang.String
     *
     * @return: java.lang.String 填报单位名称
     */
    @Column(name = "REG_CORP")
    public String getRegCorp() {
        return regCorp;
    }
    /**
     * 方法: 设置java.lang.String
     *
     * @return: java.lang.String 填报单位名称
     */
    public void setRegCorp(String regCorp) {
        this.regCorp = regCorp;
    }
    /**
     * 方法: 取得java.util.Date
     *
     * @return: java.util.Date 登记日期
     */
    @Column(name = "REG_DATE")
    public java.util.Date getRegDate() {
        return regDate;
    }
    /**
     * 方法: 设置java.util.Date
     *
     * @return: java.util.Date 登记日期
     */
    public void setRegDate(java.util.Date regDate) {
        this.regDate = regDate;
    }
    /**
     * 方法: 取得java.lang.String
     *
     * @return: java.lang.String 工商登记单号
     */
    @Column(name = "REG_NUMBER")
    public String getRegNumber() {
        return regNumber;
    }
    /**
     * 方法: 设置java.lang.String
     *
     * @return: java.lang.String 工商登记单号
     */
    public void setRegNumber(String regNumber) {
        this.regNumber = regNumber;
    }
    /**
     * 方法: 取得java.math.BigDecimal
     *
     * @return: java.math.BigDecimal 抵质押折旧
     */
    @Column(name = "PLEDGE_DEPRECIATION")
    public BigDecimal getPledgeDepreciation() {
        return pledgeDepreciation;
    }
    /**
     * 方法: 设置java.math.BigDecimal
     *
     * @return: java.math.BigDecimal 抵质押折旧
     */
    public void setPledgeDepreciation(BigDecimal pledgeDepreciation) {
        this.pledgeDepreciation = pledgeDepreciation;
    }
    /**
     * 方法: 取得java.math.BigDecimal
     *
     * @return: java.math.BigDecimal 抵质押原值
     */
    @Column(name = "PLEDGE_INIT_MONEY")
    public BigDecimal getPledgeInitMoney() {
        return pledgeInitMoney;
    }
    /**
     * 方法: 设置java.math.BigDecimal
     *
     * @return: java.math.BigDecimal 抵质押原值
     */
    public void setPledgeInitMoney(BigDecimal pledgeInitMoney) {
        this.pledgeInitMoney = pledgeInitMoney;
    }
    /**
     * 方法: 取得java.math.BigDecimal
     *
     * @return: java.math.BigDecimal 折旧率
     */
    @Column(name = "DEPRECIATION_RATE")
    public BigDecimal getDepreciationRate() {
        return depreciationRate;
    }
    /**
     * 方法: 设置java.math.BigDecimal
     *
     * @return: java.math.BigDecimal 折旧率
     */
    public void setDepreciationRate(BigDecimal depreciationRate) {
        this.depreciationRate = depreciationRate;
    }
    /**
     * 方法: 取得java.lang.String
     *
     * @return: java.lang.String 评估机构
     */
    @Column(name = "EVALUATION_DEP")
    public String getEvaluationDep() {
        return evaluationDep;
    }
    /**
     * 方法: 设置java.lang.String
     *
     * @return: java.lang.String 评估机构
     */
    public void setEvaluationDep(String evaluationDep) {
        this.evaluationDep = evaluationDep;
    }
    /**
     * 方法: 取得java.math.BigDecimal
     *
     * @return: java.math.BigDecimal 数量
     */
    @Column(name = "AMOUNT")
    public BigDecimal getAmount() {
        return amount;
    }
    /**
     * 方法: 设置java.math.BigDecimal
     *
     * @return: java.math.BigDecimal 数量
     */
    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }
    /**
     * 方法: 取得java.lang.String
     *
     * @return: java.lang.String 规格
     */
    @Column(name = "SPECIFICATION")
    public String getSpecification() {
        return specification;
    }
    /**
     * 方法: 设置java.lang.String
     *
     * @return: java.lang.String 规格
     */
    public void setSpecification(String specification) {
        this.specification = specification;
    }
    /**
     * 方法: 取得java.lang.String
     *
     * @return: java.lang.String 存放地点
     */
    @Column(name = "PLACE")
    public String getPlace() {
        return place;
    }
    /**
     * 方法: 设置java.lang.String
     *
     * @return: java.lang.String 存放地点
     */
    public void setPlace(String place) {
        this.place = place;
    }
    /**
     * 方法: 取得java.lang.String
     *
     * @return: java.lang.String 保管人
     */
    @Column(name = "KEEPER")
    public String getKeeper() {
        return keeper;
    }
    /**
     * 方法: 设置java.lang.String
     *
     * @return: java.lang.String 保管人
     */
    public void setKeeper(String keeper) {
        this.keeper = keeper;
    }
    /**
     * 方法: 取得java.lang.String
     *
     * @return: java.lang.String 调查人
     */
    @Column(name = "INVESTIGATORS")
    public String getInvestigators() {
        return investigators;
    }
    /**
     * 方法: 设置java.lang.String
     *
     * @return: java.lang.String 调查人
     */
    public void setInvestigators(String investigators) {
        this.investigators = investigators;
    }
    /**
     * 方法: 取得java.lang.Integer
     *
     * @return: java.lang.Integer 状态
     */
    @Column(name = "STATUS")
    public Integer getStatus() {
        return status;
    }
    /**
     * 方法: 设置java.lang.Integer
     *
     * @return: java.lang.Integer 状态
     */
    public void setStatus(Integer status) {
        this.status = status;
    }
    /**
     * 方法: 取得java.util.Date
     *
     * @return: java.util.Date 创建日期
     */
    @Column(name = "CREATE_DATE")
    public java.util.Date getCreateDate() {
        return createDate;
    }
    /**
     * 方法: 设置java.util.Date
     *
     * @param: java.util.Date
     * 创建日期
     */
    public void setCreateDate(java.util.Date createDate) {
        this.createDate = createDate;
    }
    /**
     * 方法: 取得java.lang.String
     *
     * @return: java.lang.String 创建人登录名称
     */
    @Column(name = "CREATE_BY")
    public String getCreateBy() {
        return createBy;
    }
    /**
     * 方法: 设置java.lang.String
     *
     * @param: java.lang.String
     * 创建人登录名称
     */
    public void setCreateBy(String createBy) {
        this.createBy = createBy;
    }
    /**
     * 方法: 取得java.util.Date
     *
     * @return: java.util.Date 更新日期
     */
    @Column(name = "UPDATE_DATE")
    public java.util.Date getUpdateDate() {
        return updateDate;
    }
    /**
     * 方法: 设置java.util.Date
     *
     * @param: java.util.Date
     * 更新日期
     */
    public void setUpdateDate(java.util.Date updateDate) {
        this.updateDate = updateDate;
    }
    /**
     * 方法: 取得java.lang.String
     *
     * @return: java.lang.String 更新人登录名称
     */
    @Column(name = "UPDATE_BY")
    public String getUpdateBy() {
        return updateBy;
    }
    /**
     * 方法: 设置java.lang.String
     *
     * @param: java.lang.String
     * 更新人登录名称
     */
    public void setUpdateBy(String updateBy) {
        this.updateBy = updateBy;
    }
    /**
     * 方法: 取得java.math.BigDecimal
     *
     * @return: java.math.BigDecimal 在途占用金额
     */
    @Column(name = "FREEZE_MONEY")
    public BigDecimal getFreezeMoney() {
        return freezeMoney;
    }
    /**
     * 方法: 设置java.math.BigDecimal
     *
     * @return: java.math.BigDecimal 在途占用金额
     */
    public void setFreezeMoney(BigDecimal freezeMoney) {
        this.freezeMoney = freezeMoney;
    }
    /**
     * 方法: 取得java.math.BigDecimal
     *
     * @return: java.math.BigDecimal 已占用金额
     */
    @Column(name = "OCCUPY_MONEY")
    public BigDecimal getOccupyMoney() {
        return occupyMoney;
    }
    /**
     * 方法: 设置java.math.BigDecimal
     *
     * @return: java.math.BigDecimal 已占用金额
     */
    public void setOccupyMoney(BigDecimal occupyMoney) {
        this.occupyMoney = occupyMoney;
    }
    /**
     * 方法: 取得java.lang.String
     *
     * @return: java.lang.String 附件ID
     */
    @Column(name = "ATTACHMENT_ID")
    public String getAttachmentId() {
        return attachmentId;
    }
    /**
     * 方法: 设置java.lang.String
     *
     * @return: java.lang.String 附件ID
     */
    public void setAttachmentId(String attachmentId) {
        this.attachmentId = attachmentId;
    }
    /**
     * 方法: 取得java.lang.String
     *
     * @return: java.lang.String 抵押机构
     */
    @Column(name = "PLEDGE_ORG")
    public String getPledgeOrg() {
        return pledgeOrg;
    }
    /**
     * 方法: 设置java.lang.String
     *
     * @return: java.lang.String 抵押机构
     */
    public void setPledgeOrg(String pledgeOrg) {
        this.pledgeOrg = pledgeOrg;
    }
    /**
     * 方法: 取得java.math.BigDecimal
     *
     * @return: java.math.BigDecimal 评估净值
     */
    @Column(name = "PLEDGE_NET_WORTH")
    public BigDecimal getPledgeNetWorth() {
        return pledgeNetWorth;
    }
    /**
     * 方法: 设置java.math.BigDecimal
     *
     * @return: java.math.BigDecimal 评估净值
     */
    public void setPledgeNetWorth(BigDecimal pledgeNetWorth) {
        this.pledgeNetWorth = pledgeNetWorth;
    }
    /**
     * 方法: 取得java.lang.String
     *
     * @return: java.lang.String 土地证号
     */
    @Column(name = "LAND_CARD_NO")
    public String getLandCardNo() {
        return landCardNo;
    }
    /**
     * 方法: 设置java.lang.String
     *
     * @return: java.lang.String 土地证号
     */
    public void setLandCardNo(String landCardNo) {
        this.landCardNo = landCardNo;
    }
    /**
     * 方法: 取得java.math.BigDecimal
     *
     * @return: java.math.BigDecimal 抵押面积
     */
    @Column(name = "PLEDGE_AREA")
    public BigDecimal getPledgeArea() {
        return pledgeArea;
    }
    /**
     * 方法: 设置java.math.BigDecimal
     *
     * @return: java.math.BigDecimal 抵押面积
     */
    public void setPledgeArea(BigDecimal pledgeArea) {
        this.pledgeArea = pledgeArea;
    }
    /**
     * 方法: 取得java.lang.String
     *
     * @return: java.lang.String 宗地号
     */
    @Column(name = "LAND_NO")
    public String getLandNo() {
        return landNo;
    }
    /**
     * 方法: 设置java.lang.String
     *
     * @return: java.lang.String 宗地号
     */
    public void setLandNo(String landNo) {
        this.landNo = landNo;
    }
    /**
     * 方法: 取得java.math.BigDecimal
     *
     * @return: java.math.BigDecimal 宗地面积
     */
    @Column(name = "LAND_AREA")
    public BigDecimal getLandArea() {
        return landArea;
    }
    /**
     * 方法: 设置java.math.BigDecimal
     *
     * @return: java.math.BigDecimal 宗地面积
     */
    public void setLandArea(BigDecimal landArea) {
        this.landArea = landArea;
    }
    /**
     * 方法: 取得java.lang.String
     *
     * @return: java.lang.String 备注
     */
    @Column(name = "RMK")
    public String getRmk() {
        return rmk;
    }
    @Column(name = "CORP_NAME")
    public String getCorpName() {
        return corpName;
    }
    public void setCorpName(String corpName) {
        this.corpName = corpName;
    }
    @Column(name = "CUR_NAME")
    public String getCurName() {
        return curName;
    }
    public void setCurName(String curName) {
        this.curName = curName;
    }
    @Column(name = "REG_CORP_NAME")
    public String getRegCorpName() {
        return regCorpName;
    }
    public void setRegCorpName(String regCorpName) {
        this.regCorpName = regCorpName;
    }
    /**
     * 方法: 设置java.lang.String
     *
     * @return: java.lang.String 备注
     */
    public void setRmk(String rmk) {
        this.rmk = rmk;
    }
}
