package com.bytter.cashVoucher.enums;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
/**
 * 凭证类型打印查询视图枚举。
 *
 * @author zhaohc
 * @date 2023/4/13 10:26
 */
public enum VoucherViewEnum {
    /* 结算模块的数据 */
    VIEW_NIS_TELE_BILL("电汇", "view_nis_tele_bill", "",ViewModel.VIEW_NIS),
    VIEW_NIS_CASH_BILL("现金支票", "view_nis_cash_bill", "",ViewModel.VIEW_NIS),
    VIEW_NIS_TRAN_BILL("转账支票", "view_nis_tran_bill", "",ViewModel.VIEW_NIS),
    /* 票据模块的数据 */
    V_BILL_MERCHANTS_INFO("承兑汇票商票", "v_bill_merchants_info", "ab_voucherinfo",ViewModel.VIEW_BILL),
    /* 凭证模块的数据 */
    VIEW_PAYEE_CHEQUE("支票托收进账单", "view_payee_cheque", "bv_payee_cheque",ViewModel.VIEW_PAYEE),
    ;
    private final String desc;
    private final String view;
    private final String table;
    private final ViewModel model;
    VoucherViewEnum(String desc, String view, String table, ViewModel model) {
        this.desc = desc;
        this.view = view;
        this.table = table;
        this.model = model;
    }
    public String getDesc() {
        return desc;
    }
    public String getView() {
        return view;
    }
    public ViewModel getModel() {
        return model;
    }
    public String getTable() {
        return table;
    }
    public static VoucherViewEnum getEnumByView(String view) {
        List<VoucherViewEnum> collect = Arrays.stream(VoucherViewEnum.values()).
                filter(voucher -> StrUtil.equals(view, voucher.view)).collect(Collectors.toList());
        return ObjectUtil.isNotEmpty(collect) ? collect.get(0) : null;
    }
    public static ViewModel getModelByView(String view) {
        VoucherViewEnum viewEnum = getEnumByView(view);
        return ObjectUtil.isNotEmpty(viewEnum) ? viewEnum.model : null;
    }
}
