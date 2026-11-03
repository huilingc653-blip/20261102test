package com.bytter.tally.entity;
import com.bytter.core.base.entity.IdEntity;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.Date;
/**   
 * @Title: Entity
 * @Description: FC_BUS_RACE
 * @author onlineGenerator
 * @date 2017-05-17 10:17:16
 * @version 1.0
 * TODO 可替换
 */
@Entity
@Table(name = "FC_BUS_RACE", schema = "")
@SuppressWarnings("serial")
public class TallyFcBusRace extends IdEntity implements java.io.Serializable {
	/**业务种类代码*/
	private String raceCode;
	/**业务种类名称*/
	private String raceName;
	/**所属网点*/
	private String netId;
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
	
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  raceCode
	 */
	@Column(name ="RACE_CODE")
	public String getRaceCode(){
		return this.raceCode;
	}
	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  raceCode
	 */
	public void setRaceCode(String raceCode){
		this.raceCode = raceCode;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  raceName
	 */
	@Column(name ="RACE_NAME")
	public String getRaceName(){
		return this.raceName;
	}
	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  raceName
	 */
	public void setRaceName(String raceName){
		this.raceName = raceName;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  netId
	 */
	@Column(name ="NET_ID")
	public String getNetId(){
		return this.netId;
	}
	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  netId
	 */
	public void setNetId(String netId){
		this.netId = netId;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  sysRaceCode
	 */
	@Column(name ="SYS_RACE_CODE")
	public String getSysRaceCode(){
		return this.sysRaceCode;
	}
	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  sysRaceCode
	 */
	public void setSysRaceCode(String sysRaceCode){
		this.sysRaceCode = sysRaceCode;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  fatherId
	 */
	@Column(name ="FATHER_ID")
	public String getFatherId(){
		return this.fatherId;
	}
	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  fatherId
	 */
	public void setFatherId(String fatherId){
		this.fatherId = fatherId;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  0 无效 1 有效
	 */
	@Column(name ="VALID_SIGN")
	public String getValidSign(){
		return this.validSign;
	}
	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  0 无效 1 有效
	 */
	public void setValidSign(String validSign){
		this.validSign = validSign;
	}
	/**
	 *方法: 取得java.util.Date
	 *@return: java.util.Date  createTime
	 */
	@Column(name ="CREATE_TIME")
	public Date getCreateTime(){
		return this.createTime;
	}
	/**
	 *方法: 设置java.util.Date
	 *@param: java.util.Date  createTime
	 */
	public void setCreateTime(Date createTime){
		this.createTime = createTime;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  createBy
	 */
	@Column(name ="CREATE_BY")
	public String getCreateBy(){
		return this.createBy;
	}
	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  createBy
	 */
	public void setCreateBy(String createBy){
		this.createBy = createBy;
	}
	/**
	 *方法: 取得java.util.Date
	 *@return: java.util.Date  updateTime
	 */
	@Column(name ="UPDATE_TIME")
	public Date getUpdateTime(){
		return this.updateTime;
	}
	/**
	 *方法: 设置java.util.Date
	 *@param: java.util.Date  updateTime
	 */
	public void setUpdateTime(Date updateTime){
		this.updateTime = updateTime;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  updateBy
	 */
	@Column(name ="UPDATE_BY")
	public String getUpdateBy(){
		return this.updateBy;
	}
	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  updateBy
	 */
	public void setUpdateBy(String updateBy){
		this.updateBy = updateBy;
	}
}
