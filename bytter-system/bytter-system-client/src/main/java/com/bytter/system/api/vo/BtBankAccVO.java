package com.bytter.system.api.vo;
import com.bytter.core.annotation.*;
import com.bytter.core.bpm.IBusinessKey;
import com.bytter.core.utils.DateUtils;
import com.bytter.poi.annotation.Excel;
import com.bytter.poi.annotation.ExcelTarget;
import com.bytter.poi.handler.ExcelModel;
import com.fasterxml.jackson.annotation.JsonFormat;


import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
/**
 * @Title: Entity
 * @Description: BT_BANK_ACC
 * @author onlineGenerator
 * @date 2017-06-02 17:50:16
 * @version 1.0
 *
 */
@ExcelTarget("BtBankAccVO")
public class BtBankAccVO extends ExcelModel implements java.io.Serializable, IBusinessKey {
	/**
	 * id
	 */

	private static final long serialVersionUID = 6490814688726935044L;


	@BtCompositeSearch(fieldName = "acc.id")
	private String id;

	/**银行账号*/

	@Excel(name = "*银行账号")
	@BtCompositeSearch(fieldName = "acc.bank_acc")
	private String bankAcc;

	/**账户名称*/

	@Excel(name = "*账户名称")
	@BtCompositeSearch(fieldName = "acc.acc_name")
	private String accName;


	private String counterAccId;


	private String superCounterAcc;

	/**父账号*/

	@BtCompositeSearch(fieldName = "acccur.prent_acc_id")
	private String prentAccId;


	private String prentAccName;


	private String prentBankAcc;


	@Dict(dictGroup = "btBankAcc.accType", inject = "prentAccTypeName")
	private String prentAccType;


	private String prentAccTypeName;

	/**父账号标志*/

	@BtCompositeSearch(fieldName = "acc.parent_sign")
	private String parentSign;

	/**单位ID*/

	@Corp(inject="corpName")
	@BtCompositeSearch(fieldName = "acc.corp_id")
	private String corpId;


	@BtCompositeSearch(fieldName ="sy.net_id")
	private String netId;


	@Excel(name = "*开户单位")
	@BtCompositeSearch(fieldName = "acc.corp_id")
	private String corpName;


	@Excel(name = "*币别")
	@BtCompositeSearch(fieldName = "bc.cur_name")
	private String curName;


	@Currencys(
			{
					@Currency(inject="curName"),
					@Currency(inject="curEnglishCode", field = "englishCode")
			}
	)
	@BtCompositeSearch(fieldName = "acccur.cur_id")
	private String curId;


	private String curEnglishCode;

	/**账户类别*/

	@Dict(dictGroup = "btBankAcc.accType", inject = "accTypeName")
	@BtCompositeSearch(fieldName = "acc.acc_type")
	private String accType;


	@Excel(name = "*账户类别")
	@BtCompositeSearch(fieldName = "acc.acc_type")
	private String accTypeName;

	/**账户属性*/

	@BtCompositeSearch(fieldName = "acc.attribute_id")
	@Dict(tableName="BIS_ACC_ATTRIBUTE",valueFieldName="id",showFiledName="ATTRIBUTE_NAME",inject="attributeName")
	private String attributeId;


	@Excel(name = "*账户属性")
	@BtCompositeSearch(fieldName = "acc.attribute_id")
	private String attributeName;

	/**账户性质*/

	@BtCompositeSearch(fieldName = "acc.nature_id")
	@Dict(tableName="BT_ACC_NATURE",valueFieldName="id",showFiledName="NATURE_NAME",inject="natureName")
	private String natureId;


	@Excel(name = "*账户性质")
	@BtCompositeSearch(fieldName = "acc.nature_id")
	private String natureName;

	/**银行类别*/

	@BtCompositeSearch(fieldName = "acc.bank_type_id")
	@Dict(tableName="BT_BANK_TYPE",valueFieldName="id",showFiledName="TYPE_NAME",inject="bankTypeName")
	@BankTypes(
			{
					@BankType(inject="bankTypeName"),
					@BankType(inject="bankPrefix", field = "bankPrefix"),
					@BankType(inject="bankNamePrefix", field = "bankNamePrefix")
			}
	)
	private String bankTypeId;


	@Excel(name = "*银行类别")
	@BtCompositeSearch(fieldName = "acc.bank_type_id")
	private String bankTypeName;


	private String bankPrefix;


	private String bankNamePrefix;

	/**开户行行号*/

	@Excel(name = "*开户行行号")
	private String bankCode;

	/**开户行名称*/

	@BtCompositeSearch(fieldName = "acc.bank_name")
	private String bankName;

	/**所在省*/

	@Region(inject="provName")
	private String prov;


	@BtCompositeSearch(fieldName = "acc.prov")
	private String provName;

	/**所在市*/

	@Region(inject="cityName")
	private String city;


	@BtCompositeSearch(fieldName = "acc.city")
	private String cityName;

	/**接口代码*/

	@BtCompositeSearch(fieldName = "acc.bif_code")
	@Dict(tableName="BIS_BIF_INIT",valueFieldName="id",showFiledName="NAME",inject="bifName")
	private String bifCode;

	/**接口代码名称*/

	@Excel(name = "*接口代码")
	@BtCompositeSearch(fieldName = "acc.bif_code")
	private String bifName;

	/**电票专户*/

	@Dict(dictGroup = "sf_yn", inject = "electricBillName")
	private String electricBill;


