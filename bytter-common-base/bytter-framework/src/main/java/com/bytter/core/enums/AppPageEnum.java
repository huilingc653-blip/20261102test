package com.bytter.core.enums;
/**
 * APP分页枚举类
 *
 * @author zhoul
 */
public enum AppPageEnum {
    APPROVE_DETAIL("approveDetail"), NOTIFICATION("notification");
    private String page;
    AppPageEnum(String page) {
        this.page = page;
    }
    public String getPage() {
        return page;
    }
    public void setPage(String page) {
        this.page = page;
    }
}
