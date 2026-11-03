package com.bytter.tally.entity;
import com.bytter.core.base.entity.IdEntity;
import lombok.Data;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
/**
 * @Title: Entity
 * @Description: BT_NET_CHALK
 * @author onlineGenerator
 * @date 2017-07-04 14:58:22
 * @version 1.0
 *
 */
@SuppressWarnings("serial")
@Data
public class TallyBtNetChalk extends IdEntity implements java.io.Serializable {
	/**交易日期*/
	private java.util.Date transTime;
	/**单位代码*/
	private String corpId;
	/**收支标识*/
	private String cdSign;
	/**币别代码*/
	private String curId;
	/**本方账号ID*/
	private String bankAccId;
	/**本方账号*/
	private String bankAcc;
	/**本方账号开户行*/
	private String bankName;
	/**本方账号名*/
	private String accName;
	/**本方内部账号*/
	private String cntAccount;
	/**对方账号*/
	private String oppAccNo;
	/**对方账号开户行*/
	private String oppBankName;
	/**对方账户名*/
	private String oppAccName;
	/**摘要*/
	private String purpose;
	/**金额*/
	private java.math.BigDecimal money;
	/**状态*/
	private String estate;
	/**记账员代码*/
	private String operatorCode;
	/**记账日期*/
	private java.util.Date tallyDate;
	/**流水号*/
	private String orderNo;
	/**凭证号*/
	private String voucherNo;
	/**复核员代码*/
	private String checkerCode;
	/**复核日期*/
	private java.util.Date checkTime;
	/**银行流水号*/
	private String bankSerial;
	/**取消原因*/
	private String refuseWhy;
	/**是否勾兑标识*/
	private String blend;
	/**退款勾兑原因*/
	private String blendInfo;
	/**创建时间*/
	private java.util.Date createTime;
	/**创建用户代码*/
	private String createBy;
	/**最后更新时间*/
	private java.util.Date updateTime;
	/**最后更新用户代码*/
	private String updateBy;
	/**账务表ID*/
	private String bookId;
	/**全局业务id*/
	private String busGlobalId;
	/**待记账提醒*/
	private String remindSign;
	/**自动记账标识**/
	private String autoTallySign;
	/**
	 * 新增 corp_type 冗余字段
	 */
	private Integer corpType;
}
