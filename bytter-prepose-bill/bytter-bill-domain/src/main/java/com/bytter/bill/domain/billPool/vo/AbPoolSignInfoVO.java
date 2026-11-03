package com.bytter.bill.domain.billPool.vo;
import com.bytter.core.annotation.BtCompositeSearch;
import com.bytter.core.annotation.Dict;
import com.fasterxml.jackson.annotation.JsonFormat;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
/**
 * @Title: AbPoolSignInfoVO
 * @Description: 票据池签约信息VO
 * @author zhengg
 * @date 2020-03-13 15:25:10
 * @version 1.0
 *
 */
public class AbPoolSignInfoVO  implements Serializable {
	private static final long serialVersionUID = 1L;
	/**流水Id*/
    private String id;
    /**单位Id*/
    @BtCompositeSearch(fieldName="pc.corp_id")
    private String corpId;
    
    /**单位代码*/
    private String corpCode;
    
    /**单位名称*/
    @BtCompositeSearch(fieldName="pc.corp_name")
    private String corpName;
    
    /**合同号*/
    @BtCompositeSearch(fieldName="s.con_no")
    private String conNo;
    
    /**合同日期*/
    @BtCompositeSearch(fieldName="s.con_date")
    @JsonFormat(pattern="yyyy-MM-dd")
    private Date conDate;
    
    /**签收日期*/
    @BtCompositeSearch(fieldName="s.sign_date")
	@JsonFormat(pattern="yyyy-MM-dd")
    private Date signDate;
    
    /**签约机构*/
    @BtCompositeSearch(fieldName="s.sign_org_name")
    private String signOrgName;
    
    /**票据池状态 0-已解约 1-已签约*/
    @Dict(dictGroup = "AB_POOL_SIGN_STATUS", inject = "validSignName")
    private String validSign;
    @BtCompositeSearch(fieldName="s.valid_sign")
    private String validSignName;
    /**保证金账户*/
    @BtCompositeSearch(fieldName="s.bank_acc")
    private String bailAcc;
    
    /**保证金余额*/
    @BtCompositeSearch(fieldName="s.bail_money")
    private BigDecimal bailMoney;
    
    /**票据池编号*/
    @BtCompositeSearch(fieldName="p.id")
    private String poolNo;
    
    /**票据池名称*/
    @BtCompositeSearch(fieldName="p.bp_name")
    private String poolName;
    
    /**企业对应票据池机构号*/
    @BtCompositeSearch(fieldName="s.orig_code")
    private String origCode;
    
    /**企业对应票据池机构名称*/
    @BtCompositeSearch(fieldName="s.orig_name")
    private String origName;
    /**
     * 创建日期
     */
	@JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @BtCompositeSearch(fieldName="s.create_Time")
    private Date createTime;
    /**
     * 修改日期
     */
	@JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @BtCompositeSearch(fieldName="s.update_time")
    private Date updateTime;
    
    
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
	public String getCorpCode() {
		return corpCode;
	}
	public void setCorpCode(String corpCode) {
		this.corpCode = corpCode;
	}
	public String getCorpName() {
		return corpName;
	}
	public void setCorpName(String corpName) {
		this.corpName = corpName;
	}
	public String getConNo() {
		return conNo;
	}
	public void setConNo(String conNo) {
		this.conNo = conNo;
	}
	public Date getSignDate() {
		return signDate;
	}
	public void setSignDate(Date signDate) {
		this.signDate = signDate;
	}
	public String getSignOrgName() {
		return signOrgName;
	}
	public void setSignOrgName(String signOrgName) {
		this.signOrgName = signOrgName;
	}
	public String getValidSign() {
		return validSign;
	}
	public void setValidSign(String validSign) {
		this.validSign = validSign;
	}
	public String getValidSignName() {
		return validSignName;
	}
	public void setValidSignName(String validSignName) {
		this.validSignName = validSignName;
	}
	public String getBailAcc() {
		return bailAcc;
	}
	public void setBailAcc(String bailAcc) {
		this.bailAcc = bailAcc;
	}
	public BigDecimal getBailMoney() {
		return bailMoney;
	}
	public void setBailMoney(BigDecimal bailMoney) {
		this.bailMoney = bailMoney;
	}
	public String getPoolNo() {
		return poolNo;
	}
	public void setPoolNo(String poolNo) {
		this.poolNo = poolNo;
	}
	public String getPoolName() {
		return poolName;
	}
	public void setPoolName(String poolName) {
		this.poolName = poolName;
	}
	public String getOrigCode() {
		return origCode;
	}
	public void setOrigCode(String origCode) {
		this.origCode = origCode;
	}
	public String getOrigName() {
		return origName;
	}
	public void setOrigName(String origName) {
		this.origName = origName;
	}
	public Date getConDate() {
		return conDate;
	}
	public void setConDate(Date conDate) {
		this.conDate = conDate;
	}
	public Date getCreateTime() {
		return createTime;
	}
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
	public Date getUpdateTime() {
		return updateTime;
	}
	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}
	@Override
	public String toString() {
		return "AbPoolSignInfoVO{" +
				"id='" + id + '\'' +
				", corpId='" + corpId + '\'' +
				", corpCode='" + corpCode + '\'' +
				", corpName='" + corpName + '\'' +
				", conNo='" + conNo + '\'' +
				", conDate=" + conDate +
				", signDate=" + signDate +
				", signOrgName='" + signOrgName + '\'' +
				", validSign='" + validSign + '\'' +
				", validSignName='" + validSignName + '\'' +
				", bailAcc='" + bailAcc + '\'' +
				", bailMoney=" + bailMoney +
				", poolNo='" + poolNo + '\'' +
				", poolName='" + poolName + '\'' +
				", origCode='" + origCode + '\'' +
				", origName='" + origName + '\'' +
				", createTime=" + createTime +
				", updateTime=" + updateTime +
				'}';
	}
}
