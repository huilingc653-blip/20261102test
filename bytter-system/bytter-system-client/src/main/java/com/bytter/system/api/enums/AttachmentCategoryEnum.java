package com.bytter.system.api.enums;
/**
 * 附件分类枚举 不支持带点到斜杠划分目录
 * Created by zhouj on 2017/5/31.
 */
public enum AttachmentCategoryEnum {
    CATEGORY_BILL("bill"),
    CATEGORY_TTPAY("ttpay"),
    CATEGORY_POOL("pool"),
    CATEGORY_FINACIAL("finacial"),
    CATEGORY_COMMON("common"),
    CATEGORY_NETBANK("netbank");
    private String name;
    public String getName() {
        return name;
    }
    AttachmentCategoryEnum(String name) {
        this.name = name;
    }
    public static AttachmentCategoryEnum getAttachmentEnum(String code) {
        for (AttachmentCategoryEnum c : AttachmentCategoryEnum.values()) {
            if (c.getName().equals(code)) {
                return c;
            }
        }
        return null;
    }
}
