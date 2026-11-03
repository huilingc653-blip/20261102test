package com.bytter.supply.enums;

import com.bytter.core.enums.IMessageEnum;

/**
 * 供应链模块消息提示枚举类
 */
public enum SupplyMessageEnum implements IMessageEnum {

    /** 资金方编码不能为空 */
    SCF_FUND_PROVIDER_CODE_NULL("02001"),
    /** 资金方编码已存在 */
    SCF_FUND_PROVIDER_CODE_EXIST("02002"),
    /** 机构类型不能为空 */
    SCF_FUND_PROVIDER_ORG_TYPE_NULL("02003"),
    /** 资金方名称不能为空 */
    SCF_FUND_PROVIDER_NAME_NULL("02004"),
    /** 银行行号不能为空 */
    SCF_FUND_PROVIDER_BANK_CODE_NULL("02005"),
    /** 该供应链金融平台未配置应付债权凭证页面模版，请联系管理端配置相应模版 */
    SCF_NOT_CONFIG_PLATFORM("02006"),
    /** 传输数据ID为空 */
    SCF_DATA_ID_NULL("02007"),

    /** 只允许业务状态为“开立中”的应付债权凭证进行撤销操作 */
    SCF_DATA_STATUS_NOT_ALLOW_REVOKE("02008"),

    /** 该平台”{0}“在线接口未开通 请联系相应人员进行配置 */
    SCF_ONLINE_INTERFACE_NOT_OPEN("02009"),

    /** {0}平台 没有开通”{1}”在线接口 请联系相应人员进行配置 */
    SCF_ONLINE_PLATFORM_INTERFACE_NOT_OPEN("02010"),
    /** 只允许对已开立的应付债权凭证进行兑付结果查询，当前凭证状态不符合要求 */
    SCF_VOUCHER_NOT_OPENED_FOR_REDEMPTION("02011"),
    /**该操作只允许线上凭证进行,请选择线上凭证*/
    SCF_VOUCHER_ONLY_ONLINE_OPERATE("02013"),

    /**该应付凭证存在审批中或已审批的兑付记录，无法重复提交兑付*/
    SCF_VOUCHER_HAS_CONFLICT_REDEMPTION("02014"),

    /**该凭证已生成撤销指令，无法重复撤销*/
    SCF_VOUCHER_HAS_CANCEL_INSTRUCTION("02015"),

    /**选择的凭证都已生成撤销指令，无法重复撤销*/
    SCF_VOUCHER_BATCH_ALL_HAS_CANCEL_INSTRUCTION("02016"),

    /**该平台已配置过直联接口，请勿重复提交*/
    SCF_ENT_PLATFORM_CONFIG_EXIST("02017"),

    /**债权凭证编号已存在，请勿重复录入*/
    SCF_VOUCHER_CODE_EXIST("02018"),

    /**只允许业务状态为“已开立/已撤销/已拒签”的应付债权凭证进行作废操作*/
    SCF_VOUCHER_STATUS_NOT_ALLOW_CANCEL("02019"),

    /**作废原因不能为空*/
    SCF_VOUCHER_CANCEL_REASON_REQUIRED("02020"),

    /**作废时间不能为空*/
    SCF_VOUCHER_CANCEL_DATE_REQUIRED("02021"),
    ;
    SupplyMessageEnum(String code) {
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
