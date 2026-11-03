package com.bytter.system.api.enums;
import com.bytter.core.enums.IMessageEnum;
/**
 * Created by zhoul on 2017/5/12.
 */
public enum CorpMessageEnum implements IMessageEnum {
    /**
     * ID不能为空
     */
    SYS_CORP_MESSAGE_ID_NULL("03001"),
    SYS_CORP_MESSAGE_TYPE_NULL("03002"),
    SYS_CORP_MESSAGE_TYPE_ERROR("03003"),
    /**
     * 网点、单位名称不能为空
     */
    SYS_CORP_MESSAGE_NAME_NOT_NULL("03004"),
    /**
     * 网点、单位代码不能为空
     */
    SYS_CORP_MESSAGE_CODE_NOT_NULL("03004"),
    /**
     * 已存在相同的网点、单位名称
     */
    SYS_CORP_MESSAGE_NAME_REPEAT("03005"),
    /**
     * 已存在相同的网点、单位代码
     */
    SYS_CORP_MESSAGE_CODE_REPEAT("03005"),
    /**
     * 不允许在单位下增加网点
     */
    SYS_CORP_MESSAGE_NOT_ALLOW_NET_UNDER_CORP("03006"),
    SYS_CORP_MESSAGE_PARENT_ID_CAN_NOT_BE_SELF_ID("03004"),
    /**
     * 有效网点数量已超出产品许可所给予的{0}个
     */
    SYS_CORP_MESSAGE_NET_NUMBER_MORE_THAN_LICENSE_NUMBER("03004"),
    /**
     * 有效单位数量已超出产品许可所给予的{0}个
     */
    SYS_CORP_MESSAGE_CORP_NUMBER_MORE_THAN_LICENSE_NUMBER("03004"),
    /**
     * 单位编号为{0},不存在!
     */
    SYS_CORP_MESSAGE_CORP_CODE_NON_EXISTENT("03005"),
    /**
     * 已存在相同的网点/单位
     */
    SYS_CORP_MESSAGE_CORP_ALREADY_EXIST("03006"),
    /**
     * {0}找不到上级网点
     */
    SYS_CORP_MESSAGE_CORP_NET_CORP_NOT_FOUND("03007"),
    /**
     * {0}找不到父级网点/单位
     */
    SYS_CORP_MESSAGE_CORP_PARENT_CORP_NOT_FOUND("03007"),
    /**
     * 单位修改不允许变更上级网点！sys.corp.message.corp.updatenetcorp.error
     */
    SYS_CORP_MESSAGE_CORP_UPDATENETCORP_ERROR("03008"),
    /**
     * 首选账号只能选择一个
     */
    SYS_CORP_MESSAGE_ISDEFAULTACC("03009"),
    /**
     * 当前租户网点/单位数量：{0}; 需要添加网点/单位数量：{1};客户签约时设置的网点数量：{2};已超出客户签约时设置的网点/单位数量，请确认
     */
    SYS_TENANT_CORP_NUM_OVER_LIMIT("03010"),
//    /**
//     * 单位属性不能为空！sys.corp.message.unitAttribute.not.null
//     */
//    SYS_CORP_MESSAGE_UNITATTRIBUTE_NOT_FOUND("03010"),
//    /**
//     * 单位属性【{0}】不正确！sys.corp.message.unitAttribute.error
//     */
//    SYS_CORP_MESSAGE_UNITATTRIBUTE_ERROR("03011"),
//    /**
//     * 单位属性为管理单元时，管理单元不能为空！sys.corp.message.unitCm.not.null
//     */
//    SYS_CORP_MESSAGE_UNITCM_NOT_FOUND("03012"),
//    /**
//     * 管理单元【{0}】不正确！sys.corp.message.unitCm.error
//     */
//    SYS_CORP_MESSAGE_UNITCM_ERROR("03013"),
//    /**
//     * 组织名称（英文）长度不能错过【{0}】！sys.corp.message.nameEn.maxLength.error
//     */
//    SYS_CORP_MESSAGE_NAMEEN_MAXLENGTH_ERROR("03014"),
//    /**
//     * 单位地址（英文）长度不能错过【{0}】！sys.corp.message.addressEn.maxLength.error
//     */
//    SYS_CORP_MESSAGE_ADDRESSEN_MAXLENGTH_ERROR("03015"),
    ;
    CorpMessageEnum(String code) {
        this.code = code;
    }
    private String code;
    public void setCode(String code) {
        this.code = code;
    }
    @Override
    public Enum getEnum() {
        return this;
    }
    @Override
    public String getCode() {
        return code;
    }
}
