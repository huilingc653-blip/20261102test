package com.bytter.bill.api.bill.dto;
import com.bytter.core.annotation.BtCompositeSearch;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
/**
 * todo? DTO不应该承载excel导出
 * @author zhengg
 * 手工记账票据业务查询DTO
 * 2018-09-06 16:30:56
 */
@SuppressWarnings("serial")
@Data
public class AbBillAccbookInfoDTO implements Serializable {
	/**流水表id*/
	private String aid;
	/**记账业务来源类型*/
	@BtCompositeSearch(fieldName="B.bus_Ori_Type")
	private Integer busOriType;
	/**业务规则定义(系统业务品种)*/
	private String sysRaceCode;
	/**票据类型：10 银票、11 商票*/
	@BtCompositeSearch(fieldName="V.bill_type")
	// @Dict(dictGroup = "AB_BILL_TYPE", inject = "billTypeName")
	private Integer billType;
	@BtCompositeSearch(fieldName="V.bill_type")
	private String  billTypeName;
	/**流通介质：10 纸票、11 电票*/
	@BtCompositeSearch(fieldName="V.bill_flag")
	// @Dict(dictGroup = "AB_BILL_FLAG", inject ="billFlagName")
	private Integer billFlag;
	@BtCompositeSearch(fieldName="V.bill_flag")
	private String billFlagName;
	/**所属单位*/
	@BtCompositeSearch(fieldName="B.corp_id")
	// @Corp(inject="corpName")
	private String corpId;
	private String corpName;
	/**所属网点*/
	@BtCompositeSearch(fieldName="B.net_id")
	// @Corp(inject="netName")
	private String netId;
	private String netName;
	/**出票人*/
	@BtCompositeSearch(fieldName="V.payer")
	private String payer;
	/**出票人*/
	@BtCompositeSearch(fieldName="V.payee")
	private String payee;
	/**承兑人*/
	@BtCompositeSearch(fieldName="V.acceptor")
	private String acceptor;
	/**票据号码*/
	@BtCompositeSearch(fieldName="V.orig_no",orderBy="desc")
	private String origNo;
	/**票据金额*/
	@BtCompositeSearch(fieldName="V.bill_money")
	private BigDecimal billMoney;
	/**出票日期*/
	@BtCompositeSearch(fieldName="V.start_date")
	@JsonFormat(pattern="yyyy-MM-dd")
	private Date startDate;
	/**到期日期*/
	@BtCompositeSearch(fieldName="V.end_date")
	@JsonFormat(pattern="yyyy-MM-dd")
	private Date endDate;
	/**申请日期 */
	private Date applyDate;
	/**业务办理日期 */
	private Date businessDate;
	/**是否隐藏 1：隐藏*/
	private String checkboxHide;
	/**收票人全称*/
	@BtCompositeSearch(fieldName="S.signerok")
	private String signerok;
//	@BtCompositeSearch(fieldName="FA.VOUCHER_STATE")
	private String voucherState;
//	@BtCompositeSearch(fieldName="FA.ORDER_NO")
	private String orderNo;
//	@BtCompositeSearch(fieldName="FA.VOUCHER_NO")
	private String voucherNo;
//	@BtCompositeSearch(fieldName="FA.BUS_GLOBAL_ID")
	private String busGlobalId;
	/**状态*/
	private Integer status;
	/**收票人账号**/
	private String payeeacc;
	/**收票人开户银行名称*/
	private String payeeBankName;
	/**收票人开户银行行号**/
	private String payeebankno;
	/**摘要***/
	private String abss;
	/**上一流水ID*/
	private String lastid;
	/**是否可以打回 1可以打回0不可以*/
	private String isBack;
	/**业务接收人名称-后手单位**/
	private String recNm;
	/**业务申请人名称-前手单位**/
	private String reqNm;
	/**是否发送指令标志：0否； 1是 ；*/
	@BtCompositeSearch(fieldName="s.send_Sign")
	// @Dict(dictGroup="AB_BILL_SEND_SIGN",inject = "sendSignName")
	private String sendSign;
	@BtCompositeSearch(fieldName="s.send_Sign")
	private String sendSignName;
	/**票据状态*/
	@BtCompositeSearch(fieldName="V.BILLS_TATUS")
	private String billstatus;
	/**所属库存*/
	@BtCompositeSearch(fieldName="P.enter_stock")
	// @Dict(dictGroup = "AB_ENTER_STOCK", inject = "enterStockName")
	private String enterStock;
	@BtCompositeSearch(fieldName="P.enter_stock")
	private String enterStockName;
	/**贴现利率*/
	@BtCompositeSearch(fieldName="interestRates")
	private BigDecimal interestRates;
	/**贴现天数*/
	private Integer daynum;
	/**贴现利息*/
	private BigDecimal interest;
	/**赎回开放日*/
	private Date rpdopendt;
	/**赎回截止日*/
	private Date rpdduedt;
	/**业务申请人账号**/
	private String reqAcctId;
	/**下拨单号*/
	@BtCompositeSearch(fieldName="t.allocatedCode")
	private String allocatedCode;
	/**录入单位名称**/
	private String recordCorpName;
	/**收票单位名称**/
	private String recevieCorpName;
	/**收票单位账号**/
	private String recevieBankacc;
	/**下拨金额**/
	private BigDecimal allocationMoney;
	/**下拨原因**/
	private String allocationReason;
	/**贴现行号*/
	private String aoaccninfAc;
}
