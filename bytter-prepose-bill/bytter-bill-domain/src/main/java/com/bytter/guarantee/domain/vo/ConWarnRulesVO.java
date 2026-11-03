package com.bytter.guarantee.domain.vo;
import com.bytter.core.annotation.Dict;
import com.fasterxml.jackson.annotation.JsonFormat;
import java.util.Date;
/**
 * @Title: ConWarnRulesVO
 * @Description: 合同预警规则VO
 * @author wangzy
 * @date 2019/11/4 15:30
 *
 */
public class ConWarnRulesVO implements java.io.Serializable {
    private static final long serialVersionUID = 1L;
    public ConWarnRulesVO() {
    }
    public ConWarnRulesVO(String conTypeId, Integer beforDay, Integer intervalDay, String validSign) {
        this.conTypeId = conTypeId;
        this.beforDay = beforDay;
        this.intervalDay = intervalDay;
        this.validSign = validSign;
    }
    private String id;
    /** 合同类型Id */
    @Dict(tableName = "CON_TYPE", valueFieldName = "ID", showFiledName = "CON_NAME", inject = "typeName")
    private String conTypeId;
    /** 合同类型名称 */
    private String typeName;
    /** 合同到期提前预警天数 */
    private Integer beforDay;
    /** 预警间隔天数 */
    private Integer intervalDay;
    /** 启用状态 是否 */
    private String validSign;
    /** 操作人Id */
    private String opId;
    /** 操作时间 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date opDate;
    /** 合同编号 */
    private String conNo;
    /** 合同Id */
    private String conId;
    /** 合同发起方单位Id */
    private String corpId;
    /** 合同失效日期 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date endDate;
    /** 合同名称 */
    private String conName;
    /** 合同状态 */
    private String status;
    private String[] conTypeIds;
    private Integer[] beforDays;
    private Integer[] intervalDays;
    private String[] validSigns;
    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }
    public String getConTypeId() {
        return conTypeId;
    }
    public String getTypeName() {
        return typeName;
    }
    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }
    public void setConTypeId(String conTypeId) {
        this.conTypeId = conTypeId;
    }
    public Integer getBeforDay() {
        return beforDay;
    }
    public void setBeforDay(Integer beforDay) {
        this.beforDay = beforDay;
    }
    public Integer getIntervalDay() {
        return intervalDay;
    }
    public void setIntervalDay(Integer intervalDay) {
        this.intervalDay = intervalDay;
    }
    public String getValidSign() {
        return validSign;
    }
    public void setValidSign(String validSign) {
        this.validSign = validSign;
    }
    public String getOpId() {
        return opId;
    }
    public void setOpId(String opId) {
        this.opId = opId;
    }
    public Date getOpDate() {
        return opDate;
    }
    public void setOpDate(Date opDate) {
        this.opDate = opDate;
    }
    public String getConNo() {
        return conNo;
    }
    public void setConNo(String conNo) {
        this.conNo = conNo;
    }
    public String getConId() {
        return conId;
    }
    public void setConId(String conId) {
        this.conId = conId;
    }
    public Date getEndDate() {
        return endDate;
    }
    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }
    public String getCorpId() {
        return corpId;
    }
    public void setCorpId(String corpId) {
        this.corpId = corpId;
    }
    public String getConName() {
        return conName;
    }
    public void setConName(String conName) {
        this.conName = conName;
    }
    public String getStatus() {
        return status;
    }
    public void setStatus(String status) {
        this.status = status;
    }
    public String[] getConTypeIds() {
        return conTypeIds;
    }
    public void setConTypeIds(String[] conTypeIds) {
        this.conTypeIds = conTypeIds;
    }
    public Integer[] getBeforDays() {
        return beforDays;
    }
    public void setBeforDays(Integer[] beforDays) {
        this.beforDays = beforDays;
    }
    public Integer[] getIntervalDays() {
        return intervalDays;
    }
    public void setIntervalDays(Integer[] intervalDays) {
        this.intervalDays = intervalDays;
    }
    public String[] getValidSigns() {
        return validSigns;
    }
    public void setValidSigns(String[] validSigns) {
        this.validSigns = validSigns;
    }
    @Override
    public String toString() {
        return "ConWarnRulesVO{" + "conTypeId='" + conTypeId + '\'' + ", beforDay='" + beforDay + '\''
                + ", intervalDay='" + intervalDay + '\'' + ", validSign='" + validSign + '\'' + '}';
    }
}
