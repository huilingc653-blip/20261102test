package com.bytter.bill.domain.bill.entity;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
/**   
 * @Title: Entity
 * @Description: 票据住状态表
 * @author onlineGenerator
 * @date 2017-06-21 15:46:42
 * @version 1.0
 *
 */
@Entity
@Table(name = "AB_BILL_STATUS", schema = "")
@SuppressWarnings("serial")
public class AbBillStatus implements java.io.Serializable {
	private Integer id;
	/**票据状态名称*/
	private String statusname;
	/**状态码*/
	private String status;
    @Id
    @Column(name = "ID")
    public Integer getId() {
        return id;
    }
    public void setId(Integer id) {
        this.id = id;
    }
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  票据状态名称
	 */
	@Column(name ="STATUSNAME",nullable=true,length=100)
	public String getStatusname(){
		return this.statusname;
	}
	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  票据状态名称
	 */
	public void setStatusname(String statusname){
		this.statusname = statusname;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  状态码
	 */
	@Column(name ="STATUS",nullable=true,length=10)
	public String getStatus(){
		return this.status;
	}
	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  状态码
	 */
	public void setStatus(String status){
		this.status = status;
	}
	@Override
	public String toString() {
		return "AbBillStatus [id=" + id + ", statusname=" + statusname + ", status=" + status + "]";
	}
	
}
