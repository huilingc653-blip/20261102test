package com.bytter.system.api.enums;
import com.bytter.core.enums.IMessageEnum;
/**
 * 客商资料
 *
 * @author zuoy on 2017/11/03
 */
public enum ExternalCorpMessageEnum implements IMessageEnum {
    /**
     * 同一单位下客户编号必须唯一
     */
    SYS_EXTERNAL_CORP_MESSAGE_CODE_UNIQUE("01001"),
    /**
     * 同一单位下客户名称必须唯一
     */
    SYS_EXTERNAL_CORP_MESSAGE_NAME_UNIQUE("01002"),
    /**
     * 请选择关联账户
     */
    SYS_EXTERNAL_CORP_MESSAGE_BANK_NULL("01003"),
    /**
     * 客户代码在系统中已存在
     */
    SYS_EXTERNAL_CORP_MESSAGE_CODE_EXIST("01004"),
    /**
     * 客户名称在系统中已存在
     */
    SYS_EXTERNAL_CORP_MESSAGE_NAME_EXIST("01005"),
    /**
     * 供应商编号已存在
     */
    SYS_SUPPLIER_TYPE_MESSAGE_CODE_EXIST("01006"),
    /**
     * 客商审核状态已发生变化
     */
    SYS_EXTERNAL_CORP_MESSAGE_AUDIT_STATUS_ERROR("01007"),
    /**
     * 所在国不能为空
     */
    SYS_EXTERNALCORP_MESSAGE_PROVNAMEFEX_NULL("01008"),
    /**
     * 所在国不存在或不匹配
     */
    SYS_EXTERNALCORP_IMPORT_PROVNAMEFEX_MISMATCH("01009"),
    /**
     * SWIFT CODE不能为空
     */
    SYS_EXTERNALCORP_MESSAGE_SWIFTCODE_NULL("01010"),
    /**
     * SWIFT CODE不存在或不匹配
     */
    SYS_EXTERNALCORP_IMPORT_SWIFTCODE_MISMATCH("01011"),
    /**
     * 机构名称不匹配
     */
    SYS_EXTERNALCORP_MESSAGE_BANK_MISMATCH("01012"),
    /**
     * 供应商类别不能为空
     */
    SYS_EXTERNALCORP_MESSAGE_SUPPLIERTYPE_NULL("01013"),
    /**
     * 供应商类别不存在或不匹配
     */
    SYS_EXTERNALCORP_IMPORT_SUPPLIERTYPE_MISMATCH("01014"),
    /**
     * 客商类型不存在或不匹配
     */
    SYS_EXTERNALCORP_IMPORT_EXTERNALTYPE_MISMATCH("01015"),
    /**
     * 账号所在国不存在或不匹配
     */
    SYS_EXTERNALCORP_IMPORT_ACCPROVNAMEFEX_MISMATCH("01016"),
    /**
     * 同一客商账号不允许相同
     */
    SYS_EXTERNALCORPBANKACC_ACC_SAME("01017"),
    /**
     * 供应商名称已存在
     */
    SYS_SUPPLIER_TYPE_MESSAGE_NAME_EXIST("01018");
    ExternalCorpMessageEnum(String code) {
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
