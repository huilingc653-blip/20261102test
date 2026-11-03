package com.bytter.biz.foreign.enums;

import com.bytter.core.enums.IMessageEnum;

public enum FexTranConfigMessageEnum implements IMessageEnum {

    /**该支付类型已被{0}交易模板数据引用*/
    PAY_TYPE_HAS_CONFIG("00001"),

    /**交易配置模板编号已存在*/
    FEX_CONFIG_TEMPLATE_NO_EXISTED("00008"),
    /**请勿配置重复数据*/
    NOT_CONFIG_DUPLICATE_DATA("00002"),
    /**数据不存在！*/
    DATA_NOT_EXIST("00003"),

    /**	本次新增数据与已有{0}交易模板数据重复，请检查*/
    FEX_CONFIG_TEMPLATE_HAS_DETAIL_DATA_CONFIGS("00004"),

    /**	该交易模板下有明细数据，请先删除！*/
    FEX_CONFIG_TEMPLATE_HAS_DETAIL_DATA("00005"),

    /**	改模板还有在途业务单据({0}),不允许该操作！*/
    HAS_UNFINISHED_BILL("00007"),

    /**配置数据不能为空，请检查！*/
    NOT_CONFIG_NULL_DATA("00006"),
    ;


    FexTranConfigMessageEnum(String code) {
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
