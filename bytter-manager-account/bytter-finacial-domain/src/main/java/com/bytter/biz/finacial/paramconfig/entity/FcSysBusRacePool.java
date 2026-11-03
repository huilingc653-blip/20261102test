package com.bytter.biz.finacial.paramconfig.entity;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
/**   
 * @Title: Entity
 * @Description: FC_SYS_BUS_RACE 
 * @author onlineGenerator
 * @date 2017-05-22 16:10:01
 * @version 1.0
 *
 */
@Entity
@Table(name = "FC_SYS_BUS_RACE_POOL", schema = "")
@SuppressWarnings("serial")
public class FcSysBusRacePool implements java.io.Serializable {
	@Id
	/**系统业务品种代码*/
	private String sysRaceCode;
	/**系统业务品种名称*/
	private String sysRaceName;
	/**有效标识*/
	private String validSign;
	/**备注*/
	private String rmk;
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
	 *@return: java.lang.String  sysRaceName
	 */
	@Column(name ="SYS_RACE_NAME")
	public String getSysRaceName(){
		return this.sysRaceName;
	}
	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  sysRaceName
	 */
	public void setSysRaceName(String sysRaceName){
		this.sysRaceName = sysRaceName;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  validSign
	 */
	@Column(name ="VALID_SIGN")
	public String getValidSign(){
		return this.validSign;
	}
	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  validSign
	 */
	public void setValidSign(String validSign){
		this.validSign = validSign;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  rmk
	 */
	@Column(name ="RMK")
	public String getRmk(){
		return this.rmk;
	}
	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  rmk
	 */
	public void setRmk(String rmk){
		this.rmk = rmk;
	}
}
