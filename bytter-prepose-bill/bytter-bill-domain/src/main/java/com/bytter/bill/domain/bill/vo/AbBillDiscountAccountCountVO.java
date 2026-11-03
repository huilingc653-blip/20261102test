package com.bytter.bill.domain.bill.vo;
import com.bytter.bill.api.bill.constant.AccebillConstant;
import com.bytter.bill.domain.bill.entity.AbAccrecords;
import com.bytter.bill.domain.bill.entity.AbVoucherinfo;
import com.bytter.bill.domain.bill.entity.AbVoucherinfoVice;
import com.bytter.core.annotation.BtCompositeSearch;
import com.bytter.core.annotation.Corp;
import com.bytter.core.annotation.Dict;
import com.bytter.core.bpm.IBusinessKey;
import lombok.Data;

import java.io.Serializable;
/**   
 * @Title: VO
 * @Description: 贴现到账统计VO
 * @author kongq
 * @date 2017-7-21 13:43:13
 * @version 1.0
 *
 */
@SuppressWarnings("serial")
@Data
public class AbBillDiscountAccountCountVO extends AbDiscountVO implements Serializable,IBusinessKey{
	
	private String id;
	/**票据主表id*/
	private String vid;
	/**票据从表id*/
	private String vvid;
	
	/**票据主表id*/
	private String vouId;
	/**票据从表id*/
	private String viceId;
	/**流水表id*/ 
	private String accId;
	
	private String businessKey;
		
	/**票据类型：10 银票、11 商票*/
	@BtCompositeSearch(fieldName="V.bill_type")
	@Dict(dictGroup = "AB_BILL_TYPE", inject = "billTypeName")
	private Integer billType;
	@BtCompositeSearch(fieldName="V.bill_type")
	private String  billTypeName;
	
	/**流通介质：10 纸票、11 电票*/
	@BtCompositeSearch(fieldName="V.bill_flag")
	@Dict(dictGroup = "AB_BILL_FLAG", inject ="billFlagName")
	private Integer billFlag;
	@BtCompositeSearch(fieldName="V.bill_flag")
	private String billFlagName;
	
	/**持票单位*/
	@BtCompositeSearch(fieldName="P.corp_id")
	@Corp(inject="corpName")
	private String corpId;
	@BtCompositeSearch(fieldName="P.corp_id")
	private String corpName;
	/**统计方式*/
	private String statistical;
	
	/**持票单位账号 */ 
	private String bankAcc;
	
	/**持票单位银行*/ 
	private String bankCode;
		
	/**出票人*/
	@BtCompositeSearch(fieldName="V.payer")
	private String payer;
		
	/**出票人账号*/ 
	private String payeracc;
		
	/**出票人开户银行名称*/ 
	private String payerBankName;
		
	/**出票人开户银行行号 */ 
	private String payerbankno;
	
	/**承兑人*/
	@BtCompositeSearch(fieldName="V.acceptor")
	private String acceptor;
		
	/**承兑人账号 */ 
	private String acceptoracc;
		
	/**承兑人开户行名称 */ 
	private String acceptorBankName;
		
	/**承兑人开户行行号 */ 
	private String acceptorbankno;
	/**贴入人名称 */
	@BtCompositeSearch(fieldName="ps.rec_Nm")
	private String recNm;
	/**票据号码*/
	@BtCompositeSearch(fieldName="V.orig_no")
	private String origNo;
	
	/**票据金额*/
	@BtCompositeSearch(fieldName="V.bill_money")
	private java.math.BigDecimal billMoney;
	
	/**出票日期*/
	@BtCompositeSearch(fieldName="V.start_date")
	private java.util.Date startDate;
	
	/**到期日期*/
	@BtCompositeSearch(fieldName="V.end_date")
	private java.util.Date endDate;
		
	/**申请日期 */ 
	private java.util.Date applyDate;
	
	/**业务办理日期 */ 
	private java.util.Date businessDate;
	private String businessDateFmt;
	/**开始日期 */
	private String gbusinessDate;
	/**结束日期 */
	private String lbusinessDate;
	/**收票人全称*/
	@BtCompositeSearch(fieldName="S.signerok")
	private String signerok;
	
	/**收票人账号*/ 
	private String payeeacc;
	
	/**收票人开户银行名称*/ 
	private String payeeBankName;
			
	/**收票人开户银行行号*/ 
	private String payeebankno;
	
