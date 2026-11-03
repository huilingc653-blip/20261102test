package com.bytter.supply.enums;

/**
 * @author ztd
 * @description 单据状态枚举
 * @since 2026/7/1
 */
public enum ApprovalStatusEnum {
    DRAFT("draft", "暂存"),
    APPROVAL_ING("approval-ing", "审批中"),
    REJECTED("rejected", "打回"),
    APPROVAL_PASS("approval-pass", "审批通过"),
    CANCELLED("cancelled", "已作废");

    private final String code;
    private final String desc;

    ApprovalStatusEnum(String code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public String getCode() {
        return code;
    }

    public String getDesc() {
        return desc;
    }

    public static ApprovalStatusEnum getByCode(String code) {
        for (ApprovalStatusEnum e : values()) {
            if (e.getCode().equals(code)) {
                return e;
            }
        }
        return null;
    }
}
