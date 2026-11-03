package com.bytter.system.enums;
/**
 * 移动端报表顶级菜单枚举类
 */
public enum MobileChartTopMenuEnum {
    netbank("4028802f5bf6b978015bf6bb00190000",
            "结算管理",
            "便捷的资金清结算",
            "/api/bankacc/appNetbankReportIndex",
            "/static/mobile/icon/chartModule/icon_fc_netbank@2x.png",
            "background: linear-gradient(#7AF9BB, #3AC48A);"),
    fbsBudget("402880275c8b05d6015c8b60ad1a0002",
            "资金计划",
            "灵活的资金计划管理",
            "/api/chart/appFbsBudgetReportIndex",
            "/static/mobile/icon/chartModule/icon_fc_fbsBudget@3x.png",
            "background: linear-gradient(#FFE186, #EFA32B);"),
    cms("28183155434f425e8ce436c2b19eb2fd",
            "信贷融资",
            "全面信贷融资业务管理",
            "/cms/appReport/reportList",
            "/static/mobile/icon/chartModule/icon_fc_cms@2x.png",
            "background: linear-gradient(#F4BE9A, #FE8739);"),
    chowmatistic("90f6219604ea4303a6b2c3d99d177d7d",
            "理财管理",
            "全局性内外投资业务管理",
            "/chowmatistic/choproductreqquery/appIndex",
            "/static/mobile/icon/chartModule/icon_fc_chowmatistic@2x.png",
            "background: linear-gradient(#68CBFF, #3590E9);"),
    statisticalReport("b007c9318c744e0193982c3a2bea432c",
            "统计报表",
            "完善的业务综合分析报表",
            "/api/chart/appStatisticalReportIndex",
            "/static/mobile/icon/chartModule/icon_statistical_report@2x.png",
            "background: linear-gradient(#68CBFF, #3590E9);");
//    accebill("4028802f5bf6b978015bf6bb00190000",
//            "承兑汇票",
//            "承兑汇票",
//            "/api/bankacc/appNetbankReportIndex",
//            "/static/mobile/icon/chartModule/icon_fc_netbank@2x.png",
//            "background: linear-gradient(#7AF9BB, #3AC48A);");
    private String menuId;
    private String name;
    private String description;
    private String url;
    private String iconUrl;
    private String iconStyle;
    public static void main(String[] args) {
        MobileChartTopMenuEnum[] datas = MobileChartTopMenuEnum.values();
        for (MobileChartTopMenuEnum data : datas) {
            System.out.println(data.name());
        }
    }
    MobileChartTopMenuEnum(String menuId, String name, String description, String url, String iconUrl, String iconStyle) {
        this.menuId = menuId;
        this.name = name;
        this.description = description;
        this.url = url;
        this.iconUrl = iconUrl;
        this.iconStyle = iconStyle;
    }
    public String getMenuId() {
        return menuId;
    }
    public void setMenuId(String menuId) {
        this.menuId = menuId;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getDescription() {
        return description;
    }
    public void setDescription(String description) {
        this.description = description;
    }
    public String getUrl() {
        return url;
    }
    public void setUrl(String url) {
        this.url = url;
    }
    public String getIconUrl() {
        return iconUrl;
    }
    public void setIconUrl(String iconUrl) {
        this.iconUrl = iconUrl;
    }
    public String getIconStyle() {
        return iconStyle;
    }
    public void setIconStyle(String iconStyle) {
        this.iconStyle = iconStyle;
    }
}
