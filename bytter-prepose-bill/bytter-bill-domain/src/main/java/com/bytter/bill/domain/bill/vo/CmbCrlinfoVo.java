package com.bytter.bill.domain.bill.vo;

import com.bytter.core.annotation.BtCompositeSearch;
import lombok.Data;

/**
 * @Title: Entity
 * @Description: 开票登记VO
 * @author luop
 * @date 2017-06-21 18:00:00
 * @version 1.0
 *
 */
@SuppressWarnings("serial")
@Data
public class CmbCrlinfoVo implements java.io.Serializable {
	/**主键ID*/
	private String id;
	/**CVM额度编号*/
	private String creditlineid;
	/**文本协议编号*/
	private String contracttextno;
	/**授信类型*/
	private String creditlinetype;
	/**信贷额度编号*/
	private String crlnum;
}
