package com.bytter.biz.inneraccount.vo;
import com.bytter.core.annotation.BtCompositeSearch;
import com.bytter.core.annotation.Corp;
import com.bytter.core.annotation.Currency;
import com.bytter.core.annotation.Dict;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
/**
 * Created by zhangxb on 2017/6/6.
 */
@ApiModel("账户冻结查询列表信息")
public class FcFreezeSearchListVO implements Serializable {
    @ApiModelProperty(value = "主键ID")
    private String id;
    @BtCompositeSearch(fieldName = "t.blnctr_acc")
    @ApiModelProperty(value = "余额账户")
    private String blnctrAcc;
    @BtCompositeSearch(fieldName = "f.acc_name")
    @ApiModelProperty(value = "账户名称")
    private String accName;
    @BtCompositeSearch(fieldName = "f.net_Id")
    @Corp(inject="netName")
    @ApiModelProperty(value = "网点ID")
    private String netId;
    @BtCompositeSearch(fieldName = "f.net_Id")
    @ApiModelProperty(value = "网点名称")
    private String netName;
    @BtCompositeSearch(fieldName = "f.corp_Id")
    @Corp(inject="corpName")
    @ApiModelProperty(value = "单位ID")
    private String corpId;
    @BtCompositeSearch(fieldName = "f.corp_Id")
    @ApiModelProperty(value = "单位名称")
    private String corpName;
    @BtCompositeSearch(fieldName = "f.cur_Id")
    @Currency(inject="curName")
    @ApiModelProperty(value = "币种ID")
    private String curId;
    @BtCompositeSearch(fieldName = "f.cur_Id")
    @ApiModelProperty(value = "币种名称")
    private String curName;
    @BtCompositeSearch(fieldName = "t.freeze_Type")
    @Dict(dictGroup = "fcFreeze.freezeType", inject = "freezeTypeName")
    @ApiModelProperty(value = "冻结类型")
    private String freezeType;
    @BtCompositeSearch(fieldName = "t.freeze_Type")
    @ApiModelProperty(value = "冻结类型名称")
    private String freezeTypeName;
    @BtCompositeSearch(fieldName = "t.begin_Date")
    @JsonFormat(pattern = "yyyy-MM-dd")
    @ApiModelProperty(value = "开始日期")
    private Date beginDate;
    @BtCompositeSearch(fieldName = "t.end_Date")
    @JsonFormat(pattern = "yyyy-MM-dd")
    @ApiModelProperty(value = "结束日期")
    private Date endDate;
    @BtCompositeSearch(fieldName = "t.freeze_Money")
    @ApiModelProperty(value = "冻结金额")
    private BigDecimal freezeMoney;
    @BtCompositeSearch(fieldName = "t.accu_Freeze_Money")
    @ApiModelProperty(value = "累计冻结金额")
    private BigDecimal accuFreezeMoney;
    @ApiModelProperty(value = "可用金额")
    private BigDecimal activeMoney;
    @ApiModelProperty(value = "解冻金额")
    private BigDecimal unFreezeMoney;
    @BtCompositeSearch(fieldName = "t.freeze_No")
    @ApiModelProperty(value = "冻结编号")
    private String freezeNo;
    @BtCompositeSearch(fieldName = "ut.freeze_no")
    @ApiModelProperty(value = "原冻结编号")
    private String oldFreezeNo;
    @ApiModelProperty(value = "冻结原因")
    private String freezeReason;
    @ApiModelProperty(value = "执行人员编号")
    private String execOfficialNo;
    @ApiModelProperty(value = "执行证件号")
    private String execCardNo;
    @ApiModelProperty(value = "冻结机构")
    private String freezeOrg;
    @ApiModelProperty(value = "解冻ID")
    private String unFreezeId;
    /**
     * 冻结(解冻)标志
     */
    @Dict(dictGroup = "innerAccount.freezeSign", inject = "freezeSignName")
    @BtCompositeSearch(fieldName = "t.freeze_Sign")
    @ApiModelProperty(value = "冻结(解冻)标志")
    private String freezeSign;
    @BtCompositeSearch(fieldName = "t.freeze_Sign")
    @ApiModelProperty(value = "冻结(解冻)标志名称")
    private String freezeSignName;
    @ApiModelProperty(value = "创建人")
    private String createBy;
    @ApiModelProperty(value = "复核员代码")
    private String checkerCode;
    @BtCompositeSearch(fieldName = "t.operate_Time")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "操作时间")
    private Date operateTime;
    @Dict(dictGroup = "fcfreeze.status", inject = "stateName")
    @BtCompositeSearch(fieldName = "t.status")
    @ApiModelProperty(value = "状态")
    private Integer status;
    @BtCompositeSearch(fieldName = "t.status")
    @ApiModelProperty(value = "状态名称")
    private String stateName;
    public String getOldFreezeNo() {
        return oldFreezeNo;
    }
    public void setOldFreezeNo(String oldFreezeNo) {
        this.oldFreezeNo = oldFreezeNo;
    }
    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }
    public String getCreateBy() {
        return createBy;
    }
    public void setCreateBy(String createBy) {
        this.createBy = createBy;
    }
    public String getCheckerCode() {
        return checkerCode;
    }
    public void setCheckerCode(String checkerCode) {
        this.checkerCode = checkerCode;
    }
    public Date getOperateTime() {
        return operateTime;
    }
    public void setOperateTime(Date operateTime) {
        this.operateTime = operateTime;
    }
    public String getFreezeSign() {
        return freezeSign;
    }
    public void setFreezeSign(String freezeSign) {
        this.freezeSign = freezeSign;
    }
    public String getFreezeSignName() {
        return freezeSignName;
    }
    public void setFreezeSignName(String freezeSignName) {
        this.freezeSignName = freezeSignName;
    }
    public String getUnFreezeId() {
        return unFreezeId;
    }
    public void setUnFreezeId(String unFreezeId) {
        this.unFreezeId = unFreezeId;
    }
    public BigDecimal getUnFreezeMoney() {
        return unFreezeMoney;
    }
    public void setUnFreezeMoney(BigDecimal unFreezeMoney) {
        this.unFreezeMoney = unFreezeMoney;
    }
    public String getExecOfficialNo() {
        return execOfficialNo;
    }
    public void setExecOfficialNo(String execOfficialNo) {
        this.execOfficialNo = execOfficialNo;
    }
    public String getExecCardNo() {
        return execCardNo;
    }
    public void setExecCardNo(String execCardNo) {
        this.execCardNo = execCardNo;
    }
    public String getFreezeOrg() {
        return freezeOrg;
    }
    public void setFreezeOrg(String freezeOrg) {
        this.freezeOrg = freezeOrg;
    }
    public String getFreezeNo() {
        return freezeNo;
    }
    public void setFreezeNo(String freezeNo) {
        this.freezeNo = freezeNo;
    }
    public String getFreezeReason() {
        return freezeReason;
    }
    public void setFreezeReason(String freezeReason) {
        this.freezeReason = freezeReason;
    }
    public BigDecimal getActiveMoney() {
        return activeMoney;
    }
    public void setActiveMoney(BigDecimal activeMoney) {
        this.activeMoney = activeMoney;
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
    public String getFreezeType() {
        return freezeType;
    }
    public void setFreezeType(String freezeType) {
        this.freezeType = freezeType;
    }
    public String getFreezeTypeName() {
        return freezeTypeName;
    }
    public void setFreezeTypeName(String freezeTypeName) {
        this.freezeTypeName = freezeTypeName;
    }
    public Date getBeginDate() {
        return beginDate;
    }
    public void setBeginDate(Date beginDate) {
        this.beginDate = beginDate;
    }
    public Date getEndDate() {
        return endDate;
    }
    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }
    public BigDecimal getFreezeMoney() {
        return freezeMoney;
    }
    public void setFreezeMoney(BigDecimal freezeMoney) {
        this.freezeMoney = freezeMoney;
    }
    public BigDecimal getAccuFreezeMoney() {
        return accuFreezeMoney;
    }
    public void setAccuFreezeMoney(BigDecimal accuFreezeMoney) {
        this.accuFreezeMoney = accuFreezeMoney;
    }
    public Integer getStatus() {
        return status;
    }
    public void setStatus(Integer status) {
        this.status = status;
    }
    public String getStateName() {
        return stateName;
    }
    public void setStateName(String stateName) {
        this.stateName = stateName;
    }
}
