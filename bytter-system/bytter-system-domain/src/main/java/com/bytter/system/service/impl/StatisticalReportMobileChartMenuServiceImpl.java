package com.bytter.system.service.impl;
import com.bytter.system.api.IMobileChartMenuService;
import com.bytter.system.api.dto.MobileChartMenuDTO;
import com.bytter.system.enums.MobileChartTopMenuEnum;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
/**
 * @author chenlw
 * @since 2020-06-22
 */
@Service("statisticalReportMobileChartMenuServiceI")
@DubboService(group = "statisticalReportMobileChartMenuServiceImpl")
public class StatisticalReportMobileChartMenuServiceImpl implements IMobileChartMenuService {
    /**
     * 综合查询-结算管理
     */
    private static String netBankMenuId = "07cbbb8132de427eb53c9d624fb7ee22";
    /**
     * 综合查询-信贷融资
     */
    private static String cmsMenuId = "b163200da8f44fb7807d33833ff684cb";
    /**
     * 综合查询-菜单
     */
    private static String integratedQueryMenuId = "959b8aaa99f743cfa41583fa9e986130";
    private static List<MobileChartMenuDTO> netBankMenuList = new ArrayList<>();
    private static List<MobileChartMenuDTO> cmsMenuList = new ArrayList<>();
    private static List<MobileChartMenuDTO> integratedQueryMenuList = new ArrayList<>();
    static {
        // 结算管理
        MobileChartMenuDTO menu1 = new MobileChartMenuDTO();
        menu1.setMenuId("91f7a32618b34cf49d0e8af883a2a24c");
        menu1.setName("资金调拔明细表");
        menu1.setUrl("/mobile/report/netbank/mobileAllocation");
        MobileChartMenuDTO menu2 = new MobileChartMenuDTO();
        menu2.setMenuId("2c39176770504791b6dbf5b30926223e");
        menu2.setName("资金余额统计表");
        menu2.setUrl("/mobile/report/fundsbalancestatistics/mobileBalance");
        netBankMenuList.add(menu1);
        netBankMenuList.add(menu2);
    }
    static {
        // 信贷融资
        MobileChartMenuDTO menu1 = new MobileChartMenuDTO();
        menu1.setMenuId("4e1173dc7432413eaffb25f46b13136c");
        menu1.setName("授信额度综合统计");
        menu1.setUrl("/mobile/report/cms/ratbkstatistics/mobileBkAmountRation");
        MobileChartMenuDTO menu2 = new MobileChartMenuDTO();
        menu2.setMenuId("c7dd8056aa1545cfa614121de49e44f4");
        menu2.setName("融资余额汇总表");
        menu2.setUrl("/mobile/report/cms/cmsbalancegather/gatherApp");
        cmsMenuList.add(menu1);
        cmsMenuList.add(menu2);
    }
    static {
        // 综合查询
        MobileChartMenuDTO menu1 = new MobileChartMenuDTO();
        menu1.setMenuId("cda30e0467214460bece03eec5fdbd2c");
        menu1.setName("资金存量汇总");
        menu1.setUrl("/mobile/bi/capitalStock/listNetMoblie");
        MobileChartMenuDTO menu2 = new MobileChartMenuDTO();
        menu2.setMenuId("2fbbd530dfbc4c0bafb5c2ca11e388fe");
        menu2.setName("到期票据分析");
        menu2.setUrl("/mobile/bill/accebill/soonExpireBill/mobile/list");
        MobileChartMenuDTO menu3 = new MobileChartMenuDTO();
        menu3.setMenuId("04193e665fa94e8382bd8a75695ea415");
        menu3.setName("到期贷款分析");
        menu3.setUrl("/mobile/report/cms/cmsExpireAnalysis/mobileExpireAnalysi");
        MobileChartMenuDTO menu4 = new MobileChartMenuDTO();
        menu4.setMenuId("1f270da3013e455987c94556e95f13e5");
        menu4.setName("贷款余额对比分析");
        menu4.setUrl("/mobile/report/cms/loanBalanceAnalysis/appList");
        MobileChartMenuDTO menu5 = new MobileChartMenuDTO();
        menu5.setMenuId("d60d9992fbcf47ff86c4cd082f1479d3");
        menu5.setName("贷款余额趋势分析");
        menu5.setUrl("/mobile/report/cms/loanTrend/mobileList");
        MobileChartMenuDTO menu6 = new MobileChartMenuDTO();
        menu6.setMenuId("2577f45666734a77a856198f62f1db20");
        menu6.setName("票据余额分析");
        menu6.setUrl("/mobile/bill/accebill/balCompare/balCompareApp");
        integratedQueryMenuList.add(menu1);
        integratedQueryMenuList.add(menu2);
        integratedQueryMenuList.add(menu3);
        integratedQueryMenuList.add(menu4);
        integratedQueryMenuList.add(menu5);
        integratedQueryMenuList.add(menu6);
    }
    @Override
    public String getMobileChartModuleKey() {
        return MobileChartTopMenuEnum.statisticalReport.name();
    }
    @Override
    public String getMobileChartModuleName() {
        return MobileChartTopMenuEnum.statisticalReport.getName();
    }
    @Override
    public List<MobileChartMenuDTO> getMobileChartMenuList(Set<String> userHavePermissionMenuIds) {
        List<MobileChartMenuDTO> menuVoList = new ArrayList<>();
        if (userHavePermissionMenuIds.contains(netBankMenuId)) {
            MobileChartMenuDTO menu1 = new MobileChartMenuDTO();
            menu1.setMenuId(netBankMenuId);
            menu1.setName("结算管理");
            menu1.setDescription("资金调拨明细表、资金余额统计表");
            menu1.setIconUrl("/static/mobile/icon/chartPage/icon_chart_netbank@2x.png");
            menu1.setJumpType("tab");
            menu1.setPageTitle("结算管理");
            menuVoList.add(menu1);
        }
        if (userHavePermissionMenuIds.contains(cmsMenuId)) {
            MobileChartMenuDTO menu1 = new MobileChartMenuDTO();
            menu1.setMenuId(cmsMenuId);
            menu1.setName("信贷融资");
            menu1.setDescription("授信额度综合统计、融资余额汇总");
            menu1.setIconUrl("/static/mobile/icon/chartPage/icon_chart_cms@2x.png");
            menu1.setJumpType("tab");
            menu1.setPageTitle("信贷融资");
            menuVoList.add(menu1);
        }
        if (userHavePermissionMenuIds.contains(integratedQueryMenuId)) {
            MobileChartMenuDTO menu1 = new MobileChartMenuDTO();
            menu1.setMenuId(integratedQueryMenuId);
            menu1.setName("综合查询");
            menu1.setDescription("资金存量汇总表、到期票据分析、到期贷款分析、贷款余额对比分析");
            menu1.setIconUrl("/static/mobile/icon/chartPage/icon_chart_intergrated_query@2x.png");
            menu1.setJumpType("tab");
            menu1.setPageTitle("综合查询");
            menuVoList.add(menu1);
        }
        return menuVoList;
    }
    @Override
    public List<MobileChartMenuDTO> getMobileChartTabMenuList(String menuId, Set<String> userHavePermissionMenuIds) {
        List<MobileChartMenuDTO> menuVoList = new ArrayList<>();
        if (netBankMenuId.equals(menuId)) {
            for (MobileChartMenuDTO MobileChartMenuDTO : netBankMenuList) {
                if (userHavePermissionMenuIds.contains(MobileChartMenuDTO.getMenuId())) {
                    menuVoList.add(MobileChartMenuDTO);
                }
            }
        }
        if (cmsMenuId.equals(menuId)) {
            for (MobileChartMenuDTO MobileChartMenuDTO : cmsMenuList) {
                if (userHavePermissionMenuIds.contains(MobileChartMenuDTO.getMenuId())) {
                    menuVoList.add(MobileChartMenuDTO);
                }
            }
        }
        if (integratedQueryMenuId.equals(menuId)) {
            for (MobileChartMenuDTO MobileChartMenuDTO : integratedQueryMenuList) {
                if (userHavePermissionMenuIds.contains(MobileChartMenuDTO.getMenuId())) {
                    menuVoList.add(MobileChartMenuDTO);
                }
            }
        }
        return menuVoList;
    }
    @Override
    public int getUserHavePermissionMenuSize(Set<String> userHavePermissionMenuIds) {
        int result = 0;
        if (userHavePermissionMenuIds.contains(netBankMenuId)) {
            for (MobileChartMenuDTO MobileChartMenuDTO : netBankMenuList) {
                if (userHavePermissionMenuIds.contains(MobileChartMenuDTO.getMenuId())) {
                    result++;
                }
            }
        }
        if (userHavePermissionMenuIds.contains(cmsMenuId)) {
            for (MobileChartMenuDTO MobileChartMenuDTO : cmsMenuList) {
                if (userHavePermissionMenuIds.contains(MobileChartMenuDTO.getMenuId())) {
                    result++;
                }
            }
        }
        if (userHavePermissionMenuIds.contains(integratedQueryMenuId)) {
            for (MobileChartMenuDTO MobileChartMenuDTO : integratedQueryMenuList) {
                if (userHavePermissionMenuIds.contains(MobileChartMenuDTO.getMenuId())) {
                    result++;
                }
            }
        }
        return result;
    }
}
