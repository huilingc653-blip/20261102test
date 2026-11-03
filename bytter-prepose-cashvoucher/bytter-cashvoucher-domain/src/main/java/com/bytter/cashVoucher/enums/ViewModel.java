package com.bytter.cashVoucher.enums;
/**
 * 凭证类型数据来源所属模块。
 *
 * @author zhaohc
 * @date 2023/4/13 10:33
 */
public enum ViewModel {
    VIEW_NIS("结算模块"),
    VIEW_BILL("票据模块"),
    VIEW_PAYEE("凭证模块"),
    ;
    private final String desc;
    ViewModel(String desc){
        this.desc = desc;
    }
    public String getDesc() {
        return desc;
    }
}
