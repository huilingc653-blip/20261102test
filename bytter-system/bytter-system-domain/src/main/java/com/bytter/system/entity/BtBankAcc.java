package com.bytter.system.entity;
import com.bytter.core.base.entity.IdEntity;
import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
/**
 * @author onlineGenerator
 * @version 1.0
 * @Title: Entity
 * @Description: BT_BANK_ACC
 * @date 2017-06-02 17:50:16
 */
@Entity
@Table(name = "BT_BANK_ACC", schema = "")
public class BtBankAcc extends IdEntity implements java.io.Serializable {
	/**
	 *
	 */
	private static final long serialVersionUID = 1291859506997225441L;
	/**
	 * 银行账号
	 */
	private java.lang.String bankAcc;
	/**
	 * 账户名称
	 */
	private java.lang.String accName;
	/**
	 * 父账号标志 1为父账号，0为子账号
	 */
	private java.lang.String parentSign = "0";
	/**
	 * 单位ID
	 */
	private java.lang.String corpId;
	/**
	 * 账户类别
	 */
	private java.lang.String accType;
	/**
	 * 账户属性
	 */
	private java.lang.String attributeId;
	/**
	 * 账户性质
	 */
	private java.lang.String natureId;
	/**
	 * 银行类别
	 */
	private java.lang.String bankTypeId;
	/**
	 * 开户行行号
	 */
	private java.lang.String bankCode;
	/**
	 * 开户行名称
	 */
	private java.lang.String bankName;
	/**
	 * 所在省
	 */
	private java.lang.String prov;
	/**
	 * 所在市
	 */
	private java.lang.String city;
	/**
	 * 接口代码
	 */
	private java.lang.String bifCode;
	/**
	 * 电票专户
	 */
	private java.lang.String electricBill;
	/**
	 * 定期期限
	 */
	private java.lang.String regulars;
	/**
	 * 定期到期时间
	 */
	private java.util.Date regularDate;
	/**
	 * 通知类型
	 */
	private java.lang.String noticeType;
	/**
	 * 协定金额
	 */
	private java.math.BigDecimal agrMoney;
	/**
	 * 协定利率
	 */
	private java.math.BigDecimal agrInterest;
	/**
	 * 协定合同
	 */
	private java.lang.String agrContract;
	/**
	 * 协定开始时间
	 */
	private java.util.Date agrBeginDate;
	/**
	 * 协定结束时间
	 */
	private java.util.Date agrEndDate;
	/**
	 * 利率
	 */
	private java.math.BigDecimal rates;
	/**
	 * 利率是否浮动
	 */
	private java.lang.String ratesFloat;
	/**
	 * 浮动百分比
	 */
	private java.math.BigDecimal ratesFloatPer;
	/**
	 * 本册笔号
	 */
	private java.lang.String volumeNum;
	/**
	 * 存则册号
	 */
	private java.lang.String bankbookNum;
	/**
	 * 计息周期
	 */
	private java.lang.Integer interestCycle;
	/**
	 * 计息日
	 */
	private java.lang.String interestDate;
	/**
	 * 开户日期
	 */
	private java.util.Date regDate;
	/**
	 * 对私编号
	 */
	private java.lang.String privateNo;
	/**
	 * 是否上线
	 */
	private java.lang.String isOnline;
	/**
	 * 是否查询余额或明细
	 */
	private java.lang.String scanFlag;
	/**
	 * 余额初始日期
	 */
	private java.util.Date initBalanceDate;
	/**
	 * 初始余额
	 */
	private java.math.BigDecimal initBalance;
	/**
	 * 备注
	 */
	private java.lang.String rmk;
	/**
	 * 有效标志
	 */
	private java.lang.String validSign;
	/**
	 * 状态
	 */
	private java.lang.Integer status;
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
	 * 币别
	 */
	private String curId;
	/**
	 * 提交标志0为暂存1为提交
	 */
	private String flag;
	/**
	 * 建行机构号
	 */
	private String orgCode;
	/**
	 * 是否核查
	 */
	private String isCheck;
	private List<BtBankAccCur> curList;
	/**
	 * 附件ID
	 */
	private java.lang.String attachmentId;
	/**
	 * 实体账号
	 **/
	private java.lang.String entityAccId;
	/**
	 * 是否资金池账号
	 **/
	private java.lang.String isCapitalPool;
	/**
	 * 流通方式 0-线下 1-线上
	 **/
	private java.lang.String circulationMode;
	/**
	 * 到期日期
	 */
	private java.util.Date endDate;
	/**
	 * 父账号
	 */
	private String prentAccId;
	/**
	 * 账户年检到期日
	 */
	private Date checkExpireDate;
	/**
	 * 银行组织机构代码
	 */
	private String bankOrgCode;
	/**
	 * 是否境内银行 1-境内，0-境外
	 */
	private String isDomesticBank;
	/**
	 * 预留印鉴图片地址
	 */
	private String sealDirectory;
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
	 * 账户注销日期
	 */
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
	 * 单据类型
	 */
	private String receiptCode;
	/**
	 * 账户组
	 */
	private String fcAccGroupCode;
	/**
	 * 绑定账号
	 **/
	private String bindingBankAcc;
	/**
	 * 是否参与资金需求计算
	 **/
	private String isDemand;
	/**
	 * 账号用途
	 **/
	private String accUseCode;
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
	 * 是否注销0否，1是
	 **/
	private String isClose;

