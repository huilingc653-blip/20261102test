package com.bytter.finance.enums;
import com.bytter.core.enums.IMessageEnum;
/**
 * 资产证券化管理消息提示枚举类
 *
 * @author ouyj
 */
public enum SecurityMessageEnum implements IMessageEnum {
    /**
     * 资产证券化类型编码和名称不能重复！
     */
    CMS_BASEDATA_SECURITYTYPE_EXISTENCE("7001"),
    /**
     * 资产证券化层级编码和名称不能重复！
     */
    CMS_BASEDATA_SECURITYLEVEL_EXISTENCE("7002"),
    /**
     * 基础资产类型编码和名称不能重复！
     */
    CMS_BASEDATA_SECURITYASSETTYPE_EXISTENCE("7003"),
    /**
     * 计划发行总额与发行规模总额不相等！
     */
    CMS_BASEDATA_SECURITY_ISSETMONEY_NOEQUITY("7004"),
    /**
     * 资产证券化发行编号已存在！
     */
    CMS_BASEDATA_SECURITY_ISSECODE_CHECK("7005"),
    /**
     * 此资产证券化类型已被其他业务引用，不可删除！
     */
    CMS_BASEDATA_SECURITYTYPE_ISQUATE("7006"),
    /**
     * 此资产证券化层级已被其他业务引用，不可删除！
     */
    CMS_BASEDATA_SECURITYLEVEL_ISQUATE("7007"),
    /**
     * 此基础资产类型已被其他业务引用，不可删除！
     */
    CMS_BASEDATA_SECURITYASSETTYPE_ISQUATE("7008"),;
    SecurityMessageEnum(String code) {
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
