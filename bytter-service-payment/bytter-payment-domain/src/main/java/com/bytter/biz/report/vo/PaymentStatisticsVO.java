package com.bytter.biz.report.vo;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * @Title: Entity
 * @Description:支付统计表
 * @author jiangw
 * @date 2024.09.11
 * @version 1.0
 *
 */
public class PaymentStatisticsVO implements java.io.Serializable {

	private static final long serialVersionUID = 6490814688726935044L;

	/**
	 * 大区
	 */
	private String areaName;
	/**
	 * 单位ID
	 */
	private String corpId;
	private String corpName;
	/**
	 * 查询时间
	 */
	private String beginDate;
	private String endDate;
	/**
	 * 数据列表
	 */
	private List<BigDecimal> dataList;
	/**
	 * 是否合计
	 */
	private int isSum;

	private String provinceName;

	public String getAreaName() {
		return areaName;
	}

	public void setAreaName(String areaName) {
		this.areaName = areaName;
	}

	public String getCorpId() {
		return corpId;
	}

	public void setCorpId(String corpId) {
		this.corpId = corpId;
	}

	public String getCorpName() {
		return corpName;
	}

	public void setCorpName(String corpName) {
		this.corpName = corpName;
	}

	public String getBeginDate() {
		return beginDate;
	}

	public void setBeginDate(String beginDate) {
		this.beginDate = beginDate;
	}

	public String getEndDate() {
		return endDate;
	}

	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}

	public List<BigDecimal> getDataList() {
		return dataList;
	}

	public void setDataList(List<BigDecimal> dataList) {
		this.dataList = dataList;
	}

	public int getIsSum() {
		return isSum;
	}

	public void setIsSum(int isSum) {
		this.isSum = isSum;
	}

	public String getProvinceName() {
		return provinceName;
	}

	public void setProvinceName(String provinceName) {
		this.provinceName = provinceName;
	}
}
