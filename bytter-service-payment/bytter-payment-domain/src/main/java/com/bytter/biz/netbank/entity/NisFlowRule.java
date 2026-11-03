package com.bytter.biz.netbank.entity;

import com.bytter.core.base.entity.IdEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.Date;

/**
 * @Title: Entity
 * @Description: 资金调拨流水规则表
 * @author onlineGenerator
 * @date 2024-05-08
 * @version 1.0
 *
 */
@Entity
@Table(name = "NIS_FLOW_RULE", schema = "")
@SuppressWarnings("serial")
public class NisFlowRule extends IdEntity implements java.io.Serializable {
	/** 调拨类型*/
	private String transferType;
	/** 调拨方式*/
	private String transferMode;
	/** 状态*/
	private String status;
	/** 创建人*/
	private String createBy;
	/** 创建时间*/
	private Date createDate;
	/** 更新人*/
	private String updateBy;
	/** 更新时间*/
	private Date updateDate;

	@Column(name ="TRANSFER_TYPE",nullable=false,length=10)
	public String getTransferType() {
		return transferType;
	}

	public void setTransferType(String transferType) {
		this.transferType = transferType;
	}

	@Column(name ="TRANSFER_MODE",nullable=false,length=10)
	public String getTransferMode() {
		return transferMode;
	}

	public void setTransferMode(String transferMode) {
		this.transferMode = transferMode;
	}

	@Column(name ="STATUS",nullable=false,length=4)
	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	@Column(name ="CREATE_BY",nullable=false,length=50)
	public String getCreateBy() {
		return createBy;
	}

	public void setCreateBy(String createBy) {
		this.createBy = createBy;
	}

	@Column(name ="CREATE_DATE",nullable=false)
	public Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	@Column(name ="UPDATE_BY",nullable=true,length=50)
	public String getUpdateBy() {
		return updateBy;
	}

	public void setUpdateBy(String updateBy) {
		this.updateBy = updateBy;
	}

	@Column(name ="UPDATE_DATE",nullable=true,length=50)
	public Date getUpdateDate() {
		return updateDate;
	}

	public void setUpdateDate(Date updateDate) {
		this.updateDate = updateDate;
	}
}
