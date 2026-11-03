package com.bytter.biz.payment.api.enums;
import com.bytter.core.enums.IMessageEnum;
public enum ForeignMessageEnum implements IMessageEnum {
    /** 名称必须唯一 */
    FEX_PRODUCT_MESSAGE_NAME_UNIQUE("09001"),
    /** 产品名称必须唯一 */
    FEX_PRODUCT_MESSAGE_PRONAME_UNIQUE("09002"),
    /** 产品组下面存在产品，不允许删除 */
    FEX_PRODUCT_MESSAGE_PRODUCTGROUP_DEL("09003"),
    /** 产品已被业务引用，不允许删除*/
    FEX_PRODUCT_MESSAGE_PRODUCT_DEL("09004"),
    /**交易对手银行账户已存在*/
    FEX_COTP_BANKACC_REPEAT("09005"),
    /**交易组名称必须唯一*/
    FEX_TRAN_GROUP_GROUPNAME_UNIQUE("09006"),
    /**交易组下面存在交易，不允许删除 */
    FEX_TRAN_MESSAGE_TRANGROUP_DEL("09007"),
    /**交易组规则详细不能为空 */
    FEX_TRAN_MESSAGE_TRANGROUP_DETAILS_NULL("09008"),
    /**交易组已存在 */
    FEX_TRAN_MESSAGE_TRANGROUP_ISEXIST("09009"),
    /**交易组规则不允许修改 */
    FEX_TRAN_MESSAGE_TRANGROUPRULE_NO_EDIT("09010"),
    /**结算日期{0}为非工作日，操作失败！ */
    FEX_TRAN_MESSAGE_SETTLEDATE_NOT_WORKDATE("09010"),
    /** 净额结算，单位需一致 */
    FEX_CASH_FLOW_MESSAGE_CORP_SAME("09011"),
    /** 净额结算，结算日期需一致 */
    FEX_CASH_FLOW_MESSAGE_SETTLEDATE_SAME("09012"),
    /** 净额结算，交易对手需一致 */
    FEX_CASH_FLOW_MESSAGE_COTP_SAME("09013"),
    /** 净额结算，币种需一致 */
    FEX_CASH_FLOW_MESSAGE_CUR_SAME("09014"),
    /** 净额结算，支付方式需一致 */
    FEX_CASH_FLOW_MESSAGE_VOUCHERTYPE_SAME("09015"),
    /** 净额结算，主体单位银行账户需一致 */
    FEX_CASH_FLOW_MESSAGE_CORPBANKACC_SAME("09016"),
    /** 净额结算，交易对手银行账户需一致 */
    FEX_CASH_FLOW_MESSAGE_COTPBANKACC_SAME("09017"),
    /** 净额结算，资金性质需一致 */
    FEX_CASH_FLOW_MESSAGE_ACCNATURE_SAME("09018"),
    /** 净额结算，交易类型需一致 */
    FEX_CASH_FLOW_MESSAGE_TRANTYPE_SAME("09019"),
    /** 净额结算，保证方式需一致 */
    FEX_CASH_FLOW_MESSAGE_BOODTYPE_SAME("09020"),
    /** 净额结算，保证金账号需一致 */
    FEX_CASH_FLOW_MESSAGE_BOODACC_SAME("09021"),
    /** 净额结算，保证金比例需一致 */
    FEX_CASH_FLOW_MESSAGE_BOODRATIO_SAME("09022"),
    /** 净额结算，保证金钞汇标识需一致 */
    FEX_CASH_FLOW_MESSAGE_BOODBANKNOTE_SAME("09023"),
    /** 净额结算，保证金币种需一致 */
    FEX_CASH_FLOW_MESSAGE_BONDCUR_SAME("09024"),
    /** 净额结算，汇款类型需一致 */
    FEX_CASH_FLOW_MESSAGE_remittype_SAME("09025"),
    /**未匹配到主体单位默认银行账户，请确认！*/
    FEX_CASH_FLOW_MESSAGE_BANKACC_MATCH("09026"),
    /** 现金流{0}的状态不为暂存或打回，不允许此操作 */
    FEX_CASH_FLOW_MESSAGE_STATUS_CHANGE("09020"),
    /** 现金流{0}的结算类型不为全额，不允许此操作 */
    FEX_CASH_FLOW_MESSAGE_SETTLETYPE_1("09021"),
    /** 现金流{0}的结算类型不为净额，不允许此操作 */
    FEX_CASH_FLOW_MESSAGE_SETTLETYPE_2("09022"),
    /** 现金流{0}的交易类型为调拨交易，不允许此操作 */
    FEX_CASH_FLOW_MESSAGE_ALLOCATION_UPDATE("09023"),
    /** 现金流{0}的收支方向为收款，不允许此操作 */
    FEX_CASH_FLOW_MESSAGE_CDSIGN_UPDATE("09024"),
    /** 买入金额为交易需求固定金额，确认修改吗? */
    FEX_TRAN_REQ_MESSAGE_BUYMONEY_UPDATE("09022"),
    /** 卖出金额为交易需求固定金额，确认修改吗? */
    FEX_TRAN_REQ_MESSAGE_SELLMONEY_UPDATE("09023"),
    /** 现金流{0}的单位账户不能为空！ */
    FEX_CASH_FLOW_MESSAGE_CORPACC_NULL("09030"),
    /** 现金流{0}的交易对手账户不能为空！ */
    FEX_CASH_FLOW_MESSAGE_COTPACC_NULL("09031"),
    /** 现金流{0}的支付方式不能为空！ */
    FEX_CASH_FLOW_MESSAGE_VOUCHERTYPE_NULL("09032"),
    /** 现金流{0}的收付方向为收款，实际收款金额不能为空！ */
    FEX_CASH_FLOW_MESSAGE_FACTSMONEY_NULL("09033"),
    /** 现金流{0}的收付方向为收款，实际收款日期不能为空！ */
    FEX_CASH_FLOW_MESSAGE_FACTSDATE_NULL("09034"),
    /** 交易需求单位与明细单位不一致 */
    FEX_TRAN_APP_CHECK_CORP("09035"),
    /** 产品组下存在有效的产品，无法更改为无效 */
    FEX_PRODUCT_CANNOT_CHANGE("09024"),
    /**账户{0}的{1}币种已存在，不允许重复维护*/
    FEX_CORP_POOL_BANKACC_ISEXIST("09036"),
    /**同币种账户不允许重复*/
    FEX_CORP_POOL_BANKACC_REPEAT("09037"),
    /**同一接口的同一外汇牌价接口类别需唯一*/
    FEX_RATE_TYPE_UNIQUE_CHECK("09038"),
    /**该交易对手/组已存在*/
    FEX_COTP_NAMEEXISTS("09039"),
    /** 类型已被业务引用，不允许删除*/
    FEX_RATETYPE_MESSAGE_RATETYPE_DEL("09004"),
    /** 模板名称必须唯一 */
    FEX_EXPOSURE_TEMPLATE_MESSAGE_TEMPLATENAME_UNIQUE("09001"),
    /** 敞口模板{0}已被使用，不允许删除！*/
    FEX_EXPOSURE_TEMPLATE_MESSAGE_EXISTS("09002"),
    /** 单位{0}已设置敞口模板{1}，请确认！*/
    FEX_CORP_EXPOSURE_TEMPLATE_MESSAGE_EXISTS("09003"),
    /** 单位敞口模板已被使用，不允许删除！*/
    FEX_CORP_EXPOSURE_TEMPLATE_MESSAGE_USED("09004"),
    /** 交易编码重复，请重新输入！*/
    FEX_ADD_WORD_TRANCODE_MESSAGE_EXISTS("09005"),
    /**【单位证件类型】【单位证件号码】不能为空，请前往【网点/单位管理】维护！ */
    BIS_FOREIGN_EXC_MESSAGE_CERTTYPE_NULL("09006"),
    /**【开户银行代码类型】不能为空，请前往【境外银行行号信息维护】维护！ */
    BIS_FOREIGN_EXC_MESSAGE_CLSFLAG_NULL_FOREIGN("09007"),
    /**【开户银行代码类型】不能为空，请前往【交易对手维护】-【识别码信息】维护！ */
    BIS_FOREIGN_EXC_MESSAGE_CLSFLAG_NULL_TRAN("09008"),
    /** 已有拆分单据提交付款，不允许撤销拆分 */
    FEX_TRAN_MESSAGE_SPLITCANCEL_VALID("09060"),
    /** 合并单据,挂预算要一致*/
    FEX_TRAN_MESSAGE_FBSBUDGET_CONSISTENT("09061"),
    /** 非合并单据，无法进行撤销合并操作 */
    FEX_TRAN_MESSAGE_MERGECANCEL_VALID("09062"),
    /** 单据{0}交易编码2不能为空 */
    FEX_TRAN_MESSAGE_TRANCODE2_NULL("09062"),
    /** 单据{0}相应金额2不能为空 */
    FEX_TRAN_MESSAGE_TRANAMT2_NULL("09063"),
    /** 单据{0}交易附言2不能为空 */
    FEX_TRAN_MESSAGE_TRANADDWORD2_NULL("09064"),

