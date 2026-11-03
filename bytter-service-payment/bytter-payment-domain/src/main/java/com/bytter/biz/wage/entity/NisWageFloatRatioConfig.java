package com.bytter.biz.wage.entity;

import com.bytter.core.base.entity.IdEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 代发工资浮动比率配置表
 *
 * @author fangcf
 * @date 2025-11-20
 */
@ApiModel("NisWageFloatRatioConfig")
@Entity
@Table(name = "NIS_WAGE_FLOAT_RATIO_CONFIG", schema = "")
public class NisWageFloatRatioConfig extends IdEntity implements java.io.Serializable {

	/**
	 * 月份
	 */
	@ApiModelProperty("月份")
	private String month;

	/**
	 * 浮动比率
	 */
	@ApiModelProperty("浮动比率")
	private BigDecimal floatRate;

	/**
	 * 是否删除：1-是，0-否
	 */
	@ApiModelProperty("是否删除：1-是，0-否")
	private String isDeleted;

	/**
	 * 创建人
	 */
	@ApiModelProperty("创建人")
	private String createBy;

	/**
	 * 创建时间
	 */
	@ApiModelProperty("创建时间")
	private Date createTime;

	/**
	 * 修改人
	 */
	@ApiModelProperty("修改人")
	private String updateBy;

	/**
	 * 修改时间
	 */
	@ApiModelProperty("修改时间")
	private Date updateTime;

	@Column(name = "MONTH")
	public String getMonth() {
		return month;
	}

	public void setMonth(String month) {
		this.month = month;
	}

	@Column(name = "FLOAT_RATE")
	public BigDecimal getFloatRate() {
		return floatRate;
	}

	public void setFloatRate(BigDecimal floatRate) {
		this.floatRate = floatRate;
	}

	@Column(name = "IS_DELETED")
	public String getIsDeleted() {
		return isDeleted;
	}

	public void setIsDeleted(String isDeleted) {
		this.isDeleted = isDeleted;
	}

	@Column(name = "CREATE_BY")
	public String getCreateBy() {
		return createBy;
	}

	public void setCreateBy(String createBy) {
		this.createBy = createBy;
	}

	@Column(name = "CREATE_TIME")
	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	@Column(name = "UPDATE_BY")
	public String getUpdateBy() {
		return updateBy;
	}

	public void setUpdateBy(String updateBy) {
		this.updateBy = updateBy;
	}

	@Column(name = "UPDATE_TIME")
	public Date getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}
}
