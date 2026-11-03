package com.bytter.finance.enums;
import com.bytter.core.enums.IMessageEnum;
/**
 * 债劵管理消息提示枚举类
 *
 * @author ouyj
 */
public enum BondMessageEnum implements IMessageEnum {
    /**
     * 债劵类型编码和名称不能重复!
     */
    CMS_BASEDATA_BONDTYPE_EXISTENCE("5001"),
    /**
     * 此债劵类型被其他业务引用，不可删除!
     */
    CMS_BASEDATA_BONDTYPE_ISQUOTE("5002"),
    /**
     * 此债劵注册信息被其他业务引用，不可删除!
     */
    CMS_BASEDATA_REGISTER_ISQUOTE("5003"),
    /**
     * 债券发行编号已存在!
     */
    CMS_BASEDATA_ISSUE_ISEXIST("5004"),
    /**
     * 债券代码已存在!
     */
    CMS_BASEDATA_REGISTER_CODE_ISEXIST("5005"),
    /**
     * 债券全称已存在!
     */
    CMS_BASEDATA_REGISTER_FULLNAME_ISEXIST("5006");
    BondMessageEnum(String code) {
        this.code = code;
    }
    private String code;
    public void setCode(String code) {
        this.code = code;
    }
    @SuppressWarnings("rawtypes")
    @Override
    public Enum getEnum() {
        return this;
    }
    @Override
    public String getCode() {
        return code;
    }
}
