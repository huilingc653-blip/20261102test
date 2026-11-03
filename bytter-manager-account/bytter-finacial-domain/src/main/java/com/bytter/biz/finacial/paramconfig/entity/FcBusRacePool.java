package com.bytter.biz.finacial.paramconfig.entity;
import com.bytter.core.base.entity.IdEntity;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.Date;
/**   
 * @Title: Entity
 * @Description: FC_BUS_RACE_POOL
 * @author onlineGenerator
 * @date 2019-5-6
 * @version 1.0
 *
 */
@Entity
@Table(name = "FC_BUS_RACE_POOL", schema = "")
@SuppressWarnings("serial")
public class FcBusRacePool extends IdEntity implements java.io.Serializable {
	/**业务种类代码*/
	private String raceCode;
	/**业务种类名称*/
	private String raceName;
	/**所属资金池*/
	private String poolId;
	/**系统业务种类*/
	private String sysRaceCode;
	/**上级业务种类*/
	private String fatherId;
	/**有效标识 0 无效 1 有效*/
	private String validSign;
	/**创建时间*/
	private Date createTime;
	/**创建用户代码*/
	private String createBy;
	/**最后更新时间*/
	private Date updateTime;
	/**最后更新用户代码*/
	private String updateBy;
	
	@Column(name="RACE_CODE")
	public String getRaceCode() {
		return raceCode;
	}
	public void setRaceCode(String raceCode) {
		this.raceCode = raceCode;
	}
	@Column(name="RACE_NAME")
	public String getRaceName() {
		return raceName;
	}
	public void setRaceName(String raceName) {
		this.raceName = raceName;
	}
	@Column(name="POOL_ID")
	public String getPoolId() {
		return poolId;
	}
	public void setPoolId(String poolId) {
		this.poolId = poolId;
	}
	@Column(name="SYS_RACE_CODE")
	public String getSysRaceCode() {
		return sysRaceCode;
	}
	public void setSysRaceCode(String sysRaceCode) {
		this.sysRaceCode = sysRaceCode;
	}
	@Column(name="FATHER_ID")
	public String getFatherId() {
		return fatherId;
	}
	public void setFatherId(String fatherId) {
		this.fatherId = fatherId;
	}
	@Column(name="VALID_SIGN")
	public String getValidSign() {
		return validSign;
	}
	public void setValidSign(String validSign) {
		this.validSign = validSign;
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
}
