package com.bytter.cashVoucher.vo;

import com.bytter.core.annotation.Dict;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
/**   
 * @Title: Entity
 * @Description: 凭证使用VO
 * @author luop
 * @date 2017-09-22 15:20:00
 * @version 1.0
 *
 */
@SuppressWarnings("serial")
public class BvVouEmployInfoVo implements Serializable {
	/**入库凭证号*/
	private String voudNo;
	/**入库凭证ID*/
	private String voudtlId;
	/**凭证使用日期*/
	private Date employDate;
	/**凭证使用人*/
	private String employPerson;
	/**凭证金额*/
	private BigDecimal vouMoney;
	/**数据来源*/
	@Dict(dictGroup = "bvVouInfoDtl.dataSource", inject = "dataSourceName")
	private String dataSource;
	private String dataSourceName;
	/**备注*/
	private String rmk;
	@Override
	public String toString() {
		return "BvVouEmployInfoVo{" +
				"voudNo='" + voudNo + '\'' +
				", voudtlId='" + voudtlId + '\'' +
				", employDate=" + employDate +
				", employPerson='" + employPerson + '\'' +
				", vouMoney=" + vouMoney +
				", dataSource='" + dataSource + '\'' +
				", dataSourceName='" + dataSourceName + '\'' +
				", rmk='" + rmk + '\'' +
				'}';
	}
	public void setVoudNo(String voudNo) {
		this.voudNo = voudNo;
	}
	public void setVoudtlId(String voudtlId) {
		this.voudtlId = voudtlId;
	}
	public void setEmployDate(Date employDate) {
		this.employDate = employDate;
	}
	public void setEmployPerson(String employPerson) {
		this.employPerson = employPerson;
	}
	public void setVouMoney(BigDecimal vouMoney) {
		this.vouMoney = vouMoney;
	}
	public void setDataSource(String dataSource) {
		this.dataSource = dataSource;
	}
	public void setDataSourceName(String dataSourceName) {
		this.dataSourceName = dataSourceName;
	}
	public String getDataSourceName() {
		return dataSourceName;
	}
	public void setRmk(String rmk) {
		this.rmk = rmk;
	}
	public String getVoudNo() {
		return voudNo;
	}
	public String getVoudtlId() {
		return voudtlId;
	}
	public Date getEmployDate() {
		return employDate;
	}
	public String getEmployPerson() {
		return employPerson;
	}
	public BigDecimal getVouMoney() {
		return vouMoney;
	}
	public String getDataSource() {
		return dataSource;
	}
	public String getRmk() {
		return rmk;
	}
}
