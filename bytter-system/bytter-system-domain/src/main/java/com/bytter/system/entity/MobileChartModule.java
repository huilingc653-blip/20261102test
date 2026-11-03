package com.bytter.system.entity;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
/**
 * APP报表模块实体类
 *
 * @author chenlw
 * @since 2020-05-12
 */
public class MobileChartModule implements Serializable {
    public static List<MobileChartModule> mobileChartModuleList;
    static {
        mobileChartModuleList = new ArrayList<>();
        MobileChartModule statisticalReport = new MobileChartModule();
        statisticalReport.setKey("statisticalReport");
        statisticalReport.setName("统计报表");
        statisticalReport.setDescription("网点票据划拨、资金存量、资金余额");
        statisticalReport.setUrl("/report/fundsbalancestatistics/mobileBalanceList");
        statisticalReport.setIconUrl("/static/mobile/icon/tongjibaobiao.png");
        statisticalReport.setIconStyle("background: linear-gradient(#68CBFF, #3590E9);");
        MobileChartModule fbsBudget = new MobileChartModule();
        fbsBudget.setKey("fbsBudget");
        fbsBudget.setName("资金计划");
        fbsBudget.setDescription("资金上报情况表、资金执行情况表");
        fbsBudget.setUrl("/report/fundsbalancestatistics/mobileBalanceList");
        fbsBudget.setIconUrl("/static/mobile/icon/icon_fc_fbsBudget.png");
        fbsBudget.setIconStyle("background: linear-gradient(#FFE186, #EFA32B);");
        MobileChartModule netbank = new MobileChartModule();
        netbank.setKey("netbank");
        netbank.setName("结算管理");
        netbank.setDescription("银行账户查询、银行账户余额查询、银行账户流水查询");
        netbank.setUrl("/report/fundsbalancestatistics/mobileBalanceList");
        netbank.setIconUrl("/static/mobile/icon/icon_fc_netbank.png");
        netbank.setIconStyle("background: linear-gradient(#7AF9BB, #3AC48A);");
        mobileChartModuleList.add(statisticalReport);
        mobileChartModuleList.add(fbsBudget);
        mobileChartModuleList.add(netbank);
    }
    public MobileChartModule() {
    }
    public MobileChartModule(String key, String name, String url, String iconUrl, String description) {
        this.key = key;
        this.name = name;
        this.url = url;
        this.iconUrl = iconUrl;
        this.description = description;
    }
    private String key;
    private String name;
    private String url;
    private String iconUrl;
    private String iconStyle;
    private String description;
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getKey() {
        return key;
    }
    public void setKey(String key) {
        this.key = key;
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
    public String getDescription() {
        return description;
    }
    public void setDescription(String description) {
        this.description = description;
    }
}
