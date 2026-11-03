package com.bytter.biz.finacial.tally.entity;
import com.bytter.core.annotation.BtCompositeSearch;
import com.bytter.core.base.entity.IdEntity;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
/**
 * 手工帐类型表
 * @author lvwl
 *
 */
@Entity
@Table(name = "FC_MANUAL_FINACIAL_TYPE", schema = "")
@SuppressWarnings("serial")
public class FcManualFinacialType extends IdEntity implements java.io.Serializable {
	/**系统业务种类Code(四位数)*/
	@BtCompositeSearch(fieldName = "a.sys_race_code")
	private String sysRaceCode;
	/**手工账名称*/
	@BtCompositeSearch(fieldName = "a.name")
	private String name;
	/**需要记手工帐数据的查询链接*/
	private String url;
	/**流程类型1记账、2改账、3复核*/
	@BtCompositeSearch(fieldName = "a.flow_type")
	private String flowType;
	@Column(name ="SYS_RACE_CODE")
	public String getSysRaceCode() {
		return sysRaceCode;
	}
	public void setSysRaceCode(String sysRaceCode) {
		this.sysRaceCode = sysRaceCode;
	}
	@Column(name ="NAME")
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	@Column(name ="URL")
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	@Column(name ="FLOW_TYPE")
	public String getFlowType() {
		return flowType;
	}
	public void setFlowType(String flowType) {
		this.flowType = flowType;
	}
}