	private String tenantId;

	/**
	 * RPA功能
	 */
	private String rpaFunction;

	/**
	 * 是否代管账户
	 */
	private String isRpaEscrow;

	@Column(name = "rpa_function")
	public String getRpaFunction() {
		return rpaFunction;
	}

	public void setRpaFunction(String rpaFunction) {
		this.rpaFunction = rpaFunction;
	}

	@Column(name = "is_rpa_escrow")
	public String getIsRpaEscrow() {
		return isRpaEscrow;
	}

	public void setIsRpaEscrow(String isRpaEscrow) {
		this.isRpaEscrow = isRpaEscrow;
	}

	@Column(name = "TENANT_ID")
	public String getTenantId() {
		return tenantId;
	}

	public void setTenantId(String tenantId) {
		this.tenantId = tenantId;
	}

	@Column(name = "RECEIPT_CODE")
	public String getReceiptCode() {
		return receiptCode;
	}

	public void setReceiptCode(String receiptCode) {
		this.receiptCode = receiptCode;
	}

	@Column(name = "FC_ACC_GROUP_CODE")
	public String getFcAccGroupCode() {
		return fcAccGroupCode;
	}

	public void setFcAccGroupCode(String fcAccGroupCode) {
		this.fcAccGroupCode = fcAccGroupCode;
	}

