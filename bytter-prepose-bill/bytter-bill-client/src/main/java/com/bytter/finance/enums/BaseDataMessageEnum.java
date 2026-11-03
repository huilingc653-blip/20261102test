package com.bytter.finance.enums;
import com.bytter.core.enums.IMessageEnum;
/**
 * 信贷模块基础数据消息提示枚举类
 */
public enum BaseDataMessageEnum implements IMessageEnum {
    /**
     * 基础平台01
     */
    /** 系统预设，不能操作 */
    CMS_BASEDATA_SYSTEM_SET("01001"),
    /** 利率类型已被利率引用，不能操作 */
    CMS_BASEDATA_BASERATE_IS_USED("01002"),
    /** 删除失败:当前操作的记录已不存在 */
    CMS_BASEDATA_DELETE_FAIL("01003"),
    /** 基准利率不存在，请先维护 */
    CMS_BASEDATA_DATA_ISNULL("01004"),
    /** 期限编号已存在 */
    CMS_BASEDATA_CREDITPERIOD_EXIST("01201"),
    /** 基准类型名称不能为空 */
    CMS_BASEDATA_LOANRATE_BASERATEEMPTY("01301"),
    /** 币别不能为空 */
    CMS_BASEDATA_LOANRATE_CUREMPTY("01302"),
    /** 拆借类型不能为空 */
    CMS_BASEDATA_LOANRATE_BORROWTYPEEMPTY("01303"),
    /** {0}的生效日期不能为空 */
    CMS_BASEDATA_LOANRATE_EFFECTTIMEEMPTY("01304"),
    /** {0}的基准利率不能为空 */
    CMS_BASEDATA_LOANRATE_RATEEMPTY("01305"),
    /** {0}利率上限不能为空 */
    CMS_BASEDATA_LOANRATE_RATEUPNLIMIT("01306"),
    /** {0}利率下限不能为空 */
    CMS_BASEDATA_LOANRATE_RATEDOWNLIMIT("01307"),
    /** {0}生效日期不能大于系统当前日期{1} */
    CMS_BASEDATA_LOANRATE_EFFECTTIMEGETODAY("01308"),
    /** {0}在{1}已存在数据 */
    CMS_BASEDATA_LOANRATE_DATAREPEAT("01309"),
    /** 利率数据被引用不能删除 */
    CMS_BASEDATA_LOANRATE_REATREFERED("01316"),
    /** 利率数据被引用不能禁用 */
    CMS_BASEDATA_LOANRATE_NOTVALIDFALSE("01319"),
    /** {0}生效日期不能小于最大利率生效日期{1} */
    CMS_BASEDATA_LOANRATE_EFFECTTIME_LE_MAXDATE("01317"),
    /** 浮动范围上限不能小于浮动范围下限 */
    CMS_BASEDATE_LOANRATE_CONTRAST("01318"),
    /** 数据已复核不能修改 */
    CMS_BASEDATA_LOANRATE_DATAPASS("01310"),
    /** 数据已删除 */
    CMS_BASEDATA_LOANRATE_DATADEL("01311"),
    /** 只能操作最大生效日期的利率数据 */
    CMS_BASEDATA_LOANRATE_EFFECTTIME_MAX("01317"),
    /** 该条数据已经被引用，不可以进行删除操作! */
    CMS_LOAN_DATA_REFERENCED_EXIST("01312"),
    /** 类型编号不能为空 */
    CMS_BASEDATA_LOANCODEEMPTY("01313"),
    /** 类型名称不能为空 */
    CMS_BASEDATA_LOANNAMEEMPTY("01314"),
    /** 类型编号长度不能大于4位 */
    CMS_BASEDATA_LOANCODELENGTH("01315"),
    /** 类型名称不能大于10个字符 */
    CMS_BASEDATA_LOANNAMELENGTH("01316"),
    /** 存在其他业务引用，无法删除 */
    CMS_BASEDATA_OTHERLINK("01317"),
    /** 利率类型名称已存在 */
    CMS_BASEDATA_BASETYPENAME_EXISTENCE("01318"),
    /**
     * 该数据已被使用无法修改或删除
     */
    CMS_LOAN_DATA_REF_OTHER("01319"),
    /** 传递参数值不允许为空！ */
    MESSAGE_ERROR_VALISNOTNULL("99004"),;
    BaseDataMessageEnum(String code) {
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