	@BtCompositeSearch(fieldName = "acc.electric_bill")
	private String electricBillName;

	/**定期期限*/

	@Excel(name = "定期期限")
	private String regulars;

	/**定期到期时间*/

	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private Date regularDate;

	/**通知类型*/

	@Excel(name = "通知类型")
	private String noticeType;

	/**协定金额*/

	@Excel(name = "协定金额")
	private BigDecimal agrMoney;

	/**协定利率*/

	@Excel(name = "协定利率")
	private BigDecimal agrInterest;

	/**协定合同*/

	@Excel(name = "协定合同")
	private String agrContract;

	/**协定开始时间*/

	@Excel(name = "协定开始日期")
	@JsonFormat(pattern = "yyyy-MM-dd")
	private Date agrBeginDate;

	/**协定结束时间*/

	@Excel(name = "协定结束日期")
	@JsonFormat(pattern = "yyyy-MM-dd")
	private Date agrEndDate;

	/**利率*/

	@Excel(name = "利率")
	@BtCompositeSearch(fieldName = "acc.rates")
	private BigDecimal rates;

	/**利率是否浮动*/

	@Dict(dictGroup = "sf_yn", inject = "ratesFloatName")
	private String ratesFloat;

	/**利率是否浮动*/

	@Excel(name = "*利率是否浮动")
	private String ratesFloatName;

	/**浮动百分比*/

	@Excel(name = "浮动百分比")
	private BigDecimal ratesFloatPer;


	private String ratesFloatPerStr;

	/**本册笔号*/

	@Excel(name = "本册笔号")
	private String volumeNum;

	/**存则册号*/

	@Excel(name = "存则册号")
	private String bankbookNum;

	/**计息周期*/

	private Integer interestCycle;


	@Excel(name = "计息周期")
	private String interestCycleStr;

	/**计息日*/

	@Excel(name = "计息日")
	private String interestDate;

	/**开户日期*/

	@Excel(name = "*开户日期")
	@JsonFormat(pattern = "yyyy-MM-dd")
	@BtCompositeSearch(fieldName = "acc.reg_date")
	private Date regDate;


	private String regDateStr;

	/**对私编号*/

	@Excel(name = "对私编号")
	private String privateNo;

	/**是否上线*/

	@Dict(dictGroup = "sf_yn", inject = "isOnlineStr")
	@BtCompositeSearch(fieldName = "acc.is_online")
	@Excel(name = "*是否上线")
	private String isOnline;


	@BtCompositeSearch(fieldName = "acc.is_online")
	private String isOnlineStr;

	/**是否查询余额或明细*/

	@Dict(dictGroup = "sf_yn", inject = "scanFlagStr")
	private String scanFlag;


	private String scanFlagStr;

	/**余额初始日期*/

	@Excel(name = "初始余额日期")
	@JsonFormat(pattern = "yyyy-MM-dd")
	private Date initBalanceDate;


	private String initBalanceDateStr;

	/**初始余额*/

	@Excel(name = "初始余额")
	private BigDecimal initBalance;

	/**备注*/
    @BtCompositeSearch(fieldName = "acc.rmk")
	@Excel(name = "备注")
	private String rmk;

	/**有效标志*/

	@Dict(dictGroup = "bankAcc.validSign", inject = "validSignStr")
	@BtCompositeSearch(fieldName = "acc.valid_sign")
	@Excel(name = "*有效标志")
	private String validSign;


	@BtCompositeSearch(fieldName = "acc.valid_sign")
	private String validSignStr;

	/**状态*/

	@Dict(dictGroup = "bankAcc.handleStatu", inject = "statusStr")
	@BtCompositeSearch(fieldName = "acc.status")
	private Integer status;


	@BtCompositeSearch(fieldName = "acc.status")
	private String statusStr;

	/**建行机构号*/

	@Excel(name = "建行机构号")
	private String orgCode;

	/**创建人登录名称*/

	@BtCompositeSearch(fieldName = "acc.create_by")
	private String createBy;

	/**创建日期*/

	private Date createDate;

	/**更新人登录名称*/

	private String updateBy;

	/**更新日期*/

	@JsonFormat(pattern = "yyyy-MM-dd")
	private Date updateDate;

	/**账号申请表id*/

	private String appId;

	/**
	 * 是否相关联
	 */

	private String isRelation;

	/**
	 * 关联的内部账号
	 */

	@BtCompositeSearch(fieldName = "counterAcc")
	private String counterAcc;

	/**
	 * 关联上级内部账号
	 */

	@BtCompositeSearch(fieldName = "superCurrentAcc")
	private String superCurrentAcc;


	private List<BtBankAccVO> childList;


	private List<BtBankAccCurVO> curList;

	/**银行账户余额*/

	private BigDecimal bankAccBalance;

	/**内部账户余额*/

	private BigDecimal fcBalance;


	private String flag;

	/** 父账户信息*/

	private BtBankAccVO parentBankAcc;

	/**附件ID*/

	private java.lang.String attachmentId;

	/**实体账号**/

	private java.lang.String entityAccId;

	/**数据来源0:开户申请1：自主办理**/

	private String isFrom;

	/**账户流水截止日期*/

	private String endDtlDate;

	/**是否资金池账号**/

	@Dict(dictGroup = "sf_yn", inject = "isCapitalPoolStr")
	private String isCapitalPool;


