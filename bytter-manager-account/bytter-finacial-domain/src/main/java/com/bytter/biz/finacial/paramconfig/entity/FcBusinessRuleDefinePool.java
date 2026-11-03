package com.bytter.biz.finacial.paramconfig.entity;
import com.bytter.core.base.entity.IdEntity;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.Date;
/**   
 * @Title: Entity
 * @Description: FC_BUSINESS_RULE_DEFINE_POOL
 * @author onlineGenerator
 * @date 2019-5-6 
 * @version 1.0
 *
 */
@Entity
@Table(name = "FC_BUSINESS_RULE_DEFINE_POOL", schema = "")
@SuppressWarnings("serial")
public class FcBusinessRuleDefinePool extends IdEntity implements java.io.Serializable {
	/**所属资金池*/
	private String poolId;
	/**指定科目*/
	private String subjectId;
	/**币别代码*/
	private String curId;
	/**有效标志*/
	private String validSign;
	/**凭证种类*/
	private String voucherType;
	/**createTime*/
	private Date createTime;
	/**createBy*/
	private String createBy;
	/**updateTime*/
	private Date updateTime;
	/**updateBy*/
	private String updateBy;
	/**业务种类编号**/
	private String sysRaceCode;
	@Column(name="POOL_ID")
	public String getPoolId() {
		return poolId;
	}
	public void setPoolId(String poolId) {
		this.poolId = poolId;
	}
	@Column(name="SUBJECT_ID")
	public String getSubjectId() {
		return subjectId;
	}
	public void setSubjectId(String subjectId) {
		this.subjectId = subjectId;
	}
	@Column(name="CUR_ID")
	public String getCurId() {
		return curId;
	}
	public void setCurId(String curId) {
		this.curId = curId;
	}
	@Column(name="VALID_SIGN")
	public String getValidSign() {
		return validSign;
	}
	public void setValidSign(String validSign) {
		this.validSign = validSign;
	}
	@Column(name="VOUCHER_TYPE")
	public String getVoucherType() {
		return voucherType;
	}
	public void setVoucherType(String voucherType) {
		this.voucherType = voucherType;
	}
	@Column(name="CREATE_TIME")
	public Date getCreateTime() {
		return createTime;
	}
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
	@Column(name="CREATE_BY")
	public String getCreateBy() {
		return createBy;
	}
	public void setCreateBy(String createBy) {
		this.createBy = createBy;
	}
	@Column(name="UPDATE_TIME")
	public Date getUpdateTime() {
		return updateTime;
	}
	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}
	@Column(name="UPDATE_BY")
	public String getUpdateBy() {
		return updateBy;
	}
	public void setUpdateBy(String updateBy) {
		this.updateBy = updateBy;
	}
	@Column(name="SYS_RACE_CODE")
	public String getSysRaceCode() {
		return sysRaceCode;
	}
	public void setSysRaceCode(String sysRaceCode) {
		this.sysRaceCode = sysRaceCode;
	}
}
