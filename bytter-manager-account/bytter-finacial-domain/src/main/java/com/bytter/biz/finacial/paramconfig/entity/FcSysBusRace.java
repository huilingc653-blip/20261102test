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
 * @Description: FC_SYS_BUS_RACE
 * @author onlineGenerator
 * @date 2017-05-22 16:10:01
 * @version 1.0
 *
 */
@Entity
@Table(name = "FC_SYS_BUS_RACE", schema = "")
@SuppressWarnings("serial")
public class FcSysBusRace  implements java.io.Serializable {
	@Id
	/**系统业务品种代码*/
	private java.lang.String sysRaceCode;
	/**系统业务品种名称*/
	private java.lang.String sysRaceName;
	/**有效标识*/
	private java.lang.String validSign;
	/**备注*/
	private java.lang.String rmk;
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
	 *@return: java.lang.String  sysRaceName
	 */
	@Column(name ="SYS_RACE_NAME")
	public java.lang.String getSysRaceName(){
		return this.sysRaceName;
	}
	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  sysRaceName
	 */
	public void setSysRaceName(java.lang.String sysRaceName){
		this.sysRaceName = sysRaceName;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  validSign
	 */
	@Column(name ="VALID_SIGN")
	public java.lang.String getValidSign(){
		return this.validSign;
	}
	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  validSign
	 */
	public void setValidSign(java.lang.String validSign){
		this.validSign = validSign;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  rmk
	 */
	@Column(name ="RMK")
	public java.lang.String getRmk(){
		return this.rmk;
	}
	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  rmk
	 */
	public void setRmk(java.lang.String rmk){
		this.rmk = rmk;
	}
}
