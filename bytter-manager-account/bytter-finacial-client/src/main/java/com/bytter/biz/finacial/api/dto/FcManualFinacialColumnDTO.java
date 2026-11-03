package com.bytter.biz.finacial.api.dto;
import lombok.Data;
@Data
public class FcManualFinacialColumnDTO implements java.io.Serializable{
	private String id;
	/**手工账类型表ID*/
	private String typeId;
	/**回写值类型 0常量 1程序提供选择*/
	private String writeBackValueType;
	/**回写数据表的表明*/
	private String writeBackTable;
	/**回写的字段名*/
	private String writeBackColumn;
	/**回写的数据对应的取值字段名*/
	private String writeBackValueColumn;
	/**与业务关联字段*/
	private String businessId;
}
