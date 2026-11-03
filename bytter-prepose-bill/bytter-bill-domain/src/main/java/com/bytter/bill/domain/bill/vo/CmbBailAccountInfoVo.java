package com.bytter.bill.domain.bill.vo;

import com.bytter.core.annotation.BtCompositeSearch;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
* @Description 招行保证金账户信息表
* @author zhous
* @date 2024/9/6 17:00
*/
@SuppressWarnings("serial")
@Data
public class CmbBailAccountInfoVo implements java.io.Serializable {
	/**主键ID*/
	private String id;

	/**
	 * 当前系统出票人账号
	 */
	@BtCompositeSearch(fieldName="c.curacc")
	private String curacc;

	/**
	 * 账号
	 */
	@BtCompositeSearch(fieldName="c.accnbr")
	private String accnbr;

	/**
	 * 户名
	 */
	@BtCompositeSearch(fieldName="c.accnam")
	private String accnam;

	/**
	 * 保证金种类
	 */
	@BtCompositeSearch(fieldName="c.hmgtyp")
	private String hmgtyp;

	private String hmgtypStr;

	/**
	 * 开户机构
	 */
	private String accbrn;

	/**
	 * 币种
	 */
	private String ccynbr;

	/**
	 * 开户日期
	 */
	@JsonFormat(pattern = "yyyy-MM-dd")
	private Date opndat;

	/**
	 * 起息日期
	 */
	@JsonFormat(pattern = "yyyy-MM-dd")
	private Date valdat;

	/**
	 * 到期日期
	 */
	@JsonFormat(pattern = "yyyy-MM-dd")
	private Date mutdat;

	/**
	 * 可用余额
	 */
    @BtCompositeSearch(fieldName="c.avlbal")
	private BigDecimal avlbal;

	/**
	 * 保证金账户id
	 */
	private String bondId;

	/**
	 * 保证金利率
	 */
	private String bondRate;
}
