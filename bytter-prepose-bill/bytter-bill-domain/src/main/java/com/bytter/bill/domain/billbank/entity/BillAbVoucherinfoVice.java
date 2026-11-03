package com.bytter.bill.domain.billbank.entity;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Transient;
/**
 * @Title: Entity
 * @Description: 票据主信息从表
 * @author onlineGenerator
 * @date 2017-06-21 15:50:03
 * @version 1.0
 *
 */
@Entity
@Table(name = "AB_VOUCHERINFO_VICE", schema = "")
@SuppressWarnings("serial")
public class BillAbVoucherinfoVice extends IdEntity implements java.io.Serializable {
	/** 申请单号 */
	private String appNo;
	/** 保证金账户 */
	private String bailAcc;
	/** 保证金 */
	private java.math.BigDecimal bailMoney;
	/** 保证金比例 */
	private java.math.BigDecimal bailPercent;
	/** 保证金利率 */
	private java.math.BigDecimal bailRate;
	/** 手续费 */
	private java.math.BigDecimal feeMoney;
	/** 所属库存:10单位库存11中心代管12银行托管 */
	private String enterStock;
	/** 打回业务类型 */
	private String backBriefId;
	/** 打回业务流水Id */
	private String backAcid;
	/** 摘要 */
	private String abs;
	/** 备注 */
	private String rmk;
	/** 创建人名称 */
	private String createName;
	/** 创建人登录名称 */
	private String createBy;
	/** 创建日期 */
	private java.util.Date createDate;
	/** 更新人名称 */
	private String updateName;
	/** 更新人登录名称 */
	private String updateBy;
	/** 更新日期 */
	private java.util.Date updateDate;
	/** 票据主表id */
	private String vid;
	/** 持票单位 */
	private String corpId;
	/** 持票单位账号 */
	private String bankAcc;
	/** 持票单位账号所属银行代码 */
	private String bankCode;
	/** 持票单位账号所属银行名称 */
	private String bankName;
	/** 银行接口代码 */
	private String bifCode;
	/** 票据来源：10收票登记11背书登记12质押登记14贴现登记15归集收入16上划收入17下拨收入 */
	private String billSource;
	/** 前手 */
	private String cnAccrecords;
	/** 内部业务标志：0 否；1 是 */
	private Integer insideSing;
	/** 0否； 1是 ；2已删除 */
	private String validSign;
	/**处理标记 10正在处理11处理完成*/
	private Integer boxStatus;
	/** 授信代码 */
	private String creditCode;
	/** 预算科目 */
	private String budgetCode;
	/** 类型 0应付票据，1应收票据*/
	private String type;
	/**开票数据来源*/
	private String source;
	/**附件ID*/
	private String attachmentId;
	/**外部系统id编码*/
	private String voucherId;
	/**是否挂授信 0否； 1是 */
	private String ratmodel;
	/**是否设置创建人 默认创建 */
	private boolean isSetCreateBy = true;
	
	/**出入池状态：0或空为池外1池内2入池在途3出池在途*/
	private String poolStatus;
	
