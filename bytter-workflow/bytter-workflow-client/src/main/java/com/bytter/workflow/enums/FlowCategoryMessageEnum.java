package com.bytter.workflow.enums;
import com.bytter.core.enums.IMessageEnum;
public enum FlowCategoryMessageEnum implements IMessageEnum {
	/** KEY不能为空*/
	BPM_FLOW_CATEGORY_KEY_NOT_NULL("01001"),
	BPM_FLOW_ILL_FORMAT("01001"),
    BPM_FLOW_THIS_CORP_HAS_FLOW("00123"),
    /** 存在待审批的业务 */
    BPM_FLOW_DELETE_RUNNING_TASKS("00124"),
    BPM_FLOW_CATEGORY_NOT_EXISTS("01001"),
    /**
     * KEY已存在
     */
    BPM_FLOW_CATEGORY_KEY_EXISIS("01001");
    FlowCategoryMessageEnum(String code) {
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
