package com.bytter.check.entity;
import com.bytter.core.base.entity.IdEntity;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
/**   
 * @Title: Entity
 * @Description: FC_COMPARE_ACC_PROC
 * @author onlineGenerator
 * @date 2017-10-17 17:03:32
 * @version 1.0
 *
 */
@Entity
@Table(name = "FC_COMPARE_ACC_PROC", schema = "")
@SuppressWarnings("serial")
public class FcCompareAccProc extends IdEntity implements java.io.Serializable {
	/**余额调节表ID*/
	private String ajustId;
	/**中心对账单ID*/
	private String fcCompareAccId;
	/**历史对账标志*/
	private String compareSignHis;
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  余额调节表ID
	 */
	@Column(name ="AJUST_ID")
	public String getAjustId(){
		return this.ajustId;
	}
	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  余额调节表ID
	 */
	public void setAjustId(String ajustId){
		this.ajustId = ajustId;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  中心对账单ID
	 */
	@Column(name ="FC_COMPARE_ACC_ID")
	public String getFcCompareAccId(){
		return this.fcCompareAccId;
	}
	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  中心对账单ID
	 */
	public void setFcCompareAccId(String fcCompareAccId){
		this.fcCompareAccId = fcCompareAccId;
	}
	@Column(name ="COMPARE_SIGN_HIS")
	public String getCompareSignHis() {
		return compareSignHis;
	}
	public void setCompareSignHis(String compareSignHis) {
		this.compareSignHis = compareSignHis;
	}
	
}
