package com.bytter.guarantee.domain.vo;
import com.bytter.core.annotation.BtCompositeSearch;
import com.bytter.core.annotation.Dict;
import com.bytter.core.bpm.IBusinessKey;
import com.bytter.poi.annotation.Excel;
import com.bytter.poi.handler.ExcelModel;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.springframework.format.annotation.DateTimeFormat;
import java.io.Serializable;
import java.math.BigDecimal;
/**
 * @author fusb
 * @version 1.0
 * @Title GuaPledgeInfoVO
 * @Description 抵质押物信息VO
 * @date 2018年12月12日
 */
public class GuaPledgeInfoVO extends ExcelModel implements Serializable, IBusinessKey {
    private static final long serialVersionUID = 8518844566933081153L;
    @BtCompositeSearch(fieldName = "b.id")
    private String id;
    /**
     * 单位ID
     */
    @BtCompositeSearch(fieldName = "b.corp_id")
//    @Corp(inject = "corpName")
    private String corpId;
    /**
     * 单位名称
     */
    @Excel(name = "*抵质押申请单位")
    @BtCompositeSearch(fieldName = "b.corp_id")
    private String corpName;
    /**
     * 抵质押物编号
     */
    @BtCompositeSearch(fieldName = "b.pledge_no")
    private String pledgeNo;
    /**
     * 抵质押物性质
     */
    @BtCompositeSearch(fieldName = "b.pledge_attr")
    private String pledgeAttr;
    /**
     * 抵质押物性质名称
     */
    @Excel(name = "*抵质押性质")
    @BtCompositeSearch(fieldName = "b.pledge_attr")
    private String pledgeAttrName;
    /**
     * 抵质押物类型
     */
    @BtCompositeSearch(fieldName = "b.pledge_type")
    @Dict(tableName = "GUA_PLEDGE_TYPE", valueFieldName = "id", showFiledName = "TYPE_NAME", inject = "typeName")
    private String pledgeType;
    @Excel(name = "*抵质押物类型")
    @BtCompositeSearch(fieldName = "e.type_name")
    private String typeName;
    /**
     * 抵质押物名称
     */
    @Excel(name = "*抵质押物名称")
    @BtCompositeSearch(fieldName = "b.pledge_name")
    private String pledgeName;
    /**
     * 开始日期
     */
    @Excel(name = "*抵质押开始日期", format = "yyyy-MM-dd")
    @BtCompositeSearch(fieldName = "b.start_date")
    @JsonFormat(pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private java.util.Date startDate;
    /**
     * 结束日期
     */
    @Excel(name = "*抵质押结束日期", format = "yyyy-MM-dd")
    @BtCompositeSearch(fieldName = "b.end_date")
    @JsonFormat(pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private java.util.Date endDate;
    /**
     * 币别
     */
//    @Currency(inject = "curName")
    @BtCompositeSearch(fieldName = "b.cur_id")
    private String curId;
    /**
     * 币别名称
     */
    @Excel(name = "*币别")
    @BtCompositeSearch(fieldName = "b.cur_id")
    private String curName;
    /**
     * 评估价值
     */
    @Excel(name = "*评估值", numFormat = "##########.00", align = HorizontalAlignment.RIGHT)
    @BtCompositeSearch(fieldName = "b.pledge_money")
    private BigDecimal pledgeMoney;
    /**
     * 可用价值
     */
    private BigDecimal availMoney;
    /**
     * 出质人
     */
    @Excel(name = "出质人")
    @BtCompositeSearch(fieldName = "b.pledge")
    private String pledge;
    /**
     * 质押权人
     */
    @BtCompositeSearch(fieldName = "b.pledgee")
    private String pledgee;
    /**
     * 资产状况
     */
    @Excel(name = "资产状况")
    private String assetState;
    /**
     * 资产产权证号
     */
    @Excel(name = "资产产权证号")
    private String assetNumber;
    /**
     * 土地面积
     */
    @Excel(name = "土地面积")
    private BigDecimal buildArea;
    /**
     * 容积率建筑面积
     */
    @Excel(name = "容积率建筑面积")
    private BigDecimal buildAreaRate;
    /**
     * 填报单位名称
     */
    @BtCompositeSearch(fieldName = "b.reg_corp")
//    @Corp(inject = "regCorpName")
    private String regCorp;
    /**
     * 填报单位名称
     */
    @Excel(name = "填报单位名称")
    @BtCompositeSearch(fieldName = "b.reg_corp")
    private String regCorpName;
    /**
     * 登记日期
     */
    @Excel(name = "登记日期", format = "yyyy-MM-dd")
    @JsonFormat(pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private java.util.Date regDate;
    /**
     * 工商登记单号
     */
    @Excel(name = "工商登记单号")
    @BtCompositeSearch(fieldName = "b.reg_number")
    private String regNumber;
    /**
     * 抵质押折旧
     */
    @Excel(name = "抵质押折旧", numFormat = "##########.00", align = HorizontalAlignment.RIGHT)
    private BigDecimal pledgeDepreciation;
    /**
     * 抵质押原值
     */
    @Excel(name = "抵质押原值", numFormat = "##########.00", align = HorizontalAlignment.RIGHT)
    private BigDecimal pledgeInitMoney;
    /**
     * 折旧率
     */
    @Excel(name = "折旧率")
    private BigDecimal depreciationRate;
    /**
     * 评估机构
     */
    @Excel(name = "评估机构")
    private String evaluationDep;
    /**
     * 数量
     */
    @Excel(name = "数量")
    private BigDecimal amount;
    /**
     * 规格
     */
    @Excel(name = "规格")
    private String specification;
    /**
     * 存放地点
     */
    @Excel(name = "存放地点")
    private String place;
    /**
     * 保管人
     */
    @Excel(name = "保管人")
    private String keeper;
    /**
     * 调查人
     */
    @Excel(name = "调查人")
    private String investigators;
    /**
     * 状态
     */
    @BtCompositeSearch(fieldName = "b.status")
    private Integer status;
    /**
     * 创建日期
     */
    private java.util.Date createDate;
    /**
     * 创建人登录名称
     */
    @BtCompositeSearch(fieldName = "B.CREATE_BY")
    private String createBy;
    @BtCompositeSearch(fieldName = "B.CREATE_BY")

    private String userName;
    /**
     * 更新日期
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
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
     * 被担保合同登记时挂接的金额
     */
    private BigDecimal guaMoney;
    /**
     * 附件ID
     */
    private String attachmentId;
    /**
     * 抵(质)押权人
     */
    @BtCompositeSearch(fieldName = "b.pledge_org")
    @Excel(name = "*抵(质)押权人")
    private String pledgeOrg;
    /**
     * 评估净值
     */
    @Excel(name = "*市场价值", numFormat = "##########.00", align = HorizontalAlignment.RIGHT)
    @BtCompositeSearch(fieldName = "b.pledge_net_worth")
    private BigDecimal pledgeNetWorth;
    /**
     * 土地证号
     */
    @Excel(name = "土地证号")
    private String landCardNo;
    /**
     * 抵押面积
     */
    @Excel(name = "抵押面积")
    private BigDecimal pledgeArea;
    /**
     * 宗地号
     */
    @Excel(name = "宗地号")
    private String landNo;
    /**
     * 宗地面积
     */
    @Excel(name = "宗地面积")
    private BigDecimal landArea;
    /**
     * 备注
     */
    @Excel(name = "备注")
    private String rmk;
    @Override
    public String getBeanName() {
        return "guaPledgeInfoService";
    }
    @Override
    public String getBusinessKey() {
        return getId();
    }
    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
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
    public String getPledgeAttrName() {
        return pledgeAttrName;
    }
    public void setPledgeAttrName(String pledgeAttrName) {
        this.pledgeAttrName = pledgeAttrName;
    }
    public String getPledgeType() {
        return pledgeType;
    }
    public void setPledgeType(String pledgeType) {
        this.pledgeType = pledgeType;
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
    public String getTypeName() {
        return typeName;
    }
    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }
    public BigDecimal getAvailMoney() {
        return availMoney;
    }
    public void setAvailMoney(BigDecimal availMoney) {
        this.availMoney = availMoney;
    }
    public BigDecimal getGuaMoney() {
        return guaMoney;
    }
    public void setGuaMoney(BigDecimal guaMoney) {
        this.guaMoney = guaMoney;
    }
    public String getAttachmentId() {
        return attachmentId;
    }
    public void setAttachmentId(String attachmentId) {
        this.attachmentId = attachmentId;
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
    public String getUserName() {
        return userName;
    }
    public void setUserName(String userName) {
        this.userName = userName;
    }
}
