package com.bytter.biz.budget.vo;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import javax.validation.constraints.NotNull;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import com.bytter.core.annotation.BtCompositeSearch;
import com.bytter.core.bpm.IBusinessKey;
import com.bytter.core.constant.CommonConstant;
import com.bytter.core.utils.StringUtils;
import com.bytter.poi.annotation.Excel;
import com.bytter.poi.handler.ExcelModel;
import com.fasterxml.jackson.annotation.JsonFormat;
/**   
 * @Title: Entity
 * @Description: 预算执行表
 * @author onlineGenerator
 * @date 2017-07-18 10:48:05
 * @version 1.0
 *
 */
public class FbsBudgetProcessVO extends ExcelModel implements Serializable,IBusinessKey {
	private String id;
	/**状态
	 * */
	@BtCompositeSearch(fieldName = "t.status")
	//@Dict(dictGroup = "fbsBudgetProcess.status", inject = "statusName") //TODO 数据字典国际化问题，待解决
	private Integer status;
	private String statusName;
	/**创建人登录名称*/
	private String createBy;
	/**创建日期*/
	@BtCompositeSearch(fieldName = "t.create_date")
	@JsonFormat(pattern = "yyyy-MM-dd")
	private Date createDate;
	/**更新人登录名称*/
	private String updateBy;
	/**更新日期*/
	private Date updateDate;
	/**单位ID
	 * */
	@BtCompositeSearch(fieldName = "t.corp_id")
	//@Corp(inject="corpName") TODO 新增冗余字段解决
	private String corpId;
	@BtCompositeSearch(fieldName = "t.corp_id")
	@Excel(name = "*单位名称")
	@NotNull( message = "nis.netbank.message.corpname.null")
	private String corpName;
	//@Currency(inject="curName") TODO 新增冗余字段解决
	private String curId;
	@Excel(name = "*币别")
	@NotNull( message = "nis.netbank.message.curname.null")
	private String curName;
	/**科目Id
	 * 本域表，直接关联查询
	 * */
	//@Dict(tableName="FBS_ITEM",valueFieldName="id",showFiledName="ITEM_NAME",inject="itemName")
	private String itemId;
	/**科目代码*/
	@BtCompositeSearch(fieldName = "t.ITEM_CODE")
	@Excel(name = "*科目代码")
	@NotNull( message = "fin.paramconfig.fcsubject.subjectcode.null")
	private String itemCode;
	/**科目名称
	 * */
	@BtCompositeSearch(fieldName = "f.ITEM_NAME")
	private String itemName;
	/**执行金额*/
	@BtCompositeSearch(fieldName = "t.PROCESS_MONEY")
	@Excel(name = "*执行金额", align = HorizontalAlignment.RIGHT, numFormat = "##########0.00")
	private BigDecimal processMoney;
	/**
	 * 折合人民币
	 */
	private BigDecimal processNatualMoney;
	/**执行日期*/
	@BtCompositeSearch(fieldName = "t.process_Date")
	@JsonFormat(pattern="yyyy-MM-dd")
	@Excel(name = "*执行日期",format = "yyyy-MM-dd")
	private Date processDate;
	/**业务单据号*/
	@BtCompositeSearch(fieldName = "t.bill_no")
	@Excel(name = "单据号")
	private String billNo;
	/**业务单ID*/
	private String billId;
	/**结算类型  0-现款 1-票据*/
	private String settlType;
	/**是否隐藏多选框 1为是*/
	private String checkboxHide;
	/**部门*/
	private java.lang.String deptName;
	/**用途*/
	private java.lang.String purpose;
	/**期望支付日期*/
	private java.util.Date wishPayday;
	/**付款账号*/
	private java.lang.String payerAcc;
	/**收款账号名称*/
	@Excel(name = "收款人")
	@BtCompositeSearch(fieldName = "t.payee_Acc_Name")
	private String payeeAccName;
	/**收款账号*/
	private java.lang.String payeeAcc;
	/**收款开户行名称*/
	private java.lang.String payeeBank;
	/**合同号*/
	private java.lang.String contract;
	/**归属日期*/
	private java.util.Date vestingDate;
	/**备注*/
	@Excel(name = "备注")
	private String rmk;
	/**执行业务名称*/
	@BtCompositeSearch(fieldName = "t.business_Name")
	private String businessName;
	/**执行系统名称*/
	@BtCompositeSearch(fieldName = "t.process_Sysname")
	private String processSysname;
	/**内转标识 1-内转 0- 非内转*/
	@BtCompositeSearch(fieldName = "t.inner_sign")
	@Excel(name = "是否内转业务", replace={"否_0","是_1"})
	private String innerSign;
	/**汇率*/
	private BigDecimal exchangeRate;
	/**执行金额-折合本身币别*/
	private BigDecimal processCurMoney;
	/**
	 * 单位是否受控 0-否 1-是
	 */
	private Integer controlBudget;
	/**
	 * 单位是否上报  0-否 1-是
	 */
	private Integer reportBudget;
	/**
	 * 计划科目
	 */
	private FbsItemVO item = new FbsItemVO();
	/**资金流向1， 资金流入-1 ，资金流出*/
	private Integer sign;
	/**是否计划控制科目 1-是 0-否*/
	private String isControl;
	@Override
	public String getBusinessKey() {
		return getId();
	}
	public String getCheckboxHide() {
		if(CommonConstant.STATUE_DELETE.equals(this.getStatus())){
			return "1";
		}else if(StringUtils.isEmpty(checkboxHide)){
			return "0";
		}
		return checkboxHide;
	}
	public void setCheckboxHide(String checkboxHide) {
		this.checkboxHide = checkboxHide;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
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
	public String getCreateBy() {
		return createBy;
	}
	public void setCreateBy(String createBy) {
		this.createBy = createBy;
	}
	public Date getCreateDate() {
		return createDate;
	}
	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}
	public String getUpdateBy() {
		return updateBy;
	}
	public void setUpdateBy(String updateBy) {
		this.updateBy = updateBy;
	}
	public Date getUpdateDate() {
		return updateDate;
	}
	public void setUpdateDate(Date updateDate) {
		this.updateDate = updateDate;
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
	public String getCurId() {
		return curId;
	}
	public void setCurId(String curId) {
		this.curId = curId;
	}
	public String getCurName() {
		return curName;
	}
	public void setCurName(String curName) {
		this.curName = curName;
	}
	public String getItemId() {
		return itemId;
	}
	public void setItemId(String itemId) {
		this.itemId = itemId;
	}
	public String getItemCode() {
		return itemCode;
	}
	public void setItemCode(String itemCode) {
		this.itemCode = itemCode;
	}
	public String getItemName() {
		return itemName;
	}
	public void setItemName(String itemName) {
		this.itemName = itemName;
	}
	public BigDecimal getProcessMoney() {
		return processMoney;
	}
	public void setProcessMoney(BigDecimal processMoney) {
		this.processMoney = processMoney;
	}
	public BigDecimal getProcessNatualMoney() {
		return processNatualMoney;
	}
	public void setProcessNatualMoney(BigDecimal processNatualMoney) {
		this.processNatualMoney = processNatualMoney;
	}
	public Date getProcessDate() {
		return processDate;
	}
	public void setProcessDate(Date processDate) {
		this.processDate = processDate;
	}
	public String getBillNo() {
		return billNo;
	}
	public void setBillNo(String billNo) {
		this.billNo = billNo;
	}
	public String getBillId() {
		return billId;
	}
	public void setBillId(String billId) {
		this.billId = billId;
	}
	public String getSettlType() {
		return settlType;
	}
	public void setSettlType(String settlType) {
		this.settlType = settlType;
	}
	public String getPayeeAccName() {
		return payeeAccName;
	}
	public void setPayeeAccName(String payeeAccName) {
		this.payeeAccName = payeeAccName;
	}
	public String getRmk() {
		return rmk;
	}
	public void setRmk(String rmk) {
		this.rmk = rmk;
	}
	public String getBusinessName() {
		return businessName;
	}
	public void setBusinessName(String businessName) {
		this.businessName = businessName;
	}
	public String getProcessSysname() {
		return processSysname;
	}
	public void setProcessSysname(String processSysname) {
		this.processSysname = processSysname;
	}
	public String getInnerSign() {
		return innerSign;
	}
	public void setInnerSign(String innerSign) {
		this.innerSign = innerSign;
	}
	public FbsItemVO getItem() {
		return item;
	}
	public void setItem(FbsItemVO item) {
		this.item = item;
	}
	public String getDeptName() {
		return deptName;
	}
	public void setDeptName(String deptName) {
		this.deptName = deptName;
	}
	public String getPurpose() {
		return purpose;
	}
	public void setPurpose(String purpose) {
		this.purpose = purpose;
	}
	public Date getWishPayday() {
		return wishPayday;
	}
	public void setWishPayday(Date wishPayday) {
		this.wishPayday = wishPayday;
	}
	public String getPayerAcc() {
		return payerAcc;
	}
	public void setPayerAcc(String payerAcc) {
		this.payerAcc = payerAcc;
	}
	public String getPayeeAcc() {
		return payeeAcc;
	}
	public void setPayeeAcc(String payeeAcc) {
		this.payeeAcc = payeeAcc;
	}
	public String getPayeeBank() {
		return payeeBank;
	}
	public void setPayeeBank(String payeeBank) {
		this.payeeBank = payeeBank;
	}
	public String getContract() {
		return contract;
	}
	public void setContract(String contract) {
		this.contract = contract;
	}
	public Date getVestingDate() {
		return vestingDate;
	}
	public void setVestingDate(Date vestingDate) {
		this.vestingDate = vestingDate;
	}
	public BigDecimal getExchangeRate() {
		return exchangeRate;
	}
	public void setExchangeRate(BigDecimal exchangeRate) {
		this.exchangeRate = exchangeRate;
	}
	public BigDecimal getProcessCurMoney() {
		return processCurMoney;
	}
	public void setProcessCurMoney(BigDecimal processCurMoney) {
		this.processCurMoney = processCurMoney;
	}
	public Integer getControlBudget() {
		return controlBudget;
	}
	public void setControlBudget(Integer controlBudget) {
		this.controlBudget = controlBudget;
	}
	public Integer getReportBudget() {
		return reportBudget;
	}
	public void setReportBudget(Integer reportBudget) {
		this.reportBudget = reportBudget;
	}
	public Integer getSign() {
		return sign;
	}
	public void setSign(Integer sign) {
		this.sign = sign;
	}
	public String getIsControl() {
		return isControl;
	}
	public void setIsControl(String isControl) {
		this.isControl = isControl;
	}
	@Override
	public String getBeanName() {
		return "fbsBudgetProcessService";
	}
}
