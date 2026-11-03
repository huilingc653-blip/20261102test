package com.bytter.system.api.enums;
import com.bytter.core.enums.IMessageEnum;
public enum BankInfoMessageEnum implements IMessageEnum {
    /**
     * 银行类型编码必须唯一
     */
    BASIC_BUSINESS_MESSAGE_BANKTYPE_UNIQUE("09001"),
    /**
     * 银行接口代码必须唯一
     */
    BASIC_BUSINESS_MESSAGE_BIFCODE_REQUIRED_UNIQUE("09002"),
    /**
     * 银行接口代码与银行类型不匹配
     */
    BASIC_BUSINESS_MESSAGE_BIFCODE_UNIQUE("09007"),
    /**
     * 开户行行号必须唯一
     */
    BASIC_BUSINESS_MESSAGE_BANKCODE_UNIQUE("09008"),
    /**
     * 开户行行号必须是数字且必须大于等于12位数
     */
    BASIC_BUSINESS_MESSAGE_BANKCODE_WRONG("09009"),
    /**
     * 单位不能为空
     */
    BASIC_BUSINESS_MESSAGE_CORPID_NULL("09011"),
    /**
     * 币别不能为空
     */
    BASIC_BUSINESS_MESSAGE_CURID_NULL("09012"),
    /**
     * 账户类别不能为空
     */
    BASIC_BUSINESS_MESSAGE_ACCTYPE_NULL("09013"),
    /**
     * 账户属性不能为空
     */
    BASIC_BUSINESS_MESSAGE_ATTRIBUTEID_NULL("09014"),
    /**
     * 账户性质不能为空
     */
    BASIC_BUSINESS_MESSAGE_NATUREID_NULL("09015"),
    /**
     * 银行类别类别不能为空
     */
    BASIC_BUSINESS_MESSAGE_BANKTYPEID_NULL("09016"),
    /**
     * 所在省不能为空
     */
    BASIC_BUSINESS_MESSAGE_PROV_NULL("09017"),
    /**
     * 所在市不能为空
     */
    BASIC_BUSINESS_MESSAGE_CITY_NULL("09018"),
    /**
     * 申请原因不能为空
     */
    BASIC_BUSINESS_MESSAGE_OPRESSION_NULL("09019"),
    /**
     * 银行账号必须唯一
     */
    BASIC_BUSINESS_MESSAGE_BANKACC_UNIQUE("09020"),
    /**
     * 银行账号不能为空
     */
    BASIC_BUSINESS_MESSAGE_BANKACC_NULL("09021"),
    /**
     * 账户名称不能为空
     */
    BASIC_BUSINESS_MESSAGE_ACCNAME_NULL("09022"),
    /**
     * 开户行名称不能为空
     */
    BASIC_BUSINESS_MESSAGE_BANKNAME_NULL("09023"),
    /**
     * 利率不能为空
     */
    BASIC_BUSINESS_MESSAGE_RATES_NULL("09024"),
    /**
     * 定期期限不能为空
     */
    BASIC_BUSINESS_MESSAGE_REGULARS_NULL("09025"),
    /**
     * 对私编码不能为空
     */
    BASIC_BUSINESS_MESSAGE_PRIVATENO_NULL("09025"),
    /**
     * 协定开始日期不能为空
     */
    BASIC_BUSINESS_MESSAGE_AGRBEGINDATE_NULL("09026"),
    /**
     * 协定结束日期不能为空
     */
    BASIC_BUSINESS_MESSAGE_AGRENDDATE_NULL("09027"),
    /**
     * 协定金额不能为空
     */
    BASIC_BUSINESS_MESSAGE_AGRMONEY_NULL("09028"),
    /**
     * 协定利率不能为空
     */
    BASIC_BUSINESS_MESSAGE_AGRINTEREST_NULL("09029"),
    /**
     * 协定合同不能为空
     */
    BASIC_BUSINESS_MESSAGE_AGRCONTRACT_NULL("09030"),
    /**
     * 通知类型不能为空
     */
    BASIC_BUSINESS_MESSAGE_NOTICETYPE_NULL("09031"),
    /**
     * 本册笔号不能为空
     */
    BASIC_BUSINESS_MESSAGE_VOLUMENUM_NULL("09032"),
    /**
     * 存折册号不能为空
     */
    BASIC_BUSINESS_MESSAGE_BANKBOOKNUM_NULL("09033"),
    /**
     * 计息周期不能为空
     */
    BASIC_BUSINESS_MESSAGE_INTERESTCYCLE_NULL("09034"),
    /**
     * 利率浮动不能为空
     */
    BASIC_BUSINESS_MESSAGE_RATESFLOATNAME_NULL("09035"),
    /**
     * 利率浮动百分比不能为空
     */
    BASIC_BUSINESS_MESSAGE_RATESFLOATPER_NULL("09036"),
    /**
     * 开户日期不能为空
     */
    BASIC_BUSINESS_MESSAGE_REGDATE_NULL("09037"),
    /**
     * 是否上线不能不能为空
     */
    BASIC_BUSINESS_MESSAGE_ISONLINESTR_NULL("09038"),
    /**
     * 是否查询余额不能为空
     */
    BASIC_BUSINESS_MESSAGE_SCANFLAGSTR_NULL("09039"),
    /**
     * 有效标志不能为空
     */
    BASIC_BUSINESS_MESSAGE_VALIDSIGNSTR_NULL("09040"),
    /**
     * 接口代码对应的银行类型不一致
     */
    BASIC_BUSINESS_MESSAGE_BIFINIT_EQ_BANKTYPEID("09041"),
    /**
     * 有效银行账号数量大于license控制的银行帐号数量
     */
    BASIC_BUSINESS_MESSAGE_BTBANKACC_ACCOUNT_LICENSE("09042"),
    /**
     * 人行前缀号未找到
     **/
    BASIC_BUSINESS_MESSAGE_BANKPREFIX_ISEXIST("09043"),
    /**
     * 银行城市编号未找到
     **/
    BASIC_BUSINESS_MESSAGE_BANKCITYCODE_ISEXIST("09044"),
    /**
     * 人行前缀号+开户行名称前缀必须唯一
     */
    BASIC_BUSINESS_MESSAGE_BANKPREFIX_AND_BANKNAMEPREFIX_UNIQUE("09045"),
    /**
     * 当前用户没有权限，无法删除ukey编号{0}的数据
     */
    TABLE_BANKACC_BTNETUKEY_DELETE_NOTCORRECT("09046"),
    /**
     * 非正常状态的银行账号不允许设置票据账户
     */
    BASIC_BUSINESS_MESSAGE_BANKACC_BILLINFO_BATCHSET("09047"),
    /**
     * 票据专户币别必须存在人民币
     */
    BASIC_BUSINESS_MESSAGE_BTBANKACC_ELECTRIC_RMB("09048"),
    /**
     * 用途为“营收账号”的数量超过最大配置数量
     */
    BASIC_BUSINESS_MESSAGE_BTBANKACC_1004_MAX("09049"),
    /**
     * 登录密码或支付密码加密失败
     */
    BASIC_BUSINESS_MESSAGE_BTBANKACC_ENCRYPTION_FAILURE("09050"),
    /**
     * 当前租户账户数量：{0}; 需要添加账户数量：{1}; 客户签约时设置的账户数量：{2}; 已超出客户签约时设置的账户数量，请确认
     */
    SYS_TENANT_ACCOUNT_NUM_OVER_LIMIT("03008"),
    /**
     * 开户行行号不能为空
     */
    BT_INPUT_BANK_SWIFT_BANKCODE_NULL("09051"),

    /**
     * SWIFT CODE不能为空
     */
    BT_INPUT_BANK_SWIFT_SWIFTCODE_NULL("09052"),

    /**
     * 开户行行号{0}已维护对应的SWIFT CODE
     */
    BT_INPUT_BANK_SWIFT_BANKCODE_EXISTS("09053"),

    ;
    BankInfoMessageEnum(String code) {
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