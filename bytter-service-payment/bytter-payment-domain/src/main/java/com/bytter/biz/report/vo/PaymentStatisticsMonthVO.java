package com.bytter.biz.report.vo;

import java.math.BigDecimal;
import java.util.List;

/**
 * @Title: Entity
 * @Description:支付统计表
 * @author jiangw
 * @date 2024.09.11
 * @version 1.0
 *
 */
public class PaymentStatisticsMonthVO implements java.io.Serializable {

	private static final long serialVersionUID = 6490814688726935044L;

	/**
	 * 年月
	 */
	private String month;
	/**
	 * 是否合计
	 */
	private int isSum;

	public String getMonth() {
		return month;
	}

	public void setMonth(String month) {
		this.month = month;
	}

	public int getIsSum() {
		return isSum;
	}

	public void setIsSum(int isSum) {
		this.isSum = isSum;
	}
}
