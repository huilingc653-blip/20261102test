package com.bytter.bill.domain.bill.vo;
import com.bytter.core.annotation.BtCompositeSearch;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
/**
 * 票据余额对比分析明细（报表）
 * @author zhenghj
 * @date 2020-1-19
 */
@Data
public class AbBillBalCompareDetailedVO implements Serializable {
	/**
	 *单位id
	 */
	@BtCompositeSearch(fieldName="b.corp_id")
	private String corpId;
	/**
	 * 所属账号
	 */
	private String bankAcc;
	/**
	 * 银行类别
	 */
	private String bankType;
	/**流通介质：10 纸票、11 电票*/
	@BtCompositeSearch(fieldName="a.BILL_FLAG")
	// @Dict(dictGroup = "AB_BILL_FLAG", inject ="billFlagName")
	private Integer billFlag;
	private String billFlagName;
	/**票据类型：10 银票、11 商票*/
	@BtCompositeSearch(fieldName="a.BILL_TYPE")
	// @Dict(dictGroup = "AB_BILL_TYPE", inject = "billTypeName")
	private Integer billType;
	/**票据类型名称：10 银票、11 商票*/
	private String billTypeName;
	/**票据号码*/
	private String origNo;
	/**票据金额*/
	private BigDecimal billMoney;
	/**出票日期*/
	@JsonFormat(pattern = "yyyy-MM-dd")
	private Date startDate;
	/**到期日期*/
	@JsonFormat(pattern = "yyyy-MM-dd")
	private Date endDate;
	/**收票人全称*/
	private String payee;
	/**
	 * 出票人
	 */
	private String payer;
	/**
	 * 承兑人
	 */
	private String acceptor;
}