	@BtCompositeSearch(fieldName = "acc.is_capital_pool")
	private String isCapitalPoolStr;

	/**流通方式 0-线下 1-线上*/

	@Dict(dictGroup = "bankAcc.circulationMode", inject = "circulationModeName")
	private String circulationMode;


	@BtCompositeSearch(fieldName = "acc.circulation_mode")
	@Excel(name = "流通方式")
	private String circulationModeName;

	/**到期日期*/
	@JsonFormat(pattern = "yyyy-MM-dd")
	private Date endDate;

	/**
	 * 资金池标识 0-非资金池；1-资金池
	 */

	private String poolSign;

	/**
	 * 资金池ID
	 */

	private String poolId;

	/**账户年检到期日*/

	@JsonFormat(pattern = "yyyy-MM-dd")
	@BtCompositeSearch(fieldName = "acc.check_expire_date")
	private Date checkExpireDate;


	private String checkExpireDateStr;

	/**
	 * 银行组织机构代码
	 */

	private String bankOrgCode;

	/**对外属性*/

	private String foreignDes;

	/**
	 * 账户余额
	 */

	private BigDecimal balance;

	/**
	 * 境内/境外账号 1-境内，0-境外
	 */

	@Dict(dictGroup = "btBankAcc.isDomesticBank", inject = "isDomesticBankName")
	private String isDomesticBank;


	@BtCompositeSearch(fieldName = "acc.is_domestic_bank")
	@Excel(name = "*境内/境外账号")
	private String isDomesticBankName;

	/**
	 * 预留印鉴图片地址
	 */

	private String sealDirectory;


	private String[] sealDirectoryList;

	/**
	 * 预留印鉴联系人
	 */

	private String sealPerson;

	/**
	 * 预留印鉴联系人电话
	 */

	private String sealPhone;

	/**
	 * 预留印鉴联系人邮箱
	 */

	private String sealEmail;

	/**
	 * 账户管理部门
	 */

	private String mgtDepartment;

	/**
	 * 网银管理员
	 */

	private String uKeyAdmin;

	/**
	 * 网银操作员
	 */

	private String uKeyOperator;

	/**
	 * 网银复核员1
	 */

	private String uKeyReviewers1;

	/**
	 * 网银复核员2
	 */

	private String uKeyReviewers2;

	/**
	 * 网银复核员3
	 */

	private String uKeyReviewers3;

	/**
	 * 网银功能
	 */

	private String uKeyFunction;

	/**
	 * 网银编码-复选框，包含1-查询功能、2-转账功能、3-电子票据、4-网上对账、5-接收功能、6-签发功能、7-二维码收款、8-其他
	 */

	private String uKeyFunctionCode;

	/**
	 * 网银功能其他信息
	 */

	private String uKeyFunctionOther;

	/**
	 * 是否启用限额管理
	 */

	private String isLimitQuota;

	/**
	 * 日累计支付限额
	 */

	private BigDecimal dayLimitQuota;

	/**
	 * 月累计支付限额
	 */

	private BigDecimal monthLimitQuota;

	/**
	 * 单笔支付限额
	 */

	private BigDecimal payLimitQuota;

	/**
	 * 透支额度
	 */

	private BigDecimal overLimitQuota;

	/**
	 * 银行类型icon
	 */

	private String icon;

	/**
	 * 账户注销日期
	 */

	@JsonFormat(pattern = "yyyy-MM-dd")
	@BtCompositeSearch(fieldName = "acc.close_Date")
	private Date closeDate;

	/**
	 * 是否线上办理
	 */

	private String isOnlineHandle;

	/**
	 * 是否离岸账户
	 */

	private String isOffshoreAccount;

	/**
	 * 是否核查
	 */

	@Dict(dictGroup = "sf_yn", inject = "isCheckStr")
	@BtCompositeSearch(fieldName = "acc.is_Check")
	private String isCheck;


	private String accUseCode;
	private String isCheckStr;


	private String  downAcc;


	private String  downAccName;


	private String  downAccPrent;


	private String  downAccNamePrent;


	/**
	 * 大区
	 */

	private String parentArea;

	/**
	 * 营运区
	 */

	private String operationAreaID;


	private String operationAreaName;

	/**
	 * 实时余额
	 */

	private BigDecimal realTimeBal;

	/**
	 * 附件标识  1-存在 0-不存在
	 */

	private String attachmentSign;

	/**
	 * 绑定账号
	 **/

	private String bindingBankAcc;


	private String bindingBankAccStr;

	/**
	 * 绑定账号单位名称
	 **/

	private String bindingBankAccCorpName;

	/**
	 * 绑定账号户名
	 **/

	private String bindingBankAccAccName;

	/**
	 * 绑定账号开户行
	 **/

	private String bindingBankAccBankName;

	/**
	 * 绑定账号银行类型名称
	 **/

	private String bindingBankTypeName;

	/**
	 * 股权
	 **/

	@BtCompositeSearch(fieldName = "sem.Equity_code")
	private String shareholding;

	/**
	 * 是否参与资金需求计算
	 **/

	private String isDemand;


	@BtCompositeSearch(fieldName = "a.sign")
	private String sign;

	private String accUseName;

	/**
	 * 绑定人名称
	 **/

	private String bindingByName;

	/**
	 * 绑定人手机
	 **/

	private String bindingByMobile;

	/**
	 * 登录密码
	 **/

	private String loginPassword;

	/**
	 * 支付密码
	 **/

	private String payPassword;

