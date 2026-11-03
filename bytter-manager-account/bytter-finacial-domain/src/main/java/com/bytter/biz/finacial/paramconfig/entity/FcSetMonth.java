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
 * @Description: 月结区间
 * @author onlineGenerator
 * @date 2017-05-24 14:18:47
 * @version 1.0
 *
 */
@Entity
@Table(name = "FC_SET_MONTH", schema = "")
@SuppressWarnings("serial")
public class FcSetMonth extends IdEntity implements java.io.Serializable {
	/**所属网点*/
	private java.lang.String netId;
	/**月份*/
	private java.lang.String overMonth;
	/**开始日期*/
	private java.util.Date dateStart;
	/**结束日期*/
	private java.util.Date dateEnd;
	
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  所属网点
	 */
	@Column(name ="NET_ID")
	public java.lang.String getNetId(){
		return this.netId;
	}
	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  所属网点
	 */
	public void setNetId(java.lang.String netId){
		this.netId = netId;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  月份
	 */
	@Column(name ="OVER_MONTH")
	public java.lang.String getOverMonth(){
		return this.overMonth;
	}
	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  月份
	 */
	public void setOverMonth(java.lang.String overMonth){
		this.overMonth = overMonth;
	}
	/**
	 *方法: 取得java.util.Date
	 *@return: java.util.Date  开始日期
	 */
	@Column(name ="DATE_START")
	public java.util.Date getDateStart(){
		return this.dateStart;
	}
	/**
	 *方法: 设置java.util.Date
	 *@param: java.util.Date  开始日期
	 */
	public void setDateStart(java.util.Date dateStart){
		this.dateStart = dateStart;
	}
	/**
	 *方法: 取得java.util.Date
	 *@return: java.util.Date  结束日期
	 */
	@Column(name ="DATE_END")
	public java.util.Date getDateEnd(){
		return this.dateEnd;
	}
	/**
	 *方法: 设置java.util.Date
	 *@param: java.util.Date  结束日期
	 */
	public void setDateEnd(java.util.Date dateEnd){
		this.dateEnd = dateEnd;
	}
	
}
