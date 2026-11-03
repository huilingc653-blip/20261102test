package com.bytter.system.service.impl;

import com.bytter.core.base.entity.IdEntity;
import com.bytter.system.api.constant.SysConfigConstant;
import com.bytter.system.dao.ConfigDao;
import com.bytter.system.dao.LicenseDao;
import com.bytter.system.entity.Config;
import com.bytter.system.entity.Menu;
import com.bytter.system.service.MenuService;
import com.bytter.system.api.constant.ConfigConstant;
import com.bytter.system.api.ILicenseService;
import com.bytter.core.utils.StringUtils;
import com.bytter.system.service.ModuleService;
import com.google.common.collect.Lists;
import jodd.typeconverter.Convert;
import org.apache.dubbo.config.annotation.DubboService;
import org.jetbrains.annotations.Nullable;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by zhoul on 2017/12/14.
 */
@Service("licenseServiceI")
@DubboService
public class LicenseServiceImpl implements ILicenseService {

    @Autowired
    private LicenseDao licenseDao;
    @Autowired
    private ConfigDao configDao;
    @Autowired
    @Lazy
    private MenuService menuService;

    @Autowired
    private ModuleService moduleService;

    @Override
    public String getLicenseType() {
        return getConfigValue("type");
    }

    /**
     * 获取系统许可配置信息。
     *
     * @param name 配置名称。
     * @return 配置值。
     */
    private String getConfigValue(String name) {
        Config config = configDao.getGlobalConfig(ConfigConstant.LICENSE, name);
        if (config == null || StringUtils.isBlank(config.getValue())) {
            return null;
        }
        return config.getValue();
    }

    @Override
    public String getCompanyNameByLicenseType(String locale) {
        if (locale.contains("zh")) {
            String companyName = getCompanyName();
            if (isTempLicense()) {
                // 读取国际化数据
                companyName = "测试许可 - " + companyName;
            }
            return companyName;
        } else {
            String companyName = companyNameForeign();
            if (isTempLicense()) {
                // 读取国际化数据
                companyName = "Test license - " + companyName;
            }
            return companyName;
        }
    }

    @Override
    public String getCompanyName() {
        return getConfigValue("companyName");
    }

    @Override
    public String companyNameForeign() {
        return getConfigValue("companyNameForeign");
    }

    @Override
    public Date getLicenseEndDate() {
        String endDate = getConfigValue("endDate");
        return Convert.toDate(endDate);
    }

    @Override
    public boolean isExpire() {
        long now = System.currentTimeMillis();
        long endDate = getLicenseEndDate().getTime();
        return now > endDate;
    }

    @Transactional(readOnly = true)
    @Override
    public boolean isReadyExpire() {
        long endDate = getLicenseEndDate().getTime();
        Calendar date = Calendar.getInstance();
        int amount = 30;
        date.setTime(new Date());
        Config config = configDao.getGlobalConfig(ConfigConstant.SYS, "licenceOverNoticeDay");
        if (null != config && null != config.getValue()) {
            amount = new Integer(config.getValue());
        }
        date.add(Calendar.DAY_OF_MONTH, amount);
        return date.getTime().getTime() > endDate;
    }

    @Override
    public boolean isTempLicense() {
        String licenseType = getLicenseType();
        return ILicenseService.LICENSE_TYPE_TEMP.equals(licenseType);
    }

    @Override
    public boolean isFinalLicense() {
        String licenseType = getLicenseType();
        return ILicenseService.LICENSE_TYPE_FINAL.equals(licenseType);
    }

    @Override
    public boolean isAllowToUseApp() {
        List<String> flag = getMobileConfig();
        if (flag == null) return false;
        return flag.contains("0");
    }

    @Override
    public boolean isAllowToUseMobilePage() {
        List<String> flag = getMobileConfig();
        if (flag == null) return false;
        return flag.contains("1");
    }

    private List<String> getMobileConfig() {
        String mobile = getConfigValue("mobile");
        if (mobile == null) {
            return null;
        }
        return Arrays.asList(mobile.split(","));
    }

    @Override
    public boolean isControlRegister() {
        return ILicenseService.LICENSE_CONTROL_TYPE_REGISTER.equals(getControlUserType());
    }

    @Override
    public String getControlUserType() {
        return getConfigValue("controlUserType");
    }

    @Override
    public Integer getControlUserNumber() {
        return Convert.toInteger(getConfigValue("controlUserNumber"));
    }

    @Override
    public Integer getNetNumber() {
        return Convert.toInteger(getConfigValue("netNumber"));
    }

    @Override
    public Integer getCorpNumber() {
        return Convert.toInteger(getConfigValue("corpNumber"));
    }

    @Override
    public Integer getBankAccountNumber() {
        return Convert.toInteger(getConfigValue("bankAccountNumber"));
    }

    @Override
    public List<String> getMenus() {
        // 查询 菜单表
        return menuService.listAll().stream().map(IdEntity::getId).collect(Collectors.toList());
    }

    public List<String> getModules() {
        return moduleService.listAll().stream().map(IdEntity::getId).collect(Collectors.toList());
    }

    /*@Override
    public boolean isMenusAuth(String menusId) {
        if (StringUtils.isEmpty(menusId)) {
            return false;
        } else if (ILicenseService.LICENSE_TYPE_DEVELOP.equalsIgnoreCase(this.getLicenseType())) {
            return true;
        }
        boolean licenseMenu = getMenus().contains(menusId);
        // 单license未授权时判断是否二开菜单是则返回true
        if (!licenseMenu) {
            Menu menu = menuService.getMenuById(menusId);
            if (menu != null
                    && "1".equals(String.valueOf(menu.getIsExtend()))) {
                licenseMenu = true;
            }
        }
        return licenseMenu;
    }*/

    /*@Override
    public List<String> listMenusAuth(List<String> menuIdList) {
        if (ILicenseService.LICENSE_TYPE_DEVELOP.equalsIgnoreCase(this.getLicenseType())) {
            return menuIdList;
        }
        List<String> resultList = Lists.newArrayList();
        final List<String> menusList = getMenus();
        menuIdList.forEach(menuId -> {
            boolean licenseMenu = menusList.contains(menuId);
            // 单license未授权时判断是否二开菜单是则返回true
            if (!licenseMenu) {
                Menu menu = menuService.getMenuById(menuId);
                if (menu != null && "1".equals(String.valueOf(menu.getIsExtend()))) {
                    resultList.add(menuId);
                }
            } else {
                resultList.add(menuId);
            }
        });
        return resultList;
    }*/

    // @Override
    // public boolean isModuleAuth(String moduleCode) {
    //     String menusId = SysConfigConstant.MODULETOMENUID.get(moduleCode);
    //     return isMenusAuth(menusId);
    // }
}
