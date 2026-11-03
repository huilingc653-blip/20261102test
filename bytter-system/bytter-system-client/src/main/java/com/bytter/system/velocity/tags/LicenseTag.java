package com.bytter.system.velocity.tags;

import com.bytter.core.utils.ApplicationContextUtil;
import com.bytter.system.api.ILicenseService;
import org.apache.velocity.tools.config.DefaultKey;

/**
 * 证书许可验证标签。
 *
 * @author zhaohc
 * @date 2023/9/4 17:01
 */
@DefaultKey("licenseTag")
public class LicenseTag {
    private final ILicenseService licenseService;

    public LicenseTag() {
        this.licenseService = ApplicationContextUtil.getContext().getBean("licenseServiceI",ILicenseService.class);
    }

//    /**
//     * 判断是否有此菜单license授权
//     * @param menusId 菜单ID
//     * @return
//     */
//    public boolean isMenusAuth(String menusId){
//        return licenseService.isMenusAuth(menusId);
//    }

    /**
     * 判断是否有此模块的授权
     * @param moduleCode SysConfigConstant.Module模块常量
     * @return
     */
/*    public boolean isModuleAuth(String moduleCode){
        return licenseService.isModuleAuth(moduleCode);
    }*/

}
