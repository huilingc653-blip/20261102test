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
public class CmbBnkbilnegVo implements java.io.Serializable {
	/**主键ID*/
	private String id;
	/**银承合作协议编号*/
	@BtCompositeSearch(fieldName="textno")
	private String textno;
	/**银承合作协议分行名称*/
	@BtCompositeSearch(fieldName="orgname")
	private String orgname;
	/**银承合作协议分行号*/
	@BtCompositeSearch(fieldName="brnnbr")
	private String brnnbr;
}