	/**票据状态*/
	@BtCompositeSearch(fieldName="S.brief_id")
	@Dict(dictGroup = "AB_BRIEF_STATUS", inject = "briefIdName")
	private String briefId;
	@BtCompositeSearch(fieldName="S.brief_id")
	private String briefIdName;
	
	/**票据业务状态*/
	@BtCompositeSearch(fieldName="S.status")
	@Dict(dictGroup = "AB_ABBILLDISCOUNT_STATUS", inject = "statusName")
	private Integer status;
	@BtCompositeSearch(fieldName="S.status")
	private String statusName;
	/**所属库存*/
	@BtCompositeSearch(fieldName="P.enter_stock")
	@Dict(dictGroup = "AB_ENTER_STOCK", inject = "enterStockName")
	private String enterStock;
	@BtCompositeSearch(fieldName="P.enter_stock")
	private String enterStockName;
	
	/**不得转让标记 */
	@Dict(dictGroup = "AB_BANEM", inject = "banendrsmtmkName")
	private String banendrsmtmk;
	private String banendrsmtmkName;
	
	/**承兑合同号 */ 
	private String acontractNo;
	
	/**发票号 */ 
	private String ainvcnb;
	
	/**前手*/
	private String cnAccrecords;
	
	/**摘要*/ 
	private String abs;
	
	/**备注*/ 
	private String rmk;
	
	/**票据主表vouIds*/
	private String vouIds;
	/**票据从表viceIds*/
	private String viceIds;
	/**流水表accIds*/ 
	private String accIds;
	/**收款人信息signeroks*/ 
	private String signeroks;
	
	/**票据主表信息*/ 
	private AbVoucherinfo abVoucherInfo;
	
	/**票据从表信息*/ 
	private AbVoucherinfoVice abVoucherinfoVice;
	
	/**流水表*/ 
	private AbAccrecords abAccrecords;
	/**是否隐藏 1：隐藏*/
	private String checkboxHide;
	/**是否查询 1：查询*/
	private String isQuery;
	/** 统计维度 */
	private String dimensions;