	/**
	 * 绑定账户性质
	 **/

	private String bindingNatureId;


	private String bindingNatureName;

	/**
	 * 绑定账户用户组
	 */

	private String bindingAccGroupCode;


	private String corpParentIds;


	private String sysProvinceId;


	private String sysProvinceName;


	private String isClose;

	/**
	 * RPA功能
	 */
	private String rpaFunction;

	/**
	 * 是否代管账户
	 */
	private String isRpaEscrow;

	public String getAttachmentSign() {
		return attachmentSign;
	}

	public void setAttachmentSign(String attachmentSign) {
		this.attachmentSign = attachmentSign;
	}

	public String getRpaFunction() {
		return rpaFunction;
	}

	public void setRpaFunction(String rpaFunction) {
		this.rpaFunction = rpaFunction;
	}

	public String getIsRpaEscrow() {
		return isRpaEscrow;
	}

	public void setIsRpaEscrow(String isRpaEscrow) {
		this.isRpaEscrow = isRpaEscrow;
	}

	public String getAccUseCode() {
		return accUseCode;
	}

	public void setAccUseCode(String accUseCode) {
		this.accUseCode = accUseCode;
	}

	public String getParentArea() {
		return parentArea;
	}

	public void setParentArea(String parentArea) {
		this.parentArea = parentArea;
	}

	public String getOperationAreaID() {
		return operationAreaID;
	}

	public void setOperationAreaID(String operationAreaID) {
		this.operationAreaID = operationAreaID;
	}

	public String getOperationAreaName() {
		return operationAreaName;
	}

	public void setOperationAreaName(String operationAreaName) {
		this.operationAreaName = operationAreaName;
	}

	public BigDecimal getRealTimeBal() {
		return realTimeBal;
	}

	public void setRealTimeBal(BigDecimal realTimeBal) {
		this.realTimeBal = realTimeBal;
	}


