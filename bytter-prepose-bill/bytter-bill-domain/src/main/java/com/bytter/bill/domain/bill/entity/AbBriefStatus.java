package com.bytter.bill.domain.bill.entity;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
/**   
 * @Title: Entity
 * @Description: 流水业务状态
 * @author onlineGenerator
 * @date 2017-06-21 15:33:13
 * @version 1.0
 *
 */
@Entity
@Table(name = "AB_BRIEF_STATUS", schema = "")
@SuppressWarnings("serial")
public class AbBriefStatus implements java.io.Serializable {
	
	private Integer id;
	
	/**业务状态代码*/
	private String status;
	/**业务状态名称*/
	private String statusname;
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
	 *@return: java.lang.String  业务状态代码
	 */
	@Column(name ="STATUS",nullable=true,length=20)
	public String getStatus(){
		return this.status;
	}
	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  业务状态代码
	 */
	public void setStatus(String status){
		this.status = status;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  业务状态名称
	 */
	@Column(name ="STATUSNAME",nullable=true,length=100)
	public String getStatusname(){
		return this.statusname;
	}
	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  业务状态名称
	 */
	public void setStatusname(String statusname){
		this.statusname = statusname;
	}
	@Override
	public String toString() {
		return "AbBriefStatus [id=" + id + ", status=" + status + ", statusname=" + statusname + "]";
	}
	
}
