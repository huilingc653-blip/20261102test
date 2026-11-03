package com.bytter.system.api.vo;
import com.bytter.core.annotation.*;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;
import java.util.Date;
/**
 * Created by zhangxb on 2017/6/6.
 */
@ApiModel("账号信息")
public class FcAccListVO implements Serializable {
    @ApiModelProperty(value = "主键ID")
    private String id;
    @ApiModelProperty(value = "内部账号")
    @BtCompositeSearch(fieldName = "blnctr_acc")
    private String blnctrAcc;
    @ApiModelProperty(value = "账号名称")
    @BtCompositeSearch(fieldName = "acc_Name")
    private String accName;
    /**
     * 网点
     */
    @ApiModelProperty(value = "网点")
    @BtCompositeSearch(fieldName = "net_Id")
    @Corp(inject="netName")
    private String netId;
    @ApiModelProperty(value = "网点名称")
    @BtCompositeSearch(fieldName = "net_Id")
    private String netName;
    /**
     * 资金池
     */
    @ApiModelProperty(value = "资金池")
    @BtCompositeSearch(fieldName = "net_Id")
    @CorpPool(inject="netPoolName")
    private String netPoolId;
    @ApiModelProperty(value = "资金池名称")
    private String netPoolName;
    @ApiModelProperty(value = "单位ID")
    @BtCompositeSearch(fieldName = "corp_Id")
    @Corp(inject="corpName")
    private String corpId;
    @ApiModelProperty(value = "单位名称")
    @BtCompositeSearch(fieldName = "corp_Id")
    private String corpName;
    @ApiModelProperty(value = "币种ID")
    @BtCompositeSearch(fieldName = "cur_Id")
    @Currency(inject="curName")
    private String curId;
    @ApiModelProperty(value = "币种名称")
    @BtCompositeSearch(fieldName = "cur_Id")
    private String curName;
    @ApiModelProperty(value = "科目ID")
    //@Dict(tableName="FC_SUBJECT",valueFieldName="id",showFiledName="SUBJECT_NAME",inject="subjectName")
    //@BtCompositeSearch(fieldName = "subject_Id")
    private String subjectId;
    @ApiModelProperty(value = "科目名称")
    @BtCompositeSearch(fieldName = "f.subject_name")
    private String subjectName;
    
    @ApiModelProperty(value = "系统科目代码")
    private String sysSubjectCode;
    @ApiModelProperty(value = "账号类型")
    @BtCompositeSearch(fieldName = "acc_Type")
    @Dict(dictGroup = "InnerAccount.AccType", inject = "accTypeName")
    private String accType;
    @ApiModelProperty(value = "账号类型名称")
    @BtCompositeSearch(fieldName = "acc_Type")
    private String accTypeName;
    @ApiModelProperty(value = "余额方式")
    @BtCompositeSearch(fieldName = "balance_Way")
    @Dict(dictGroup = "Admin.BalanceWay", inject = "balanceWayName")
    private Integer balanceWay;
    @ApiModelProperty(value = "余额方式名称")
    @BtCompositeSearch(fieldName = "balance_Way")
    private String balanceWayName;
    @ApiModelProperty(value = "账号状态")
    @Dict(dictGroup = "inneraccount.fcStatus", inject = "accStateName")
    @BtCompositeSearch(fieldName = "acc_State")
    private String accState;
    @ApiModelProperty(value = "账号状态名称")
    @BtCompositeSearch(fieldName = "acc_State")
    private String accStateName;
    @ApiModelProperty(value = "账号开通时间")
    @BtCompositeSearch(fieldName = "acc_Open_Time")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date accOpenTime;
    /**
     * 资金池标识 0-非资金池；1-资金池
     */
    @ApiModelProperty(value = "资金池标识 0-非资金池；1-资金池")
    @BtCompositeSearch(fieldName = "pool_sign")
    private String poolSign;
    /**
     * 银行代码
     */
    @ApiModelProperty(value = "银行代码")
    private String bankCode;
    /**
     * 银行类型id
     */
    @ApiModelProperty(value = "银行类型id")
    private String bankTypeId;
    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }
    public Date getAccOpenTime() {
        return accOpenTime;
    }
    public void setAccOpenTime(Date accOpenTime) {
        this.accOpenTime = accOpenTime;
    }
    public String getNetName() {
        return netName;
    }
    public void setNetName(String netName) {
        this.netName = netName;
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
    public String getCorpName() {
        return corpName;
    }
    public void setCorpName(String corpName) {
        this.corpName = corpName;
    }
    public String getCurName() {
        return curName;
    }
    public void setCurName(String curName) {
        this.curName = curName;
    }
    public String getSubjectName() {
        return subjectName;
    }
    public void setSubjectName(String subjectName) {
        this.subjectName = subjectName;
    }
    public String getAccTypeName() {
        return accTypeName;
    }
    public void setAccTypeName(String accTypeName) {
        this.accTypeName = accTypeName;
    }
    public String getBalanceWayName() {
        return balanceWayName;
    }
    public void setBalanceWayName(String balanceWayName) {
        this.balanceWayName = balanceWayName;
    }
    public String getAccStateName() {
        return accStateName;
    }
    public void setAccStateName(String accStateName) {
        this.accStateName = accStateName;
    }
    public String getBlnctrAcc() {
        return blnctrAcc;
    }
    public void setBlnctrAcc(String blnctrAcc) {
        this.blnctrAcc = blnctrAcc;
    }
    public String getAccName() {
        return accName;
    }
    public void setAccName(String accName) {
        this.accName = accName;
    }
    public String getNetId() {
        return netId;
    }
    public void setNetId(String netId) {
        this.netId = netId;
    }
    public String getCorpId() {
        return corpId;
    }
    public void setCorpId(String corpId) {
        this.corpId = corpId;
    }
    public String getCurId() {
        return curId;
    }
    public void setCurId(String curId) {
        this.curId = curId;
    }
    public String getSubjectId() {
        return subjectId;
    }
    public void setSubjectId(String subjectId) {
        this.subjectId = subjectId;
    }
    public String getSysSubjectCode() {
        return sysSubjectCode;
    }
    public void setSysSubjectCode(String sysSubjectCode) {
        this.sysSubjectCode = sysSubjectCode;
    }
    public String getAccType() {
        return accType;
    }
    public void setAccType(String accType) {
        this.accType = accType;
    }
    public Integer getBalanceWay() {
        return balanceWay;
    }
    public void setBalanceWay(Integer balanceWay) {
        this.balanceWay = balanceWay;
    }
    public String getAccState() {
        return accState;
    }
    public void setAccState(String accState) {
        this.accState = accState;
    }
    public String getPoolSign() {
        return poolSign;
    }
    public void setPoolSign(String poolSign) {
        this.poolSign = poolSign;
    }
    public String getBankCode() {
        return bankCode;
    }
    public void setBankCode(String bankCode) {
        this.bankCode = bankCode;
    }
    public String getBankTypeId() {
        return bankTypeId;
    }
    public void setBankTypeId(String bankTypeId) {
        this.bankTypeId = bankTypeId;
    }
}
