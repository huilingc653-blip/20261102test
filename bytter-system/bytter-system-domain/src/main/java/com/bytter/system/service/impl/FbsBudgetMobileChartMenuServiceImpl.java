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
@Service("fbsBudgetMobileChartMenuServiceI")
@DubboService(group = "fbsBudgetMobileChartMenuServiceImpl")
public class FbsBudgetMobileChartMenuServiceImpl implements IMobileChartMenuService {
    private static String planUpMenuId = "515fa929b150477b8b8efc38b1a7a77e";
    private static String planOperationMenuId = "eae390a359ce4fe69c8cc4eef0407df2";
    @Override
    public String getMobileChartModuleKey() {
        return MobileChartTopMenuEnum.fbsBudget.name();
    }
    @Override
    public String getMobileChartModuleName() {
        return MobileChartTopMenuEnum.fbsBudget.getName();
    }
    @Override
    public List<MobileChartMenuDTO> getMobileChartMenuList(Set<String> userHavePermissionMenuIds) {
        List<MobileChartMenuDTO> menuVoList = new ArrayList<>();
        if (userHavePermissionMenuIds.contains(planUpMenuId)) {
            MobileChartMenuDTO planUp = new MobileChartMenuDTO();
            planUp.setMenuId(planUpMenuId);
            planUp.setName("计划上报");
            planUp.setDescription("上报情况汇总表");
            planUp.setIconUrl("/static/mobile/icon/chartPage/fbsBudget/icon_chart_fbsBudget_jhsb@2x.png");
            planUp.setJumpType("tab");
            planUp.setPageTitle("计划上报");
            menuVoList.add(planUp);
        }
        if (userHavePermissionMenuIds.contains(planOperationMenuId)) {
            MobileChartMenuDTO planOperation = new MobileChartMenuDTO();
            planOperation.setMenuId(planOperationMenuId);
            planOperation.setName("计划执行");
            planOperation.setDescription("执行情况汇总表");
            planOperation.setIconUrl("/static/mobile/icon/chartPage/fbsBudget/icon_chart_fbsBudget_jhzx@2x.png");
            planOperation.setJumpType("tab");
            planOperation.setPageTitle("计划执行");
            menuVoList.add(planOperation);
        }
        return menuVoList;
    }
    @Override
    public List<MobileChartMenuDTO> getMobileChartTabMenuList(String menuId, Set<String> userHavePermissionMenuIds) {
        List<MobileChartMenuDTO> menuVoList = new ArrayList<>();
        if (planUpMenuId.equals(menuId)) {
            MobileChartMenuDTO menuVo = new MobileChartMenuDTO();
            menuVo.setName("计划上报");
            menuVo.setUrl("/mobile/budget/fbsbudget/mobileReqInput");
            menuVoList.add(menuVo);
        } else if (planOperationMenuId.equals(menuId)) {
            MobileChartMenuDTO menuVo = new MobileChartMenuDTO();
            menuVo.setName("计划执行");
            menuVo.setUrl("/mobile/budget/fbsbudget/mobileReqProess");
            menuVoList.add(menuVo);
        }
        return menuVoList;
    }
    @Override
    public int getUserHavePermissionMenuSize(Set<String> userHavePermissionMenuIds) {
        int result = 0;
        if (userHavePermissionMenuIds.contains(planUpMenuId)) {
            result++;
        }
        if (userHavePermissionMenuIds.contains(planOperationMenuId)) {
            result++;
        }
        return result;
    }
}
