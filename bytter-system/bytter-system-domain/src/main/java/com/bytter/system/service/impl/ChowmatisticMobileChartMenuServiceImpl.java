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
@Service("chowmatisticMobileChartMenuServiceI")
@DubboService(group = "chowmatisticMobileChartMenuServiceImpl")
public class ChowmatisticMobileChartMenuServiceImpl implements IMobileChartMenuService {
    /**
     * 银行账户查询菜单ID
     */
    private static String wealthManagementMenuId = "d5d0a5e42aa2408388ea4d50e3e4c64b";
    @Override
    public String getMobileChartModuleKey() {
        return MobileChartTopMenuEnum.chowmatistic.name();
    }
    @Override
    public String getMobileChartModuleName() {
        return MobileChartTopMenuEnum.chowmatistic.getName();
    }
    @Override
    public List<MobileChartMenuDTO> getMobileChartMenuList(Set<String> userHavePermissionMenuIds) {
        List<MobileChartMenuDTO> menuVoList = new ArrayList<>();
        if (userHavePermissionMenuIds.contains(wealthManagementMenuId)) {
            MobileChartMenuDTO bankAccountQuery = new MobileChartMenuDTO();
            bankAccountQuery.setMenuId(wealthManagementMenuId);
            bankAccountQuery.setName("理财投资类管理");
            bankAccountQuery.setDescription("理财投资类管理");
            bankAccountQuery.setUrl("/mobile/chowmatistic/choproductreqquery/mobileReq");
            bankAccountQuery.setIconUrl("/static/mobile/icon/chartPage/chowmatistic/icon_chart_chowmatistic_lccp@2x.png");
            menuVoList.add(bankAccountQuery);
        }
        return menuVoList;
    }
    @Override
    public List<MobileChartMenuDTO> getMobileChartTabMenuList(String menuId, Set<String> userHavePermissionMenuIds) {
        return null;
    }
    @Override
    public int getUserHavePermissionMenuSize(Set<String> userHavePermissionMenuIds) {
        int result = 0;
        if (userHavePermissionMenuIds.contains(wealthManagementMenuId)) {
            result++;
        }
        return result;
    }
}
