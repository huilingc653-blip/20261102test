package com.bytter.increment.domain.vo;
import com.bytter.core.annotation.BtCompositeSearch;
import com.bytter.core.bpm.IBusinessKey;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.ToString;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
/**
 * @Title: Entity
 * @Description: 银行理财VO
 * @author onlineGenerator
 * @date 2017-08-18 14:57:06
 * @version 1.0
 */
@Data
@ToString
public class ChoBankFinanceVO implements Serializable,  IBusinessKey {
	private static final long serialVersionUID = 3650731181127660208L;
	private String id;
	/**业务单号*/
	@BtCompositeSearch(fieldName = "f.bill_No")
	private String billNo;
	/**单位ID*/
	@BtCompositeSearch(fieldName = "f.corp_Id")
	// @Corp(inject="corpName")
	private String corpId;
	/**单位名称*/

    @BtCompositeSearch(fieldName = "f.corp_name")
	private String corpName;
	/**业务类型 10-活期转定期 11-定期转活期 12-活期转通知 13-通知转活期 14-通知预约 15-取消通知*/
	@BtCompositeSearch(fieldName = "f.bus_Type")
	private Integer busType;
	/**付款账号ID*/
	private String payerAccId;
	/**付款账号*/
	@BtCompositeSearch(fieldName = "f.payer_Acc")
	private String payerAcc;
	/**付款账号名称*/
	@BtCompositeSearch(fieldName = "f.acc_Name")
	private String payerAccName;
	/**付款开户行名称*/
	@BtCompositeSearch(fieldName = "f.bank_Name")
	private String payerBank;
	/**付款账户余额*/
	private BigDecimal balance;
	/**收款账号ID*/
	private String payeeAccId;
	/**收款账号*/
	@BtCompositeSearch(fieldName = "f.payee_Acc")
	private String payeeAcc;
	/**币别*/
	// @Currency(inject="curName")
	private String curId;
	/**币别名称*/
	@BtCompositeSearch(fieldName = "f.cur_Id")
	private String curName;
	/**金额*/
	@BtCompositeSearch(fieldName = "f.pay_Money")
	private BigDecimal payMoney;
	/**期限（活期转定期）：3个月、6个月、1年、2年、3年、5年*/
	@BtCompositeSearch(fieldName = "f.day_Limit")
	private Integer dayLimit;
	/**日期单位 Y-年、M-月、D-日*/
	@BtCompositeSearch(fieldName = "f.date_Unit")
	private String dateUnit;
	/**会计凭证号*/
	@BtCompositeSearch(fieldName = "f.bursar_Bill_No")
	private String bursarBillNo;
	/**利息*/
	@BtCompositeSearch(fieldName = "f.interest")
	private BigDecimal interest;
	/**到期处理方式0-不处理 1-本金续存 2-本息续存*/
	@BtCompositeSearch(fieldName = "f.certificate_Way")
	private Integer certificateWay;
	/**期望支付日期*/
	@JsonFormat(pattern = "yyyy-MM-dd")
	@BtCompositeSearch(fieldName = "f.wish_Payday")
	private Date wishPayday;
	/**利率*/
	@BtCompositeSearch(fieldName = "f.interest_Rate")
	private BigDecimal interestRate;
	/**用途*/
	@BtCompositeSearch(fieldName = "f.purpose")
	private String purpose;
	/**附言*/
	@BtCompositeSearch(fieldName = "f.add_Word")
	private String addWord;
	/**取款日期*/
	@BtCompositeSearch(fieldName = "f.pay_Date")
	private Date payDate;
	/**取款单据ID（定期转活期，通知转活期）*/
	@BtCompositeSearch(fieldName = "f.pay_Bill_Id")
	private String payBillId;
	/**通知日期*/
	@BtCompositeSearch(fieldName = "f.notify_Time")
	private Date notifyTime;
	/**补录利率*/
	@BtCompositeSearch(fieldName = "f.bl_Interest_Rate")
	private BigDecimal blInterestRate;
	/**是否补录 0-否 1-是*/
	private String isBl;
	/**补录单号*/
	@BtCompositeSearch(fieldName = "f.bank_Bill_No")
	private String bankBillNo;
	/**补录利息*/
	@BtCompositeSearch(fieldName = "f.bl_Interest")
	private BigDecimal blInterest;
	/**状态*/
	@BtCompositeSearch(fieldName = "f.status")
	private Integer status;
	/**起息日期*/
	@BtCompositeSearch(fieldName = "f.interest_start_date")
	@JsonFormat(pattern = "yyyy-MM-dd")
	private Date interestStartDate;
	/**到期日期*/
	@BtCompositeSearch(fieldName = "f.deadline_date")
	@JsonFormat(pattern = "yyyy-MM-dd")
	private Date deadlineDate;
	/**创建人登录名称*/
	@BtCompositeSearch(fieldName = "f.create_By")
	private String createBy;
	/**创建日期*/
	@BtCompositeSearch(fieldName = "f.create_Date")
	@JsonFormat(pattern = "yyyy-MM-dd")
	private Date createDate;
	/**更新人登录名称*/
	private String updateBy;
	/**更新日期*/
	private Date updateDate;
	/**交易状态*/
	@BtCompositeSearch(fieldName = "e.voucher_Stat")
	private String voucherStat;
	/**指令ID*/
	private String excId;
	/**已支取金额*/
	@BtCompositeSearch(fieldName = "drawMoney")
	private BigDecimal drawMoney;
	/**剩余本金*/
	@BtCompositeSearch(fieldName = "drawBal")
	private BigDecimal drawBal;
	/**定期账号余额*/
	private BigDecimal timeBalance;
	/**参考收益*/
	private BigDecimal referInterest;
	/**最大转账日期*/
	@JsonFormat(pattern = "yyyy-MM-dd")
	private Date maxWishPayday;
	/**截止日期*/
	@JsonFormat(pattern = "yyyy-MM-dd")
	@BtCompositeSearch(fieldName = "calEndDate")
	private Date calEndDate;
	/**计算天数*/
	private Integer calDays;
	/**应收利息*/
	private BigDecimal interestReceivable;
	/**应收未收利息*/
	private BigDecimal interestReceivableNot;
	/**银行流水认领组件挂接确认*/
	private String collectionConfirm;
	/**是否上线*/
	@BtCompositeSearch(fieldName = "f.is_Online")
	private String isOnline;
	/**账号上线*/
	private String bankAccOnline;
	/**期限展示 将limit与unit进行组合*/
	private String limitShow;
	/**利率展示*/
	private String interestShow;
	@Override
	public String getBusinessKey() {
		return getId();
	}
}
