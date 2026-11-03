package com.bytter.guarantee.enums;
import com.bytter.core.enums.IMessageEnum;
/**
 * 担保管理消息提示枚举类
 *
 * @author fusb
 * @version 1.0
 */
public enum GuaranteeMessageEnum implements IMessageEnum {
    /** 当前抵质押单有效期与签订被担保合同有效期存在冲突，必须先变更被担保合同的日期，再变更抵质押单日期！ */
    CMS_GUARANTEE_GUAPLEDGECHANGE_DATEERROR("09901"),
    /** 当前质押单的评估价值小于已占用的质押价值，不能执行变更！ */
    CMS_GUARANTEE_GUAPLEDGECHANGE_MONEYERROR("09902"),
    /** 当前被担保合同有效期与抵质押单有效期存在冲突，必须先变更抵质押的日期，再变更被担保合同日期变更！ */
    CMS_GUARANTEE_GUACONTRACTCHANGE_DATEERROR("09903"),
    /** 担保方式编号必须唯一 */
    CMS_GUARANTEE_GUAWAY_CODE_NOT_UNIQUE("09904"),
    /** 担保方式名称必须唯一 */
    CMS_GUARANTEE_GUAWAY_NAME_NOT_UNIQUE("09905"),
    /** 担保方式被引用，不允许修改 */
    CMS_GUARANTEE_GUAWAY_ISUSED("09906"),
    /** 担保方式被担保合同登记应用，不允许删除 */
    CMS_GUARANTEE_GUAWAY_ISREGISTER("09907"),
    /** 担保方式被引用，不允许删除 */
    CMS_GUARANTEE_GUAWAY_ISUSED_NOT_DEL("09908"),
    /** 请录入担保方式详情 */
    CMS_GUARANTEE_GUACONTRACT_GUAWAY_NULL("09909"),
    /** 对外担保限额单位:{0}币别{1}已经维护！ */
    CMS_EXGUARANTEE_AMOUNT_UNIQUE("09910"),
    /** 此单位对外担保总额不能超过单位限额！ */
    CMS_EXGUARANTEE_CORP_AMOUNT_OUT("09911"),

    /** 担保编号已存在，请勿重复录入！ */
    CMS_EXGUARANTEE_REPEATED("09912");
    GuaranteeMessageEnum(String code) {
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
