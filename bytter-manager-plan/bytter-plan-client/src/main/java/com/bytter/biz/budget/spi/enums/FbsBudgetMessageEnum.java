package com.bytter.biz.budget.spi.enums;
import com.bytter.core.enums.IMessageEnum;
/**
 * 资金计划消息枚举类
 * @author huangx
 * @since 2017/7/12
 */
public enum FbsBudgetMessageEnum implements IMessageEnum {
	/**该单位已上报过计划，不能初始化零计划！*/
	FBS_BUDGET_INPUT_MESSAGE_ZERO("01001"),
	/**该单位已上报过计划，不能取消！*/
	FBS_BUDGET_INPUT_MESSAGE_CANCEL("01002"),
    /** {0}科目或子科目代码已被使用，不能删除！*/
    FBS_BUDGET_ITEM_CODE_MESSAGE_EXISTS("02001"),
    /**该单位已上报过计划，不可修改科目！*/
    FBS_BUDGET_ITEM_CODE_MESSAGE_CORPID("03001"),
    /**下级单位已上报过计划，不可修改科目！*/
    FBS_BUDGET_ITEM_CODE_MESSAGE_CHILDRENCORPID("03002"),
    /**无下级单位不能设置汇总！*/
    FBS_BUDGET_ITEM_CODE_MESSAGE_CHILDRENCORPNULL("03003"),
    /**非计划控制单位，不允许初始化零计划！*/
    FBS_BUDGET_INPUT_MESSAGE_NOTCTLBUDGET("03004"),
    /**单位未分配计划科目,请与管理员联系！*/
    FBS_BUDGET_INPUT_MESSAGE_ITMECODE("03005"),
    /**单位计划科目超出上级单位科目范围*/
    FBS_BUDGET_ITEM_CODE_MESSAGE_CHILDRENCORPMOREPARENT("03006"),
    /**下级子单位已分配计划科目，请先调整下级单位科目*/
    FBS_BUDGET_ITEM_CODE_MESSAGE_ITEMALREADYTOCHILDREN("03007"),
    /**科目:{0} 下级科目存在有效科目，故此科目有效标识不可变更!*/
    FBS_BUDGET_ITEM_CODE_MESSAGE_SUB_VALIDSIGN("03008"),
    /**顶级网点不能设置成汇总单位*/
    FBS_BUDGET_CORP_MESSAGE_ISPARENT("03009"),
    /**只能释放状态为占用的执行金额*/
    FBS_BUDGETPROCESS_DELETE_STATUE_CHECK("04001"),
	/**下级{0}单位未分配计划科目！*/
	FBS_BUDGET_INPUT_MESSAGE_CHILD_CORP_ITMECODE("03005"),
	/**下级单位存在待审批的计划，不可提交！*/
	FBS_BUDGET_CHILD_COLLECT_CORP_STATUS("03005"),
    /**{0}单位已存在上报的计划，不可减少计划科目*/
    FBS_BUDGET_ITEM_CODE_MESSAGE_ALREADYEXIST("03010"),
    /**{0}单位已存在上报的计划，不可减少计划科目*/
    FBS_MESSAGE_DETAILMONEY_GT_ITEMBALANCE("03011"),
    /**{0}单位已存在上报的计划，不可减少计划科目*/
    FBS_MESSAGE_DETAILMONEY_GT_SUPERITEMBALANCE("03012"),
    /**{0}单位已存在上报的计划，不可减少计划科目*/
    FBS_MESSAGE_DETAILMONEY_GT_TOPITEMBALANCE("03013"),
    /**当前计划执行周期单位计划未上报或计划未编制完成！*/
    FBS_BUDGET_PROCESS_MESSAGE_NOTINPUTBUDGET("03014"),
    /**执行规则[×××]已存在，请重新输入!*/
    FBS_PROCESS_RULE_MESSAGE_EXIST_PROCESSRULE("03015"),
    /**优先级[×××]已存在，请重新输入!*/
    FBS_PROCESS_RULE_MESSAGE_EXIST_SORT("03016");
    FbsBudgetMessageEnum(String code) {
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
