package com.bytter.check.enums;
import com.bytter.core.enums.IMessageEnum;
/**
 * 对账信息枚举类
 * @author liyh
 * @since 2017-7-6
 */
public enum TemplateTypeEnum implements IMessageEnum {
	/**
	 * 02 对账管理
	 * xx1 系统维护
	 * xx2 中心数据管理
	 * xx3 银行数据管理
	 * xx4 ERP数据管理
	 * xx5 对账
	 * xx6 对账查询
	 */
	/**数据中存在已结转的数据，不能重新生成*/
	TEMP_STATUS_TEMP("0","暂存"),
	/**凭证号不能为空*/
	TEMP_STATUS_SAVE("1","已提交");
	
	TemplateTypeEnum(String code,String name) {
		this.name = name;
        this.code = code;
    }
    private String code;
    
    private String name;
    public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
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
