package com.bytter.biz.bdlink.vo;
import com.bytter.core.annotation.BtCompositeSearch;
import com.bytter.core.annotation.Corp;
import com.bytter.core.annotation.Currency;
import com.bytter.core.annotation.Dict;
import com.bytter.core.utils.EmptyUtils;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
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

	@BtCompositeSearch(fieldName = "t.id")
	private String dtlId;
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
	@Corp(inject = "corpName")
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
	 * 收入金额
	 */
	@NumberFormat(pattern = "#,##0.00")
	@BtCompositeSearch(fieldName="t.amt")
	private java.math.BigDecimal amtIn;

	/**
	 * 支出金额
	 */
	@NumberFormat(pattern = "#,##0.00")
	@BtCompositeSearch(fieldName="t.amt")
	private java.math.BigDecimal amtOut;
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
	 * 付款方式
	 */
	private String settlType;
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
	 * 备注
	 */
	private java.lang.String rmk;
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
	@BtCompositeSearch(fieldName = "t.bus_type")
	private String busType;
	/**
	 * 票据认领中间表id
	 */
	private String claimId;

	@BtCompositeSearch(fieldName = "t.PAYMENTBLENDING_DATE")
	private Date paymentBlendingDate;

	@BtCompositeSearch(fieldName = "t.REFUND_BLEND_TIME")
	private Date refundBlendTime;
	/**
	 * 账户组
	 */
	private String fcAccGroupCode;
	private String fcAccGroupName;

	/**
	 * 推送erp状态
	 */
	private String syncStatus;

	/**电子回单or历史电子回单 0电子回单 1历史电子回单**/
	private String receiptFlag;

	/**关联回单数量**/
	private Integer receiptNum;

	/**
	 * 关联电子回单id
	 */
	private String receiptId;
}
