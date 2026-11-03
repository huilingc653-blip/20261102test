package com.bytter.biz.finacial.paramconfig.entity;
import java.math.BigDecimal;
import java.util.Date;
import java.lang.String;
import java.lang.Double;
import java.lang.Integer;
import java.math.BigDecimal;
import javax.xml.soap.Text;
import java.sql.Blob;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;
import org.hibernate.annotations.GenericGenerator;
import com.bytter.core.base.entity.IdEntity;
/**   
 * @Title: Entity
 * @Description: FC_BUS_RACE
 * @author onlineGenerator
 * @date 2017-05-17 10:17:16
 * @version 1.0
 *
 */
@Entity
@Table(name = "FC_BUS_RACE", schema = "")
@SuppressWarnings("serial")
public class FcBusRace extends IdEntity implements java.io.Serializable {
	/**业务种类代码*/
	private java.lang.String raceCode;
	/**业务种类名称*/
	private java.lang.String raceName;
	/**所属网点*/
	private java.lang.String netId;
	/**系统业务种类*/
	private java.lang.String sysRaceCode;
	/**上级业务种类*/
	private java.lang.String fatherId;
	/**有效标识 0 无效 1 有效*/
	private java.lang.String validSign;
	/**创建时间*/
	private java.util.Date createTime;
	/**创建用户代码*/
	private java.lang.String createBy;
	/**最后更新时间*/
	private java.util.Date updateTime;
	/**最后更新用户代码*/
	private java.lang.String updateBy;
	
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  raceCode
	 */
	@Column(name ="RACE_CODE")
	public java.lang.String getRaceCode(){
		return this.raceCode;
	}
	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  raceCode
	 */
	public void setRaceCode(java.lang.String raceCode){
		this.raceCode = raceCode;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  raceName
	 */
	@Column(name ="RACE_NAME")
	public java.lang.String getRaceName(){
		return this.raceName;
	}
	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  raceName
	 */
	public void setRaceName(java.lang.String raceName){
		this.raceName = raceName;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  netId
	 */
	@Column(name ="NET_ID")
	public java.lang.String getNetId(){
		return this.netId;
	}
	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  netId
	 */
	public void setNetId(java.lang.String netId){
		this.netId = netId;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  sysRaceCode
	 */
	@Column(name ="SYS_RACE_CODE")
	public java.lang.String getSysRaceCode(){
		return this.sysRaceCode;
	}
	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  sysRaceCode
	 */
	public void setSysRaceCode(java.lang.String sysRaceCode){
		this.sysRaceCode = sysRaceCode;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  fatherId
	 */
	@Column(name ="FATHER_ID")
	public java.lang.String getFatherId(){
		return this.fatherId;
	}
	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  fatherId
	 */
	public void setFatherId(java.lang.String fatherId){
		this.fatherId = fatherId;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  0 无效 1 有效
	 */
	@Column(name ="VALID_SIGN")
	public java.lang.String getValidSign(){
		return this.validSign;
	}
	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  0 无效 1 有效
	 */
	public void setValidSign(java.lang.String validSign){
		this.validSign = validSign;
	}
	/**
	 *方法: 取得java.util.Date
	 *@return: java.util.Date  createTime
	 */
	@Column(name ="CREATE_TIME")
	public java.util.Date getCreateTime(){
		return this.createTime;
	}
	/**
	 *方法: 设置java.util.Date
	 *@param: java.util.Date  createTime
	 */
	public void setCreateTime(java.util.Date createTime){
		this.createTime = createTime;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  createBy
	 */
	@Column(name ="CREATE_BY")
	public java.lang.String getCreateBy(){
		return this.createBy;
	}
	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  createBy
	 */
	public void setCreateBy(java.lang.String createBy){
		this.createBy = createBy;
	}
	/**
	 *方法: 取得java.util.Date
	 *@return: java.util.Date  updateTime
	 */
	@Column(name ="UPDATE_TIME")
	public java.util.Date getUpdateTime(){
		return this.updateTime;
	}
	/**
	 *方法: 设置java.util.Date
	 *@param: java.util.Date  updateTime
	 */
	public void setUpdateTime(java.util.Date updateTime){
		this.updateTime = updateTime;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  updateBy
	 */
	@Column(name ="UPDATE_BY")
	public java.lang.String getUpdateBy(){
		return this.updateBy;
	}
	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  updateBy
	 */
	public void setUpdateBy(java.lang.String updateBy){
		this.updateBy = updateBy;
	}
}