	/** 申请单位 */
	private String applyCorpId;
	/** 业务单位 */
	private String busCorpId;
	/** 业务人员 */
	private String busPersonnel;
	@Column(name = "RAT_MODEL")
	public String getRatmodel() {
		return ratmodel;
	}
	public void setRatmodel(String ratmodel) {
		this.ratmodel = ratmodel;
	}
	/**
	 * 方法: 获取附件ID
	 * @return: 附件ID
	 */
	@Column(name = "ATTACHMENT_ID")
	public String getAttachmentId() {
		return attachmentId;
	}
	/**
	 * 方法: 设置附件ID
	 */
	public void setAttachmentId(String attachmentId) {
		this.attachmentId = attachmentId;
	}
	/**
	 * 方法: 取得java.lang.String
	 *
	 * @return: java.lang.String 保证金账户
	 */
	@Column(name = "BAIL_ACC")
	public String getBailAcc() {
		return this.bailAcc;
	}
	/**
	 * 方法: 设置java.lang.String
	 *
	 * @param: java.lang.String
	 *             保证金账户
	 */
	public void setBailAcc(String bailAcc) {
		this.bailAcc = bailAcc;
	}
	/**
	 * 方法: 取得申请单号
	 * @return: 申请单号
	 */
	@Column(name = "APP_NO")
	public String getAppNo() {
		return this.appNo;
	}
	/**
	 * 方法: 设置申请单号
	 *
	 * @param: 申请单号
	 */
	public void setAppNo(String appNo) {
		this.appNo = appNo;
	}
	/**
	 * 方法: 取得java.math.BigDecimal
	 *
	 * @return: java.math.BigDecimal 保证金
	 */
	@Column(name = "BAIL_MONEY")
	public java.math.BigDecimal getBailMoney() {
		return this.bailMoney;
	}
	/**
	 * 方法: 设置java.math.BigDecimal
	 *
	 * @param: java.math.BigDecimal
	 *             保证金
	 */
	public void setBailMoney(java.math.BigDecimal bailMoney) {
		this.bailMoney = bailMoney;
	}
	/**
	 * 方法: 取得java.math.BigDecimal
	 *
	 * @return: java.math.BigDecimal 保证金比例
	 */
	@Column(name = "BAIL_PERCENT")
	public java.math.BigDecimal getBailPercent() {
		return this.bailPercent;
	}
	/**
	 * 方法: 设置java.math.BigDecimal
	 *
	 * @param: java.math.BigDecimal
	 *             保证金比例
	 */
	public void setBailPercent(java.math.BigDecimal bailPercent) {
		this.bailPercent = bailPercent;
	}
	/**
	 * 方法: 取得java.math.BigDecimal
	 *
	 * @return: java.math.BigDecimal 保证金利率
	 */
	@Column(name = "BAIL_RATE")
	public java.math.BigDecimal getBailRate() {
		return this.bailRate;
	}
	/**
	 * 方法: 设置java.math.BigDecimal
	 *
	 * @param: java.math.BigDecimal
	 *             保证金利率
	 */
	public void setBailRate(java.math.BigDecimal bailRate) {
		this.bailRate = bailRate;
	}
	/**
	 * 方法: 取得java.math.BigDecimal
	 *
	 * @return: java.math.BigDecimal 手续费
	 */
	@Column(name = "FEE_MONEY")
	public java.math.BigDecimal getFeeMoney() {
		return this.feeMoney;
	}
	/**
	 * 方法: 设置java.math.BigDecimal
	 *
	 * @param: java.math.BigDecimal
	 *             手续费
	 */
	public void setFeeMoney(java.math.BigDecimal feeMoney) {
		this.feeMoney = feeMoney;
	}
	/**
	 * 方法: 取得java.lang.String
	 *
	 * @return: java.lang.String 所属库存:10单位库存11中心代管12银行托管
	 */
	@Column(name = "ENTER_STOCK")
	public String getEnterStock() {
		return this.enterStock;
	}
	/**
	 * 方法: 设置java.lang.String
	 *
	 * @param: java.lang.String
	 *             所属库存:10单位库存11中心代管12银行托管
	 */
	public void setEnterStock(String enterStock) {
		this.enterStock = enterStock;
	}
	/**
	 * 方法: 取得java.lang.String
	 *
	 * @return: java.lang.String 摘要
	 */
	@Column(name = "ABS")
	public String getAbs() {
		return this.abs;
	}
	/**
	 * 方法: 设置java.lang.String
	 *
	 * @param: java.lang.String
	 *             摘要
	 */
	public void setAbs(String abs) {
		this.abs = abs;
	}
	/**
	 * 方法: 取得java.lang.String
	 *
	 * @return: java.lang.String 备注
	 */
	@Column(name = "RMK")
	public String getRmk() {
		return this.rmk;
	}
	/**
	 * 方法: 设置java.lang.String
	 *
	 * @param: java.lang.String
	 *             备注
	 */
	public void setRmk(String rmk) {
		this.rmk = rmk;
	}
	/**
	 * 方法: 取得java.lang.String
	 *
	 * @return: java.lang.String 创建人名称
	 */
	@Column(name = "CREATE_NAME")
	public String getCreateName() {
		return this.createName;
	}
	/**
	 * 方法: 设置java.lang.String
	 *
	 * @param: java.lang.String
	 *             创建人名称
	 */
	public void setCreateName(String createName) {
		this.createName = createName;
	}
	/**
	 * 方法: 取得java.lang.String
	 *
	 * @return: java.lang.String 创建人登录名称
	 */
	@Column(name = "CREATE_BY")
	public String getCreateBy() {
		return this.createBy;
	}
	/**
	 * 方法: 设置java.lang.String
	 *
	 * @param: java.lang.String
	 *             创建人登录名称
	 */
	public void setCreateBy(String createBy) {
		this.createBy = createBy;
	}
	/**
	 * 方法: 取得java.util.Date
	 *
	 * @return: java.util.Date 创建日期
	 */
	@Column(name = "CREATE_DATE")
	public java.util.Date getCreateDate() {
		return this.createDate;
	}
	/**
	 * 方法: 设置java.util.Date
	 *
	 * @param: java.util.Date
	 *             创建日期
	 */
	public void setCreateDate(java.util.Date createDate) {
		this.createDate = createDate;
	}
	/**
	 * 方法: 取得java.lang.String
	 *
	 * @return: java.lang.String 更新人名称
	 */
	@Column(name = "UPDATE_NAME")
	public String getUpdateName() {
		return this.updateName;
	}
	/**
	 * 方法: 设置java.lang.String
	 *
	 * @param: java.lang.String
	 *             更新人名称
	 */
	public void setUpdateName(String updateName) {
		this.updateName = updateName;
	}
	/**
	 * 方法: 取得java.lang.String
	 *
	 * @return: java.lang.String 更新人登录名称
	 */
	@Column(name = "UPDATE_BY")
	public String getUpdateBy() {
		return this.updateBy;
	}
	/**
	 * 方法: 设置java.lang.String
	 *
	 * @param: java.lang.String
	 *             更新人登录名称
	 */
	public void setUpdateBy(String updateBy) {
		this.updateBy = updateBy;
	}
	/**
	 * 方法: 取得java.util.Date
	 *
	 * @return: java.util.Date 更新日期
	 */
	@Column(name = "UPDATE_DATE")
	public java.util.Date getUpdateDate() {
		return this.updateDate;
	}
	/**
	 * 方法: 设置java.util.Date
	 *
	 * @param: java.util.Date
	 *             更新日期
	 */
	public void setUpdateDate(java.util.Date updateDate) {
		this.updateDate = updateDate;
	}
	/**
	 * 方法: 取得java.lang.String
	 *
	 * @return: java.lang.String 票据主表id
	 */
	@Column(name = "VID")
	public String getVid() {
		return this.vid;
	}
	/**
	 * 方法: 设置java.lang.String
	 *
	 * @param: java.lang.String
	 *             票据主表id
	 */
	public void setVid(String vid) {
		this.vid = vid;
	}
	/**
	 * 方法: 取得java.lang.String
	 *
	 * @return: java.lang.String 持票单位
	 */
	@Column(name = "CORP_ID")
	public String getCorpId() {
		return this.corpId;
	}
	/**
	 * 方法: 设置java.lang.String
	 *
	 * @param: java.lang.String
	 *             持票单位
	 */
	public void setCorpId(String corpId) {
		this.corpId = corpId;
	}
	/**
	 * 方法: 取得java.lang.String
	 *
	 * @return: java.lang.String 持票单位账号
	 */
	@Column(name = "BANK_ACC")
	public String getBankAcc() {
		return this.bankAcc;
	}
	/**
	 * 方法: 设置java.lang.String
	 *
	 * @param: java.lang.String
	 *             持票单位账号
	 */
	public void setBankAcc(String bankAcc) {
		this.bankAcc = bankAcc;
	}
	/**
	 * 方法: 取得java.lang.String
	 *
	 * @return: java.lang.String 持票单位银行
	 */
	@Column(name = "BANK_CODE")
	public String getBankCode() {
		return this.bankCode;
	}
	/**
	 * 方法: 设置java.lang.String
	 *
	 * @param: java.lang.String
	 *             持票单位银行
	 */
	public void setBankCode(String bankCode) {
		this.bankCode = bankCode;
	}
	/**
	 * 方法: 取得java.lang.String
	 *
	 * @return: java.lang.String 银行接口代码
	 */
	@Column(name = "BIF_CODE")
	public String getBifCode() {
		return this.bifCode;
	}
	/**
	 * 方法: 设置java.lang.String
	 *
	 * @param: java.lang.String
	 *             银行接口代码
	 */
	public void setBifCode(String bifCode) {
		this.bifCode = bifCode;
	}
	/**
	 * 方法: 取得String
	 *
	 * @return: String 10收票登记11背书登记12质押登记14贴现登记15归集收入16上划收入17下拨收入
	 */
	@Column(name = "BILL_SOURCE")
	public String getBillSource() {
		return this.billSource;
	}
	/**
	 * 方法: 设置String
	 *
	 * @param: String
	 *             10收票登记11背书登记12质押登记14贴现登记15归集收入16上划收入17下拨收入
	 */
	public void setBillSource(String billSource) {
		this.billSource = billSource;
	}
	/**
	 * 方法: 取得java.lang.String
	 *
	 * @return: java.lang.String 前手
	 */
	@Column(name = "CN_ACCRECORDS")
	public String getCnAccrecords() {
		return this.cnAccrecords;
	}
	/**
	 * 方法: 设置java.lang.String
	 *
	 * @param: java.lang.String
	 *             前手
	 */
	public void setCnAccrecords(String cnAccrecords) {
		this.cnAccrecords = cnAccrecords;
	}
	/**
	 * 方法: 取得java.lang.Integer
	 *
	 * @return: java.lang.Integer 内部业务标志：0 否；1 是
	 */
	@Column(name = "INSIDE_SING")
	public Integer getInsideSing() {
		return this.insideSing;
	}
	/**
	 * 方法: 设置java.lang.Integer
	 *
	 * @param: java.lang.Integer
	 *             内部业务标志：0 否；1 是
	 */
	public void setInsideSing(Integer insideSing) {
		this.insideSing = insideSing;
	}
	/**
	 * 方法: 取得java.lang.String
	 *
	 * @return: java.lang.String 0否； 1是 ；2已删除
	 */
	@Column(name = "VALID_SIGN")
	public String getValidSign() {
		return this.validSign;
	}
	/**
	 * 方法: 设置java.lang.String
	 *
	 * @param: java.lang.String
	 *             0否； 1是 ；2已删除
	 */
	public void setValidSign(String validSign) {
		this.validSign = validSign;
	}
	/**
	 * 方法: 取得java.lang.String
	 *
	 * @return: java.lang.String 授信代码
	 */
	@Column(name = "CREDIT_CODE")
	public String getCreditCode() {
		return this.creditCode;
	}
	/**
	 * 方法: 设置java.lang.String
	 *
	 * @param: java.lang.String
	 *             授信代码
	 */
	public void setCreditCode(String creditCode) {
		this.creditCode = creditCode;
	}
	/**
	 * 方法: 取得java.lang.String
	 *
	 * @return: java.lang.String 预算科目
	 */
	@Column(name = "BUDGET_CODE")
	public String getBudgetCode() {
		return this.budgetCode;
	}
	/**
	 * 方法: 设置java.lang.String
	 *
	 * @param: java.lang.String
	 *             预算科目
	 */
	public void setBudgetCode(String budgetCode) {
		this.budgetCode = budgetCode;
	}
	@Column(name = "BACK_ACID")
	public String getBackAcid() {
		return backAcid;
	}
	public void setBackAcid(String backAcid) {
		this.backAcid = backAcid;
	}
	@Column(name = "BACK_BRIEF_ID")
	public String getBackBriefId() {
		return backBriefId;
	}
	public void setBackBriefId(String backBriefId) {
		this.backBriefId = backBriefId;
	}
	@Column(name = "TYPE")
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	@Column(name = "BANK_NAME")
	public String getBankName() {
		return bankName;
	}
	public void setBankName(String bankName) {
		this.bankName = bankName;
	}
	/**
	 *方法: 取得java.lang.Integer
	 *@return: java.lang.Integer  处理标记 10正在处理11处理完成
	 */
	@Column(name ="BOX_STATUS")
	public Integer getBoxStatus() {
		return boxStatus;
	}
	public void setBoxStatus(Integer boxStatus) {
		this.boxStatus = boxStatus;
	}
	@Column(name ="SOURCE")
	public String getSource() {
		return source;
	}
	public void setSource(String source) {
		this.source = source;
	}
	@Column(name ="VOUCHER_ID")
	public String getVoucherId() {
		return voucherId;
	}
	public void setVoucherId(String voucherId) {
		this.voucherId = voucherId;
	}
	@Transient
	public boolean getIsSetCreateBy() {
		return isSetCreateBy;
	}
	public void setIsSetCreateBy(boolean isSetCreateBy) {
		this.isSetCreateBy = isSetCreateBy;
	}
	@Column(name ="POOL_STATUS")
	public String getPoolStatus() {
		return poolStatus;
	}
	public void setPoolStatus(String poolStatus) {
		this.poolStatus = poolStatus;
	}
	
