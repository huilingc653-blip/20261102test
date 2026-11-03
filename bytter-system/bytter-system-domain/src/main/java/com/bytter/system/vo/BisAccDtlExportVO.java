package com.bytter.system.vo;
import com.bytter.core.annotation.BtCompositeSearch;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.format.annotation.NumberFormat;
import java.math.BigDecimal;
import java.util.Date;
/**
 * @author onlineGenerator
 * @version 1.0
 * @Title: Entity
 * @Description: 银行账号明细录入表
 * @date 2017-06-29 13:51:01
 */
@SuppressWarnings("serial")
public class BisAccDtlExportVO implements java.io.Serializable {
	@BtCompositeSearch(fieldName = "t.id")
	private String id;
	/**
	 * 业务单号
	 */
	@BtCompositeSearch(fieldName = "t.ACC_NO")
	private String accNo;
	/**
	 * 交易时间
	 */
	@BtCompositeSearch(fieldName = "t.trans_time")
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss.SSS")
	private Date transTime;
	/**
	 * 摘要
	 */
	@BtCompositeSearch(fieldName = "t.remark")
	private String remark;
	/**
	 * 单位ID
	 */
	//@BtCompositeSearch(fieldName = "acc.corp_id")
	private String corpId;
	/**
	 * 单位
	 */
	//@BtCompositeSearch(fieldName = "acc.corp_id")
	private String corpName;
	/**
	 * 本方账号
	 */
	@BtCompositeSearch(fieldName = "t.bank_acc")
	private String bankAcc;
	/**
	 * 本方账户名
	 */
	@BtCompositeSearch(fieldName = "t.acc_name")
	private String accName;
	/**
	 * 本方账户开户行
	 */
	@BtCompositeSearch(fieldName = "t.bank_name")
	private String bankName;
	/**
	 * 接口代码
	 */
	//@BtCompositeSearch(fieldName = "t.bif_code")
	private String bifCode;
	//@BtCompositeSearch(fieldName = "t.bif_code")
	private String bifName;
	/**
	 * 借贷标志 0-收 1-支
	 */
	@BtCompositeSearch(fieldName = "t.cd_sign")
	private String cdSign;
	@BtCompositeSearch(fieldName = "t.cd_sign")
	private String cdSignName;
	/**
	 * 红蓝标志 1-红 2-蓝
	 */
	@BtCompositeSearch(fieldName = "t.rb_sign")
	private String rbSign;
	private String rbSignName;
	/**
	 * 币别ID
	 */
	@BtCompositeSearch(fieldName = "t.cur_id")
	private String curId;
	/**
	 * 币别
	 */
	@BtCompositeSearch(fieldName = "t.cur_id")
	private String curName;
	/**
	 * 对方账号
	 */
	@BtCompositeSearch(fieldName = "t.opp_acc_no")
	private String oppAccNo;
	/**
	 * 对方账户名
	 */
	@BtCompositeSearch(fieldName = "t.opp_acc_name")
	private String oppAccName;
	/**
	 * 对方账户开户行
	 */
	@BtCompositeSearch(fieldName = "t.opp_acc_bank")
	private String oppAccBank;
	/**
	 * 交易金额
	 */
	@NumberFormat(pattern = "#,##0.00")
	@BtCompositeSearch(fieldName = "t.amt")
	private BigDecimal amt;
	/**
	 * 余额
	 */
	@NumberFormat(pattern = "#,##0.00")
	@BtCompositeSearch(fieldName = "t.bal")
	private BigDecimal bal;
	/**
	 * 用途
	 */
	@BtCompositeSearch(fieldName = "t.uses")
	private String uses;
	/**
	 * 附言
	 */
	@BtCompositeSearch(fieldName = "t.postscript")
	private String postscript;
	/**
	 * 企业流水号
	 */
	private String voucherNo;
	/**
	 * 银行流水号
	 */
	@BtCompositeSearch(fieldName = "t.bank_serial_no")
	private String bankSerialNo;
	/**
	 * 返回时间
	 */
	private Date returnTime;
	/**
	 * 创建人登录名称
	 */
	private String createBy;
	/**
	 * 创建日期
	 */
	private Date createDate;
	/**
	 * 更新人登录名称
	 */
	private String updateBy;
	/**
	 * 更新日期
	 */
	private Date updateDate;
	/**
	 * 银行接口导出标识
	 */
	private String importSign;
	/**
	 * 导出日期
	 */
	private Date importDate;
	/**
	 * 计划状态 0未确认 1已确认 2已删除
	 */
	private String fbsStatus;
	/**
	 * 项目信息
	 */
	@BtCompositeSearch(fieldName = "t.project_remark")
	private String projectRemark;
	/**
	 * 账户属性
	 */
	//@BtCompositeSearch(fieldName = "acc.attribute_Id")
	private String attributeId;
	//@BtCompositeSearch(fieldName = "acc.attribute_Id")
	private String attributeName;
	/**
	 * 勾兑标志 0-未勾兑，1-已勾兑
	 */
	@BtCompositeSearch(fieldName = "t.BLEND_SIGN")
	private String blendSign;
	private String blendSignName;
	/**
	 *原单处理方式 1-不处理原单、2-生成新单、3-打回原单
	 */
	@BtCompositeSearch(fieldName = "t.REFUND_BLEND_TYPE")
	private String refundBlendType;
	private String refundBlendTypeName;
	/**
	 * 账户性质
	 */
	//@BtCompositeSearch(fieldName = "acc.nature_id")
	private String natureId;
	//@BtCompositeSearch(fieldName = "acc.nature_id")
	private String natureName;
	/**账户类别*/
	//@BtCompositeSearch(fieldName = "acc.acc_type")
	private String accType;
	//@BtCompositeSearch(fieldName = "acc.acc_type")
	private String accTypeName;
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
	public String getClaimId() {
		return claimId;
	}
	public void setClaimId(String claimId) {
		this.claimId = claimId;
	}
	public String getBlendSign() {
		return blendSign;
	}
	public void setBlendSign(String blendSign) {
		this.blendSign = blendSign;
	}
	public String getBlendSignName() {
		return blendSignName;
	}
	public void setBlendSignName(String blendSignName) {
		this.blendSignName = blendSignName;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getAccNo() {
		return accNo;
	}
	public void setAccNo(String accNo) {
		this.accNo = accNo;
	}
	public Date getTransTime() {
		return transTime;
	}
	public void setTransTime(Date transTime) {
		this.transTime = transTime;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
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
	public String getBankName() {
		return bankName;
	}
	public void setBankName(String bankName) {
		this.bankName = bankName;
	}
	public String getBifCode() {
		return bifCode;
	}
	public void setBifCode(String bifCode) {
		this.bifCode = bifCode;
	}
	public String getBifName() {
		return bifName;
	}
	public void setBifName(String bifName) {
		this.bifName = bifName;
	}
	public String getCdSign() {
		return cdSign;
	}
	public void setCdSign(String cdSign) {
		this.cdSign = cdSign;
		if("0".equals(this.cdSign)){
			this.cdSignName = "收入";
		}else if("1".equals(this.cdSign)){
			this.cdSignName = "支出";
		}
	}
	public String getRbSign() {
		return rbSign;
	}
	public void setRbSign(String rbSign) {
		this.rbSign = rbSign;
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
	public String getOppAccNo() {
		return oppAccNo;
	}
	public void setOppAccNo(String oppAccNo) {
		this.oppAccNo = oppAccNo;
	}
	public String getOppAccName() {
		return oppAccName;
	}
	public void setOppAccName(String oppAccName) {
		this.oppAccName = oppAccName;
	}
	public String getOppAccBank() {
		return oppAccBank;
	}
	public void setOppAccBank(String oppAccBank) {
		this.oppAccBank = oppAccBank;
	}
	public BigDecimal getAmt() {
		return amt;
	}
	public void setAmt(BigDecimal amt) {
		this.amt = amt;
	}
	public BigDecimal getBal() {
		return bal;
	}
	public void setBal(BigDecimal bal) {
		this.bal = bal;
	}
	public String getFbsStatus() {
		return fbsStatus;
	}
	public void setFbsStatus(String fbsStatus) {
		this.fbsStatus = fbsStatus;
	}
	public String getUses() {
		return uses;
	}
	public void setUses(String uses) {
		this.uses = uses;
	}
	public String getPostscript() {
		return postscript;
	}
	public void setPostscript(String postscript) {
		this.postscript = postscript;
	}
	public String getVoucherNo() {
		return voucherNo;
	}
	public void setVoucherNo(String voucherNo) {
		this.voucherNo = voucherNo;
	}
	public String getBankSerialNo() {
		return bankSerialNo;
	}
	public void setBankSerialNo(String bankSerialNo) {
		this.bankSerialNo = bankSerialNo;
	}
	public Date getReturnTime() {
		return returnTime;
	}
	public void setReturnTime(Date returnTime) {
		this.returnTime = returnTime;
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
	public String getImportSign() {
		return importSign;
	}
	public void setImportSign(String importSign) {
		this.importSign = importSign;
	}
	public Date getImportDate() {
		return importDate;
	}
	public void setImportDate(Date importDate) {
		this.importDate = importDate;
	}
	public String getCdSignName() {
		return cdSignName;
	}
	public void setCdSignName(String cdSignName) {
		this.cdSignName = cdSignName;
	}
	public String getRbSignName() {
		return rbSignName;
	}
	public void setRbSignName(String rbSignName) {
		this.rbSignName = rbSignName;
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
	public String getProjectRemark() {
		return projectRemark;
	}
	public void setProjectRemark(String projectRemark) {
		this.projectRemark = projectRemark;
	}
	public String getRefundBlendType() {
		return refundBlendType;
	}
	public void setRefundBlendType(String refundBlendType) {
		this.refundBlendType = refundBlendType;
	}
	public String getRefundBlendTypeName() {
		return refundBlendTypeName;
	}
	public void setRefundBlendTypeName(String refundBlendTypeName) {
		this.refundBlendTypeName = refundBlendTypeName;
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
	public String getBillId() {
		return billId;
	}
	public void setBillId(String billId) {
		this.billId = billId;
	}
	public String getMergeId() {
		return mergeId;
	}
	public void setMergeId(String mergeId) {
		this.mergeId = mergeId;
	}
	public String getSplitId() {
		return splitId;
	}
	public void setSplitId(String splitId) {
		this.splitId = splitId;
	}
	public Integer getStatus() {
		return status;
	}
	public void setStatus(Integer status) {
		this.status = status;
	}
	public String getBusType() {
		return busType;
	}
	public void setBusType(String busType) {
		this.busType = busType;
	}
}
