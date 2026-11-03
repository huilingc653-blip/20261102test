package com.bytter.guarantee.enums;
import com.bytter.core.enums.IMessageEnum;
/**
 * 合同管理异常提醒信息
 * 
 * @author zhengg
 *
 */
public enum ConMessageEnum implements IMessageEnum {
    /** 操作成功 */
    CONSTACT_MESSAGE_SUCCESS("98000"),
    /** 数据已变更，重新刷新再做业务！ */
    CONSTACT_MESSAGE_ERROR_DATAISUPDATEERROR("98001"),
    /** 该合同类型代码已存在系统中 */
    CONTRACT_CODE_ISEXIST("98002"),
    /** 此合同已被挂接，不允许重复挂接！ */
    // contract.hook.isexist
    CONTRACT_HOOK_ISEXIST("98003"),
    /** 挂接合同ID不能为空！ */
    // contract.hook.conid.isnotnull
    CONTRACT_HOOK_CONID_ISNOTNULL("98004"),
    /** 挂接业务ID不能为空！ */
    // contract.hook.busid.isnotnull
    CONTRACT_HOOK_BUSID_ISNOTNULL("98005"),
    /** 业务生成合同所属单位ID不能为空！ */
    // contract.hook.corpid.isnotnull
    CONTRACT_HOOK_CORPID_ISNOTNULL("98006"),
    /** 业务生成合同合同类型ID不能为空！ */
    // contract.hook.typeid.isnotnull
    CONTRACT_HOOK_TYPEID_ISNOTNULL("98007"),
    /** 业务生成合同合同名称不能为空！ */
    // contract.hook.conname.isnotnull
    CONTRACT_HOOK_CONNAME_ISNOTNULL("98008"),
    /** 业务生成合同合同生效日期不能为空！ */
    // contract.hook.begindate.isnotnull
    CONTRACT_HOOK_BEGINDATE_ISNOTNULL("98009"),
    /** 业务生成合同业务数据信息不能为空！ */
    // contract.hook.outbusdata.isnotnull
    CONTRACT_HOOK_OUTBUSDATA_ISNOTNULL("98010"),
    /** 业务生成合同业务系统类别不能为空！ */
    // contract.hook.conpdtype.isnotnull
    CONTRACT_HOOK_CONPDTYPE_ISNOTNULL("98011"),
    /** 业务生成合同，必填业务明细（{0}）未映射字段！ */
    // contract.hook.pddtlcode.isnotnull
    CONTRACT_HOOK_PDDTLCODE_ISNOTNULL("98012"),
    /** 业务生成合同，必填数据字段（{0}）不存在！ */
    // contract.hook.pddtlcode.fieldisnull
    CONTRACT_HOOK_PDDTLCODE_FIELDISNULL("98013"),
    /** 业务生成合同，必填数据字段（{0}）值为空！ */
    // contract.hook.pddtlcode.valueisnull
    CONTRACT_HOOK_PDDTLCODE_VALUEISNULL("98014"),
    /** 履约原因不能为空！ */
    // contract.hook.ly.opmsg.isnotnull
    CONTRACT_HOOK_LY_OPMSG_ISNOTNULL("98015"),
    /** 履约日期不能为空！ */
    // contract.hook.ly.opdate.isnotnull
    CONTRACT_HOOK_LY_OPDATE_ISNOTNULL("98016"),
    /** 终止原因不能为空！ */
    // contract.hook.zz.opmsg.isnotnull
    CONTRACT_HOOK_ZZ_OPMSG_ISNOTNULL("98017"),
    /** 终止日期不能为空！ */
    // contract.hook.zz.opdate.isnotnull
    CONTRACT_HOOK_ZZ_OPDATE_ISNOTNULL("98018"),
    /** 此合同未挂接，不允许履约或者终止！ */
    // contract.hook.isnotexist
    CONTRACT_HOOK_ISNOtEXIST("98019"),
    /** 合同不存在！ */
    // contract.hook.con.isnotexist
    CONTRACT_HOOK_CON_ISNOTEXIST("98020"),
    /** 合同状态为【{0}】，不允许履约，请检查并完成合同业务流程! */
    // contract.hook.ly.statusiserror
    CONTRACT_HOOK_LY_STATUSISERROR("98021"),
    /** 合同状态为【{0}】，不允许终止，请检查并完成合同业务流程! */
    // contract.hook.zz.statusiserror
    CONTRACT_HOOK_ZZ_STATUSISERROR("98022"),
    /** 传递参数值不允许为空！ */
    CONSTACT_MESSAGE_ERROR_VALISNOTNULL("98023"),
    /** 合同类型被使用，如已经登记合同或者起草合同是有效及没有被删除，则不允许删除合同类型 **/
    CONSTACT_MESSAGE_ERROR_OCCUPY("98024"),
    /** 失败1 */
    CONSTACT_MESSAGE_ERROR("98999"),
    /**
     * 合同业务要素缺失
     */
    CONSTACT_BUS_KEY_MESSAGE_ERROR("99000");
    private String code;
    ConMessageEnum(String code) {
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
