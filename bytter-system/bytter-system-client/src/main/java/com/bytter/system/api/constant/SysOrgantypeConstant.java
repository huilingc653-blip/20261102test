package com.bytter.system.api.constant;
/**
 * 组织架构常量类
 *
 * @author zhenghj
 * 2020-05-18
 **/
public class SysOrgantypeConstant {
    //组织架构维护操作资源常量类
    /***	组织架构维护查询	操作资源		sys:organtype:list  **/
    public static final String SYS_RESOURCE_ORGANTYPEQUERY_OP = "sys:organtype:list";
    /***	组织架构维护操作	操作资源		sys:organtype:edit  **/
    public static final String SYS_RESOURCE_ORGANTYPEEDIT_OP = "sys:organtype:edit";
    /***	组织架构对应关系维护	操作资源		sys:organtypemodule:operat  **/
    public static final String SYS_RESOURCE_ORGANTYPEMODULE_OP = "sys:organtypemodule:operat";
    /*** 状态：0 禁用 */
    public static final String SYS_ORGANTYPE_VALID_SIGN_0 = "0";
    /*** 状态：1 启用 */
    public static final String SYS_ORGANTYPE_VALID_SIGN_1 = "1";
    /*** 状态：2 删除 */
    public static final String SYS_ORGANTYPE_VALID_SIGN_2 = "2";
    /*** 初始化 0否 */
    public static final Integer SYS_ORGANTYPE_IS_INIT_0 = 0;
    /*** 初始化 1是 */
    public static final Integer SYS_ORGANTYPE_IS_INIT_1 = 1;
    /*** 是否改变单位 0否 */
    public static final String SYS_ORGANTYPE_IS_CHANG_CORP_0 = "0";
    /*** 是否改变单位 1是*/
    public static final String SYS_ORGANTYPE_IS_CHANG_CORP_1 = "1";
    /**
     * 状态 字典DEY SYS_ORGANTYPE_STATUS
     */
    public static final String ORGANTYPE_STATUS_DICT = "SYS_ORGANTYPE_STATUS";
    /**
     * 组织类型 字典DEY SYS_ORGANTYPE_CORP_TYPE
     */
    public static final String ORGANTYPE_TYPE_DICT = "SYS_ORGANTYPE_CORP_TYPE";
    /**
     * 单位属性 字典DEY sys_corp_attribute_type
     */
    public static final String ORGANTYPE_UNITATTRIBUTE_DICT = "sys_corp_attribute_type";
}
