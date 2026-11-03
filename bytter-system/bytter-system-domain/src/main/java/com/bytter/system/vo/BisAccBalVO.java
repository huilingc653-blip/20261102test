package com.bytter.system.vo;
import com.bytter.core.annotation.BtCompositeSearch;
import com.bytter.core.annotation.Corp;
import com.bytter.core.annotation.Currency;
import com.bytter.core.annotation.Dict;
import com.bytter.core.bpm.IBusinessKey;
import com.bytter.poi.annotation.Excel;
import com.bytter.poi.annotation.ExcelTarget;
import com.bytter.poi.handler.ExcelModel;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.springframework.format.annotation.NumberFormat;
import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.Map;
/**
 * @author onlineGenerator
 * @version 1.0
 * @Title: Entity
 * @Description: BT_BANK_ACC
 * @date 2017-06-02 17:50:16
 */
@ExcelTarget("BisAccBalVO")
public class BisAccBalVO extends ExcelModel implements Serializable, IBusinessKey {
	private java.lang.String id;
	/**
	 * 业务单号
	 */
	private java.lang.String accNo;
	/**
	 * 银行账号
	 */
	@Excel(name = "*银行账号")
	@BtCompositeSearch(fieldName = "b.bank_acc")
	private java.lang.String bankAcc;
	/**
	 * 银行名称
	 */
	@BtCompositeSearch(fieldName = "a.acc_name")
	private java.lang.String accName;
	/**
	 * 开户行
	 */
	@BtCompositeSearch(fieldName = "a.bank_name")
	private java.lang.String bankName;
	/**
	 * 单位ID
	 */
	@Corp(inject = "corpName")
	@BtCompositeSearch(fieldName = "b.corp_id")
	private java.lang.String corpId;
	/**
	 * 单位
	 */
	@BtCompositeSearch(fieldName = "b.corp_id")
	private java.lang.String corpName;
	/**
	 * 币别ID
	 */
	@Currency(inject = "curName")
	@BtCompositeSearch(fieldName = "b.cur_id")
	private java.lang.String curId;
	/**
	 * 币别
	 */
	@Excel(name = "*币别")
	@BtCompositeSearch(fieldName = "b.cur_id")
	private java.lang.String curName;
	/**
	 * 接口代码
	 */
	@BtCompositeSearch(fieldName = "b.bif_code")
	private java.lang.String bifCode;
	/**
	 * 获取时间
	 */
	@BtCompositeSearch(fieldName = "b.get_date")
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private java.util.Date getDate;
	/**
	 * 余额日期
	 */
	@Excel(name = "*余额日期")
	@JsonFormat(pattern = "yyyy-MM-dd")
	@BtCompositeSearch(fieldName = "b.bal_date")
	private java.util.Date balDate;
	/**
	 * 余额
	 */
	@NumberFormat(pattern = "#,##0.00")
	@Excel(name = "*余额", align = HorizontalAlignment.RIGHT, numFormat = "##########0.00")
	@BtCompositeSearch(fieldName = "b.bal")
	private java.math.BigDecimal bal;
	/**
	 * 可用余额
	 */
	@NumberFormat(pattern = "#,##0.00")
	@Excel(name = "可用余额", align = HorizontalAlignment.RIGHT, numFormat = "##########0.00")
	@BtCompositeSearch(fieldName = "b.avail_bal")
	private java.math.BigDecimal availBal;
	/**
	 * 冻结金额
	 */
	@NumberFormat(pattern = "#,##0.00")
	@BtCompositeSearch(fieldName = "b.frz_bal")
	private java.math.BigDecimal frzBal;
	/**
	 * 返回码
	 */
	private java.lang.String returnCode;
	/**
	 * 返回描述信息
	 */
	@BtCompositeSearch(fieldName = "b.return_Msg")
	private java.lang.String returnMsg;
	/**
	 * 状态
	 */
	@Dict(dictGroup = "cgsBill.status", inject = "statusLang")
	@BtCompositeSearch(fieldName = "b.status")
	private java.lang.Integer status;
	private String statusLang;
	/**
	 * 创建人登录名称
	 */
	private java.lang.String createBy;
	/**
	 * 创建日期
	 */
	private java.util.Date createDate;
	/**
	 * 更新人登录名称
	 */
	private java.lang.String updateBy;
	/**
	 * 更新日期
	 */
	private java.util.Date updateDate;
	/**
	 * 查询银行账户集合
	 */
	private List<Map<String, Object>> btBankAccList;
	/**
	 * 由明细生成余额开始时间
	 */
	private Date beginDate;
	/**
	 * 由明细生成余额结束时间
	 */
	private Date endDate;
	/**
	 * 是否需要重新生成余额
	 */
	private Integer generateBalance;
	/**
	 * 银行类型
	 */
	@BtCompositeSearch(fieldName = "a.bank_type_id")
	private String bankTypeId;
	/**
	 * 账户属性
	 */
	@BtCompositeSearch(fieldName = "a.attribute_Id")
	@Dict(tableName="BIS_ACC_ATTRIBUTE",valueFieldName="id",showFiledName="ATTRIBUTE_NAME",inject="attributeName")
	private String attributeId;
	@BtCompositeSearch(fieldName = "a.attribute_Id")
	private String attributeName;
	/**
	 * 账户性质
	 */
	@BtCompositeSearch(fieldName = "a.nature_id")
	@Dict(tableName="BT_ACC_NATURE",valueFieldName="id",showFiledName="NATURE_NAME",inject="natureName")
	private String natureId;
	@BtCompositeSearch(fieldName = "a.nature_id")
	private String natureName;
	/**账户类别*/
	@Dict(dictGroup = "btBankAcc.accType", inject = "accTypeName")
	@BtCompositeSearch(fieldName = "a.acc_type")
	private String accType;
	@BtCompositeSearch(fieldName = "a.acc_type")
	private String accTypeName;
	/**
	 * 新增标志：0-表银行账户余额管理新增 1-表银行账户流水管理新增
	 */
	private String addFlag;
	/**
	 * 初始化余额标记（针对离线账号）
	 */
	private String initSign;
	public java.lang.String getId() {
		return id;
	}
	public void setId(java.lang.String id) {
		this.id = id;
	}
	public java.lang.String getAccNo() {
		return accNo;
	}
	public void setAccNo(java.lang.String accNo) {
		this.accNo = accNo;
	}
	public java.lang.String getBankAcc() {
		return bankAcc;
	}
	public void setBankAcc(java.lang.String bankAcc) {
		this.bankAcc = bankAcc;
	}
	public java.lang.String getAccName() {
		return accName;
	}
	public void setAccName(java.lang.String accName) {
		this.accName = accName;
	}
	public java.lang.String getBankName() {
		return bankName;
	}
	public void setBankName(java.lang.String bankName) {
		this.bankName = bankName;
	}
	public java.lang.String getCorpId() {
		return corpId;
	}
	public void setCorpId(java.lang.String corpId) {
		this.corpId = corpId;
	}
	public java.lang.String getCorpName() {
		return corpName;
	}
	public void setCorpName(java.lang.String corpName) {
		this.corpName = corpName;
	}
	public java.lang.String getCurId() {
		return curId;
	}
	public void setCurId(java.lang.String curId) {
		this.curId = curId;
	}
	public java.lang.String getCurName() {
		return curName;
	}
	public void setCurName(java.lang.String curName) {
		this.curName = curName;
	}
	public java.lang.String getBifCode() {
		return bifCode;
	}
	public void setBifCode(java.lang.String bifCode) {
		this.bifCode = bifCode;
	}
	public java.util.Date getGetDate() {
		return getDate;
	}
	public void setGetDate(java.util.Date getDate) {
		this.getDate = getDate;
	}
	public java.util.Date getBalDate() {
		return balDate;
	}
	public void setBalDate(java.util.Date balDate) {
		this.balDate = balDate;
	}
	public java.math.BigDecimal getBal() {
		return bal;
	}
	public void setBal(java.math.BigDecimal bal) {
		this.bal = bal;
	}
	public java.math.BigDecimal getAvailBal() {
		return availBal;
	}
	public void setAvailBal(java.math.BigDecimal availBal) {
		this.availBal = availBal;
	}
	public java.math.BigDecimal getFrzBal() {
		return frzBal;
	}
	public void setFrzBal(java.math.BigDecimal frzBal) {
		this.frzBal = frzBal;
	}
	public String getReturnCode() {
		return returnCode;
	}
	public void setReturnCode(String returnCode) {
		this.returnCode = returnCode;
	}
	public java.lang.String getReturnMsg() {
		return returnMsg;
	}
	public void setReturnMsg(java.lang.String returnMsg) {
		this.returnMsg = returnMsg;
	}
	public java.lang.Integer getStatus() {
		return status;
	}
	public void setStatus(java.lang.Integer status) {
		this.status = status;
	}
	public java.lang.String getCreateBy() {
		return createBy;
	}
	public void setCreateBy(java.lang.String createBy) {
		this.createBy = createBy;
	}
	public java.util.Date getCreateDate() {
		return createDate;
	}
	public void setCreateDate(java.util.Date createDate) {
		this.createDate = createDate;
	}
	public java.lang.String getUpdateBy() {
		return updateBy;
	}
	public void setUpdateBy(java.lang.String updateBy) {
		this.updateBy = updateBy;
	}
	public java.util.Date getUpdateDate() {
		return updateDate;
	}
	public void setUpdateDate(java.util.Date updateDate) {
		this.updateDate = updateDate;
	}
	public List<Map<String, Object>> getBtBankAccList() {
		return btBankAccList;
	}
	public void setBtBankAccList(List<Map<String, Object>> btBankAccList) {
		this.btBankAccList = btBankAccList;
	}
	public Date getBeginDate() {
		return beginDate;
	}
	public void setBeginDate(Date beginDate) {
		this.beginDate = beginDate;
	}
	public Date getEndDate() {
		return endDate;
	}
	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}
	public Integer getGenerateBalance() {
		return generateBalance;
	}
	public void setGenerateBalance(Integer generateBalance) {
		this.generateBalance = generateBalance;
	}
	public String getBankTypeId() {
		return bankTypeId;
	}
	public void setBankTypeId(String bankTypeId) {
		this.bankTypeId = bankTypeId;
	}
	public String getAttributeId() {
		return attributeId;
	}
	public void setAttributeId(String attributeId) {
		this.attributeId = attributeId;
	}
	public String getAttributeName() {
		return attributeName;
	}
	public void setAttributeName(String attributeName) {
		this.attributeName = attributeName;
	}
	public String getNatureId() {
		return natureId;
	}
	public void setNatureId(String natureId) {
		this.natureId = natureId;
	}
	public String getNatureName() {
		return natureName;
	}
	public void setNatureName(String natureName) {
		this.natureName = natureName;
	}
	public String getAccType() {
		return accType;
	}
	public void setAccType(String accType) {
		this.accType = accType;
	}
	public String getAccTypeName() {
		return accTypeName;
	}
	public void setAccTypeName(String accTypeName) {
		this.accTypeName = accTypeName;
	}
	public String getStatusLang() {
		return statusLang;
	}
	public void setStatusLang(String statusLang) {
		this.statusLang = statusLang;
	}
	public String getAddFlag() {
		return addFlag;
	}
	public void setAddFlag(String addFlag) {
		this.addFlag = addFlag;
	}
	public String getInitSign() {
		return initSign;
	}
	public void setInitSign(String initSign) {
		this.initSign = initSign;
	}
	@Override
	public String getBusinessKey() {
		return getId();
	}
	@Override
	public String getBeanName() {
		return "bisAccBalService";
	}
	@Override
	public String toString() {
		return "BisAccBalVO{" +
				"id='" + id + '\'' +
				", accNo='" + accNo + '\'' +
				", bankAcc='" + bankAcc + '\'' +
				", accName='" + accName + '\'' +
				", bankName='" + bankName + '\'' +
				", corpId='" + corpId + '\'' +
				", corpName='" + corpName + '\'' +
				", curId='" + curId + '\'' +
				", curName='" + curName + '\'' +
				", bifCode='" + bifCode + '\'' +
				", getDate=" + getDate +
				", balDate=" + balDate +
				", bal=" + bal +
				", availBal=" + availBal +
				", frzBal=" + frzBal +
				", returnCode='" + returnCode + '\'' +
				", returnMsg='" + returnMsg + '\'' +
				", status=" + status +
				", createBy='" + createBy + '\'' +
				", createDate=" + createDate +
				", updateBy='" + updateBy + '\'' +
				", updateDate=" + updateDate +
				", btBankAccList=" + btBankAccList +
				", beginDate=" + beginDate +
				", endDate=" + endDate +
				", generateBalance=" + generateBalance +
				", bankTypeId='" + bankTypeId + '\'' +
				", attributeId='" + attributeId + '\'' +
				", natureId='" + attributeId + '\'' +
				", natureName='" + natureName + '\'' +
				", accType='" + accType + '\'' +
				", accTypeName='" + accTypeName + '\'' +
				'}';
	}
}
