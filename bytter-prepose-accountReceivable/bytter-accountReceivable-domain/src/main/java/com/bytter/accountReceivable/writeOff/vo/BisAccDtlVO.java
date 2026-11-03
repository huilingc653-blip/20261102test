package com.bytter.accountReceivable.writeOff.vo;

import com.bytter.accountReceivable.constant.AccountReceivableConstant;
import com.bytter.core.annotation.BtCompositeSearch;
import com.bytter.core.annotation.Corp;
import com.bytter.core.annotation.Currency;
import com.bytter.core.annotation.Dict;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
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
@Data
public class BisAccDtlVO implements java.io.Serializable {
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
	@JsonFormat(pattern = "yyyy-MM-dd")
	private Date transTime;
	/**
	 * 摘要
	 */
	@BtCompositeSearch(fieldName = "t.remark")
	private String remark;
	/**
	 * 单位ID
	 */
	@Corp(inject = "corpName")
	@BtCompositeSearch(fieldName = "ba.corp_id")
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
	 * 银行类型（银行类别）
	 */
	@BtCompositeSearch(fieldName = "bbt.id")
	private String bankType;
	//@BtCompositeSearch(fieldName = "bbt.id")
	private String bankTypeName;
	/**
	 * 借贷标志 0-收 1-支
	 */
	@Dict(dictGroup = "btBankAccDtl.cdSign",inject = "cdSignName")
	@BtCompositeSearch(fieldName = "t.cd_sign")
	private String cdSign;
	@BtCompositeSearch(fieldName = "t.cd_sign")
	private String cdSignName;
	/**
	 * 红蓝标志 1-红 2-蓝
	 */
	@Dict(dictGroup = "btBankAccDtl.rbSign",inject = "rbSignName")
	@BtCompositeSearch(fieldName = "t.rb_sign")
	private String rbSign;
	private String rbSignName;
	/**
	 * 币别ID
	 */
	@Currency(inject = "curName")
	@BtCompositeSearch(fieldName = "t.cur_id")
	private String curId;
	private String enCode;
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
	@Dict(tableName="BIS_ACC_ATTRIBUTE",valueFieldName="id",showFiledName="ATTRIBUTE_NAME",inject="attributeName")
	private String attributeId;
	//@BtCompositeSearch(fieldName = "acc.attribute_Id")
	private String attributeName;
	private String newWriteOffStatusName;

	/**
	 * 勾兑标志 0-未勾兑，1-已勾兑
	 */
	@BtCompositeSearch(fieldName = "t.BLEND_SIGN")
	@Dict(dictGroup = "paymentblending_status",inject = "blendSignName")
	private String blendSign;
	private String blendSignName;

	/**
	 *原单处理方式 1-不处理原单、2-生成新单、3-打回原单
	 */
	@BtCompositeSearch(fieldName = "t.REFUND_BLEND_TYPE")
	@Dict(dictGroup = "refundblending_handle",inject = "refundBlendTypeName")
	private String refundBlendType;
	private String refundBlendTypeName;

	/**
	 * 账户性质
	 */
	//@BtCompositeSearch(fieldName = "acc.nature_id")
	@Dict(tableName="BT_ACC_NATURE",valueFieldName="id",showFiledName="NATURE_NAME",inject="natureName")
	private String natureId;
	//@BtCompositeSearch(fieldName = "acc.nature_id")
	private String natureName;

	/**账户类别*/
	@Dict(dictGroup = "btBankAcc.accType", inject = "accTypeName")
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

	@BtCompositeSearch(fieldName = "t.tenant_id")
	private String tenantId;

	/**
	 * 核销状态
	 */
	@BtCompositeSearch(fieldName = "t.write_off_status")
	@Dict(dictGroup = "writeOff.status", inject = "writeOffStatusName")
	private String writeOffStatus;
	private String writeOffStatusName;
	/**
	 * 核销类型
	 */
	@BtCompositeSearch(fieldName = "t.write_off_type")
	@Dict(dictGroup = "writeOff.type", inject = "writeOffTypeName")
	private String writeOffType;
	private String writeOffTypeName;

	@BtCompositeSearch(fieldName = "case when t.WRITE_OFF_bal is not null then t.WRITE_OFF_bal else t.AMT end")
	private BigDecimal writeOffBal;

	/**
	 * 执行金额
	 */
	private BigDecimal executeMoney;
	/**交易日期*/
	@JsonFormat(pattern = "HH:mm:ss")

	@BtCompositeSearch(fieldName = "t.trans_time")
	private String transDateTime;
	private String customCol1;
	private String customCol2;
	private String customCol3;
	private String customCol4;
	private String customCol5;


	private BigDecimal newWriteOffBal;

	private String flag;
	/**
	 * 是否隐藏多选框 1为是
	 */
	private String checkboxHide;
	public String getCheckboxHide() {
		if(!"1".equals(this.getFlag())){
			return "1";
		}
		return checkboxHide;
	}
}
