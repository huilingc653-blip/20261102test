package com.bytter.increment.api.dto;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
/**   
 * Title: DTO
 * Description: BIS_FINANCE_EXC
 * @author huangx
 * @since 2017-08-23 11:35:12
 * @version 1.0
 *
 */
@Data
public class BisFinanceExcDTO implements Serializable {
	/**ID*/
	private String id;
	/**指令编号*/
	private String voucherNo;
	/**业务单ID*/
	private String cbsBillId;
	/**业务单号*/
	private String cbsBillNo;
	/**单位ID*/
	private String corpId;
	/**申请日期*/
	private Date reqDate;
	/**接口代码*/
	private String bifCode;
	/**付款账号*/
	private String payerAcc;
	/**付款账号名称*/
	private String payerAccName;
	/**付款开户行名称*/
	private String payerBank;
	/**币别*/
	private String curId;
	/**金额*/
	private BigDecimal payMoney;
	/**用途*/
	private String purpose;
	/**期望支付日期*/
	private Date wishPayday;
	/**期限（活期转定期）：3个月、6个月、1年、2年、3年、5年*/
	private Integer dayLimit;
	/**日期单位 Y-年、M-月、D-日*/
	private String dateUnit;
	/**会计凭证号*/
	private String bursarBillNo;
	/**到期处理方式 0-不处理 1-本金续存 2-本息续存*/
	private Integer certificateWay;
	/**业务类型*/
	private Integer busType;
	/**银行返回开户日期*/
	private Date openDate;
	/**银行返回收款账号*/
	private String payeeAcc;
	/**银行返回收款账号名称*/
	private String payeeAccName;
	/**活期账号余额*/
	private BigDecimal currBalance;
	/**定期账号余额*/
	private BigDecimal timeBalance;
	/**银行返回利率*/
	private BigDecimal interestRate;
	/**消息提醒标志*/
	private String noticeSign;
	/**银行流水号*/
	private String bankSerialNo;
	/**交易状态*/
	private String voucherStat;
	/**交易时间*/
	private Date transTime;
	/**返回时间*/
	private Date returnTime;
	/**返回信息（交易前）*/
	private String firstReturnMsg;
	/**返回信息（交易后）*/
	private String nextReturnMsg;
	/**创建人登录名称*/
	private String createBy;
	/**创建日期*/
	private Date createDate;
	/**更新人登录名称*/
	private String updateBy;
	/**更新日期*/
	private Date updateDate;
	/**退回原因*/
	private String backMsg;

	/**单位名称*/
	private String corpName;

	/**接口代码名称*/
	private String bifName;

	/**币别名称*/
	private String curName;

	private String dateUnitName;

	private String busTypeName;

	private String voucherStatName;

	/**指令重发标志*/
	private String resendSign;


}