    /** 现金流{0}已提交,不允许取消勾对！*/
    FEX_REFUNDCHECK_NOTCANCEL_CASHFLOW("09065"),

    /** 交易单{0}已提交,不允许取消勾对！*/
    FEX_REFUNDCHECK_NOTCANCEL_TRAN("09066"),
    /** 交易对手维护国家地址不能为空 */
    FEX_COTP_COUNTRY_ADDRESS_NULL("fex.cotp.country.address.null"),
    /** 交易对手维护国家地址不存在 */
    FEX_COTP_COUNTRY_ADDRESS_NOEXISTS("fex.cotp.country.address.noexists"),
    /** 客商名称（英文）+不属于+单位名称 */
    FEX_EXTERNAL_NOT_WITH_CORP("09067"),
    /** 交易货币对在外汇牌价查询的外汇牌价类型{0}中无报价，请先在外汇牌价查询中录入 */
    FEX_RATE_INFO_IS_NULL("09068"),
    /** 请先维护外汇牌价类型！ */
    FEX_RATE_TYPE_IS_NULL("09068"),
    /**
     * 结算日期为非工作日，请重新选择
     */
    FEX_SETTLE_DATE_NOT_WORK("09069"),
    /**
     * 合并单据,模板要一致
     */
    FEX_TRAN_MESSAGE_TEMPLATE_DIFF("09070"),