	public String getDownAcc() {
		return downAcc;
	}
	public void setDownAcc(String downAcc) {
		this.downAcc = downAcc;
	}
	public String getDownAccName() {
		return downAccName;
	}
	public void setDownAccName(String downAccName) {
		this.downAccName = downAccName;
	}
	public String getDownAccPrent() {
		return downAccPrent;
	}
	public void setDownAccPrent(String downAccPrent) {
		this.downAccPrent = downAccPrent;
	}
	public String getDownAccNamePrent() {
		return downAccNamePrent;
	}
	public void setDownAccNamePrent(String downAccNamePrent) {
		this.downAccNamePrent = downAccNamePrent;
	}
	/**
	 * 招行虚拟账户信息
	 */
	private List<BtBankAccVirtualVO> bankAccVirtual = new ArrayList<>();
	public String getIcon() {
		return icon;
	}
	public void setIcon(String icon) {
		this.icon = icon;
	}
	public String getIsLimitQuota() {
		return isLimitQuota;
	}
	public void setIsLimitQuota(String isLimitQuota) {
		this.isLimitQuota = isLimitQuota;
	}
	public BigDecimal getDayLimitQuota() {
		return dayLimitQuota;
	}
	public void setDayLimitQuota(BigDecimal dayLimitQuota) {
		this.dayLimitQuota = dayLimitQuota;
	}
	public BigDecimal getMonthLimitQuota() {
		return monthLimitQuota;
	}
	public void setMonthLimitQuota(BigDecimal monthLimitQuota) {
		this.monthLimitQuota = monthLimitQuota;
	}
	public BigDecimal getPayLimitQuota() {
		return payLimitQuota;
	}
	public void setPayLimitQuota(BigDecimal payLimitQuota) {
		this.payLimitQuota = payLimitQuota;
	}
	public BigDecimal getOverLimitQuota() {
		return overLimitQuota;
	}
	public void setOverLimitQuota(BigDecimal overLimitQuota) {
		this.overLimitQuota = overLimitQuota;
	}
	public String getuKeyAdmin() {
		return uKeyAdmin;
	}
	public void setuKeyAdmin(String uKeyAdmin) {
		this.uKeyAdmin = uKeyAdmin;
	}
	public String getuKeyOperator() {
		return uKeyOperator;
	}
	public void setuKeyOperator(String uKeyOperator) {
		this.uKeyOperator = uKeyOperator;
	}
	public String getuKeyReviewers1() {
		return uKeyReviewers1;
	}
	public void setuKeyReviewers1(String uKeyReviewers1) {
		this.uKeyReviewers1 = uKeyReviewers1;
	}
	public String getuKeyReviewers2() {
		return uKeyReviewers2;
	}
	public void setuKeyReviewers2(String uKeyReviewers2) {
		this.uKeyReviewers2 = uKeyReviewers2;
	}
	public String getuKeyReviewers3() {
		return uKeyReviewers3;
	}
	public void setuKeyReviewers3(String uKeyReviewers3) {
		this.uKeyReviewers3 = uKeyReviewers3;
	}
	public String getuKeyFunction() {
		return uKeyFunction;
	}
	public void setuKeyFunction(String uKeyFunction) {
		this.uKeyFunction = uKeyFunction;
	}
	public String getuKeyFunctionCode() {
		return uKeyFunctionCode;
	}
	public void setuKeyFunctionCode(String uKeyFunctionCode) {
		this.uKeyFunctionCode = uKeyFunctionCode;
	}
	public String getBankOrgCode() {
		return bankOrgCode;
	}
	public void setBankOrgCode(String bankOrgCode) {
		this.bankOrgCode = bankOrgCode;
	}
	public String getCheckExprireDateStr() {
		return checkExpireDateStr;
	}
	public void setCheckExprireDateStr(String checkExpireDateStr) {
		this.checkExpireDateStr = checkExpireDateStr;
	}
	public String getIsFrom() {
		return isFrom;
	}
	public void setIsFrom(String isFrom) {
		this.isFrom = isFrom;
	}
	public String getBankAcc() {
		return bankAcc;
	}
	public void setBankAcc(String bankAcc) {
		this.bankAcc = bankAcc;
	}
	public String getAccName() {
		return accName;
	}
	public void setAccName(String accName) {
		this.accName = accName;
	}
	public String getCounterAccId() {
		return counterAccId;
	}
	public void setCounterAccId(String counterAccId) {
		this.counterAccId = counterAccId;
	}
	public String getPrentAccId() {
		return prentAccId;
	}
	public void setPrentAccId(String prentAccId) {
		this.prentAccId = prentAccId;
	}
	public String getParentSign() {
		return parentSign;
	}
	public void setParentSign(String parentSign) {
		this.parentSign = parentSign;
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
	public String getCurName() {
		return curName;
	}
	public void setCurName(String curName) {
		this.curName = curName;
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
	public String getBankTypeId() {
		return bankTypeId;
	}
	public void setBankTypeId(String bankTypeId) {
		this.bankTypeId = bankTypeId;
	}
	public String getBankTypeName() {
		return bankTypeName;
	}
	public void setBankTypeName(String bankTypeName) {
		this.bankTypeName = bankTypeName;
	}
	public String getBankPrefix() {
		return bankPrefix;
	}
	public void setBankPrefix(String bankPrefix) {
		this.bankPrefix = bankPrefix;
	}
	public String getBankNamePrefix() {
		return bankNamePrefix;
	}
	public void setBankNamePrefix(String bankNamePrefix) {
		this.bankNamePrefix = bankNamePrefix;
	}
	public String getBankCode() {
		return bankCode;
	}
	public void setBankCode(String bankCode) {
		this.bankCode = bankCode;
	}
	public String getBankName() {
		return bankName;
	}
	public void setBankName(String bankName) {
		this.bankName = bankName;
	}
	public String getProv() {
		return prov;
	}
	public void setProv(String prov) {
		this.prov = prov;
	}
	public String getProvName() {
		return provName;
	}
	public void setProvName(String provName) {
		this.provName = provName;
	}
	public String getCity() {
		return city;
	}
	public void setCity(String city) {
		this.city = city;
	}
	public String getCityName() {
		return cityName;
	}
	public void setCityName(String cityName) {
		this.cityName = cityName;
	}
	public String getBifCode() {
		return bifCode;
	}
	public void setBifCode(String bifCode) {
		this.bifCode = bifCode;
	}
	public String getRegulars() {
		return regulars;
	}
	public void setRegulars(String regulars) {
		this.regulars = regulars;
	}
	public Date getRegularDate() {
		return regularDate;
	}
	public void setRegularDate(Date regularDate) {
		this.regularDate = regularDate;
	}
	public String getNoticeType() {
		return noticeType;
	}
	public void setNoticeType(String noticeType) {
		this.noticeType = noticeType;
	}
	public BigDecimal getAgrMoney() {
		return agrMoney;
	}
	public void setAgrMoney(BigDecimal agrMoney) {
		this.agrMoney = agrMoney;
	}
	public BigDecimal getAgrInterest() {
		return agrInterest;
	}
	public void setAgrInterest(BigDecimal agrInterest) {
		this.agrInterest = agrInterest;
	}
	public String getAgrContract() {
		return agrContract;
	}
	public void setAgrContract(String agrContract) {
		this.agrContract = agrContract;
	}
	public Date getAgrBeginDate() {
		return agrBeginDate;
	}
	public void setAgrBeginDate(Date agrBeginDate) {
		this.agrBeginDate = agrBeginDate;
	}
	public Date getAgrEndDate() {
		return agrEndDate;
	}
	public void setAgrEndDate(Date agrEndDate) {
		this.agrEndDate = agrEndDate;
	}
	public BigDecimal getRates() {
		return rates;
	}
	public void setRates(BigDecimal rates) {
		this.rates = rates;
	}
	public String getRatesFloat() {
		return ratesFloat;
	}
	public void setRatesFloat(String ratesFloat) {
		this.ratesFloat = ratesFloat;
	}
	public BigDecimal getRatesFloatPer() {
		return ratesFloatPer;
	}
	public void setRatesFloatPer(BigDecimal ratesFloatPer) {
		this.ratesFloatPer = ratesFloatPer;
		if(null != ratesFloatPer){
			this.ratesFloatPerStr = ratesFloatPer.toString();
		}else{
			ratesFloatPer = BigDecimal.ZERO;
			this.ratesFloatPerStr = ratesFloatPer.toString();
		}
	}
	public String getVolumeNum() {
		return volumeNum;
	}
	public void setVolumeNum(String volumeNum) {
		this.volumeNum = volumeNum;
	}
	public String getBankbookNum() {
		return bankbookNum;
	}
	public void setBankbookNum(String bankbookNum) {
		this.bankbookNum = bankbookNum;
	}
	public Integer getInterestCycle() {
		return interestCycle;
	}
	public void setInterestCycle(Integer interestCycle) {
		this.interestCycle = interestCycle;
	}
	public String getInterestCycleStr() {
		return interestCycleStr;
	}
	public void setInterestCycleStr(String interestCycleStr) {
		this.interestCycleStr = interestCycleStr;
	}
	public String getInterestDate() {
		return interestDate;
	}
	public void setInterestDate(String interestDate) {
		this.interestDate = interestDate;
	}
	public Date getRegDate() {
		return regDate;
	}
	public void setRegDate(Date regDate) {
		this.regDate = regDate;
		if (null != regDate) {
			this.regDateStr = DateUtils.formatDate(this.regDate);
		}
	}
	public String getPrivateNo() {
		return privateNo;
	}
	public void setPrivateNo(String privateNo) {
		this.privateNo = privateNo;
	}
	public String getIsOnline() {
		return isOnline;
	}
	public void setIsOnline(String isOnline) {
		this.isOnline = isOnline;
	}
	public Date getInitBalanceDate() {
		return initBalanceDate;
	}
	public void setInitBalanceDate(Date initBalanceDate) {
		this.initBalanceDate = initBalanceDate;
		if (null != initBalanceDate) {
			this.initBalanceDateStr = DateUtils.formatDate(this.initBalanceDate);
		}
	}
	public BigDecimal getInitBalance() {
		return initBalance;
	}
	public void setInitBalance(BigDecimal initBalance) {
		this.initBalance = initBalance;
	}
	public String getRmk() {
		return rmk;
	}
	public void setRmk(String rmk) {
		this.rmk = rmk;
	}
	public String getValidSign() {
		return validSign;
	}
	public void setValidSign(String validSign) {
		this.validSign = validSign;
	}
	public Integer getStatus() {
		return status;
	}
	public void setStatus(Integer status) {
		this.status = status;
	}
	public String getOrgCode() {
		return orgCode;
	}
	public void setOrgCode(String orgCode) {
		this.orgCode = orgCode;
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
	public String getCurId() {
		return curId;
	}
	public void setCurId(String curId) {
		this.curId = curId;
	}
	public String getAppId() {
		return appId;
	}
	public void setAppId(String appId) {
		this.appId = appId;
	}
	public List<BtBankAccVO> getChildList() {
		return childList;
	}
	public void setChildList(List<BtBankAccVO> childList) {
		this.childList = childList;
	}
	public String getNetId() {
		return netId;
	}
	public void setNetId(String netId) {
		this.netId = netId;
	}
	public String getBifName() {
		return bifName;
	}
	public void setBifName(String bifName) {
		this.bifName = bifName;
	}
	public String getPrentAccName() {
		return prentAccName;
	}
	public void setPrentAccName(String prentAccName) {
		this.prentAccName = prentAccName;
	}
	public String getPrentBankAcc() {
		return prentBankAcc;
	}
	public void setPrentBankAcc(String prentBankAcc) {
		this.prentBankAcc = prentBankAcc;
	}
	public String getPrentAccType() {
		return prentAccType;
	}
	public void setPrentAccType(String prentAccType) {
		this.prentAccType = prentAccType;
	}
	public String getPrentAccTypeName() {
		return prentAccTypeName;
	}
	public void setPrentAccTypeName(String prentAccTypeName) {
		this.prentAccTypeName = prentAccTypeName;
	}
	public String getIsOnlineStr() {
		return isOnlineStr;
	}
	public void setIsOnlineStr(String isOnlineStr) {
		this.isOnlineStr = isOnlineStr;
	}
	public String getValidSignStr() {
		return validSignStr;
	}
	public void setValidSignStr(String validSignStr) {
		this.validSignStr = validSignStr;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getRatesFloatName() {
		return ratesFloatName;
	}
	public void setRatesFloatName(String ratesFloatName) {
		this.ratesFloatName = ratesFloatName;
	}
	public String getInitBalanceDateStr() {
		return initBalanceDateStr;
	}
	public void setInitBalanceDateStr(String initBalanceDateStr) {
		this.initBalanceDateStr = initBalanceDateStr;
	}
	public String getRegDateStr() {
		return regDateStr;
	}
	public void setRegDateStr(String regDateStr) {
		this.regDateStr = regDateStr;
	}
	public String getRatesFloatPerStr() {
		return ratesFloatPerStr;
	}
	public void setRatesFloatPerStr(String ratesFloatPerStr) {
		this.ratesFloatPerStr = ratesFloatPerStr;
	}
	public String getIsRelation() {
		return isRelation;
	}
	public void setIsRelation(String isRelation) {
		this.isRelation = isRelation;
	}
	public String getCounterAcc() {
		return counterAcc;
	}
	public void setCounterAcc(String counterAcc) {
		this.counterAcc = counterAcc;
	}
	public String getSuperCurrentAcc() {
		return superCurrentAcc;
	}
	public void setSuperCurrentAcc(String superCurrentAcc) {
		this.superCurrentAcc = superCurrentAcc;
	}
	public BigDecimal getBankAccBalance() {
		return bankAccBalance;
	}
	public void setBankAccBalance(BigDecimal bankAccBalance) {
		this.bankAccBalance = bankAccBalance;
	}
	public String getFlag() {
		return flag;
	}
	public void setFlag(String flag) {
		this.flag = flag;
	}
	public String getSuperCounterAcc() {
		return superCounterAcc;
	}
	public void setSuperCounterAcc(String superCounterAcc) {
		this.superCounterAcc = superCounterAcc;
	}
	public BtBankAccVO getParentBankAcc() {
		return parentBankAcc;
	}
	public void setParentBankAcc(BtBankAccVO parentBankAcc) {
		this.parentBankAcc = parentBankAcc;
	}
	public BigDecimal getFcBalance() {
		return fcBalance;
	}
	public void setFcBalance(BigDecimal fcBalance) {
		this.fcBalance = fcBalance;
	}
	public String getAttachmentId() {
		return attachmentId;
	}
	public void setAttachmentId(String attachmentId) {
		this.attachmentId = attachmentId;
	}
	@Override
	public String getBeanName() {
		return "btBankAccHandleService";
	}
	public String getEntityAccId() {
		return entityAccId;
	}
	public void setEntityAccId(String entityAccId) {
		this.entityAccId = entityAccId;
	}
	public String getIsCapitalPool() {
		return isCapitalPool;
	}
	public void setIsCapitalPool(String isCapitalPool) {
		this.isCapitalPool = isCapitalPool;
	}
	public String getEndDtlDate() {
		return endDtlDate;
	}
	public void setEndDtlDate(String endDtlDate) {
		this.endDtlDate = endDtlDate;
	}
	public String getElectricBill() {
		return electricBill;
	}
	public void setElectricBill(String electricBill) {
		this.electricBill = electricBill;
	}
	public String getElectricBillName() {
		return electricBillName;
	}
	public void setElectricBillName(String electricBillName) {
		this.electricBillName = electricBillName;
	}
	public String getScanFlag() {
		return scanFlag;
	}
	public void setScanFlag(String scanFlag) {
		this.scanFlag = scanFlag;
	}
	public String getScanFlagStr() {
		return scanFlagStr;
	}
	public void setScanFlagStr(String scanFlagStr) {
		this.scanFlagStr = scanFlagStr;
	}
	public String getCirculationMode() {
		return circulationMode;
	}
	public void setCirculationMode(String circulationMode) {
		this.circulationMode = circulationMode;
	}
	public String getCirculationModeName() {
		return circulationModeName;
	}
	public void setCirculationModeName(String circulationModeName) {
		this.circulationModeName = circulationModeName;
	}
	public Date getEndDate() {
		return endDate;
	}
	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}
	public String getIsCapitalPoolStr() {
		return isCapitalPoolStr;
	}
	public void setIsCapitalPoolStr(String isCapitalPoolStr) {
		this.isCapitalPoolStr = isCapitalPoolStr;
	}
	public String getPoolSign() {
		return poolSign;
	}
	public void setPoolSign(String poolSign) {
		this.poolSign = poolSign;
	}
	public String getPoolId() {
		return poolId;
	}
	public void setPoolId(String poolId) {
		this.poolId = poolId;
	}
	public Date getCheckExpireDate() { return checkExpireDate; }
	public void setCheckExpireDate(Date checkExpireDate) {
		this.checkExpireDate = checkExpireDate;
		if (null != checkExpireDate) {
			this.checkExpireDateStr = DateUtils.formatDate(this.checkExpireDate);
		}
	}
	public String getForeignDes() {
		return foreignDes;
	}
	public void setForeignDes(String foreignDes) {
		this.foreignDes = foreignDes;
	}
	public BigDecimal getBalance() {
		return balance;
	}
	public void setBalance(BigDecimal balance) {
		this.balance = balance;
	}
	public String getIsDomesticBank() {
		return isDomesticBank;
	}
	public void setIsDomesticBank(String isDomesticBank) {
		this.isDomesticBank = isDomesticBank;
	}
	public String getIsDomesticBankName() {
		return isDomesticBankName;
	}
	public void setIsDomesticBankName(String isDomesticBankName) {
		this.isDomesticBankName = isDomesticBankName;
	}
	public String getCurEnglishCode() {
		return curEnglishCode;
	}
	public void setCurEnglishCode(String curEnglishCode) {
		this.curEnglishCode = curEnglishCode;
	}
	public String getCheckExpireDateStr() {
		return checkExpireDateStr;
	}
	public void setCheckExpireDateStr(String checkExpireDateStr) {
		this.checkExpireDateStr = checkExpireDateStr;
	}
	public String getSealPerson() {
		return sealPerson;
	}
	public void setSealPerson(String sealPerson) {
		this.sealPerson = sealPerson;
	}
	public String getSealPhone() {
		return sealPhone;
	}
	public void setSealPhone(String sealPhone) {
		this.sealPhone = sealPhone;
	}
	public String getSealEmail() {
		return sealEmail;
	}
	public void setSealEmail(String sealEmail) {
		this.sealEmail = sealEmail;
	}
	public String getMgtDepartment() {
		return mgtDepartment;
	}
	public void setMgtDepartment(String mgtDepartment) {
		this.mgtDepartment = mgtDepartment;
	}
	public String getSealDirectory() {
		return sealDirectory;
	}
	public void setSealDirectory(String sealDirectory) {
		this.sealDirectory = sealDirectory;
	}
	public String[] getSealDirectoryList() {
		return sealDirectoryList;
	}
	public void setSealDirectoryList(String[] sealDirectoryList) {
		this.sealDirectoryList = sealDirectoryList;
	}
	public String getuKeyFunctionOther() {
		return uKeyFunctionOther;
	}
	public void setuKeyFunctionOther(String uKeyFunctionOther) {
		this.uKeyFunctionOther = uKeyFunctionOther;
	}
	@Override
	public String getBusinessKey() {
		return getId();
	}
	public String getStatusStr() {
		return statusStr;
	}
	public void setStatusStr(String statusStr) {
		this.statusStr = statusStr;
	}
	public Date getCloseDate() {
		return closeDate;
	}
	public void setCloseDate(Date closeDate) {
		this.closeDate = closeDate;
	}
	public String getIsOnlineHandle() {
		return isOnlineHandle;
	}
	public void setIsOnlineHandle(String isOnlineHandle) {
		this.isOnlineHandle = isOnlineHandle;
	}
	public List<BtBankAccVirtualVO> getBankAccVirtual() {
		return bankAccVirtual;
	}
	public void setBankAccVirtual(List<BtBankAccVirtualVO> bankAccVirtual) {
		this.bankAccVirtual = bankAccVirtual;
	}
	public List<BtBankAccCurVO> getCurList() {
		return curList;
	}
	public void setCurList(List<BtBankAccCurVO> curList) {
		this.curList = curList;
	}
	public String getIsOffshoreAccount() {
		return isOffshoreAccount;
	}
	public void setIsOffshoreAccount(String isOffshoreAccount) {
		this.isOffshoreAccount = isOffshoreAccount;
	}

	public String getBindingBankAcc() {
		return bindingBankAcc;
	}

	public void setBindingBankAcc(String bindingBankAcc) {
		this.bindingBankAcc = bindingBankAcc;
	}

	public String getBindingBankAccStr() {
		return bindingBankAccStr;
	}

	public void setBindingBankAccStr(String bindingBankAccStr) {
		this.bindingBankAccStr = bindingBankAccStr;
	}

	public String getIsCheck() {
		return isCheck;
	}

	public void setIsCheck(String isCheck) {
		this.isCheck = isCheck;
	}

	public String getIsCheckStr() {
		return isCheckStr;
	}

	public void setIsCheckStr(String isCheckStr) {
		this.isCheckStr = isCheckStr;
	}

	public String getBindingBankAccCorpName() {
		return bindingBankAccCorpName;
	}

	public void setBindingBankAccCorpName(String bindingBankAccCorpName) {
		this.bindingBankAccCorpName = bindingBankAccCorpName;
	}

	public String getBindingBankAccAccName() {
		return bindingBankAccAccName;
	}

	public void setBindingBankAccAccName(String bindingBankAccAccName) {
		this.bindingBankAccAccName = bindingBankAccAccName;
	}

	public String getBindingBankAccBankName() {
		return bindingBankAccBankName;
	}

	public void setBindingBankAccBankName(String bindingBankAccBankName) {
		this.bindingBankAccBankName = bindingBankAccBankName;
	}

	public String getShareholding() {
		return shareholding;
	}

	public void setShareholding(String shareholding) {
		this.shareholding = shareholding;
	}

	public String getIsDemand() {
		return isDemand;
	}

	public void setIsDemand(String isDemand) {
		this.isDemand = isDemand;
	}

	public String getBindingBankTypeName() {
		return bindingBankTypeName;
	}

	public void setBindingBankTypeName(String bindingBankTypeName) {
		this.bindingBankTypeName = bindingBankTypeName;
	}

	public String getSign() {
		return sign;
	}

	public void setSign(String sign) {
		this.sign = sign;
	}

	public String getAccUseName() {
		return accUseName;
	}

	public void setAccUseName(String accUseName) {
		this.accUseName = accUseName;
	}

	public String getBindingByName() {
		return bindingByName;
	}

	public void setBindingByName(String bindingByName) {
		this.bindingByName = bindingByName;
	}

	public String getBindingByMobile() {
		return bindingByMobile;
	}

	public void setBindingByMobile(String bindingByMobile) {
		this.bindingByMobile = bindingByMobile;
	}

	public String getLoginPassword() {
		return loginPassword;
	}

	public void setLoginPassword(String loginPassword) {
		this.loginPassword = loginPassword;
	}

	public String getPayPassword() {
		return payPassword;
	}

	public void setPayPassword(String payPassword) {
		this.payPassword = payPassword;
	}

	public String getBindingNatureId() {
		return bindingNatureId;
	}

	public void setBindingNatureId(String bindingNatureId) {
		this.bindingNatureId = bindingNatureId;
	}

	public String getBindingNatureName() {
		return bindingNatureName;
	}

	public void setBindingNatureName(String bindingNatureName) {
		this.bindingNatureName = bindingNatureName;
	}

	public String getBindingAccGroupCode() {
		return bindingAccGroupCode;
	}

	public void setBindingAccGroupCode(String bindingAccGroupCode) {
		this.bindingAccGroupCode = bindingAccGroupCode;
	}

	public String getCorpParentIds() {
		return corpParentIds;
	}

	public void setCorpParentIds(String corpParentIds) {
		this.corpParentIds = corpParentIds;
	}

	public String getSysProvinceId() {
		return sysProvinceId;
	}

	public void setSysProvinceId(String sysProvinceId) {
		this.sysProvinceId = sysProvinceId;
	}

	public String getSysProvinceName() {
		return sysProvinceName;
	}

	public void setSysProvinceName(String sysProvinceName) {
		this.sysProvinceName = sysProvinceName;
	}

	public String getIsClose() {
		return isClose;
	}

	public void setIsClose(String isClose) {
		this.isClose = isClose;
	}
}
