package com.bytter.system.api.constant;
/**
 * Created by zhoul on 2017/5/23.
 */
public class CorpConstant {
    public static final String CORP_TYPE_NET = "net";
    public static final String CORP_TYPE_CORP = "corp";
    public static final String CORP_TYPE_RAT = "rat";//授信主体单位
    /**
     * 网点
     */
    public static final Integer CORP_TYPE_CODE_NET = 0;
    /**
     * 单位
     */
    public static final Integer CORP_TYPE_CODE_CORP = 1;
    /**
     * 启用
     */
    public static final Integer CORP_STATUS_ENABLE = 1;
    /**
     * 禁用
     */
    public static final Integer CORP_STATUS_DISABLE = 0;
    /**
     * 单位属性 字典DEY sys_corp_attribute_type
     */
    public static final String CORP_ATTRIBUTE_TYPE_DICT = "sys_corp_attribute_type";
    /**
     * 单位属性： 01	法人公司
     */
    public static final String CORP_ATTRIBUTE_TYPE_01 = "01";
    /*** 单位属性：02	管理单元*/
    public static final String CORP_ATTRIBUTE_TYPE_02 = "02";
    /*** 校验类型 delete删除校验*/
    public static final String CORP_VERIFY_DELETE = "delete";
    /*** 校验类型 disable 单位禁用校验*/
    public static final String CORP_VERIFY_DISABLE = "disable";
    /*** 校验类型 parentChang 单位上级单位变化校验*/
    public static final String CORP_VERIFY_PARENTCHANG = "parentChang";
    /**
     * 单位属性不能为空！sys.corp.message.unitAttribute.not.null
     */
    public static final String SYS_CORP_MESSAGE_UNITATTRIBUTE_NOT_FOUND = "sys.corp.message.unitAttribute.not.null";
    /**
     * 单位属性【{0}】不正确！sys.corp.message.unitAttribute.error
     */
    public static final String SYS_CORP_MESSAGE_UNITATTRIBUTE_ERROR = "sys.corp.message.unitAttribute.error";
    /**
     * 单位属性为管理单元时，管理单元不能为空！sys.corp.message.unitCm.not.null
     */
    public static final String SYS_CORP_MESSAGE_UNITCM_NOT_FOUND = "sys.corp.message.unitCm.not.null";
    /**
     * 管理单元【{0}】不正确！sys.corp.message.unitCm.error
     */
    public static final String SYS_CORP_MESSAGE_UNITCM_ERROR = "sys.corp.message.unitCm.error";
    /**
     * 组织名称（英文）长度不能错过【{0}】！sys.corp.message.nameEn.maxLength.error
     */
    public static final String SYS_CORP_MESSAGE_NAMEEN_MAXLENGTH_ERROR = "sys.corp.message.nameEn.maxLength.error";
    /**
     * 单位地址（英文）长度不能错过【{0}】！sys.corp.message.addressEn.maxLength.error
     */
    public static final String SYS_CORP_MESSAGE_ADDRESSEN_MAXLENGTH_ERROR = "sys.corp.message.addressEn.maxLength.error";
    /**
     * 删除单位时单位不允许有下级单位！sys.corp.child.delete
     */
    public static final String SYS_CORP_MESSAGE_CHILD_DELETE_ERROR = "sys.corp.child.delete";
    /**
     * 禁用单位时单位不允许有有效的下级单位！sys.corp.child.disable
     */
    public static final String SYS_CORP_MESSAGE_CHILD_DISABLE_ERROR = "sys.corp.child.disable";
    /**
     * 删除单位校验异常！sys.corp.verify.delete
     */
    public static final String SYS_CORP_MESSAGE_VERIFY_DELETE_ERROR = "sys.corp.verify.delete";
    /**
     * 禁用单位校验异常！sys.corp.verify.disable
     */
    public static final String SYS_CORP_MESSAGE_VERIFY_DISABLE_ERROR = "sys.corp.verify.disable";
    /**
     * 变更上级单位校验异常！sys.corp.verify.parentChang
     */
    public static final String SYS_CORP_MESSAGE_VERIFY_PARENTCHANG_ERROR = "sys.corp.verify.parentChang";
}
