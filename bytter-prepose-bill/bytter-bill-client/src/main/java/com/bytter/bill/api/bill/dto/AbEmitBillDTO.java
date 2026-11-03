package com.bytter.bill.api.bill.dto;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
/**
 * @author luop
 * 应付开票暂存-接口参数对象
 * 2017-08-26 16:30:56
 */
@SuppressWarnings("serial")
@Data
public class AbEmitBillDTO implements Serializable {
	private String billId;
	/**持票单位*/
	private String corpId;
	/**票据号*/
	private String origNo;
	/**票据金额*/
	private BigDecimal billMoney;
	/**出票人*/
	private String payer;
	/**出票人账号*/
	private String payeracc;
	/**出票人开户银行名称*/
	private String payerBankName;
	/**出票人开户银行行号*/
	private String payerbankno;
	/**收票人全称*/
	private String payee;
	/**收票人账号*/
	private String payeeacc;
	/**收票人账号名称*/
	private String payeeAccName;
	/**收票人开户银行名称*/
	private String payeeBankName;
	/**收票人开户银行行号*/
	private String payeebankno;
	/**承兑人账号 */
	private String acceptoracc;
	/**承兑人开户行名称*/
	private String acceptorBankName;
	/**承兑人开户行行号*/
	private String acceptorbankno;
	/**承兑人开户行地址*/
	private String acceptorbankaddress;
	/**开出方式：0  对外开出；1 对内自开*/
	private String insideSing;
	/**前手*/
	private String cnAccrecords;
	/**业务归属日期*/
	private Date businessDate;
	/**票据类型：10 银票、11 商票*/
	private Integer billType;
	/**流通介质：10 纸票、11 电票*/
	private Integer billFlag;
	/**出票日期*/
	private Date startDate;
	/**到期日期*/
	private Date endDate;
	/**承兑协议编号 */
	private String accptncagrmtnb;
	/**交易合同号*/
	private String acontractNo;
	/**不得转让标记EM00可再转让EM01不得转让*/
	private String banendrsmtmk;
	/**开票信用等级*/
	private String payerCdtRatgs;
	/**开票评级机构*/
	private String payerCdtRatgGgcy;
	/**开票评级到期日*/
	private Date payerCdtRatgDuedt;
	/**发票号*/
	private String ainvcnb;
	/**保证金比例*/
	private BigDecimal bailpercent;
	/**保证金金额*/
	private BigDecimal bailmoney;
	/**保证金利率 */
	private BigDecimal bailrate;
	/**缴存保证金账户*/
	private String bailAcc;
	/**外部系统id编码*/
	private String voucherId;
	/**手续费 */
	private BigDecimal feemoney;
	/**摘要*/
	private String abs;
	/**备注*/
	private String rmk;
	/** 申请单位 */
	private String applyCorpId;
	/** 业务单位 */
	private String busCorpId;
	/** 业务人员 */
	private String busPersonnel;
	private String sbbllRngStrtSn;//		子票区间开始序号。旧票据默认0，新票据 12位长度，不足12位前面补0。
	private String sbbllRngEndSn;//				子票区间结束序号。旧票据默认0，新票据 12位长度，不足12位前面补0。
	/**出票标志。0：老系统出票,1：新系统出票*/
	private String newStmIssuofpprInd;
	/**收款人统一社会信用代码*/
	private String blRcvpymtpsUnnIdNo;
	/**
	 * 是否批次开票数据1为是
	 */
	private String isBatchBill;
	private String aid;
	/**
	 * 付款方式id
	 */
	private String voucherTypeId;
}
