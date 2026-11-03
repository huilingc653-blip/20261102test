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
 * @Description: BT_SUMMARY摘要词组维护
 * @author LIXI
 * @date 2017-05-16 15:36:55
 * @version V1.0
 *
 */
@Entity
@Table(name = "BT_SUMMARY", schema = "")
@SuppressWarnings("serial")
public class BtSummary extends IdEntity implements java.io.Serializable {
	/**摘要代码*/
	private java.lang.String summaryCode;
	/**摘要内容*/
	private java.lang.String summaryContent;
	/**备注*/
	private java.lang.String rmk;
	/**有效标识 0 无效 1 有效*/
	private java.lang.String validSign;
	/**用途分类*/
	private java.lang.String usingtype;
	/**createTime*/
	private java.util.Date createTime;
	/**createBy*/
	private java.lang.String createBy;
	/**updateTime*/
	private java.util.Date updateTime;
	/**updateBy*/
	private java.lang.String updateBy;
	
	
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  摘要代码
	 */
	@Column(name ="SUMMARY_CODE",length=4)
	public java.lang.String getSummaryCode(){
		return this.summaryCode;
	}
	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  摘要代码
	 */
	public void setSummaryCode(java.lang.String summaryCode){
		this.summaryCode = summaryCode;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  摘要内容
	 */
	@Column(name ="SUMMARY_CONTENT")
	public java.lang.String getSummaryContent(){
		return this.summaryContent;
	}
	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  摘要内容
	 */
	public void setSummaryContent(java.lang.String summaryContent){
		this.summaryContent = summaryContent;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  备注
	 */
	@Column(name ="RMK")
	public java.lang.String getRmk(){
		return this.rmk;
	}
	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  备注
	 */
	public void setRmk(java.lang.String rmk){
		this.rmk = rmk;
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
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  usingtype
	 */
	@Column(name ="USINGTYPE")
	public java.lang.String getUsingtype(){
		return this.usingtype;
	}
	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  usingtype
	 */
	public void setUsingtype(java.lang.String usingtype){
		this.usingtype = usingtype;
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
