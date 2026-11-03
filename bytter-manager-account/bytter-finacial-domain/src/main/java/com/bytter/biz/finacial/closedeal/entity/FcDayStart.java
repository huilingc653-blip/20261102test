package com.bytter.biz.finacial.closedeal.entity;
import com.bytter.core.base.entity.IdEntity;
import javax.persistence.*;
import java.util.Date;
/**   
 * @Title: Entity
 * @Description: FC_DAY_START
 * @author onlineGenerator
 * @date 2019-11-12 10:51:02
 * @version 1.0
 *
 */
@Entity
@Table(name = "FC_DAY_START", schema = "")
@SuppressWarnings("serial")
public class FcDayStart extends IdEntity implements java.io.Serializable {
	
	/**所属网点*/
	private String netCode;
	/**日初日期*/
	private java.util.Date startDate;
	/**上次日初日期*/
	private java.util.Date lastStartDate;
	/**创建时间*/
	private java.util.Date createTime;
	/**创建用户代码*/
	private String createBy;
	/**最后更新时间*/
	private java.util.Date updateTime;
	/**最后更新用户代码*/
	private String updateBy;
	@Column(name ="NET_CODE")
	public String getNetCode() {
		return netCode;
	}
	public void setNetCode(String netCode) {
		this.netCode = netCode;
	}
	@Column(name ="START_DATE")
	public Date getStartDate() {
		return startDate;
	}
	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}
	@Column(name ="LAST_START_DATE")
	public Date getLastStartDate() {
		return lastStartDate;
	}
	public void setLastStartDate(Date lastStartDate) {
		this.lastStartDate = lastStartDate;
	}
	@Column(name ="CREATE_TIME")
	public Date getCreateTime() {
		return createTime;
	}
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
	@Column(name ="CREATE_BY")
	public String getCreateBy() {
		return createBy;
	}
	public void setCreateBy(String createBy) {
		this.createBy = createBy;
	}
	@Column(name ="UPDATE_TIME")
	public Date getUpdateTime() {
		return updateTime;
	}
	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}
	@Column(name ="UPDATE_BY")
	public String getUpdateBy() {
		return updateBy;
	}
	public void setUpdateBy(String updateBy) {
		this.updateBy = updateBy;
	}
}
