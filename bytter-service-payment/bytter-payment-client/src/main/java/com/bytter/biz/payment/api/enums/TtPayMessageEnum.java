package com.bytter.biz.payment.api.enums;
import com.bytter.core.enums.IMessageEnum;
/**
 * 第三方支付异常提醒信息
 * @author zhengg
 *
 */
public enum TtPayMessageEnum implements IMessageEnum {
    /** 操作成功!*/
//	ttpay.message.success("92000"),
	TTPAY_MESSAGE_SUCCESS("92000"),
	/** 主商户号重复!*/
//	ttpay._message.error.merchantno.father.rep("92001"),
	TTPAY_MESSAGE_ERROR_MERCHANTNO_FATHER_REP("92001"),
	/** 子商户号重复!*/
//	ttpay.message.error.merchantno.son.rep("92002"),
	TTPAY_MESSAGE_ERROR_MERCHANTNO_SON_REP("92002"),
    //商户类别重复
    TTPAY_MESSAGE_ERROR_SHOPTYPE_TYPE("92003"),
    //商户类别编号重复
    TTPAY_MESSAGE_ERROR_SHOPTYPE_CODE("92004"),
    //对应支付类型不能为空
    TTPAY_MESSAGE_ERROR_PAYTYPE_NULL("92005"),
    //编号不能重复
    TTPAY_MESSAGE_ERROR_BUSSINESS_CODE("92006"),
    //商户号类别重复
    TTPAY_MESSAGE_ERROR_SHOPTYPENO_TYPE("92007"),
    //商户号类别编号重复
    TTPAY_MESSAGE_ERROR_SHOPTYPENO_CODE("92008"),
    //至少填写一项关联账号
    TTPAY_MESSAGE_ERROR_SHOPTYPENO_LEASTONE("92009"),
	/** 失败!*/
//	ttpay.message.error("92999"),
	TTPAY_MESSAGE_ERROR("92999"),
    /** 所选业务单据非‘银行处理失败’状态，不允许退回！*/
    TTPAY_MESSAGE_RECALL_NO("90011"),
    ;
    
    private String code;
    TtPayMessageEnum(String code) {
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
