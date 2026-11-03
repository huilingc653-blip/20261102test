package com.bytter.system.api.enums;
import com.bytter.core.enums.IMessageEnum;
/**
 * 授信管理消息提示枚举类
 * 
 * @author liyh
 *
 */
public enum RatMessageEnum implements IMessageEnum {
    /** 该业务种类已经被引用，不能删除 */
    FIN_PARAMCONFIG_MESSAGE_BUSRACE_ISUSED("01402"),
    /** 该业务种类已经被引用，不能编辑 */
    FIN_PARAMCONFIG_MESSAGE_BUSRACE_ISNOTEDIT("01404"),
    /** 编号不能为空 */
    CMS_RAT_CODE_NULL("08001"),
    /** 编号已存在 */
    CMS_RAT_CODE_EXIST("08002"),
    /** 自定义授信品种与对应的系统授信品种只能维护一对一的关系 */
    CMS_RAT_ITEMCODE_UNIQUE("08003"),
    /** 自定义授信品种名称已存在 */
    CMS_RAT_ITEMNAME_UNIQUE("08004"),
    /** 此授信品种已经在使用中,不能删除 */
    CMS_RAT_ITEMCODE_USE("08005"),
    /** 系统已存在日期重叠的授信额度，此笔不允许保存 */
    CMS_RAT_ITEMDATE_CONTAIN("08006"),
    /** 当前授信时间与其他授信存在交叉且当前授信时间小于其他授信时间，此笔不允许保存 */
    CMS_RAT_ITEMDATE_BEFORE("08007"),
    /** 二次授信单位{0}已经分配过授信 */
    CMS_RAT_DIS_CORP_EXIST("08008"),
    /** 追加品种{0}已存在同单位同银行同币别的时间区间重复或包含，不可以为此授信品种追加金额！ */
    CMS_RAT_ADD_ITEM_EXIST("08009"),
    /** 银行授信额度分配方式为总额共享，分配编号为{0}还未通过审批，不可进行调剂操作！ */
    CMS_RAT_DIS_NOT_CHECK("08010"),
    /** 该笔授信占用已被释放！ */
    CMS_RAT_USE_RELEASED("08011"),
    /** 授信延期结束日期应大于授信结束日期！ */
    CMS_RAT_DELAY_DATEAFTER("08012"),
    /** 请至少选择一条授信占用数据进行转移！ */
    CMS_RAT_USE_SELECTNULL("08013"),
    /** 授信占用信息不能结转，原因：没有符合要求的相同品种新授信存在! */
    CMS_RAT_DISCODE_NOTEXISTS("08014"),
    /** 授信占用信息不能结转，原因：新的授信品种{0}使用额度不够! */
    CMS_RAT_ITEMCODE_MONEY_LOWER("08015"),
    /** 授信占用信息不能结转，原因：新的授信品种{0}最高使用额度不够! */
    CMS_RAT_ITEMCODE_MAXMONEY_LOWER("08016"),
    /** 授信占用信息不能结转，原因：新的授信品种不存在与之对应的综合品种项! */
    CMS_RAT_DISCOMPCODE_NOTEXISTS("08017"),
    /** 授信占用信息不能结转，原因：原授信不是综合授信! */
    CMS_RAT_DIS_NOTCOMP_ITEMCODE("08018"),
    /** 自定义授信品种{0}没有维护对应的系统授信品种！ */
    CMS_RAT_ITEMCODE_NOSYSITEMCODE("08019"),
    /** 授信信息不能占用，原因：授信品种{0}使用额度不够! */
    CMS_RAT_ITEMCODE_MONEY_ENOUGH("08020"),
    /** 授信信息不能占用，原因：授信品种{0}最高使用额度不够! */
    CMS_RAT_ITEMCODE_MAXMONEY_ENOUGH("08021"),
    /** 授信合同同单位相同品种间不能调剂! */
    CMS_RAT_RELIEF_SAME_CORP("08022"),
    /** 授信合同总额共享的单位间相同品种间不能调剂! */
    CMS_RAT_RELIEF_AMOUNT_SAME_CORP("08023"),
    /** 授信品种类型被授信品种引用，不能删除! */
    CMS_RAT_ITEMTYPE_USE("08024"),
    /** 此授信额度类型已经在使用中,不能删除 */
    CMS_RAT_CREDITTYPE_USE("08025"),
    /** 授信调剂品种的剩余可用额度不足，不能调剂 */
    CMS_RAT_RELIEF_MONEY_BIGGER("08026"),
    /** 授信调剂品种{0}的最高剩余额度不足，不能调剂 */
    CMS_RAT_RELIEF_MAXMONEY_BIGGER("08027"),
    /** 调剂后授信调剂品种{0}的最高剩余额度不能大于综合品种的剩余额度，不能调剂 */
    CMS_RAT_RELIEF_MAXMONEY_LEFT("08028"),
    /** 授信占用品种的剩余可用额度不足，不能占用 */
    CMS_RAT_APPLY_MONEY_BIGGER("08029"),
    /** 授信占用品种{0}的最高剩余额度不足，不能占用 */
    CMS_RAT_APPLY_MAXMONEY_BIGGER("08030"),
    /** 占用后授信综合品种中{0}的最高剩余额度不能大于综合品种的剩余额度，不能占用 */
    CMS_RAT_APPLY_MAXMONEY_LEFT("08031"),
    /** 请录入授信品种追加额度 */
    CMS_RAT_ADD_MONEY_NULL("08032"),
    /** 请录入授信品种分配额度 */
    CMS_RAT_DIS_MONEY_NULL("08033"),
    /** 请录入授信占用品种额度 */
    CMS_RAT_APPLY_MONEY_NULL("08034"),
    /** 请录入授信占用明细 */
    CMS_RAT_ADD_ITEM_NULL("08035"),
    /** 授信调剂额度应大于零 */
    CMS_RAT_RELIEF_MONEY_ZERO("08036"),
    /** 综合授信未录入最高分配额度 */
    CMS_RAT_DIS_COMPITEM_ZERO("08037"),
    /** 请选择授信占用品种 */
    CMS_RAT_APPLY_ITEM_NULL("08038"),
    /** 授信剩余额度不足，请修改分配额度 */
    CMS_RAT_DIS_MONEY_ENOUGH("08039"),
    /** 授信占用信息不能结转，原因：原授信不是非综合授信! */
    CMS_RAT_DIS_NOTSING_ITEMCODE("08040"),
    /** 单位({0})未分配授信额度! */
    CMS_RAT_DIS_CORP_NOTEXIST("08041"),
    /** 授信品种不能为空 */
    CMS_RAT_DTL_ITEMCODE_ISNULL("08042"),
    /** 授信额度不能为空 */
    CMS_RAT_DTL_MONEY_ISNULL("08043"),
    /** 业务名称必须唯一 */
    CMS_RAT_RACE_UNIQUE("08044"),
    /** 请先处理授信调整暂存数据！ */
    CMS_RAT_ADJUST_ONTEMP("08045"),
    /** 请先处理授信调整打回数据！ */
    CMS_RAT_ADJUST_ONBACK("08046"),
    /** 请先处理授信调整待审批数据! */
    CMS_RAT_ADJUST_ONCHECKED("08047"),
    /** 未维护本位币，无法进行金额折算! */
    CMS_RAT_REPORT_NATUALCUR_NOTEXIST("08048"),
    /** 启用银行敞口授信时，保证金额度不能超过授信额度! */
    CMS_RAT_OPENMONEY_CHECK("08049"),
    /** 被结转授信存在未完成的业务，无法进行结转！ */
    CMS_CHANGE_RATBKRATION_CHECK("08050"),
    /** 被结转授信被关闭，无法进行结转！ */
    CMS_CHANGE_RATBKRATION_CLOSE("08051");
    RatMessageEnum(String code) {
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
