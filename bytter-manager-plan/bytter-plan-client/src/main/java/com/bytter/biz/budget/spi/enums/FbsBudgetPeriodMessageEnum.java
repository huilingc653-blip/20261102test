package com.bytter.biz.budget.spi.enums;
import com.bytter.core.enums.IMessageEnum;
/**
 * 资金计划自定义周期消息枚举类
 * @author huangx
 * @since 2017/7/12
 */
public enum FbsBudgetPeriodMessageEnum implements IMessageEnum {
	/**自定义周保存出错 */
	FBS_BUDGET_PERIOD_MESSAGE_SAVE("01001"),
	/**上报金额不能为空*/
	FBS_BUDGET_DETAIL_MESSAGE_BMONEY_NULL("01002"),
	/**追加金额不能为空*/
	FBS_BUDGET_DETAIL_MESSAGE_BTEMPMONEY_NULL("01003"),
	/**归属日期不能为空*/
	FBS_BUDGET_DETAIL_MESSAGE_VESTINGDATE("01004"),
    /**归属日期应该属于当前计划周期*/
    FBS_BUDGET_DETAIL_MESSAGE_VESTINGDATE_ERROR("01005"),
    /**资金计划时间接口出错！*/
    FBS_BUDGET_TIME_INTERFACE_ERROR("01006");
	
	FbsBudgetPeriodMessageEnum(String code) {
        this.code = code;
    }
    private String code;
    public void setCode(String code) {
        this.code = code;
    }
    @SuppressWarnings("rawtypes")
	@Override
    public Enum getEnum() {
        return this;
    }
    @Override
    public String getCode() {
        return code;
    }
}
