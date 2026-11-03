package com.bytter.biz.creditRating.enums;

import com.bytter.core.enums.IMessageEnum;

public enum CreditRatingEnum implements IMessageEnum {
    CREDIT_RATING_RAWDATA_NULL("120001"),
    COMMON_MESSAGE_REQ_PARAM_ERROR("120002"),
    CREDIT_RATING_INSTITUTION_ONE("120003"),
    /**
     * 日志任务Id不能为空
     */
    EVALUATION_LOG_QUOTEID_NOT_NULL("120004"),

    /**
     * 评价组织不能为空
     */
    EVALUATION_CORP_IS_NOT_NULL("120005"),
    /**
     * 信用评估邮箱验证错误
     */
    CREDIT_FORM_VERIFY_EMAIL_ERROR("120006"),
    /**
     * 备注字符长度不能大于200!
     *
     */
    CREDIT_RATING_DESCRIPTION_TOO_LONG("120007"),

    CREDIT_SCORING_RUBRIC_TOO_LONG("120008"),

    /**
     * 金融许可证机构编码 字符长度不能大于200!
     */
    CREDIT_FINANCIALLICENSEINST_CODE_TOO_LONG("120009"),
    /**
     *  法人代表 字符长度不能大于200!
     */
    CREDIT_LEGALREPRESENTATIVE_TOO_LONG("120010"),
    /**
     *  电子邮箱 字符长度不能大于200!
     */
    CREDIT_EMAIL_TOO_LONG("120011"),
    /**
     * 机构名称 字符长度不能大于200!
     */
    CREDIT_INSTITUTION_NAME_TOO_LONG("120012"),
    /**
     * 统一社会信用代码
     */
    CREDIT_UNIFIEDSOCIALCREDITCODE_TOO_LONG("120013"),

    CREDIT_EMPLOYEE_COUNT_TOO_LONG("120014"),
    /**
     * 未找到对应评级标准，请维护
     */
    NOT_RATING_STANDARD("120015"),
    /**
     * 评估任务备注字符长度不能大于800!
     */
    CREDIT_TASK_RMK_TOO_LONG("120016"),
    /**
     * 该数据已被使用，无法修改成删除！
     */
    CREDIT_TASK_EXIST_COMPUTATIONAL_MODEL("120017"),

    /**
     * 该指标已被模型引用，无法修改或删除！
     */
    CREDIT_SCORING_INDICATOR_IS_USED("120018"),
    ;


    private String code;

    CreditRatingEnum(String code) {
        this.code = code;
    }

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
