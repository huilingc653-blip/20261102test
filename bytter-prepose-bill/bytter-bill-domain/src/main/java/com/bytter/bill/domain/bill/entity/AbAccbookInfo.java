package com.bytter.bill.domain.bill.entity;
import com.bytter.core.base.entity.IdEntity;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.Date;
/**   
 * @Title: Entity
 * @Description: 票据记账信息表
 * @author onlineGenerator
 * @date 2018-09-06
 * @version 1.0
 *
 */
@Entity
@Table(name = "AB_ACCBOOK_INFO", schema = "")
@SuppressWarnings("serial")
public class AbAccbookInfo extends IdEntity implements java.io.Serializable {
	/**流水ID*/
	private String aid;
	/**网点ID*/
	private String netId;
	/**网点名称*/
	private String netName;
	/**单位ID*/
	private String corpId;
	/**单位名称*/
	private String corpName;
	/**记账业务来源类型*/
	private Integer busOriType;
	/**业务规则定义(系统业务品种)*/
	private String sysRaceCode;
	/**是否可以打回 1可以打回0不可以*/
	private String isBack;
	/**创建日期*/
	private Date createDate;
	/**更新日期*/
	private Date updateDate;
	/**
	 * 操作人
	 */
	private String operatorCode;
	@Column(name = "NET_NAME")
	public String getNetName() {
		return netName;
	}
	public void setNetName(String netName) {
		this.netName = netName;
	}
	@Column(name = "CORP_NAME")
	public String getCorpName() {
		return corpName;
	}
	public void setCorpName(String corpName) {
		this.corpName = corpName;
	}
	@Column(name = "AID", length = 32)
	public String getAid() {
		return aid;
	}
	public void setAid(String aid) {
		this.aid = aid;
	}
	@Column(name = "NET_ID", length = 32)
	public String getNetId() {
		return netId;
	}
	public void setNetId(String netId) {
		this.netId = netId;
	}
	@Column(name = "CORP_ID", length = 32)
	public String getCorpId() {
		return corpId;
	}
	public void setCorpId(String corpId) {
		this.corpId = corpId;
	}
	@Column(name = "BUS_ORI_TYPE")
	public Integer getBusOriType() {
		return busOriType;
	}
	public void setBusOriType(Integer busOriType) {
		this.busOriType = busOriType;
	}
	@Column(name = "SYS_RACE_CODE")
	public String getSysRaceCode() {
		return sysRaceCode;
	}
	public void setSysRaceCode(String sysRaceCode) {
		this.sysRaceCode = sysRaceCode;
	}
	@Column(name = "CREATE_DATE")
	public Date getCreateDate() {
		return createDate;
	}
	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}
	@Column(name = "UPDATE_DATE")
	public Date getUpdateDate() {
		return updateDate;
	}
	public void setUpdateDate(Date updateDate) {
		this.updateDate = updateDate;
	}
	@Column(name = "IS_BACK")
	public String getIsBack() {
		return isBack;
	}
	public void setIsBack(String isBack) {
		this.isBack = isBack;
	}
	@Column(name = "OPERATOR_CODE")
	public String getOperatorCode() {
		return operatorCode;
	}
	public void setOperatorCode(String operatorCode) {
		this.operatorCode = operatorCode;
	}
	@Override
	public String toString() {
		return "AbAccbookInfo [aid=" + aid + ", netId=" + netId + ", corpId=" + corpId + ", busOriType=" + busOriType
				+ ", sysRaceCode=" + sysRaceCode + ", isBack=" + isBack + ", createDate=" + createDate + ", updateDate="
				+ updateDate + "]";
	}
	
}
