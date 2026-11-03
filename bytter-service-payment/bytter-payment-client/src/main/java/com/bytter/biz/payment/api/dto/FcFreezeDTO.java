package com.bytter.biz.payment.api.dto;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
/**   
 * Title: Entity
 * Description: 内部账号冻结解冻
 * @author onlineGenerator
 * @since 2017-06-16 17:31:26
 * @version 1.0
 *
 */
public class FcFreezeDTO implements Serializable {
	private String id;
	private String blnctrAcc;
	private String accName;
	private String netId;
	private String netName;
	private String corpId;
	private String corpName;
	private String curId;
	private String curName;
	private String freezeType;
	private String freezeTypeName;
	private String freezeOperator;
	private Date beginDate;
	private Date endDate;
	private BigDecimal freezeMoney;
	private BigDecimal accuFreezeMoney;
	private BigDecimal activeMoney;
	private BigDecimal unFreezeMoney;
	private String freezeNo;
	private String freezeReason;
	private String execOfficialNo;
	private String execCardNo;
	private String freezeOrg;
	private String unFreezeId;
	private Integer status;
	private String stateName;
	/**
	 * 经办时间
	 */
	private Date operateTime;
	public String getFreezeOperator() {
		return freezeOperator;
	}

	public void setFreezeOperator(String freezeOperator) {
		this.freezeOperator = freezeOperator;
	}

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
	public BigDecimal getActiveMoney() {
		return activeMoney;
	}
	public void setActiveMoney(BigDecimal activeMoney) {
		this.activeMoney = activeMoney;
	}
	public BigDecimal getUnFreezeMoney() {
		return unFreezeMoney;
	}
	public void setUnFreezeMoney(BigDecimal unFreezeMoney) {
		this.unFreezeMoney = unFreezeMoney;
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
	public String getUnFreezeId() {
		return unFreezeId;
	}
	public void setUnFreezeId(String unFreezeId) {
		this.unFreezeId = unFreezeId;
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
	public Date getOperateTime() {
		return operateTime;
	}
	public void setOperateTime(Date operateTime) {
		this.operateTime = operateTime;
	}
}
