package com.bytter.bill.domain.bill.vo;

import com.bytter.core.annotation.BtCompositeSearch;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
* @Description 民生保证金账户信息表
* @author zhous
* @date 2024/9/6 17:00
*/
@SuppressWarnings("serial")
@Data
public class CmbcBailAccountInfoVo implements java.io.Serializable {
	/**主键ID*/
	private String id;

	/**
	 * 保证金账号
	 */
	@BtCompositeSearch(fieldName="c.acct_no")
	private String acctNo;

	/**
	 * 账户名称
	 */
	@BtCompositeSearch(fieldName="c.acct_name")
	private String acctName;

	/**
	 * 币种
	 */
	private String cur;

	/**
	 * 开户日期
	 */
	@JsonFormat(pattern = "yyyy-MM-dd")
	private Date openDate;

	/**
	 * 到期日
	 */
	@JsonFormat(pattern = "yyyy-MM-dd")
	private Date expireDate;

	/**
	 * 开户机构
	 */
	private String deptName;

	/**
	 * 利率
	 */
	private BigDecimal rate;

	/**
	 * 保证金类型
	 */
	@BtCompositeSearch(fieldName="c.tim_type")
	private String timType;

	private String timTypeValue;


	/**
	 * 可用余额
	 */
	private BigDecimal availBal;

	/**
	 * 余额
	 */
	private BigDecimal bal;

	/**
	 * 保证金账户id
	 */
	private String bondId;

}
