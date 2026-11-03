package com.bytter.biz.finacial.api.dto;

import com.bytter.core.base.entity.IdEntity;
import lombok.Data;

import javax.persistence.Entity;
import java.util.Date;
/**
 * @Title: Entity
 * @Description: FC_BANK_ACC_DTL
 * @author onlineGenerator
 * @date 2019-5-13
 * @version 1.0
 *
 */
@Entity
@Data
@SuppressWarnings("serial")
public class FcBankAccDtlDTO extends IdEntity implements java.io.Serializable{
	/**流水ID**/
	private String dtlId;
	/**所属网点资金池**/
	private String poolId;
	/**所属单位**/
	private String corpId;
	/**流水号**/
	private String orderNo;
	/**凭证号**/
	private String voucherNo;
	/**全局I**/
	private String busGlobalId;
	/**记账人**/
	private String tallyCode;
	/***记账时间*/
	private Date tallyDate;
	/**复核人**/
	private String checkCode;
	/**复核时间**/
	private Date checkDate;
	/**记账状态
	 0：待记账（改账打回）;1：记账复核通过;2：记账无效;3：记账待复核;4：记账打回;5：记账暂存;8：自动记账失败**/
	private String tallyStatus;
}