    /** 单据最多支持合并100笔 */
    FEX_TRAN_MESSAGE_MERGE_MAX("fex.tran.message.merge.max"),

    /** 业务单据{0}未选择模板，请重新选择！ */
    FEX_TRAN_MESSAGE_TEMPLATE_NULL("09071"),

    /** 银行标签已被使用，请检查预置字段和自定义字段！ */
    FEX_CUST_FIELD_MESSAGE_CODE_UNIQUE("09072"),

    /** 字段编码已被使用，请检查预置字段和自定义字段！ */
    FEX_CUST_FIELD_MESSAGE_FIELDCODE_UNIQUE("09073"),
    /**
     * 该条数据已经被{0}引用，不可以进行删除操作!
     */
    FEX_CUST_FIELD_REFERENCED_EXIST("09074"),
    /**
     * 单据{0}存在表单要素未填写!
     */
    FEX_TRAN_MESSAGE_FORM_UN_FILL("09075"),
    /** 损益管理必填项未填写 */
    FEX_PROFIT_LOSS_REQUIRED("fex.profitLoss.required"),
    /** 损益币种不属于交易货币对 */
    FEX_PROFIT_LOSS_CUR_INVALID("fex.profitLoss.cur.invalid"),
    /** 已有生命周期处理时损益币种不可修改 */
    FEX_PROFIT_LOSS_CUR_LOCKED("fex.profitLoss.cur.locked"),
    /** 折本位币汇率不能为空 */
    FEX_PROFIT_LOSS_CONVERT_RATE_REQUIRED("fex.profitLoss.convertRate.required"),
    ;



    ForeignMessageEnum(String code) {
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
