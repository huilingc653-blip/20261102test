package com.bytter.basic.vo;
import com.bytter.core.annotation.Dict;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
/**
 * Created by zhangxb on 2017/7/11.
 */
@ApiModel("FcInstrateAdjustSimpleVO")
public class FcInstrateAdjustSimpleVO implements Serializable {
    @ApiModelProperty("ID")
    private String id;

    /**
     * 账户
     */
    @Dict(tableName = "fc_acc", valueFieldName = "BLNCTR_ACC", showFiledName = "ACC_NAME", inject = "accName")
    @ApiModelProperty("账户")
    private String account;

    @ApiModelProperty("账户名称")
    private String accName;

    /**
     * 原利率
     */
    @ApiModelProperty("原利率")
    private BigDecimal oldInstrate;

    /**
     * 新利率
     */
    @ApiModelProperty("新利率")
    private BigDecimal newInstrate;

    /**
     * 利率类型
     */
    @Dict(dictGroup = "Common.InstrtType", inject = "nterestName")
    @ApiModelProperty("利率类型")
    private String instrtType;

    @ApiModelProperty("利率类型名称")
    private String interestName;

    @ApiModelProperty("调整类型")
    @Dict(dictGroup = "interestRateType", inject = "changtypeName")
    private String changtype;
    private String changtypeName;

    /**
     * 调整人
     */
    @ApiModelProperty("调整人")
    private String operatorCode;

    /**
     * 生效日期
     */
    @ApiModelProperty("生效日期")
    private Date adjustTime;

    @ApiModelProperty("上次调整日期")
    private Date lastAdjustTime;

    @ApiModelProperty("网点ID")
    private String netId;

    @ApiModelProperty("网点名称")
    private String netName;

    @ApiModelProperty("单位ID")
    private String corpId;

    @ApiModelProperty("单位名称")
    private String corpName;

    @ApiModelProperty("货币ID")
    private String curId;

    @ApiModelProperty("货币名称")
    private String curName;

    @ApiModelProperty("科目ID")
    private String subjectId;

    @ApiModelProperty("科目名称")
    private String subjectName;
    /**
     * 账号启用日期
     */
    private Date accStartDate;
    public String getChangtype() {
        return changtype;
    }
    public void setChangtype(String changtype) {
        this.changtype = changtype;
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
    public Date getLastAdjustTime() {
        return lastAdjustTime;
    }
    public void setLastAdjustTime(Date lastAdjustTime) {
        this.lastAdjustTime = lastAdjustTime;
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
    public String getAccName() {
        return accName;
    }
    public void setAccName(String accName) {
        this.accName = accName;
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
    public Date getAccStartDate() {
        return accStartDate;
    }
    public void setAccStartDate(Date accStartDate) {
        this.accStartDate = accStartDate;
    }


    public String getInterestName() {
        return interestName;
    }

    public void setInterestName(String interestName) {
        this.interestName = interestName;
    }

    public String getChangtypeName() {
        return changtypeName;
    }

    public void setChangtypeName(String changtypeName) {
        this.changtypeName = changtypeName;
    }
}