	@BtCompositeSearch(fieldName="p.BANK_TYPE_ID")
	private String bankTypeId;
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getVid() {
		return vid;
	}
	public void setVid(String vid) {
		this.vid = vid;
	}
	public String getVvid() {
		return vvid;
	}
	public void setVvid(String vvid) {
		this.vvid = vvid;
	}
	public String getVouId() {
		return vouId;
	}
	public void setVouId(String vouId) {
		this.vouId = vouId;
	}
	public String getViceId() {
		return viceId;
	}
	public void setViceId(String viceId) {
		this.viceId = viceId;
	}
	public String getAccId() {
		return accId;
	}
	public void setAccId(String accId) {
		this.accId = accId;
	}
	public Integer getBillType() {
		return billType;
	}
	public void setBillType(Integer billType) {
		this.billType = billType;
	}
	public String getBillTypeName() {
		return billTypeName;
	}
	public void setBillTypeName(String billTypeName) {
		this.billTypeName = billTypeName;
	}
	public Integer getBillFlag() {
		return billFlag;
	}
	public void setBillFlag(Integer billFlag) {
		this.billFlag = billFlag;
	}
	public String getBillFlagName() {
		return billFlagName;
	}
	public void setBillFlagName(String billFlagName) {
		this.billFlagName = billFlagName;
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
	public String getStatistical() {
		return statistical;
	}
	public void setStatistical(String statistical) {
		this.statistical = statistical;
	}
	public String getBankAcc() {
		return bankAcc;
	}
	public void setBankAcc(String bankAcc) {
		this.bankAcc = bankAcc;
	}
	public String getBankCode() {
		return bankCode;
	}
	public void setBankCode(String bankCode) {
		this.bankCode = bankCode;
	}
	public String getPayer() {
		return payer;
	}
	public void setPayer(String payer) {
		this.payer = payer;
	}
	
	public String getPayeracc() {
		return payeracc;
	}
	public void setPayeracc(String payeracc) {
		this.payeracc = payeracc;
	}
	public String getPayerBankName() {
		return payerBankName;
	}
	public void setPayerBankName(String payerBankName) {
		this.payerBankName = payerBankName;
	}
	public String getPayerbankno() {
		return payerbankno;
	}
	public void setPayerbankno(String payerbankno) {
		this.payerbankno = payerbankno;
	}
	public String getAcceptor() {
		return acceptor;
	}
	public void setAcceptor(String acceptor) {
		this.acceptor = acceptor;
	}
	public String getAcceptoracc() {
		return acceptoracc;
	}
	public void setAcceptoracc(String acceptoracc) {
		this.acceptoracc = acceptoracc;
	}
	public String getAcceptorBankName() {
		return acceptorBankName;
	}
	public void setAcceptorBankName(String acceptorBankName) {
		this.acceptorBankName = acceptorBankName;
	}
	public String getAcceptorbankno() {
		return acceptorbankno;
	}
	public void setAcceptorbankno(String acceptorbankno) {
		this.acceptorbankno = acceptorbankno;
	}
	@Override
	public String getRecNm() {
		return recNm;
	}
	@Override
	public void setRecNm(String recNm) {
		this.recNm = recNm;
	}
	public String getOrigNo() {
		return origNo;
	}
	public void setOrigNo(String origNo) {
		this.origNo = origNo;
	}
	public java.math.BigDecimal getBillMoney() {
		return billMoney;
	}
	public void setBillMoney(java.math.BigDecimal billMoney) {
		this.billMoney = billMoney;
	}
	public java.util.Date getStartDate() {
		return startDate;
	}
	public void setStartDate(java.util.Date startDate) {
		this.startDate = startDate;
	}
	public java.util.Date getEndDate() {
		return endDate;
	}
	public void setEndDate(java.util.Date endDate) {
		this.endDate = endDate;
	}
	public java.util.Date getApplyDate() {
		return applyDate;
	}
	public void setApplyDate(java.util.Date applyDate) {
		this.applyDate = applyDate;
	}
	
	public java.util.Date getBusinessDate() {
		return businessDate;
	}
	public void setBusinessDate(java.util.Date businessDate) {
		this.businessDate = businessDate;
	}
	public String getBusinessDateFmt() {
		return businessDateFmt;
	}
	public void setBusinessDateFmt(String businessDateFmt) {
		this.businessDateFmt = businessDateFmt;
	}
	public String getGbusinessDate() {
		return gbusinessDate;
	}
	public void setGbusinessDate(String gbusinessDate) {
		this.gbusinessDate = gbusinessDate;
	}
	public String getLbusinessDate() {
		return lbusinessDate;
	}
	public void setLbusinessDate(String lbusinessDate) {
		this.lbusinessDate = lbusinessDate;
	}
	public String getSignerok() {
		return signerok;
	}
	public void setSignerok(String signerok) {
		this.signerok = signerok;
	}
	public String getPayeeacc() {
		return payeeacc;
	}
	public void setPayeeacc(String payeeacc) {
		this.payeeacc = payeeacc;
	}
	public String getPayeeBankName() {
		return payeeBankName;
	}
	public void setPayeeBankName(String payeeBankName) {
		this.payeeBankName = payeeBankName;
	}
	public String getPayeebankno() {
		return payeebankno;
	}
	public void setPayeebankno(String payeebankno) {
		this.payeebankno = payeebankno;
	}
	public String getBriefId() {
		return briefId;
	}
	public void setBriefId(String briefId) {
		this.briefId = briefId;
	}
	public String getBriefIdName() {
		return briefIdName;
	}
	public void setBriefIdName(String briefIdName) {
		this.briefIdName = briefIdName;
	}
	public Integer getStatus() {
		return status;
	}
	public void setStatus(Integer status) {
		this.status = status;
	}
	public String getStatusName() {
		return statusName;
	}
	public void setStatusName(String statusName) {
		this.statusName = statusName;
	}
	public String getEnterStock() {
		return enterStock;
	}
	public void setEnterStock(String enterStock) {
		this.enterStock = enterStock;
	}
	public String getEnterStockName() {
		return enterStockName;
	}
	public void setEnterStockName(String enterStockName) {
		this.enterStockName = enterStockName;
	}
	public String getBanendrsmtmk() {
		return banendrsmtmk;
	}
	public void setBanendrsmtmk(String banendrsmtmk) {
		this.banendrsmtmk = banendrsmtmk;
	}
	public String getBanendrsmtmkName() {
		return banendrsmtmkName;
	}
	public void setBanendrsmtmkName(String banendrsmtmkName) {
		this.banendrsmtmkName = banendrsmtmkName;
	}
	
	public String getAcontractNo() {
		return acontractNo;
	}
	public void setAcontractNo(String acontractNo) {
		this.acontractNo = acontractNo;
	}
	public String getAinvcnb() {
		return ainvcnb;
	}
	public void setAinvcnb(String ainvcnb) {
		this.ainvcnb = ainvcnb;
	}
	public String getCnAccrecords() {
		return cnAccrecords;
	}
	public void setCnAccrecords(String cnAccrecords) {
		this.cnAccrecords = cnAccrecords;
	}
	public String getAbs() {
		return abs;
	}
	public void setAbs(String abs) {
		this.abs = abs;
	}
	public String getRmk() {
		return rmk;
	}
	public void setRmk(String rmk) {
		this.rmk = rmk;
	}
	public String getVouIds() {
		return vouIds;
	}
	public void setVouIds(String vouIds) {
		this.vouIds = vouIds;
	}
	public String getViceIds() {
		return viceIds;
	}
	public void setViceIds(String viceIds) {
		this.viceIds = viceIds;
	}
	public String getAccIds() {
		return accIds;
	}
	public void setAccIds(String accIds) {
		this.accIds = accIds;
	}
	public String getSigneroks() {
		return signeroks;
	}
	public void setSigneroks(String signeroks) {
		this.signeroks = signeroks;
	}
	public AbVoucherinfo getAbVoucherInfo() {
		return abVoucherInfo;
	}
	public void setAbVoucherInfo(AbVoucherinfo abVoucherInfo) {
		this.abVoucherInfo = abVoucherInfo;
	}
	public AbVoucherinfoVice getAbVoucherinfoVice() {
		return abVoucherinfoVice;
	}
	public void setAbVoucherinfoVice(AbVoucherinfoVice abVoucherinfoVice) {
		this.abVoucherinfoVice = abVoucherinfoVice;
	}
	public AbAccrecords getAbAccrecords() {
		return abAccrecords;
	}
	public void setAbAccrecords(AbAccrecords abAccrecords) {
		this.abAccrecords = abAccrecords;
	}
	
	public String getBusinessKey() {
		return businessKey;
	}
	public void setBusinessKey(String businessKey) {
		this.businessKey = businessKey;
	}
	
	public String getCheckboxHide() {
		if(this.getStatus()!=null&& AccebillConstant.AB_BILL_BUS_STATUS11.equals(this.getStatus())){
			return "1";
		}
		return checkboxHide;
	}
	public void setCheckboxHide(String checkboxHide) {
		this.checkboxHide = checkboxHide;
	}
	public String getIsQuery() {
		return isQuery;
	}
	public void setIsQuery(String isQuery) {
		this.isQuery = isQuery;
	}
	public String getDimensions() {
		return dimensions;
	}
	public void setDimensions(String dimensions) {
		this.dimensions = dimensions;
	}
	@Override
	public String toString() {
		return "AbBillDiscountAccountVO [id=" + id + ", vid=" + vid + ", vvid=" + vvid + ", vouId=" + vouId
				+ ", viceId=" + viceId + ", accId=" + accId + ", businessKey=" + businessKey + ", billType=" + billType
				+ ", billTypeName=" + billTypeName + ", billFlag=" + billFlag + ", billFlagName=" + billFlagName
				+ ", corpId=" + corpId + ", corpName=" + corpName + ", bankAcc=" + bankAcc + ", bankCode=" + bankCode
				+ ", payer=" + payer + ", payeracc=" + payeracc + ", payerBankName=" + payerBankName + ", payerbankno="
				+ payerbankno + ", acceptor=" + acceptor + ", acceptoracc=" + acceptoracc + ", acceptorBankName="
				+ acceptorBankName + ", acceptorbankno=" + acceptorbankno + ", origNo=" + origNo + ", billMoney="
				+ billMoney + ", startDate=" + startDate + ", endDate=" + endDate + ", applyDate=" + applyDate
				+ ", businessDate=" + businessDate + ", signerok=" + signerok + ", payeeacc=" + payeeacc
				+ ", payeeBankName=" + payeeBankName + ", payeebankno=" + payeebankno + ", briefId=" + briefId
				+ ", briefIdName=" + briefIdName + ", status=" + status + ", statusName=" + statusName + ", enterStock="
				+ enterStock + ", enterStockName=" + enterStockName + ", banendrsmtmk=" + banendrsmtmk
				+ ", banendrsmtmkName=" + banendrsmtmkName + ", acontractNo=" + acontractNo + ", ainvcnb=" + ainvcnb
				+ ", cnAccrecords=" + cnAccrecords + ", abs=" + abs + ", rmk=" + rmk + ", vouIds=" + vouIds
				+ ", viceIds=" + viceIds + ", accIds=" + accIds + ", signeroks=" + signeroks + ", checkboxHide="
				+ checkboxHide + "]";
	}
	
}
