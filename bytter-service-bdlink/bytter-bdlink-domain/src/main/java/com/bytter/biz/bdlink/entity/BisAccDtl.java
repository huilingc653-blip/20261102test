package com.bytter.biz.bdlink.entity;
import com.bytter.core.base.entity.IdEntity;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.Date;
/**
 * @author onlineGenerator
 * @version 1.0
 * @Title: Entity
 * @Description: 银行账号明细表
 * @date 2017-07-05 11:20:30
 */
@Entity
@Table(name = "BIS_ACC_DTL", schema = "")
@SuppressWarnings("serial")
public class BisAccDtl extends IdEntity implements java.io.Serializable {
	/**
	 * 业务单号
	 */
	private java.lang.String accNo;
	/**
	 * 本方账号
	 */
	private java.lang.String bankAcc;
	/**
	 * 本方账户名
	 */
	private java.lang.String accName;
	/**
	 * 本方账户开户行
	 */
	private java.lang.String bankName;
	/**
	 * 接口代码
	 */
	private java.lang.String bifCode;
	/**
	 * 对方账号
	 */
	private java.lang.String oppAccNo;
	/**
	 * 对方账户名
	 */
	private java.lang.String oppAccName;
	/**
	 * 对方账户开户行
	 */
	private java.lang.String oppAccBank;
	/**
	 * 借贷标志
	 */
	private java.lang.String cdSign;
	/**
	 * 红蓝标志 1-红 2-蓝
	 */
	private java.lang.String rbSign;
	/**
	 * 交易金额
	 */
	private java.math.BigDecimal amt;
	/**
	 * 余额
	 */
	private java.math.BigDecimal bal;
	/**
	 * 币别ID
	 */
	private java.lang.String curId;
	/**
	 * 用途
	 */
	private java.lang.String uses;
	/**
	 * 附言
	 */
	private java.lang.String postscript;
	/**
	 * 摘要
	 */
	private java.lang.String remark;
	/**
	 * 交易时间
	 */
	private java.util.Date transTime;
	/**
	 * 企业流水号
	 */
	private java.lang.String voucherNo;
	/**
	 * 银行流水号
	 */
	private java.lang.String bankSerialNo;
	/**
	 * 返回时间
	 */
	private java.util.Date returnTime;
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
	 * 银行接口导出标识
	 */
	private java.lang.String importSign;
	/**
	 * 导出日期
	 */
	private java.util.Date importDate;
	/**
	 * 资金计划挂接状态
	 */
	private java.lang.String fbsStatus;
	/**
	 * 付款方式
	 */
	private java.lang.String settlType;
	/**
	 * 项目信息
	 */
	private java.lang.String projectRemark;
	/**
	 * 勾兑标志 0-未勾兑，1-已勾兑
	 */
	private java.lang.String blendSign;
	/**
	 *原单处理方式 1-不处理原单、2-生成新单、3-打回原单
	 */
	private java.lang.String refundBlendType;
	/**
	 * 备注
	 */
	private java.lang.String rmk;
	/**
	 * 业务单id
	 */
	private String billId;
	/**
	 * 合并后单据ID
	 */
	private String mergeId;
	/**
	 * 拆分前单据ID
	 */
	private String splitId;
	/**
	 * 状态
	 * 95是我们离线录入审批通过 的
	 * 如果是银行接口写进来的就没有状态
	 */
	private Integer status;
	/**
	 * 业务类型
	 */
	private String busType;
	/**
	 * 票据认领中间表id
	 */
	private String claimId;
	
	private String autoSign;
	
	private String againSign;
	
	@Column(name = "AUTO_SIGN")
	public String getAutoSign() {
		return autoSign;
	}
	
	public void setAutoSign(String autoSign) {
		this.autoSign = autoSign;
	}
	
	@Column(name = "AGAIN_SIGN")
	public String getAgainSign() {
		return againSign;
	}
	
	public void setAgainSign(String againSign) {
		this.againSign = againSign;
	}
	
