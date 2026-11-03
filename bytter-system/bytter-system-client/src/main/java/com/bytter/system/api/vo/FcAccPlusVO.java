package com.bytter.system.api.vo;
import com.bytter.core.annotation.Dict;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
/**
 * Created by zhangxb on 2017/11/8.
 */
public class FcAccPlusVO implements Serializable {
    @ApiModelProperty(value = "主键ID")
    private String id;
    @ApiModelProperty(value = "内部账号")
    private String blnctrAcc;
    @ApiModelProperty(value = "账号名称")
    private String accName;
    @ApiModelProperty(value = "网点ID")
    private String netId;
    @ApiModelProperty(value = "单位ID")
    private String corpId;
    @ApiModelProperty(value = "币种ID")
    private String curId;
    @ApiModelProperty(value = "科目ID")
    @Dict(tableName="FC_SUBJECT",valueFieldName="id",showFiledName="SUBJECT_NAME",inject="subjectName")
    private String subjectId;
    @ApiModelProperty(value = "账号类型")
    private String accType;
    @ApiModelProperty(value = "余额方式")
    private Integer balanceWay;
    @ApiModelProperty(value = "单位名称")
    private String corpName;
    @ApiModelProperty(value = "网点名称")
    private String netName;
    @ApiModelProperty(value = "账号状态")
    private String accState;
    @ApiModelProperty(value = "科目名称")
    private String subjectName;
    @ApiModelProperty(value = "币种名称")
    private String curName;
    @ApiModelProperty(value = "账号开通时间")
    private Date accOpenTime;
    @ApiModelProperty(value = "内部账户余额")
    private BigDecimal fcBalance;
    @ApiModelProperty(value = "起息日期")
    private Date interestStartDate;
    @ApiModelProperty(value = "账号启用日期")
    private Date accStartDate;
    @ApiModelProperty(value = "到期日期")
    private Date deadlineDate;
    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
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
    public String getCorpName() {
        return corpName;
    }
    public void setCorpName(String corpName) {
        this.corpName = corpName;
    }
    public String getNetName() {
        return netName;
    }
    public void setNetName(String netName) {
        this.netName = netName;
    }
    public String getAccState() {
        return accState;
    }
    public void setAccState(String accState) {
        this.accState = accState;
    }
    public String getSubjectName() {
        return subjectName;
    }
    public void setSubjectName(String subjectName) {
        this.subjectName = subjectName;
    }
    public String getCurName() {
        return curName;
    }
    public void setCurName(String curName) {
        this.curName = curName;
    }
	public Date getAccOpenTime() {
		return accOpenTime;
	}
	public void setAccOpenTime(Date accOpenTime) {
		this.accOpenTime = accOpenTime;
	}
	public BigDecimal getFcBalance() {
		return fcBalance;
	}
	public void setFcBalance(BigDecimal fcBalance) {
		this.fcBalance = fcBalance;
	}
    public Date getInterestStartDate() {
        return interestStartDate;
    }
    public void setInterestStartDate(Date interestStartDate) {
        this.interestStartDate = interestStartDate;
    }
    public Date getAccStartDate() {
        return accStartDate;
    }
    public void setAccStartDate(Date accStartDate) {
        this.accStartDate = accStartDate;
    }
    public Date getDeadlineDate() {
        return deadlineDate;
    }
    public void setDeadlineDate(Date deadlineDate) {
        this.deadlineDate = deadlineDate;
    }
}
