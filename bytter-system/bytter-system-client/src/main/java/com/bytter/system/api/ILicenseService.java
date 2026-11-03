package com.bytter.system.api;

import java.util.Date;
import java.util.List;

/**
 * 证书许可接口
 * Created by zhoul on 2017/12/14.
 */
public interface ILicenseService {
    /**
     * 临时许可
     */
    String LICENSE_TYPE_TEMP = "T";
    /**
     * 正式许可
     */
    String LICENSE_TYPE_RIGHT = "R";
    /**
     * 最终许可
     */
    String LICENSE_TYPE_FINAL = "F";
    /**
     * 开发许可
     */
    String LICENSE_TYPE_DEVELOP = "D";
    /**
     * 控制在线人数
     */
    String LICENSE_CONTROL_TYPE_ONLINE = "O";
    /**
     * 控制注册人数
     */
    String LICENSE_CONTROL_TYPE_REGISTER = "R";

    /**
     * 获取许可类型
     *
     * @return 证书类型。
     */
    String getLicenseType();

    /**
     * 获取公司名称
     *
     * @param locale 语言
     * @return 公司名称。
     */
    String getCompanyNameByLicenseType(String locale);

    /**
     * 获取公司名称
     *
     * @return 公司名称。
     */
    String getCompanyName();

    /**
     * 获取公司名称（外语）
     *
     * @return 英文公司名称。
     */
    String companyNameForeign();

    /**
     * 获取证书到期日期
     *
     * @return 到期日期。
     */
    Date getLicenseEndDate();

    /**
     * 校验证书是否到期
     *
     * @return true:到期 false:未到期
     */
    boolean isExpire();

    /**
     * 获取证书即将到期时间
     *
     * @return true:即将到期 false:未到期
     */
    boolean isReadyExpire();

    /**
     * 是否临时许可
     *
     * @return true:临时 false:正式
     */
    boolean isTempLicense();

    /**
     * 是否正式许可
     *
     * @return true:正式 false:临时
     */
    boolean isFinalLicense();

    /**
     * 是否允许使用APP
     *
     * @return true:允许 false:不允许
     */
    boolean isAllowToUseApp();

    /**
     * 是否允许使用H5移动端
     *
     * @return true:允许 false:不允许
     */
    boolean isAllowToUseMobilePage();

    /**
     * 是否控制注册人数
     *
     * @return true:控制 false:不控制
     */
    boolean isControlRegister();

    /**
     * 获取控制类型
     *
     * @return 控制类型
     */
    String getControlUserType();

    /**
     * 获取在线/注册人数
     *
     * @return 控制人数
     */
    Integer getControlUserNumber();

    /**
     * 获取控制的网点数量
     *
     * @return 控制：返回控制数量。不控制：返回0
     */
    Integer getNetNumber();

    /**
     * 获取控制的单位数量
     *
     * @return 控制：返回控制数量。不控制：返回0
     */
    Integer getCorpNumber();

    /**
     * 获取控制的银行帐号数量
     *
     * @return 控制：返回控制数量。不控制：返回0
     */
    Integer getBankAccountNumber();

    /**
     * 获取控制的菜单
     *
     * @return 控制菜单
     */
    List<String> getMenus();

    /**
     * 判断是否有此菜单license授权
     *
     * @param menusId 菜单ID
     * @return true:授权 false:未授权
     */
    /*boolean isMenusAuth(String menusId);*/

    /**
     * 批量判断是否有此菜单license授权
     *
     * @param menuIdList 菜单ID集合
     * @return 返回授权菜单ID集合
     */
    /*List<String> listMenusAuth(List<String> menuIdList);*/

    // /**
    //  * 判断是否有此模块的授权
    //  *
    //  * @param moduleCode SysConfigConstant.Module模块常量
    //  * @return true:授权 false:未授权
    //  */
    // boolean isModuleAuth(String moduleCode);

}
