package com.bytter.basic.vo;
import com.bytter.core.annotation.*;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
/**
 * Created by zhangxb on 2017/7/11.
 */
@ApiModel("FcInstrateAdjustVO")
public class FcInstrateAdjustVO implements Serializable {
    @ApiModelProperty("id")
    private String id;

    /**
     * 账户
     */
    @BtCompositeSearch(fieldName = "t.account")
    @ApiModelProperty("账户")
    private String account;

    @BtCompositeSearch(fieldName = "f.net_Id")
    @Corp(inject = "netName")
    @ApiModelProperty("网络ID")
    private String netId;

    @ApiModelProperty("网络名称")
    private String netName;

    @BtCompositeSearch(fieldName = "f.cur_Id")
    @Currency(inject = "curName")
    @ApiModelProperty("货币ID")
    private String curId;

    @ApiModelProperty("货币名称")
    private String curName;

    @BtCompositeSearch(fieldName = "f.subject_Id")
   // @Dict(tableName = "fc_subject", valueFieldName = "ID", showFiledName = "SUBJECT_NAME", inject = "subjectName")
    @ApiModelProperty("科目ID")
    private String subjectId;

    @ApiModelProperty("科目名称")
    private String subjectName;

    /**
     * 原利率
     */
    @BtCompositeSearch(fieldName = "t.old_Instrate")
    @ApiModelProperty("原利率")
    private BigDecimal oldInstrate;

    /**
     * 新利率
     */
    @BtCompositeSearch(fieldName = "t.new_Instrate")
    @ApiModelProperty("新利率")
    private BigDecimal newInstrate;

    /**
     * 利率类型
     */
    @BtCompositeSearch(fieldName = "t.instrt_Type")
    @Dict(dictGroup = "Common.InstrtType", inject = "interestName")
    @ApiModelProperty("利率类型")
    private String instrtType;

    @ApiModelProperty("利率类型名称")
    private String interestName;

    /**
     * 调整类型
     */
    @Dict(dictGroup = "interestRateType", inject = "changTypeName")
    @ApiModelProperty("调整类型")
    private String changType;

    @ApiModelProperty("调整类型名称")
    private String changTypeName;

    /**
     * 备注
     */
    @ApiModelProperty("备注")
    private String rmk;

    /**
     * 调整人
     */
    @ApiModelProperty("调整人")
    private String operatorCode;

    /**
     * 生效日期
     */
    @BtCompositeSearch(fieldName = "t.adjust_Time")
    @ApiModelProperty("生效日期")
    private Date adjustTime;

    /**
     * 复核人
     */
    @ApiModelProperty("复核人")
    private String checkerCode;

    /**
     * 复核日期
     */
    @ApiModelProperty("复核日期")
    private Date checkTime;

    /**
     * 状态
     */
//dictGroup为字典组，inject为注入当前对象的属性，该属性的类型必须为String
    @BtCompositeSearch(fieldName = "t.status")
    @Dict(dictGroup = "fcfreeze.status", inject = "statusName")
    @ApiModelProperty(value = "状态", example = "0")
    private Integer status;

    @ApiModelProperty("状态名称")
    private String statusName;

    @ApiModelProperty("利率ID")
    private String instrateId;

    /**
     * 资金池标识 0-非资金池；1-资金池
     */
    @ApiModelProperty("资金池标识 0-非资金池；1-资金池")
    private String poolSign;

    /**
     * 资金池ID
     */
    @ApiModelProperty("资金池ID")
    private String poolId;

   // @Dict(tableName = "fc_subject_pool", valueFieldName = "ID", showFiledName = "SUBJECT_NAME", inject = "subjectPoolName")
    @ApiModelProperty("科目池ID")
    private String subjectPoolId;

    @ApiModelProperty("科目池名称")
    private String subjectPoolName;

    @CorpPool(inject = "netPoolName")
    @ApiModelProperty("网络池ID")
    private String netPoolId;

    @ApiModelProperty("网络池名称")
    private String netPoolName;
    public String getInstrateId() {
        return instrateId;
    }
    public void setInstrateId(String instrateId) {
        this.instrateId = instrateId;
    }
    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }
    public String getAccount() {
        return account;
    }
    public void setAccount(String account) {
        this.account = account;
    }
    public String getNetId() {
        return netId;
    }
    public void setNetId(String netId) {
        this.netId = netId;
    }
    public String getNetName() {
        return netName;
    }
    public void setNetName(String netName) {
        this.netName = netName;
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
    public String getSubjectId() {
        return subjectId;
    }
    public void setSubjectId(String subjectId) {
        this.subjectId = subjectId;
    }
    public String getSubjectName() {
        return subjectName;
    }
    public void setSubjectName(String subjectName) {
        this.subjectName = subjectName;
    }
    public BigDecimal getOldInstrate() {
        return oldInstrate;
    }
    public void setOldInstrate(BigDecimal oldInstrate) {
        this.oldInstrate = oldInstrate;
    }
    public BigDecimal getNewInstrate() {
        return newInstrate;
    }
    public void setNewInstrate(BigDecimal newInstrate) {
        this.newInstrate = newInstrate;
    }
    public String getInstrtType() {
        return instrtType;
    }
    public void setInstrtType(String instrtType) {
        this.instrtType = instrtType;
    }
    public String getInterestName() {
        return interestName;
    }
    public void setInterestName(String interestName) {
        this.interestName = interestName;
    }
    public String getChangType() {
        return changType;
    }
    public void setChangType(String changType) {
        this.changType = changType;
    }
    public String getChangTypeName() {
        return changTypeName;
    }
    public void setChangTypeName(String changTypeName) {
        this.changTypeName = changTypeName;
    }
    public String getRmk() {
        return rmk;
    }
    public void setRmk(String rmk) {
        this.rmk = rmk;
    }
    public String getOperatorCode() {
        return operatorCode;
    }
    public void setOperatorCode(String operatorCode) {
        this.operatorCode = operatorCode;
    }
    public Date getAdjustTime() {
        return adjustTime;
    }
    public void setAdjustTime(Date adjustTime) {
        this.adjustTime = adjustTime;
    }
    public String getCheckerCode() {
        return checkerCode;
    }
    public void setCheckerCode(String checkerCode) {
        this.checkerCode = checkerCode;
    }
    public Date getCheckTime() {
        return checkTime;
    }
    public void setCheckTime(Date checkTime) {
        this.checkTime = checkTime;
    }
    public Integer getStatus() {
        return status;
    }
    public void setStatus(Integer status) {
        this.status = status;
    }
    public String getStatusName() {
        return statusName;
    }
    public void setStatusName(String statusName) {
        this.statusName = statusName;
    }
    public String getPoolSign() {
        return poolSign;
    }
    public void setPoolSign(String poolSign) {
        this.poolSign = poolSign;
    }
    public String getPoolId() {
        return poolId;
    }
    public void setPoolId(String poolId) {
        this.poolId = poolId;
    }
    public String getSubjectPoolId() {
        return subjectPoolId;
    }
    public void setSubjectPoolId(String subjectPoolId) {
        this.subjectPoolId = subjectPoolId;
    }
    public String getSubjectPoolName() {
        return subjectPoolName;
    }
    public void setSubjectPoolName(String subjectPoolName) {
        this.subjectPoolName = subjectPoolName;
    }
    public String getNetPoolId() {
        return netPoolId;
    }
    public void setNetPoolId(String netPoolId) {
        this.netPoolId = netPoolId;
    }
    public String getNetPoolName() {
        return netPoolName;
    }
    public void setNetPoolName(String netPoolName) {
        this.netPoolName = netPoolName;
    }
}