	@Column(name = "APPLY_CORP_ID")
	public String getApplyCorpId() {
		return applyCorpId;
	}
	public void setApplyCorpId(String applyCorpId) {
		this.applyCorpId = applyCorpId;
	}
	@Column(name = "BUS_CORP_ID")
	public String getBusCorpId() {
		return busCorpId;
	}
	public void setBusCorpId(String busCorpId) {
		this.busCorpId = busCorpId;
	}
	@Column(name = "BUS_PERSONNEL")
	public String getBusPersonnel() {
		return busPersonnel;
	}
	public void setBusPersonnel(String busPersonnel) {
		this.busPersonnel = busPersonnel;
	}


	public String toString() {
		return "AbVoucherinfoVice [appNo=" + appNo + ", bailAcc=" + bailAcc + ", bailMoney=" + bailMoney
				+ ", bailPercent=" + bailPercent + ", bailRate=" + bailRate + ", feeMoney=" + feeMoney + ", enterStock="
				+ enterStock + ", backBriefId=" + backBriefId + ", backAcid=" + backAcid + ", abs=" + abs + ", rmk="
				+ rmk + ", createName=" + createName + ", createBy=" + createBy + ", createDate=" + createDate
				+ ", updateName=" + updateName + ", updateBy=" + updateBy + ", updateDate=" + updateDate + ", vid="
				+ vid + ", corpId=" + corpId + ", bankAcc=" + bankAcc + ", bankCode=" + bankCode + ", bankName="
				+ bankName + ", bifCode=" + bifCode + ", billSource=" + billSource + ", cnAccrecords=" + cnAccrecords
				+ ", insideSing=" + insideSing + ", validSign=" + validSign + ", boxStatus=" + boxStatus
				+ ", creditCode=" + creditCode + ", budgetCode=" + budgetCode + ", type=" + type + ", source=" + source
				+ ", attachmentId=" + attachmentId + ", voucherId=" + voucherId + ", ratmodel=" + ratmodel
				+ ", isSetCreateBy=" + isSetCreateBy + ", applyCorpId=" + applyCorpId +
				", busCorpId=" + busCorpId + ", busPersonnel=" + busPersonnel + "]";
	}
}