	@Column(name = "PAY_LIMIT_QUOTA")
	public BigDecimal getPayLimitQuota() {
		return payLimitQuota;
	}
	public void setPayLimitQuota(BigDecimal payLimitQuota) {
		this.payLimitQuota = payLimitQuota;
	}
	@Column(name = "OVER_LIMIT_QUOTA")
	public BigDecimal getOverLimitQuota() {
		return overLimitQuota;
	}
	public void setOverLimitQuota(BigDecimal overLimitQuota) {
		this.overLimitQuota = overLimitQuota;
	}
	@Column(name = "IS_LIMIT_QUOTA")
	public String getIsLimitQuota() {
		return isLimitQuota;
	}
	public void setIsLimitQuota(String isLimitQuota) {
		this.isLimitQuota = isLimitQuota;
	}
	@Column(name = "DAY_LIMIT_QUOTA")
	public BigDecimal getDayLimitQuota() {
		return dayLimitQuota;
	}
	public void setDayLimitQuota(BigDecimal dayLimitQuota) {
		this.dayLimitQuota = dayLimitQuota;
	}
	@Column(name = "MONTH_LIMIT_QUOTA")
	public BigDecimal getMonthLimitQuota() {
		return monthLimitQuota;
	}
	public void setMonthLimitQuota(BigDecimal monthLimitQuota) {
		this.monthLimitQuota = monthLimitQuota;
	}
	@Column(name = "U_KEY_ADMIN")
	public String getuKeyAdmin() {
		return uKeyAdmin;
	}
	public void setuKeyAdmin(String uKeyAdmin) {
		this.uKeyAdmin = uKeyAdmin;
	}
	@Column(name = "U_KEY_OPERATOR")
	public String getuKeyOperator() {
		return uKeyOperator;
	}
	public void setuKeyOperator(String uKeyOperator) {
		this.uKeyOperator = uKeyOperator;
	}
	@Column(name = "U_KEY_REVIEWERS1")
	public String getuKeyReviewers1() {
		return uKeyReviewers1;
	}
	public void setuKeyReviewers1(String uKeyReviewers1) {
		this.uKeyReviewers1 = uKeyReviewers1;
	}
	@Column(name = "U_KEY_REVIEWERS2")
	public String getuKeyReviewers2() {
		return uKeyReviewers2;
	}
	public void setuKeyReviewers2(String uKeyReviewers2) {
		this.uKeyReviewers2 = uKeyReviewers2;
	}
	@Column(name = "U_KEY_REVIEWERS3")
	public String getuKeyReviewers3() {
		return uKeyReviewers3;
	}
	public void setuKeyReviewers3(String uKeyReviewers3) {
		this.uKeyReviewers3 = uKeyReviewers3;
	}
	@Column(name = "U_KEY_FUNCTION")
	public String getUKeyFunction() {
		return uKeyFunction;
	}
	public void setUKeyFunction(String uKeyFunction) {
		this.uKeyFunction = uKeyFunction;
	}
	@Column(name = "BANK_ORG_CODE", nullable = true, length = 20)
	public String getBankOrgCode() {
		return bankOrgCode;
	}
	public void setBankOrgCode(String bankOrgCode) {
		this.bankOrgCode = bankOrgCode;
	}
	/**
	 * 方法: 取得java.lang.String
	 *
	 * @return: java.lang.String  银行账号
	 */
	@Column(name = "BANK_ACC", nullable = true, length = 30)
	public java.lang.String getBankAcc() {
		return this.bankAcc;
	}
	/**
	 * 方法: 设置java.lang.String
	 *
	 * @param: java.lang.String  银行账号
	 */
	public void setBankAcc(java.lang.String bankAcc) {
		this.bankAcc = bankAcc;
	}
	/**
	 * 方法: 取得java.lang.String
	 *
	 * @return: java.lang.String  账户名称
	 */
	@Column(name = "ACC_NAME", nullable = true, length = 140)
	public java.lang.String getAccName() {
		return this.accName;
	}
	/**
	 * 方法: 设置java.lang.String
	 *
	 * @param: java.lang.String  账户名称
	 */
	public void setAccName(java.lang.String accName) {
		this.accName = accName;
	}
	/**
	 * 方法: 取得java.lang.String
	 *
	 * @return: java.lang.String  父账号标志
	 */
	@Column(name = "PARENT_SIGN", nullable = true, length = 1)
	public java.lang.String getParentSign() {
		return this.parentSign;
	}
	/**
	 * 方法: 设置java.lang.String
	 *
	 * @param: java.lang.String  父账号标志
	 */
	public void setParentSign(java.lang.String parentSign) {
		this.parentSign = parentSign;
	}
	/**
	 * 方法: 取得java.lang.String
	 *
	 * @return: java.lang.String  单位ID
	 */
	@Column(name = "CORP_ID", nullable = true, length = 32)
	public java.lang.String getCorpId() {
		return this.corpId;
	}
	/**
	 * 方法: 设置java.lang.String
	 *
	 * @param: java.lang.String  单位ID
	 */
	public void setCorpId(java.lang.String corpId) {
		this.corpId = corpId;
	}
	/**
	 * 方法: 取得java.lang.String
	 *
	 * @return: java.lang.String  账户类别
	 */
	@Column(name = "ACC_TYPE", nullable = true, length = 2)
	public java.lang.String getAccType() {
		return this.accType;
	}
	/**
	 * 方法: 设置java.lang.String
	 *
	 * @param: java.lang.String  账户类别
	 */
	public void setAccType(java.lang.String accType) {
		this.accType = accType;
	}
	/**
	 * 方法: 取得java.lang.String
	 *
	 * @return: java.lang.String  账户属性
	 */
	@Column(name = "ATTRIBUTE_ID", nullable = true, length = 32)
	public java.lang.String getAttributeId() {
		return this.attributeId;
	}
	/**
	 * 方法: 设置java.lang.String
	 *
	 * @param: java.lang.String  账户属性
	 */
	public void setAttributeId(java.lang.String attributeId) {
		this.attributeId = attributeId;
	}
	/**
	 * 方法: 取得java.lang.String
	 *
	 * @return: java.lang.String  账户性质
	 */
	@Column(name = "NATURE_ID", nullable = true, length = 32)
	public java.lang.String getNatureId() {
		return this.natureId;
	}
	/**
	 * 方法: 设置java.lang.String
	 *
	 * @param: java.lang.String  账户性质
	 */
	public void setNatureId(java.lang.String natureId) {
		this.natureId = natureId;
	}
	/**
	 * 方法: 取得java.lang.String
	 *
	 * @return: java.lang.String  银行类别
	 */
	@Column(name = "BANK_TYPE_ID", nullable = true, length = 32)
	public java.lang.String getBankTypeId() {
		return this.bankTypeId;
	}
	/**
	 * 方法: 设置java.lang.String
	 *
	 * @param: java.lang.String  银行类别
	 */
	public void setBankTypeId(java.lang.String bankTypeId) {
		this.bankTypeId = bankTypeId;
	}
	/**
	 * 方法: 取得java.lang.String
	 *
	 * @return: java.lang.String  开户行行号
	 */
	@Column(name = "BANK_CODE", nullable = true, length = 20)
	public java.lang.String getBankCode() {
		return this.bankCode;
	}
	/**
	 * 方法: 设置java.lang.String
	 *
	 * @param: java.lang.String  开户行行号
	 */
	public void setBankCode(java.lang.String bankCode) {
		this.bankCode = bankCode;
	}
	/**
	 * 方法: 取得java.lang.String
	 *
	 * @return: java.lang.String  开户行名称
	 */
	@Column(name = "BANK_NAME", nullable = true, length = 150)
	public java.lang.String getBankName() {
		return this.bankName;
	}
	/**
	 * 方法: 设置java.lang.String
	 *
	 * @param: java.lang.String  开户行名称
	 */
	public void setBankName(java.lang.String bankName) {
		this.bankName = bankName;
	}
	/**
	 * 方法: 取得java.lang.String
	 *
	 * @return: java.lang.String  所在省
	 */
	@Column(name = "PROV", nullable = true, length = 32)
	public java.lang.String getProv() {
		return this.prov;
	}
	/**
	 * 方法: 设置java.lang.String
	 *
	 * @param: java.lang.String  所在省
	 */
	public void setProv(java.lang.String prov) {
		this.prov = prov;
	}
	/**
	 * 方法: 取得java.lang.String
	 *
	 * @return: java.lang.String  所在市
	 */
	@Column(name = "CITY", nullable = true, length = 32)
	public java.lang.String getCity() {
		return this.city;
	}
	/**
	 * 方法: 设置java.lang.String
	 *
	 * @param: java.lang.String  所在市
	 */
	public void setCity(java.lang.String city) {
		this.city = city;
	}
	/**
	 * 方法: 取得java.lang.String
	 *
	 * @return: java.lang.String  接口代码
	 */
	@Column(name = "BIF_CODE", nullable = true, length = 4)
	public java.lang.String getBifCode() {
		return this.bifCode;
	}
	/**
	 * 方法: 设置java.lang.String
	 *
	 * @param: java.lang.String  接口代码
	 */
	public void setBifCode(java.lang.String bifCode) {
		this.bifCode = bifCode;
	}
	/**
	 * 方法: 取得java.lang.String
	 *
	 * @return: java.lang.String  电票专户
	 */
	@Column(name = "ELECTRIC_BILL", nullable = true, length = 1)
	public java.lang.String getElectricBill() {
		return this.electricBill;
	}
	/**
	 * 方法: 设置java.lang.String
	 *
	 * @param: java.lang.String  电票专户
	 */
	public void setElectricBill(java.lang.String electricBill) {
		this.electricBill = electricBill;
	}
	/**
	 * 方法: 取得java.lang.String
	 *
	 * @return: java.lang.String  定期期限
	 */
	@Column(name = "REGULARS", nullable = true, length = 10)
	public java.lang.String getRegulars() {
		return this.regulars;
	}
	/**
	 * 方法: 设置java.lang.String
	 *
	 * @param: java.lang.String  定期期限
	 */
	public void setRegulars(java.lang.String regulars) {
		this.regulars = regulars;
	}
	/**
	 * 方法: 取得java.util.Date
	 *
	 * @return: java.util.Date  定期到期时间
	 */
	@Column(name = "REGULAR_DATE", nullable = true)
	public java.util.Date getRegularDate() {
		return this.regularDate;
	}
	/**
	 * 方法: 设置java.util.Date
	 *
	 * @param: java.util.Date  定期到期时间
	 */
	public void setRegularDate(java.util.Date regularDate) {
		this.regularDate = regularDate;
	}
	/**
	 * 方法: 取得java.lang.String
	 *
	 * @return: java.lang.String  通知类型
	 */
	@Column(name = "NOTICE_TYPE", nullable = true, length = 10)
	public java.lang.String getNoticeType() {
		return this.noticeType;
	}
	/**
	 * 方法: 设置java.lang.String
	 *
	 * @param: java.lang.String  通知类型
	 */
	public void setNoticeType(java.lang.String noticeType) {
		this.noticeType = noticeType;
	}
	/**
	 * 方法: 取得java.math.BigDecimal
	 *
	 * @return: java.math.BigDecimal  协定金额
	 */
	@Column(name = "AGR_MONEY", nullable = true, scale = 4, length = 20)
	public java.math.BigDecimal getAgrMoney() {
		return this.agrMoney;
	}
	/**
	 * 方法: 设置java.math.BigDecimal
	 *
	 * @param: java.math.BigDecimal  协定金额
	 */
	public void setAgrMoney(java.math.BigDecimal agrMoney) {
		this.agrMoney = agrMoney;
	}
	/**
	 * 方法: 取得java.math.BigDecimal
	 *
	 * @return: java.math.BigDecimal  协定利率
	 */
	@Column(name = "AGR_INTEREST", nullable = true, scale = 4, length = 20)
	public java.math.BigDecimal getAgrInterest() {
		return this.agrInterest;
	}
	/**
	 * 方法: 设置java.math.BigDecimal
	 *
	 * @param: java.math.BigDecimal  协定利率
	 */
	public void setAgrInterest(java.math.BigDecimal agrInterest) {
		this.agrInterest = agrInterest;
	}
	/**
	 * 方法: 取得java.lang.String
	 *
	 * @return: java.lang.String  协定合同
	 */
	@Column(name = "AGR_CONTRACT", nullable = true, length = 40)
	public java.lang.String getAgrContract() {
		return this.agrContract;
	}
	/**
	 * 方法: 设置java.lang.String
	 *
	 * @param: java.lang.String  协定合同
	 */
	public void setAgrContract(java.lang.String agrContract) {
		this.agrContract = agrContract;
	}
	/**
	 * 方法: 取得java.util.Date
	 *
	 * @return: java.util.Date  协定开始时间
	 */
	@Column(name = "AGR_BEGIN_DATE", nullable = true)
	public java.util.Date getAgrBeginDate() {
		return this.agrBeginDate;
	}
	/**
	 * 方法: 设置java.util.Date
	 *
	 * @param: java.util.Date  协定开始时间
	 */
	public void setAgrBeginDate(java.util.Date agrBeginDate) {
		this.agrBeginDate = agrBeginDate;
	}
	/**
	 * 方法: 取得java.util.Date
	 *
	 * @return: java.util.Date  协定结束时间
	 */
	@Column(name = "AGR_END_DATE", nullable = true)
	public java.util.Date getAgrEndDate() {
		return this.agrEndDate;
	}
	/**
	 * 方法: 设置java.util.Date
	 *
	 * @param: java.util.Date  协定结束时间
	 */
	public void setAgrEndDate(java.util.Date agrEndDate) {
		this.agrEndDate = agrEndDate;
	}
	/**
	 * 方法: 取得java.math.BigDecimal
	 *
	 * @return: java.math.BigDecimal  利率
	 */
	@Column(name = "RATES", nullable = true, scale = 4, length = 6)
	public java.math.BigDecimal getRates() {
		return this.rates;
	}
	/**
	 * 方法: 设置java.math.BigDecimal
	 *
	 * @param: java.math.BigDecimal  利率
	 */
	public void setRates(java.math.BigDecimal rates) {
		this.rates = rates;
	}
	/**
	 * 方法: 取得java.lang.String
	 *
	 * @return: java.lang.String  利率是否浮动
	 */
	@Column(name = "RATES_FLOAT", nullable = true, length = 1)
	public java.lang.String getRatesFloat() {
		return this.ratesFloat;
	}
	/**
	 * 方法: 设置java.lang.String
	 *
	 * @param: java.lang.String  利率是否浮动
	 */
	public void setRatesFloat(java.lang.String ratesFloat) {
		this.ratesFloat = ratesFloat;
	}
	/**
	 * 方法: 取得java.math.BigDecimal
	 *
	 * @return: java.math.BigDecimal  浮动百分比
	 */
	@Column(name = "RATES_FLOAT_PER", nullable = true, scale = 4, length = 20)
	public java.math.BigDecimal getRatesFloatPer() {
		return this.ratesFloatPer;
	}
	/**
	 * 方法: 设置java.math.BigDecimal
	 *
	 * @param: java.math.BigDecimal  浮动百分比
	 */
	public void setRatesFloatPer(java.math.BigDecimal ratesFloatPer) {
		this.ratesFloatPer = ratesFloatPer;
	}
	/**
	 * 方法: 取得java.lang.String
	 *
	 * @return: java.lang.String  本册笔号
	 */
	@Column(name = "VOLUME_NUM", nullable = true, length = 40)
	public java.lang.String getVolumeNum() {
		return this.volumeNum;
	}
	/**
	 * 方法: 设置java.lang.String
	 *
	 * @param: java.lang.String  本册笔号
	 */
	public void setVolumeNum(java.lang.String volumeNum) {
		this.volumeNum = volumeNum;
	}
	/**
	 * 方法: 取得java.lang.String
	 *
	 * @return: java.lang.String  存则册号
	 */
	@Column(name = "BANKBOOK_NUM", nullable = true, length = 40)
	public java.lang.String getBankbookNum() {
		return this.bankbookNum;
	}
	/**
	 * 方法: 设置java.lang.String
	 *
	 * @param: java.lang.String  存则册号
	 */
	public void setBankbookNum(java.lang.String bankbookNum) {
		this.bankbookNum = bankbookNum;
	}
	/**
	 * 方法: 取得java.lang.Integer
	 *
	 * @return: java.lang.Integer  计息周期
	 */
	@Column(name = "INTEREST_CYCLE", nullable = true, length = 10)
	public java.lang.Integer getInterestCycle() {
		return this.interestCycle;
	}
	/**
	 * 方法: 设置java.lang.Integer
	 *
	 * @param: java.lang.Integer  计息周期
	 */
	public void setInterestCycle(java.lang.Integer interestCycle) {
		this.interestCycle = interestCycle;
	}
	/**
	 * 方法: 取得java.lang.String
	 *
	 * @return: java.lang.String  计息日
	 */
	@Column(name = "INTEREST_DATE", nullable = true, length = 10)
	public java.lang.String getInterestDate() {
		return this.interestDate;
	}
	/**
	 * 方法: 设置java.lang.String
	 *
	 * @param: java.lang.String  计息日
	 */
	public void setInterestDate(java.lang.String interestDate) {
		this.interestDate = interestDate;
	}
	/**
	 * 方法: 取得java.util.Date
	 *
	 * @return: java.util.Date  开户日期
	 */
	@Column(name = "REG_DATE", nullable = true)
	public java.util.Date getRegDate() {
		return this.regDate;
	}
	/**
	 * 方法: 设置java.util.Date
	 *
	 * @param: java.util.Date  开户日期
	 */
	public void setRegDate(java.util.Date regDate) {
		this.regDate = regDate;
	}
	/**
	 * 方法: 取得java.lang.String
	 *
	 * @return: java.lang.String  对私编号
	 */
	@Column(name = "PRIVATE_NO", nullable = true, length = 30)
	public java.lang.String getPrivateNo() {
		return this.privateNo;
	}
	/**
	 * 方法: 设置java.lang.String
	 *
	 * @param: java.lang.String  对私编号
	 */
	public void setPrivateNo(java.lang.String privateNo) {
		this.privateNo = privateNo;
	}
	/**
	 * 方法: 取得java.lang.String
	 *
	 * @return: java.lang.String  是否上线
	 */
	@Column(name = "IS_ONLINE", nullable = true, length = 1)
	public java.lang.String getIsOnline() {
		return this.isOnline;
	}
	/**
	 * 方法: 设置java.lang.String
	 *
	 * @param: java.lang.String  是否上线
	 */
	public void setIsOnline(java.lang.String isOnline) {
		this.isOnline = isOnline;
	}
	/**
	 * 方法: 取得java.lang.String
	 *
	 * @return: java.lang.String  是否查询余额或明细
	 */
	@Column(name = "SCAN_FLAG", nullable = true, length = 1)
	public java.lang.String getScanFlag() {
		return this.scanFlag;
	}
	/**
	 * 方法: 设置java.lang.String
	 *
	 * @param: java.lang.String  是否查询余额或明细
	 */
	public void setScanFlag(java.lang.String scanFlag) {
		this.scanFlag = scanFlag;
	}
	/**
	 * 方法: 取得java.util.Date
	 *
	 * @return: java.util.Date  余额初始日期
	 */
	@Column(name = "INIT_BALANCE_DATE", nullable = true)
	public java.util.Date getInitBalanceDate() {
		return this.initBalanceDate;
	}
	/**
	 * 方法: 设置java.util.Date
	 *
	 * @param: java.util.Date  余额初始日期
	 */
	public void setInitBalanceDate(java.util.Date initBalanceDate) {
		this.initBalanceDate = initBalanceDate;
	}
	/**
	 * 方法: 取得java.math.BigDecimal
	 *
	 * @return: java.math.BigDecimal  初始余额
	 */
	@Column(name = "INIT_BALANCE", nullable = true, scale = 2, length = 15)
	public java.math.BigDecimal getInitBalance() {
		return this.initBalance;
	}
	/**
	 * 方法: 设置java.math.BigDecimal
	 *
	 * @param: java.math.BigDecimal  初始余额
	 */
	public void setInitBalance(java.math.BigDecimal initBalance) {
		this.initBalance = initBalance;
	}
	/**
	 * 方法: 取得java.lang.String
	 *
	 * @return: java.lang.String  备注
	 */
	@Column(name = "RMK", nullable = true, length = 100)
	public java.lang.String getRmk() {
		return this.rmk;
	}
	/**
	 * 方法: 设置java.lang.String
	 *
	 * @param: java.lang.String  备注
	 */
	public void setRmk(java.lang.String rmk) {
		this.rmk = rmk;
	}
	/**
	 * 方法: 取得java.lang.String
	 *
	 * @return: java.lang.String  有效标志
	 */
	@Column(name = "VALID_SIGN", nullable = true, length = 1)
	public java.lang.String getValidSign() {
		return this.validSign;
	}
	/**
	 * 方法: 设置java.lang.String
	 *
	 * @param: java.lang.String  有效标志
	 */
	public void setValidSign(java.lang.String validSign) {
		this.validSign = validSign;
	}
	/**
	 * 方法: 取得java.lang.Integer
	 *
	 * @return: java.lang.Integer  状态
	 */
	@Column(name = "STATUS", nullable = true, length = 10)
	public java.lang.Integer getStatus() {
		return this.status;
	}
	/**
	 * 方法: 设置java.lang.Integer
	 *
	 * @param: java.lang.Integer  状态
	 */
	public void setStatus(java.lang.Integer status) {
		this.status = status;
	}
	/**
	 * 方法: 取得java.lang.String
	 *
	 * @return: java.lang.String  创建人登录名称
	 */
	@Column(name = "CREATE_BY", nullable = true, length = 50)
	public java.lang.String getCreateBy() {
		return this.createBy;
	}
	/**
	 * 方法: 设置java.lang.String
	 *
	 * @param: java.lang.String  创建人登录名称
	 */
	public void setCreateBy(java.lang.String createBy) {
		this.createBy = createBy;
	}
	/**
	 * 方法: 取得java.util.Date
	 *
	 * @return: java.util.Date  创建日期
	 */
	@Column(name = "CREATE_DATE", nullable = true)
	public java.util.Date getCreateDate() {
		return this.createDate;
	}
	/**
	 * 方法: 设置java.util.Date
	 *
	 * @param: java.util.Date  创建日期
	 */
	public void setCreateDate(java.util.Date createDate) {
		this.createDate = createDate;
	}
	/**
	 * 方法: 取得java.lang.String
	 *
	 * @return: java.lang.String  更新人登录名称
	 */
	@Column(name = "UPDATE_BY", nullable = true, length = 50)
	public java.lang.String getUpdateBy() {
		return this.updateBy;
	}
	/**
	 * 方法: 设置java.lang.String
	 *
	 * @param: java.lang.String  更新人登录名称
	 */
	public void setUpdateBy(java.lang.String updateBy) {
		this.updateBy = updateBy;
	}
	/**
	 * 方法: 取得java.util.Date
	 *
	 * @return: java.util.Date  更新日期
	 */
	@Column(name = "UPDATE_DATE", nullable = true)
	public java.util.Date getUpdateDate() {
		return this.updateDate;
	}
	/**
	 * 方法: 设置java.util.Date
	 *
	 * @param: java.util.Date  更新日期
	 */
	public void setUpdateDate(java.util.Date updateDate) {
		this.updateDate = updateDate;
	}
	@Transient
	public String getCurId() {
		return curId;
	}
	public void setCurId(String curId) {
		this.curId = curId;
	}
	@Transient
	public String getFlag() {
		return flag;
	}
	public void setFlag(String flag) {
		this.flag = flag;
	}
	/**
	 * 方法: 取得java.lang.String
	 *
	 * @return: java.lang.String  建行机构号
	 */
	@Column(name = "ORG_CODE", nullable = true, length = 50)
	public String getOrgCode() {
		return orgCode;
	}
	public void setOrgCode(String orgCode) {
		this.orgCode = orgCode;
	}
	@OneToMany(fetch = FetchType.EAGER, mappedBy = "btBankAcc")
	public List<BtBankAccCur> getCurList() {
		return curList;
	}
	public void setCurList(List<BtBankAccCur> curList) {
		this.curList = curList;
	}
	@Column(name = "ATTACHMENT_ID", nullable = true, length = 32)
	public String getAttachmentId() {
		return attachmentId;
	}
	public void setAttachmentId(String attachmentId) {
		this.attachmentId = attachmentId;
	}
	@Column(name = "ENTITY_ACC_ID", nullable = true, length = 32)
	public String getEntityAccId() {
		return entityAccId;
	}
	public void setEntityAccId(String entityAccId) {
		this.entityAccId = entityAccId;
	}
	@Column(name = "IS_CAPITAL_POOL", nullable = true, length = 1)
	public String getIsCapitalPool() {
		return isCapitalPool;
	}
	public void setIsCapitalPool(String isCapitalPool) {
		this.isCapitalPool = isCapitalPool;
	}
	@Column(name = "CIRCULATION_MODE")
	public String getCirculationMode() {
		return circulationMode;
	}
	public void setCirculationMode(String circulationMode) {
		this.circulationMode = circulationMode;
	}
	@Column(name = "END_DATE")
	public Date getEndDate() {
		return endDate;
	}
	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}
	@Column(name = "CHECK_EXPIRE_DATE")
	public Date getCheckExpireDate() {
		return checkExpireDate;
	}
	public void setCheckExpireDate(Date checkExpireDate) {
		this.checkExpireDate = checkExpireDate;
	}
	@Transient
	public String getPrentAccId() {
		return prentAccId;
	}
	public void setPrentAccId(String prentAccId) {
		this.prentAccId = prentAccId;
	}
	@Column(name = "IS_DOMESTIC_BANK", nullable = true, length = 1)
	public String getIsDomesticBank() {
		return isDomesticBank;
	}
	public void setIsDomesticBank(String isDomesticBank) {
		this.isDomesticBank = isDomesticBank;
	}
	@Column(name = "SEAL_DIRECTORY")
	public String getSealDirectory() {
		return sealDirectory;
	}
	public void setSealDirectory(String sealDirectory) {
		this.sealDirectory = sealDirectory;
	}
	@Column(name = "SEAL_PERSON")
	public String getSealPerson() {
		return sealPerson;
	}
	public void setSealPerson(String sealPerson) {
		this.sealPerson = sealPerson;
	}
	@Column(name = "SEAL_PHONE")
	public String getSealPhone() {
		return sealPhone;
	}
	public void setSealPhone(String sealPhone) {
		this.sealPhone = sealPhone;
	}
	@Column(name = "SEAL_EMAIL")
	public String getSealEmail() {
		return sealEmail;
	}
	public void setSealEmail(String sealEmail) {
		this.sealEmail = sealEmail;
	}
	@Column(name = "MGT_DEPARTMENT")
	public String getMgtDepartment() {
		return mgtDepartment;
	}
	public void setMgtDepartment(String mgtDepartment) {
		this.mgtDepartment = mgtDepartment;
	}
	@Column(name = "U_KEY_FUNCTION_OTHER")
	public String getuKeyFunctionOther() {
		return uKeyFunctionOther;
	}
	public void setuKeyFunctionOther(String uKeyFunctionOther) {
		this.uKeyFunctionOther = uKeyFunctionOther;
	}
	@Column(name = "CLOSE_DATE")
	public Date getCloseDate() {
		return closeDate;
	}
	public void setCloseDate(Date closeDate) {
		this.closeDate = closeDate;
	}
	@Column(name = "IS_ONLINE_HANDLE")
	public String getIsOnlineHandle() {
		return isOnlineHandle;
	}
	public void setIsOnlineHandle(String isOnlineHandle) {
		this.isOnlineHandle = isOnlineHandle;
	}
	@Column(name = "IS_OFFSHORE_ACCOUNT")
	public String getIsOffshoreAccount() {
		return isOffshoreAccount;
	}
	public void setIsOffshoreAccount(String isOffshoreAccount) {
		this.isOffshoreAccount = isOffshoreAccount;
	}

	@Column(name = "BINDING_BANK_ACC")
	public String getBindingBankAcc() {
		return bindingBankAcc;
	}

	public void setBindingBankAcc(String bindingBankAcc) {
		this.bindingBankAcc = bindingBankAcc;
	}

	@Column(name = "IS_CHECK")
	public String getIsCheck() {
		return isCheck;
	}

	public void setIsCheck(String isCheck) {
		this.isCheck = isCheck;
	}

	@Column(name = "IS_DEMAND")
	public String getIsDemand() {
		return isDemand;
	}

	public void setIsDemand(String isDemand) {
		this.isDemand = isDemand;
	}

	@Column(name = "ACC_USE_CODE")
	public String getAccUseCode() {
		return accUseCode;
	}

	public void setAccUseCode(String accUseCode) {
		this.accUseCode = accUseCode;
	}

	@Column(name = "BINDING_BY_NAME")
	public String getBindingByName() {
		return bindingByName;
	}

	public void setBindingByName(String bindingByName) {
		this.bindingByName = bindingByName;
	}

	@Column(name = "BINDING_BY_MOBILE")
	public String getBindingByMobile() {
		return bindingByMobile;
	}

	public void setBindingByMobile(String bindingByMobile) {
		this.bindingByMobile = bindingByMobile;
	}

	@Column(name = "LOGIN_PASSWORD")
	public String getLoginPassword() {
		return loginPassword;
	}

	public void setLoginPassword(String loginPassword) {
		this.loginPassword = loginPassword;
	}

	@Column(name = "PAY_PASSWORD")
	public String getPayPassword() {
		return payPassword;
	}

	public void setPayPassword(String payPassword) {
		this.payPassword = payPassword;
	}

	@Column(name = "IS_CLOSE")
	public String getIsClose() {
		return isClose;
	}

	public void setIsClose(String isClose) {
		this.isClose = isClose;
	}
}
