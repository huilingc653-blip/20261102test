package com.bytter.check.enums;
import com.bytter.core.enums.IMessageEnum;
public enum DataExchangeMessageEnum implements IMessageEnum {
	/** 接口编码必须唯一 */
	EXCHANGE_MESSAGE_TYPECODE_REQUIRED_UNIQUE("11003"),
	/** 类型代码必须唯一 */
	EXCHANGE_MESSAGE_CODE_REQUIRED_UNIQUE("11002"),
	/** 数据类型出现异常，请检查配置项，重新配置 */
	EXCHANGE_MESSAGE_PARAMS("11001"),
	/** 该模板类型下存在模板，不允许删除！ */
	DATAEXCHANGE_MESSAGE_TEMPLATETYPE_ISNOTDEL("11004"),
	/** 该模板已被使用，不允许删除！ */
	DATAEXCHANGE_MESSAGE_TEMPLATE_ISNOTDEL("11005"),
    /** 导入数据中币别不存在！ */
    DATAEXCHANGE_MESSAGE_CURRENT_NOTEXISTS("11010");
	DataExchangeMessageEnum(String code) {
		this.code = code;
	}
	private String code;
	public void setCode(String code) {
		this.code = code;
	}
	@Override
	public Enum getEnum() {
		return this;
	}
	@Override
	public String getCode() {
		return code;
	}
}
