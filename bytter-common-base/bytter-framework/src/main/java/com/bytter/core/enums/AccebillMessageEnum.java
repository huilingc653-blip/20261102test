package com.bytter.core.enums;

/**
 * 票据异常提醒信息
 * @author zhengg
 *
 */
public enum AccebillMessageEnum implements IMessageEnum {
    /** 操作成功*/
    ACCEBILL_MESSAGE_SUCCESS("99000"),
    /** 发送人账户不允许为空！ */
    ACCEBILL_MESSAGE_ERROR_REQBANKACC_ISNOTNULL("99001"),
    /** 票据信息不存在！ */
    ACCEBILL_MESSAGE_ERROR_BILLISNONULL("99002"),
    /** 数据已变更，重新刷新再做业务！ */
    ACCEBILL_MESSAGE_ERROR_DATAISUPDATEERROR("99003"),
    /**传递参数值不允许为空！*/
    ACCEBILL_MESSAGE_ERROR_VALISNOTNULL("99004"),
    /**单位组织机构代码证错误请检查！*/
    ACCEBILL_MESSAGE_ERROR_COMMIDFALSE("99005"),
    /**收票登记票据号不允许为空！*/
    ACCEBILL_MESSAGE_ERROR_ADDBILL_ORIGNOISNULL("99006"),
    /**提交的票据中存在已经被操作的数据，请检刷新后重试！*/
    ACCEBILL_MESSAGE_ERROR_ADDBILL_CONFLICT("99007"),
    /**没有上级中心，不能进行上划操作！*/
    ACCEBILL_MESSAGE_ERROR_NETIDISNULL("99008"),
    /**中心没有维护票据专户，电票不能上划!*/
    ACCEBILL_MESSAGE_ERROR_NETBANKACCISNULL("99009"),
    /** 接收人账户不允许为空！ */
    ACCEBILL_MESSAGE_ERROR_RECBANKACC_ISNOTNULL("99010"),
    /** 银行接口代码已存在维护的数据，请重新选择！ */
    ACCEBILL_MESSAGE_ERROR_BANKAUTO_BIFCODE_REPEAT("99011"),
    /** 该账户不是票据专户！ */
    ACCEBILL_MESSAGE_ERROR_ELECTRICBILL_ISNOTEXIST("99012"),
    /** {0}单号的票据没有选择资金计划，请编辑后再提交! */
    ACCEBILL_MESSAGE_ERROR_ISBUDGET_ISNOTEXIST("99013"),
    /** 收票人账号不存在！ */
    ACCEBILL_MESSAGE_ERROR_PAYEEACC_ISNOTEXIST("99014"),
    /** 收票人账号不是票据专户！ */
    ACCEBILL_MESSAGE_ERROR_PAYEEACC_ISNOTELECTRICBILL("99015"),
    /** 出票人账号不存在！ */
    ACCEBILL_MESSAGE_ERROR_PAYERACC_ISNOTEXIST("99016"),
    /** 出票人账号不是票据专户！ */
    ACCEBILL_MESSAGE_ERROR_PAYERACC_ISNOTELECTRICBILL("99017"),
    /** 持票人账号不存在！ */
    ACCEBILL_MESSAGE_ERROR_BEARERACC_ISNOTEXIST("99018"),
    /** 持票人账号不是票据专户！ */
    ACCEBILL_MESSAGE_ERROR_BEARERACC_ISNOTELECTRICBILL("99019"),
    /** 所属账户必须存在且为票据专户！ */
    ACCEBILL_MESSAGE_ERROR_REQBANKACC_ISBILLNO("99020"),
    /** 电票业务所属票据专户必须为在线账户！ */
    ACCEBILL_MESSAGE_ERROR_REQBANKACC_ISBILLONLINE("99021"),
    /** 贴入人账号[{0}],账户信息不存在或已失效！ */
    ACCEBILL_MESSAGE_ERROR_DISCOUNTRECBANKACC_ISNOTNULL("99022"),
    /** 贴出人账号[{0}],账户信息不存在或已失效！ */
    ACCEBILL_MESSAGE_ERROR_DISCUNTREQBANKACC_ISNOTNULL("99023"),
    /** 内部贴现贴入单位获取异常！ */
    ACCEBILL_MESSAGE_ERROR_DISCOUNTRECCORPINFO_ISNOTNULL("99024"),
    /**  第【{0}】条数据票据号【{1}】不能重复！ */
    ACCEBILL_MESSAGE_SUBMIT_ORIGNO_REPEAT("99025"),
    /**  有效标志的规则不允许删除！ */
    ACCEBILL_MESSAGE_ERROR_CLAIMACCOUNT_VALIDSIGN("99026"),
    /**  单位+账号+业务类型不允许重复！ */
    ACCEBILL_MESSAGE_ERROR_CLAIMACCOUNT_REPEAT("99027"),
    /**  银行类别+ 业务类型不允许重复！*/
    ACCEBILL_MESSAGE_ERROR_CLAIMACCOUNT_ADDBIllNO("99028"),
    //批量开票补录要素检查
    /**开票登记票据号码存在空值请检查!*/
    ACCEBILL_MESSAGE_ERROR_ABEMITBILL_ORIGNOISNULL("99100"),
    /**开票登记出票日期存在空值请检查!*/
    ACCEBILL_MESSAGE_ERROR_ABEMITBILL_STARTDATEISNULL("99101"),
    /**开票登记到期日期存在空值请检查!*/
    ACCEBILL_MESSAGE_ERROR_ABEMITBILL_ENDDATEISNULL("99102"),
    /**开票登记出票人账号存在空值请检查!*/
    ACCEBILL_MESSAGE_ERROR_ABEMITBILL_PAYERACCISNULL("99103"),
    /**开票登记承兑人存在空值请检查!*/
    ACCEBILL_MESSAGE_ERROR_ABEMITBILL_ACCEPTORISNULL("99104"),
    /**开票登记承兑人账号存在空值请检查!*/
    ACCEBILL_MESSAGE_ERROR_ABEMITBILL_ACCEPTORACCISNULL("99105"),
    /**该票据号已经存在,请检查!*/
    ACCEBILL_MESSAGE_ERROR_ORIGNO_REPEAT("99106"),
    /**票据金额不能为空*/
    ACCEBILL_MESSAGE_ERROR_BILLMONEY_ISNOTNULL("99108"),
    /**所属单位不能为空*/
    ACCEBILL_MESSAGE_ERROR_CORPID_ISNOTNULL("99107"),
    /**出票人不能为空*/
    ACCEBILL_MESSAGE_ERROR_PAYER_ISNOTNULL("99109"),
    /**出票人账号不能为空*/
    ACCEBILL_MESSAGE_ERROR_PAYERACC_ISNOTNULL("99110"),
    /**收票人不能为空*/
    ACCEBILL_MESSAGE_ERROR_PAYEE_ISNOTNULL("99111"),
    /**收票人账号不能为空*/
    ACCEBILL_MESSAGE_ERROR_PAYEEACC_ISNOTNULL("99112"),
    /**收票人开户银行名称不能为空*/
    ACCEBILL_MESSAGE_ERROR_PAYEEBANKNAME_ISNOTNULL("99113"),
    /**收票人开户银行行号不能为空*/
    ACCEBILL_MESSAGE_ERROR_PAYEEBANKNO_ISNOTNULL("99114"),
    /**外部系统id不能为空!*/
    ACCEBILL_MESSAGE_ERROR_VOUCHERID_ISNOTNULL("99115"),
    /**内部贴现没有贴现到账，不能发起贴现赎回申请!*/
    ACCEBILL_MESSAGE_ERROR_CHECKDISCOUNTACCOUNT("99116"),
    /**票据号:{0}已不属于下拨中心,请编辑重新选票！*/
    ACCEBILL_MESSAGE_ERROR_VALIDSINGERROR("99117"),
    /**票据号:{0}已做业务或被锁定状态，请编辑重新选票！*/
    ACCEBILL_MESSAGE_ERROR_BOXSTATUSERROR("99118"),
    /**质权人账号不存在*/
    ACCEBILL_MESSAGE_ERROR_PLEDGE_BANKACC_ISNOTEXIST("99119"),
    /**{0},单位信息不存在!*/
    ACCEBILL_MESSAGE_ERROR_SYSCORP_ISNOTEXIST("99220"),
    /** 当前用户没有内部账号使用权限，请先去授权*/
    FIN_BOOK_TALLY_ACCOUNT_IS_NOT_PERMISSION("99221"),
    /** 电票金额不能大于十亿！*/
    TABLE_ABVOUCHERINFO_BILLMONEY_BILLFLAG11("99222"),
    /*** 纸票金额不能大于一百万*/
    TABLE_ABVOUCHERINFO_BILLMONEY_BILLFLAG10("99223"),
    /** 票据金额最小值为0.01 */
    ACCEBILL_MESSAGE_BILLMONEYMIX("99224"),
    /** 纸票金额不能超过设置的额度 */
    TABLE_ABVOUCHERINFO_BILLFLAG10_MAXBILLMONEY("99225"),
    /**
     * 主表id不能为空 voucherInfo
     */
    VOUCHER_INFO_ID_DISC("99226"),
    /**
     * 拆分金额不能小于0.01元
     */
    BILL_SPLITTING_AMOUNT("99227"),
    /**
     * 总数不等
     */
    TOTAL_UNEQUAL("99228"),
    /**
     *流水表记录为空
     */
    BILL_ACCRECORDS_MESSAGE_IS_NULL("99229"),
    /**
     * 拆票金额为空
     */
    BILL_SPLIT_TICKET_MONEY_IS_NULL("99230"),
    /**
     * 新一代票据不能线下清算
     */
    NEW_GEN_BILL_NOT_OFFLINE_PAY("99231"),
    //票据记账异常98000
    /**票据自动记账失败!*/
    ACCEBILL_ACCBOOK_MESSAGE_ERROR("98001"),
    /**票据记账失败，设置全局流水号失败!*/
    ACCEBILL_ACCBOOK_MESSAGE_BUSGLOBALID_ERROR("98002"),
    /**票据手工记账失败，票据流水号不能为空!*/
    ACCEBILL_ACCBOOK_MESSAGE_AID_ISNULL("98003"),
    /**票据手工记账失败，记账提交设置全局流水号不能为空!*/
    ACCEBILL_ACCBOOK_MESSAGE_BUSGLOBALID_ISNULL("98004"),
    /**票据手工记账失败，未匹配到票据记账实现服务!*/
    ACCEBILL_ACCBOOK_MESSAGE_SERVICE_ISNULL("98005"),
    /**票据手工记账失败，记账业务类型不能为空!*/
    ACCEBILL_ACCBOOK_MESSAGE_BUSORITYPE_ISNULL("98006"),
    /**票据手工记账失败，此记账不允许打回!*/
    ACCEBILL_ACCBOOK_MESSAGE_ISNOTBACK("98007"),
    /**记账规则{0}会计科目未维护!*/
    ACCEBILL_ACCBOOK_MESSAGE_SUBJECTCODE_ISNULL("98008"),
    //票据挂授信异常97000
    /**票据主信息不能为空！ */
    ACCEBILL_RATURATION_MESSAGE_VOUINFONOTNULL("97001"),
    /**票据从表数据信息不能为空！ */
    ACCEBILL_RATURATION_MESSAGE_VOUVICEINFONOTNULL("97002"),
    /** {0}单号的票据没有编辑授信信息，请编辑后再提交! */
    ACCEBILL_MESSAGE_ERROR_ISRELATERAT_ISNOTEXIST("97003"),
    /** 失败1*/
    ACCEBILL_MESSAGE_ERROR_2("99999"),
    /**
     * 智能选票异常
     */
//    要素已被使用，无法删除
    ACCEBILL_INTELLECT_ELEMENT_MESSAGE1("99030"),
    /**
     * 选票规则编码已存在!
     */
    ACCEBILL_INTELLECT_RULE_CODE_IS_EXISTS("99040"),
    /**
     * 选票规则名称已存在!
     */
    ACCEBILL_INTELLECT_RULE_NAME_IS_EXISTS("99041"),
    /**
     * 选票规则组名称已存在!
     */
    ACCEBILL_INTELLECT_RULE_GROUP_NAME_IS_EXISTS("99042"),
    /**
     * 选票规则组编码已存在!
     */
    ACCEBILL_INTELLECT_RULE_GROUP_CODE_IS_EXISTS("99043"),
    /**
     * 该选票规则组被引用，无法删除!
     */
    ACCEBILL_INTELLECT_RULE_GROUP_IS_QUOTE("99044"),
    /**
     * 该选票规则被引用，无法删除!
     */
    ACCEBILL_INTELLECT_RULE_IS_QUOTE("99045"),
    /**
     * 该选票要素被引用，无法删除!
     */
    ACCEBILL_INTELLECT_ELEMENT_IS_QUOTE("99046"),
    /**
     * 选票要素名称已存在!
     */
    ACCEBILL_INTELLECT_ELEMENT_NAME_IS_EXISTS("99047"),
    /**
     * 选票要素编码已存在!
     */
    ACCEBILL_INTELLECT_ELEMENT_CODE_IS_EXISTS("99048"),
    ACCEBILL_MESSAGE_ERROR_OUTOF_BOUND("80001"),
    COMMON_MESSAGE_CONFIRM_SELECTDSHNR("100000"),
    COMMON_MESSAGE_CONFIRM_NEWSELECTDSHNR("100002"),
    COMMON_MESSAGE_CONFIRM_SELECTDSHNRRSN("100001");
    private String code;
    AccebillMessageEnum(String code) {
        this.code = code;
    }
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
