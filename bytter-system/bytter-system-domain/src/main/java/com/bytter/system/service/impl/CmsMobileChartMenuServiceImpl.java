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
@Service("cmsMobileChartMenuServiceI")
@DubboService(group = "cmsMobileChartMenuServiceImpl")
public class CmsMobileChartMenuServiceImpl implements IMobileChartMenuService {
    /**
     * 查询菜单
     */
    private static String queryMenuId = "8210f38c7eb045ba88578c3086ab434e";
    /**
     * 担保管理
     */
    private static String guaranteeManagementMenuId = "f6651ae1b18b4da18ccf5490fbd655d0";
    /**
     * 查询的子菜单列表
     */
    private static List<MobileChartMenuDTO> cmsQueryMenuList = new ArrayList<>();
    /**
     * 担保管理的子菜单列表
     */
    private static List<MobileChartMenuDTO> cmsGuaranteeManagementMenuList = new ArrayList<>();
    static {
        MobileChartMenuDTO bankLoanInquiry = new MobileChartMenuDTO();
        bankLoanInquiry.setMenuId("4d67739e49434b57bb7e5a0541624aae");
        bankLoanInquiry.setName("银行借款查询");
        bankLoanInquiry.setUrl("/mobile/cms/provide/loaninfo/loanMobileQuery");
        cmsQueryMenuList.add(bankLoanInquiry);
    }
    static {
        MobileChartMenuDTO bankLoanInquiry = new MobileChartMenuDTO();
        bankLoanInquiry.setMenuId("b66b6aa726c4481eaac7abba533bedde");
        bankLoanInquiry.setName("被担保合同查询");
        bankLoanInquiry.setUrl("/mobile/cms/guarantee/guaContractInfo/appReportlist");
        cmsGuaranteeManagementMenuList.add(bankLoanInquiry);
    }
    @Override
    public String getMobileChartModuleKey() {
        return MobileChartTopMenuEnum.cms.name();
    }
    @Override
    public String getMobileChartModuleName() {
        return MobileChartTopMenuEnum.cms.getName();
    }
    @Override
    public List<MobileChartMenuDTO> getMobileChartMenuList(Set<String> userHavePermissionMenuIds) {
        List<MobileChartMenuDTO> menuVoList = new ArrayList<>();
        if (userHavePermissionMenuIds.contains(queryMenuId)) {
            MobileChartMenuDTO menu1 = new MobileChartMenuDTO();
            menu1.setMenuId(queryMenuId);
            menu1.setName("查询");
            menu1.setDescription("信贷综合类业务查询");
            menu1.setIconUrl("/static/mobile/icon/chartPage/icon_chart_intergrated_query@2x.png");
            menu1.setJumpType("tab");
            menu1.setPageTitle("查询");
            menuVoList.add(menu1);
        }
        if (userHavePermissionMenuIds.contains(guaranteeManagementMenuId)) {
            MobileChartMenuDTO menu2 = new MobileChartMenuDTO();
            menu2.setMenuId(guaranteeManagementMenuId);
            menu2.setName("担保管理");
            menu2.setDescription("被担保合同查询");
            menu2.setIconUrl("/static/mobile/icon/chartPage/cms/icon_chart_cms_dbgl@2x.png");
            menu2.setJumpType("tab");
            menu2.setPageTitle("担保管理");
            menuVoList.add(menu2);
        }
        return menuVoList;
    }
    @Override
    public List<MobileChartMenuDTO> getMobileChartTabMenuList(String menuId, Set<String> userHavePermissionMenuIds) {
        List<MobileChartMenuDTO> menuVoList = new ArrayList<>();
        if (queryMenuId.equals(menuId)) {
            for (MobileChartMenuDTO MobileChartMenuDTO : cmsQueryMenuList) {
                if (userHavePermissionMenuIds.contains(MobileChartMenuDTO.getMenuId())) {
                    menuVoList.add(MobileChartMenuDTO);
                }
            }
        }
        if (guaranteeManagementMenuId.equals(menuId)) {
            for (MobileChartMenuDTO MobileChartMenuDTO : cmsGuaranteeManagementMenuList) {
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
        if (userHavePermissionMenuIds.contains(queryMenuId)) {
            for (MobileChartMenuDTO MobileChartMenuDTO : cmsQueryMenuList) {
                if (userHavePermissionMenuIds.contains(MobileChartMenuDTO.getMenuId())) {
                    result++;
                }
            }
        }
        if (userHavePermissionMenuIds.contains(guaranteeManagementMenuId)) {
            for (MobileChartMenuDTO MobileChartMenuDTO : cmsGuaranteeManagementMenuList) {
                if (userHavePermissionMenuIds.contains(MobileChartMenuDTO.getMenuId())) {
                    result++;
                }
            }
        }
        return result;
    }
}