	@Column(name = "claim_id")
	public String getClaimId() {
		return claimId;
	}
	public void setClaimId(String claimId) {
		this.claimId = claimId;
	}
	/**
	 * 方法: 取得java.lang.String
	 *
	 * @return: java.lang.String  业务单号
	 */
	@Column(name = "ACC_NO", nullable = true, length = 40)
	public java.lang.String getAccNo() {
		return this.accNo;
	}
	/**
	 * 方法: 设置java.lang.String
	 *
	 * @param: java.lang.String  业务单号
	 */
	public void setAccNo(java.lang.String accNo) {
		this.accNo = accNo;
	}
	/**
	 * 方法: 取得java.lang.String
	 *
	 * @return: java.lang.String  本方账号
	 */
	@Column(name = "BANK_ACC", nullable = true, length = 60)
	public java.lang.String getBankAcc() {
		return this.bankAcc;
	}
	/**
	 * 方法: 设置java.lang.String
	 *
	 * @param: java.lang.String  本方账号
	 */
	public void setBankAcc(java.lang.String bankAcc) {
		this.bankAcc = bankAcc;
	}
	/**
	 * 方法: 取得java.lang.String
	 *
	 * @return: java.lang.String  本方账户名
	 */
	@Column(name = "ACC_NAME", nullable = true, length = 200)
	public java.lang.String getAccName() {
		return this.accName;
	}
	/**
	 * 方法: 设置java.lang.String
	 *
	 * @param: java.lang.String  本方账户名
	 */
	public void setAccName(java.lang.String accName) {
		this.accName = accName;
	}
	/**
	 * 方法: 取得java.lang.String
	 *
	 * @return: java.lang.String  本方账户开户行
	 */
	@Column(name = "BANK_NAME", nullable = true, length = 200)
	public java.lang.String getBankName() {
		return this.bankName;
	}
	/**
	 * 方法: 设置java.lang.String
	 *
	 * @param: java.lang.String  本方账户开户行
	 */
	public void setBankName(java.lang.String bankName) {
		this.bankName = bankName;
	}
	/**
	 * 方法: 取得java.lang.String
	 *
	 * @return: java.lang.String  接口代码
	 */
	@Column(name = "BIF_CODE", nullable = true, length = 32)
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
	 * @return: java.lang.String  对方账号
	 */
	@Column(name = "OPP_ACC_NO", nullable = true, length = 60)
	public java.lang.String getOppAccNo() {
		return this.oppAccNo;
	}
	/**
	 * 方法: 设置java.lang.String
	 *
	 * @param: java.lang.String  对方账号
	 */
	public void setOppAccNo(java.lang.String oppAccNo) {
		this.oppAccNo = oppAccNo;
	}
	/**
	 * 方法: 取得java.lang.String
	 *
	 * @return: java.lang.String  对方账户名
	 */
	@Column(name = "OPP_ACC_NAME", nullable = true, length = 200)
	public java.lang.String getOppAccName() {
		return this.oppAccName;
	}
	/**
	 * 方法: 设置java.lang.String
	 *
	 * @param: java.lang.String  对方账户名
	 */
	public void setOppAccName(java.lang.String oppAccName) {
		this.oppAccName = oppAccName;
	}
	/**
	 * 方法: 取得java.lang.String
	 *
	 * @return: java.lang.String  对方账户开户行
	 */
	@Column(name = "OPP_ACC_BANK", nullable = true, length = 200)
	public java.lang.String getOppAccBank() {
		return this.oppAccBank;
	}
	/**
	 * 方法: 设置java.lang.String
	 *
	 * @param: java.lang.String  对方账户开户行
	 */
	public void setOppAccBank(java.lang.String oppAccBank) {
		this.oppAccBank = oppAccBank;
	}
	/**
	 * 方法: 取得java.lang.String
	 *
	 * @return: java.lang.String  借贷标志
	 */
	@Column(name = "CD_SIGN", nullable = true, length = 1)
	public java.lang.String getCdSign() {
		return this.cdSign;
	}
	/**
	 * 方法: 设置java.lang.String
	 *
	 * @param: java.lang.String  借贷标志
	 */
	public void setCdSign(java.lang.String cdSign) {
		this.cdSign = cdSign;
	}
	/**
	 * 方法: 取得java.lang.String
	 *
	 * @return: java.lang.String  红蓝标志 1-红 2-蓝
	 */
	@Column(name = "RB_SIGN", nullable = true, length = 1)
	public java.lang.String getRbSign() {
		return this.rbSign;
	}
	/**
	 * 方法: 设置java.lang.String
	 *
	 * @param: java.lang.String  红蓝标志 1-红 2-蓝
	 */
	public void setRbSign(java.lang.String rbSign) {
		this.rbSign = rbSign;
	}
	/**
	 * 方法: 取得java.math.BigDecimal
	 *
	 * @return: java.math.BigDecimal  交易金额
	 */
	@Column(name = "AMT", nullable = true, scale = 2, length = 19)
	public java.math.BigDecimal getAmt() {
		return this.amt;
	}
	/**
	 * 方法: 设置java.math.BigDecimal
	 *
	 * @param: java.math.BigDecimal  交易金额
	 */
	public void setAmt(java.math.BigDecimal amt) {
		this.amt = amt;
	}
	/**
	 * 方法: 取得java.math.BigDecimal
	 *
	 * @return: java.math.BigDecimal  余额
	 */
	@Column(name = "BAL", nullable = true, scale = 2, length = 19)
	public java.math.BigDecimal getBal() {
		return this.bal;
	}
	/**
	 * 方法: 设置java.math.BigDecimal
	 *
	 * @param: java.math.BigDecimal  余额
	 */
	public void setBal(java.math.BigDecimal bal) {
		this.bal = bal;
	}
	/**
	 * 方法: 取得java.lang.String
	 *
	 * @return: java.lang.String  币别ID
	 */
	@Column(name = "CUR_ID", nullable = true, length = 32)
	public java.lang.String getCurId() {
		return this.curId;
	}
	/**
	 * 方法: 设置java.lang.String
	 *
	 * @param: java.lang.String  币别ID
	 */
	public void setCurId(java.lang.String curId) {
		this.curId = curId;
	}
	/**
	 * 方法: 取得java.lang.String
	 *
	 * @return: java.lang.String  用途
	 */
	@Column(name = "USES", nullable = true, length = 80)
	public java.lang.String getUses() {
		return this.uses;
	}
	/**
	 * 方法: 设置java.lang.String
	 *
	 * @param: java.lang.String  用途
	 */
	public void setUses(java.lang.String uses) {
		this.uses = uses;
	}
	/**
	 * 方法: 取得java.lang.String
	 *
	 * @return: java.lang.String  附言
	 */
	@Column(name = "POSTSCRIPT", nullable = true, length = 80)
	public java.lang.String getPostscript() {
		return this.postscript;
	}
	/**
	 * 方法: 设置java.lang.String
	 *
	 * @param: java.lang.String  附言
	 */
	public void setPostscript(java.lang.String postscript) {
		this.postscript = postscript;
	}
	/**
	 * 方法: 取得java.lang.String
	 *
	 * @return: java.lang.String  摘要
	 */
	@Column(name = "REMARK", nullable = true, length = 80)
	public java.lang.String getRemark() {
		return this.remark;
	}
	/**
	 * 方法: 设置java.lang.String
	 *
	 * @param: java.lang.String  摘要
	 */
	public void setRemark(java.lang.String remark) {
		this.remark = remark;
	}
	/**
	 * 方法: 取得java.util.Date
	 *
	 * @return: java.util.Date  交易时间
	 */
	@Column(name = "TRANS_TIME", nullable = true)
	public java.util.Date getTransTime() {
		return this.transTime;
	}
	/**
	 * 方法: 设置java.util.Date
	 *
	 * @param: java.util.Date  交易时间
	 */
	public void setTransTime(java.util.Date transTime) {
		this.transTime = transTime;
	}
	/**
	 * 方法: 取得java.lang.String
	 *
	 * @return: java.lang.String  企业流水号
	 */
	@Column(name = "VOUCHER_NO", nullable = true, length = 30)
	public java.lang.String getVoucherNo() {
		return this.voucherNo;
	}
	/**
	 * 方法: 设置java.lang.String
	 *
	 * @param: java.lang.String  企业流水号
	 */
	public void setVoucherNo(java.lang.String voucherNo) {
		this.voucherNo = voucherNo;
	}
	/**
	 * 方法: 取得java.lang.String
	 *
	 * @return: java.lang.String  银行流水号
	 */
	@Column(name = "BANK_SERIAL_NO", nullable = true, length = 60)
	public java.lang.String getBankSerialNo() {
		return this.bankSerialNo;
	}
	/**
	 * 方法: 设置java.lang.String
	 *
	 * @param: java.lang.String  银行流水号
	 */
	public void setBankSerialNo(java.lang.String bankSerialNo) {
		this.bankSerialNo = bankSerialNo;
	}
	/**
	 * 方法: 取得java.util.Date
	 *
	 * @return: java.util.Date  返回时间
	 */
	@Column(name = "RETURN_TIME", nullable = true)
	public java.util.Date getReturnTime() {
		return this.returnTime;
	}
	/**
	 * 方法: 设置java.util.Date
	 *
	 * @param: java.util.Date  返回时间
	 */
	public void setReturnTime(java.util.Date returnTime) {
		this.returnTime = returnTime;
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
	@Column(name = "IMPORT_SIGN", length = 1)
	public String getImportSign() {
		return importSign;
	}
	public void setImportSign(String importSign) {
		this.importSign = importSign;
	}
	@Column(name = "IMPORT_DATE")
	public Date getImportDate() {
		return importDate;
	}
	public void setImportDate(Date importDate) {
		this.importDate = importDate;
	}
	/**
	 * 方法: 设置java.util.String
	 *
	 * @param: java.util.String  资金计划挂接状态
	 */
	@Column(name = "FBS_STATUS")
	public String getFbsStatus() {
		return fbsStatus;
	}
	public void setFbsStatus(String fbsStatus) {
		this.fbsStatus = fbsStatus;
	}
	/**
	 * 方法: 设置java.util.String
	 *
	 * @param: java.util.String  结算方式
	 */
	@Column(name = "SETTL_TYPE")
	public String getSettlType() {
		return settlType;
	}
	public void setSettlType(String settlType) {
		this.settlType = settlType;
	}
	@Column(name = "PROJECT_REMARK")
	public String getProjectRemark() {
		return projectRemark;
	}
	public void setProjectRemark(String projectRemark) {
		this.projectRemark = projectRemark;
	}
	@Column(name = "BLEND_SIGN")
	public String getBlendSign() {
		return blendSign;
	}
	public void setBlendSign(String blendSign) {
		this.blendSign = blendSign;
	}
	@Column(name = "rmk")
	public String getRmk() {
		return rmk;
	}
	public void setRmk(String rmk) {
		this.rmk = rmk;
	}
	@Column(name = "REFUND_BLEND_TYPE")
	public String getRefundBlendType() {
		return refundBlendType;
	}
	public void setRefundBlendType(String refundBlendType) {
		this.refundBlendType = refundBlendType;
	}
	@Column(name = "BILL_ID")
	public String getBillId() {
		return billId;
	}
	public void setBillId(String billId) {
		this.billId = billId;
	}
	@Column(name = "MERGE_ID")
	public String getMergeId() {
		return mergeId;
	}
	public void setMergeId(String mergeId) {
		this.mergeId = mergeId;
	}
	@Column(name = "SPLIT_ID")
	public String getSplitId() {
		return splitId;
	}
	public void setSplitId(String splitId) {
		this.splitId = splitId;
	}
	@Column(name = "STATUS")
	public Integer getStatus() {
		return status;
	}
	public void setStatus(Integer status) {
		this.status = status;
	}
	@Column(name = "BUS_TYPE")
	public String getBusType() {
		return busType;
	}
	public void setBusType(String busType) {
		this.busType = busType;
	}
}
