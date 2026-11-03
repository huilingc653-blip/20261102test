package com.bytter.bill.api.bill.enums;
import com.bytter.core.enums.IMessageEnum;
/**
 * 空白凭证异常提醒信息
 * @author zhengg
 *
 */
public enum BlankvouMessageEnum implements IMessageEnum {
    /** 操作成功*/
    BLANDVOU_MESSAGE_SUCCESS("96000"),
    /** 数据已变更，重新刷新再做业务！ */
    BLANDVOU_MESSAGE_ERROR_DATAISUPDATEERROR("96001"),
    /**传递参数值不允许为空！*/
    BLANDVOU_MESSAGE_ERROR_VALISNOTNULL("96002"),
    /**单位组织机构代码证错误请检查！*/
    BLANDVOU_MESSAGE_ERROR_COMMIDFALSE("96003"),
    /**没有上级中心，不能进行上划操作！*/
    BLANDVOU_MESSAGE_ERROR_NETIDISNULL("96004"),
    /**回调业务初始服务失败！*/
    BLANDVOU_MESSAGE_ERROR_PRINTSERVICEISNULL("96005"),
    /**选中数据第{0}行,凭证类型已被使用,不能删除！*/
    BLANKVOU_MESSAGE_ISUSED_BANBATCHDELETE("96006"),
    /**凭证类型已被使用,不能删除！*/
    BLANKVOU_MESSAGE_ISUSED_BANDELETE("96007"),
    /**校验数据源错误信息！  {0}!*/
    BLANKVOU_MESSAGE_ERROR_CHECKDATASOURCE("96008"),
    /**程序发生错误,请检查！*/
    BLANKVOU_MESSAGE_ERROR_CAUSEISNULL("96009"),
    /**取值字段[{0}]在表[{1}]中不存在!*/
    BLANKVOU_MESSAGE_ERROR_CHECKFIELDCODE("96010"),
    /**第【{0}】行数据存在重复凭证，请检查!!*/
    BLANKVOU_MESSAGE_ERROR_CHECKRULENO("96011"),
    /**新增的票据已存在*/
    BILL_BLANKVOU_BVPAYEECHEQUE_SUBMIT_EXIST("96012"),
	/** 失败1*/
	ACCEBILL_MESSAGE_ERROR_2("96999"),
    /**凭证类型中的数据来源没有填写 */
    BLANKVOU_MESSAGE_NO_DATA_SOURE("96013"),
    /**
     * 套打中的数据来源填写非法
     * */
    BLANKVOU_MESSAGE_DATA_SOURCE_ILLEGAL("96014");

    private String code;
    BlankvouMessageEnum(String code) {
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
