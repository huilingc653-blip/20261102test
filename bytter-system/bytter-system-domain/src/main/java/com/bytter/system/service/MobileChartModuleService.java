package com.bytter.system.service;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import com.bytter.system.api.IMobileChartMenuService;
import com.bytter.system.api.dto.MobileChartMenuDTO;
import com.bytter.system.dao.UserMobileChartModuleDao;
import com.bytter.system.entity.UserMobileChartModule;
import com.bytter.system.enums.MobileChartTopMenuEnum;
/**
 * @author chenlw
 * @since 2020-05-12
 */
@Service("mobileChartModuleService")
@Scope(proxyMode = ScopedProxyMode.TARGET_CLASS)
public class MobileChartModuleService {
    @Autowired
    private UserMobileChartModuleDao userMobileChartModuleDao;
    @Autowired
    private List<IMobileChartMenuService> mobileChartMenuServiceList;
    @Autowired
    //@Qualifier("menuService")
    private MenuService menuService;
    /**
     * 获取用户可以看到的APP报表模块列表
     * 报表模块显示策略:
     * - 用户-报表模块关联数据不为空时，根据用户-报表模块关联数据显示报表模块。
     * - 用户-报表模块关联数据为空时，默认隐藏全部的报表模块列表。
     * - 当项目后续新增报表模块时，新增的模块不显示，需要用户在APP端【新增模块】页面将其置为显示状态方能显示。
     * - 要根据菜单权限进行控制。
     *
     * @param userId 用户ID
     * @return APP报表模块列表
     */
    public List<Map<String, Object>> getShowMobileChartTopMenuEnumList(String userId) {
        List<Map<String, Object>> showDataList = new ArrayList<>();
        List<UserMobileChartModule> userMobileChartModuleList = userMobileChartModuleDao.getUserMobileChartModuleList(userId);
        if (CollectionUtils.isEmpty(userMobileChartModuleList)) {
            return showDataList;
        }
        List<MobileChartTopMenuEnum> userHavePermissionMenuEnumList = new ArrayList<>();
        // 获取顶级菜单
        MobileChartTopMenuEnum[] chartTopMenuEnums = MobileChartTopMenuEnum.values();
        // 查找出用户可查看的报表菜单ID
        Set<String> userCanViewMenuIdSet = new HashSet<>();
        List<String> enableMenuIdList = menuService.listEnableMenuIdByUserId(userId);
        // 用户有权限的菜单ID集合
        for (MobileChartTopMenuEnum chartTopMenuEnum : chartTopMenuEnums) {
            for (String menuId : enableMenuIdList) {
                if (chartTopMenuEnum.getMenuId().equals(menuId)) {
                    userCanViewMenuIdSet.add(chartTopMenuEnum.getMenuId());
                    break;
                }
            }
        }
        for (MobileChartTopMenuEnum chartTopMenuEnum : chartTopMenuEnums) {
            if (!userCanViewMenuIdSet.contains(chartTopMenuEnum.getMenuId())) {
                continue;
            }
            boolean showFlag = false;
            for (UserMobileChartModule userMobileChartModule : userMobileChartModuleList) {
                if (chartTopMenuEnum.name().equals(userMobileChartModule.getChartModuleKey())
                        && userMobileChartModule.getStatus() != null
                        && userMobileChartModule.getStatus() == 0) {
                    showFlag = true;
                }
            }
            if (showFlag) {
                userHavePermissionMenuEnumList.add(chartTopMenuEnum);
            }
        }
        Set<String> userHavePermissionMenuIdSet = new HashSet<>(enableMenuIdList);
        for (MobileChartTopMenuEnum mobileChartTopMenuEnum : userHavePermissionMenuEnumList) {
            IMobileChartMenuService mobileChartMenuService = getMobileChartMenuService(mobileChartTopMenuEnum.name());
            if (mobileChartMenuService != null && mobileChartMenuService.getUserHavePermissionMenuSize(userHavePermissionMenuIdSet) > 0) {
                Map<String, Object> mobileChart = mobileChartTopMenuEnum2Map(mobileChartTopMenuEnum);
                showDataList.add(mobileChart);
            }
        }
        return showDataList;
    }
    public String getMobileChartModuleName(String chartModuleKey) {
        IMobileChartMenuService mobileChartMenuService = getMobileChartMenuService(chartModuleKey);
        if (mobileChartMenuService != null) {
            return mobileChartMenuService.getMobileChartModuleName();
        }
        return "报表";
    }
    public List<MobileChartMenuDTO> getMobileChartMenuList(String userId, String chartModuleKey) {
        IMobileChartMenuService mobileChartMenuService = getMobileChartMenuService(chartModuleKey);
        if (mobileChartMenuService != null) {
            List<String> enableMenuIdList = menuService.listEnableMenuIdByUserId(userId);
            return mobileChartMenuService.getMobileChartMenuList(new HashSet<>(enableMenuIdList));
        }
        return new ArrayList<>();
    }
    public List<MobileChartMenuDTO> getMobileChartTabMenuList(String userId, String menuId, String chartModuleKey) {
        IMobileChartMenuService mobileChartMenuService = getMobileChartMenuService(chartModuleKey);
        if (mobileChartMenuService != null) {
            List<String> enableMenuIdList = menuService.listEnableMenuIdByUserId(userId);
            return mobileChartMenuService.getMobileChartTabMenuList(menuId, new HashSet<>(enableMenuIdList));
        }
        return new ArrayList<>();
    }
    private IMobileChartMenuService getMobileChartMenuService(String chartModuleKey) {
        for (IMobileChartMenuService mobileChartMenuService : mobileChartMenuServiceList) {
            if (mobileChartMenuService.getMobileChartModuleKey().equals(chartModuleKey)) {
                return mobileChartMenuService;
            }
        }
        return null;
    }
    private Map<String, Object> mobileChartTopMenuEnum2Map(MobileChartTopMenuEnum mobileChartTopMenuEnum) {
        Map<String, Object> mobileChart = new HashMap<>();
        mobileChart.put("chartModuleKey", mobileChartTopMenuEnum.name());
        mobileChart.put("menuId", mobileChartTopMenuEnum.getMenuId());
        mobileChart.put("name", mobileChartTopMenuEnum.getName());
        mobileChart.put("description", mobileChartTopMenuEnum.getDescription());
        mobileChart.put("url", mobileChartTopMenuEnum.getUrl());
        mobileChart.put("iconUrl", mobileChartTopMenuEnum.getIconUrl());
        mobileChart.put("iconStyle", mobileChartTopMenuEnum.getIconStyle());
        mobileChart.put("pageTitle", mobileChartTopMenuEnum.getName());
        return mobileChart;
    }
    /**
     * 获取用户有权限查看的APP报表模块列表
     *
     * @return 用户有权限查看的APP报表模块列表
     */
    public List<Map<String, Object>> getUserHavePermissionMobileChartModuleList(String userId) {
        List<Map<String, Object>> showDataList = new ArrayList<>();
        Set<String> userHavePermissionMenuIdSet = menuService.getEnableMenuIdSetByUserId(userId);
        // 获取顶级菜单
        MobileChartTopMenuEnum[] chartTopMenuEnums = MobileChartTopMenuEnum.values();
        for (MobileChartTopMenuEnum mobileChartTopMenuEnum : chartTopMenuEnums) {
            if (!userHavePermissionMenuIdSet.contains(mobileChartTopMenuEnum.getMenuId())) {
                continue;
            }
            IMobileChartMenuService mobileChartMenuService = getMobileChartMenuService(mobileChartTopMenuEnum.name());
            if (mobileChartMenuService != null && mobileChartMenuService.getUserHavePermissionMenuSize(userHavePermissionMenuIdSet) > 0) {
                Map<String, Object> mobileChart = mobileChartTopMenuEnum2Map(mobileChartTopMenuEnum);
                showDataList.add(mobileChart);
            }
        }
        return showDataList;
    }
    /**
     * 获取用户的报表模块资源数据
     *
     * @param userId 用户ID
     * @return 用户的报表模块资源数据
     */
    public Object getUserMobileChartModuleResource(String userId) {
        Map<String, Object> userResourceMap = new HashMap<>();
        List<UserMobileChartModule> userMobileChartModuleList = userMobileChartModuleDao.getUserMobileChartModuleList(userId);
        userResourceMap.put("userMobileChartModuleList", userMobileChartModuleList);
        userResourceMap.put("mobileChartModuleList", getUserHavePermissionMobileChartModuleList(userId));
        return userResourceMap;
    }
    /**
     * 保存 用户-报表模块的状态信息
     *
     * @param userMobileChartModuleList
     */
    public void saveUserMobileChartModuleResource(List<UserMobileChartModule> userMobileChartModuleList) {
        userMobileChartModuleDao.saveUserMobileChartModuleResource(userMobileChartModuleList);
    }
}
