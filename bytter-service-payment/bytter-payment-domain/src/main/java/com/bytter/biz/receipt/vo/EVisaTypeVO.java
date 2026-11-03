package com.bytter.biz.receipt.vo;
import java.io.Serializable;
import com.bytter.core.annotation.BtCompositeSearch;
import com.bytter.core.annotation.Corp;
import lombok.Data;
/**
 * @author zhangxb
 * @date 2017/7/4.
 */
@Data
public class EVisaTypeVO implements Serializable{
	private String id;
	/**网点*/
	@Corp(inject="netName")
	@BtCompositeSearch(fieldName = "net_id")
	private java.lang.String netId;
	/**网点名称*/
	private java.lang.String netName;
	/**回单类型*/
	@BtCompositeSearch(fieldName = "bill_type")
	private java.lang.String billType;
	@BtCompositeSearch(fieldName = "bill_type")
	private java.lang.String billTypeName;
	/**回单类型名称*/
	@BtCompositeSearch(fieldName = "bill_name")
	private java.lang.String billName;
	/**签章图片名称*/
	private java.lang.String fileName;
	/**路径*/
	private java.lang.String dir;
	/**宽度*/
	@BtCompositeSearch(fieldName = "pic_width")
	private java.lang.String picWidth;
	/**高度*/
	@BtCompositeSearch(fieldName = "pic_height")
	private java.lang.String picHeight;
	/**有效标志 0，否 1，是*/
	@BtCompositeSearch(fieldName = "valid_sign")
	private java.lang.String validSign;
	/**有效标志 0，否 1，是*/
	@BtCompositeSearch(fieldName = "valid_sign")
	private java.lang.String validSignName;
	/**创建人登录名称*/
	private java.lang.String createBy;
	/**创建日期*/
	private java.util.Date createDate;
	/**更新人登录名称*/
	private java.lang.String updateBy;
	/**更新日期*/
	private java.util.Date updateDate;
	
}
