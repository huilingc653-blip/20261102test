package com.bytter.biz.bdlink.vo;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.io.Serializable;
import java.util.Date;

/**
 * @author wangzx
 * @version 1.0
 * @Title: Entity
 * @Description: BIS_ACC_BAL_AUTO
 * @date 2018-08-08 17:50:16
 */
public class BisAccBalAutoVO implements Serializable {
	private String id;
	/**
	 * 银行账号Id
	 */
	private String bankAccId;
	/**
	 * 币别Id
	 */
	private String curId;
	/**
	 * 计算截止日期
	 */
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private Date endDate;
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getBankAccId() {
		return bankAccId;
	}
	public void setBankAccId(String bankAccId) {
		this.bankAccId = bankAccId;
	}
	public String getCurId() {
		return curId;
	}
	public void setCurId(String curId) {
		this.curId = curId;
	}
	public Date getEndDate() {
		return endDate;
	}
	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}
}
