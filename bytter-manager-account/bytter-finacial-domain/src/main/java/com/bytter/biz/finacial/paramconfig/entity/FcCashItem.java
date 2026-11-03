package com.bytter.biz.finacial.paramconfig.entity;
import com.bytter.core.base.entity.IdEntity;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.Date;
/**   
 * @Title: Entity
 * @Description: 现金流量科目
 * @author
 * @date 2020-09-10 09:18:39
 * @version V1.0
 *
 */
@Entity
@Table(name = "FC_CASH_ITEM", schema = "")
@SuppressWarnings("serial")
public class FcCashItem extends IdEntity implements java.io.Serializable {
	/**现金流项目代码*/
	private String itemCode;
	/**现金流项目名称*/
	private String itemName;
	/**备注*/
	private String rmk;
	/**有效标识*/
	private String validSign;
	/**流入流出*/
	private String inOut;
	/**创建时间*/
	private Date createTime;
	/**创建用户代码*/
	private String createBy;
	/**最后更新时间*/
	private Date updateTime;
	/**最后更新用户代码*/
	private String updateBy;
    @Column(name ="ITEM_CODE")
    public String getItemCode() {
        return itemCode;
    }
    public void setItemCode(String itemCode) {
        this.itemCode = itemCode;
    }
    @Column(name ="ITEM_NAME")
    public String getItemName() {
        return itemName;
    }
    public void setItemName(String itemName) {
        this.itemName = itemName;
    }
    @Column(name ="RMK")
    public String getRmk() {
        return rmk;
    }
    public void setRmk(String rmk) {
        this.rmk = rmk;
    }
    @Column(name ="VALID_SIGN")
    public String getValidSign() {
        return validSign;
    }
    public void setValidSign(String validSign) {
        this.validSign = validSign;
    }
    @Column(name ="IN_OUT")
    public String getInOut() {
        return inOut;
    }
    public void setInOut(String inOut) {
        this.inOut = inOut;
    }
    /**
	 *方法: 取得java.util.Date
	 *@return: java.util.Date  创建时间
	 */
	@Column(name ="CREATE_TIME")
	public Date getCreateTime(){
		return this.createTime;
	}
	/**
	 *方法: 设置java.util.Date
	 *@param: java.util.Date  创建时间
	 */
	public void setCreateTime(Date createTime){
		this.createTime = createTime;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  创建用户代码
	 */
	@Column(name ="CREATE_BY")
	public String getCreateBy(){
		return this.createBy;
	}
	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  创建用户代码
	 */
	public void setCreateBy(String createBy){
		this.createBy = createBy;
	}
	/**
	 *方法: 取得java.util.Date
	 *@return: java.util.Date  最后更新时间
	 */
	@Column(name ="UPDATE_TIME")
	public Date getUpdateTime(){
		return this.updateTime;
	}
	/**
	 *方法: 设置java.util.Date
	 *@param: java.util.Date  最后更新时间
	 */
	public void setUpdateTime(Date updateTime){
		this.updateTime = updateTime;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  最后更新用户代码
	 */
	@Column(name ="UPDATE_BY")
	public String getUpdateBy(){
		return this.updateBy;
	}
	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  最后更新用户代码
	 */
	public void setUpdateBy(String updateBy){
		this.updateBy = updateBy;
	}
	
}
