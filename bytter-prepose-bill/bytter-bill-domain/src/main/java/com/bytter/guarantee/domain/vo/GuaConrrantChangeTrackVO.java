package com.bytter.guarantee.domain.vo;
import java.io.Serializable;
import com.bytter.core.annotation.Dict;
import com.bytter.poi.handler.ExcelModel;
import com.fasterxml.jackson.annotation.JsonFormat;
public class GuaConrrantChangeTrackVO extends ExcelModel implements Serializable {
    /** 担保id */
    private String guaId;
    /** 担保编号 */
    private String guaNo;
    /** 被担保合同编号 */
    private String contractCode;
    /** 抵质押物id */
    private String pledgeId;
    /** 方式名称 */
    private String guaWayName;
    /** 占用或释放金额 */
    private java.math.BigDecimal money;
    /** 类型 */
    private String trackType;
    /** 类型名称 */
    private String typeName;
    /** 创建日期 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private java.util.Date createTime;
    /** 创建人登录名称 */
    private String createBy;
    /** 抵押物编号 */
    private String pledgeNo;
    /** 抵质押物名称 */
    private String pledgeName;
    /** 评估价值 */
    private java.math.BigDecimal pledgeMoney;
    /** 开始日期 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    private java.util.Date startDate;
    /** 结束日期 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    private java.util.Date endDate;
    /** 抵质押物性质 */
    private String pledgeAttr;
    /** 担保类型 */
    @Dict(dictGroup = "Gua.conType", inject = "conTypeName")
    private String conType;
    /** 担保类型名称 */
    private String conTypeName;
    public String getGuaId() {
        return guaId;
    }
    public void setGuaId(String guaId) {
        this.guaId = guaId;
    }
    public String getGuaNo() {
        return guaNo;
    }
    public void setGuaNo(String guaNo) {
        this.guaNo = guaNo;
    }
    public String getContractCode() {
        return contractCode;
    }
    public void setContractCode(String contractCode) {
        this.contractCode = contractCode;
    }
    public String getPledgeId() {
        return pledgeId;
    }
    public void setPledgeId(String pledgeId) {
        this.pledgeId = pledgeId;
    }
    public java.math.BigDecimal getMoney() {
        return money;
    }
    public void setMoney(java.math.BigDecimal money) {
        this.money = money;
    }
    public String getTrackType() {
        return trackType;
    }
    public void setTrackType(String trackType) {
        this.trackType = trackType;
    }
    public java.util.Date getCreateTime() {
        return createTime;
    }
    public void setCreateTime(java.util.Date createTime) {
        this.createTime = createTime;
    }
    public String getCreateBy() {
        return createBy;
    }
    public void setCreateBy(String createBy) {
        this.createBy = createBy;
    }
    public String getPledgeNo() {
        return pledgeNo;
    }
    public void setPledgeNo(String pledgeNo) {
        this.pledgeNo = pledgeNo;
    }
    public String getPledgeName() {
        return pledgeName;
    }
    public void setPledgeName(String pledgeName) {
        this.pledgeName = pledgeName;
    }
    public java.math.BigDecimal getPledgeMoney() {
        return pledgeMoney;
    }
    public void setPledgeMoney(java.math.BigDecimal pledgeMoney) {
        this.pledgeMoney = pledgeMoney;
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
    public String getTypeName() {
        return typeName;
    }
    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }
    public String getPledgeAttr() {
        return pledgeAttr;
    }
    public void setPledgeAttr(String pledgeAttr) {
        this.pledgeAttr = pledgeAttr;
    }
    public String getGuaWayName() {
        return guaWayName;
    }
    public void setGuaWayName(String guaWayName) {
        this.guaWayName = guaWayName;
    }
    public String getConType() {
        return conType;
    }
    public void setConType(String conType) {
        this.conType = conType;
    }
    public String getConTypeName() {
        return conTypeName;
    }
    public void setConTypeName(String conTypeName) {
        this.conTypeName = conTypeName;
    }
}
