package com.bytter.bill.api.billPool.enums;
import com.bytter.core.enums.IMessageEnum;
/**
 * 票据池异常提醒信息
 * @author zhenghj
 *
 */
public enum AbPoolMessageEnum implements IMessageEnum {
    /** 操作成功*/
    AB_POOL_MESSAGE_SUCCESS("12000"),
    /**传递参数值不允许为空！*/
    AB_POOL_MESSAGE_ERROR_VALISNOTNULL("12001"), 
    /**质权人名称重复判断*/
    BILLPOOL_PLEDGEE_CHECK_REPEAT("12591"),
    /**质权人因存在在途状态，无法禁用*/
    BILLPOOL_PLEDGEE_ERROR_TIPS("12598"),
    /**业务类型重复判断*/
    BILLPOOL_BUSTYPE_CHECK_REPEAT("12592"),
    /**选择的单位中或其子单位中有单位处于启用状态，无法删除！*/
    AB_POOL_MESSAGE_POOLCORP_ERROR("12002"),
    /**选中的单位下有子单位处于启用状态，无法删除！*/
    AB_POOL_MESSAGE_SONPOOLCORP_ERROR("12003"),
    /**不允许删除融资申请流转来的数据，请重新选择！*/
    AB_POOL_MESSAGE_ERROR_EMITDELETE("12004"),
    /**申请人账号和票据池只能是同一银行类型！*/
    AB_POOL_MESSAGE_EMIT_ERROR("12005"),
    /**申请人银行账号和票据池只能是兴业银行！*/
    AB_POOL_MESSAGE_EMITFINA_ERROR("12006"),
    /**兴业银行暂不支持保证金线上存入！*/
    AB_POOL_MESSAGE_INBAIL_ERROR("12007"),
    /**选择的业务存在启用的状态，无法删除！*/
    AB_POOL_MESSAGE_BUSTYPE_ERROR("12056"),
    AB_POOL_MESSAGE_BUSTYPEALL1_ERROR("12357"),
    AB_POOL_MESSAGE_ERROR("12059"),
    AB_POOL_MESSAGE_BUSTYPEALL_ERROR("12359"),
    /**电票入池必须s*/
    AB_POOL_INTO_MESSAGE_TRANSFER_ERROR("12057"),
    /**电票入池必须是线上的*/
    AB_POOL_INTO_MESSAGE_SENDSIGN_ERROR("12058"),
    /**入池申请的账号和票据池的接口代码不允许为空*/
    AB_POOL_INTO_MESSAGE_NOTNULL_ERROR("12061"),
    /**电票只支持同行入池*/
    AB_POOL_INTO_MESSAGE_BIFCODE_ERROR("12062"),
    
    /**兴业银行电票只支持质押入池*/
    AB_POOL_INTO_MESSAGE_INTOTYPE_ERROR("12063"),
    /**中信银行电票只支持质押入池*/
    AB_POOL_INTO_MESSAGE_CITIC_INTOTYPE_ERROR("12064"),
    /**兴业银行电票不支持转换*/
    AB_POOL_TST_MESSAGE_BIFCODE_ERROR("12069"),
    
    /**电票入池质权人信息未配置*/
    AB_POOL_INTO_MESSAGE_PLEDGEE_ERROR("12070"),
    /**
     * 检查是否存在业务数据异常提醒信息
     */
    /**额度申领存在审批数据*/
    AB_POOL_CHECK_MESSAGE_LIMITAPPLY_APPROVE("12100"),
    /**额度申领存在指令处理数据*/
    AB_POOL_CHECK_MESSAGE_LIMITAPPLY_INSTRUCT("12101"),
    /**额度申领核准存在审批数据*/
    AB_POOL_CHECK_MESSAGE_LIMITAPPLYCHECK_APPROVE("12102"),
    /**额度申领核准存在指令处理数据*/
    AB_POOL_CHECK_MESSAGE_LIMITAPPLYCHECK_INSTRUCT("12103"),
    /**额度分配存在审批数据*/
    AB_POOL_CHECK_MESSAGE_LIMITALLOT_APPROVE("12104"),
    /**额度分配存在指令处理数据*/
    AB_POOL_CHECK_MESSAGE_LIMITALLOT_INSTRUCT("12105"),
    /**额度分配核准存在审批数据*/
    AB_POOL_CHECK_MESSAGE_LIMITALLOTCHECK_APPROVE("12106"),
    /**额度分配核准存在指令处理数据*/
    AB_POOL_CHECK_MESSAGE_LIMITALLOTCHECK_INSTRUCT("12107"),
    /**保证金存入存在审批数据*/
    AB_POOL_CHECK_MESSAGE_BAILIN_APPROVE("12108"),
    /**保证金存入存在指令处理数据*/
    AB_POOL_CHECK_MESSAGE_BAILIN_INSTRUCT("12109"),
    /**保证金提取存在审批数据*/
    AB_POOL_CHECK_MESSAGE_BAILOUT_APPROVE("12110"),
    /**保证金提取存在指令处理数据*/
    AB_POOL_CHECK_MESSAGE_BAILOUT_INSTRUCT("12111"),
    /**入池申请存在审批数据*/
    AB_POOL_CHECK_MESSAGE_INTOAPPLY_APPROVE("12112"),
    /**入池申请存在指令处理数据*/
    AB_POOL_CHECK_MESSAGE_INTOAPPLY_INSTRUCT("12113"),
    /**出池申请存在审批数据*/
    AB_POOL_CHECK_MESSAGE_OUTAPPLY_APPROVE("12114"),
    /**出池申请存在指令处理数据*/
    AB_POOL_CHECK_MESSAGE_OUTAPPLY_INSTRUCT("12115"),
    /**质押转托管存在审批数据*/
    AB_POOL_CHECK_MESSAGE_PDTST1_APPROVE("12116"),
    /**质押转托管存在指令处理数据*/
    AB_POOL_CHECK_MESSAGE_PDTST1_INSTRUCT("12117"),
    /**托管转质押存在审批数据*/
    AB_POOL_CHECK_MESSAGE_PDTST2_APPROVE("12118"),
    /**托管转质押存在指令处理数据*/
    AB_POOL_CHECK_MESSAGE_PDTST2_INSTRUCT("12119"),
    /**提示付款存在审批数据*/
    AB_POOL_CHECK_MESSAGE_COLLECTIONAPPLY_APPROVE("12120"),
    /**提示付款存在指令处理数据*/
    AB_POOL_CHECK_MESSAGE_COLLECTIONAPPLY_INSTRUCT("12121"),
    /**出票申请存在审批数据*/
    AB_POOL_CHECK_MESSAGE_EMITAPPLY_APPROVE("12122"),
    /**出票申请存在指令处理数据*/
    AB_POOL_CHECK_MESSAGE_EMITAPPLY_INSTRUCT("12123"),
    /**背书申请存在审批数据*/
    AB_POOL_CHECK_MESSAGE_ENDORSE_APPROVE("12124"),
    /**背书申请存在指令处理数据*/
    AB_POOL_CHECK_MESSAGE_ENDORSE_INSTRUCT("12125"),
    /**贴现申请存在审批数据*/
    AB_POOL_CHECK_MESSAGE_DISCOUNT_APPROVE("12126"),
    /**贴现申请存在指令处理数据*/
    AB_POOL_CHECK_MESSAGE_DISCOUNT_INSTRUCT("12127"),
    /**融资申请管理存在审批数据*/
    AB_POOL_CHECK_MESSAGE_FINAAPPLY_APPROVE("12128"),
    /**融资申请管理存在指令处理数据*/
    AB_POOL_CHECK_MESSAGE_FINAAPPLY_INSTRUCT("12129"),
    /**电票签收存在审批数据*/
    AB_POOL_CHECK_MESSAGE_SIGN_APPROVE("12130"),
    /**电票签收存在指令处理数据*/
    AB_POOL_CHECK_MESSAGE_SIGN_INSTRUCT("12131"),
    
    
    
    //浙商银行
    /**电票入池质权人信息未配置*/
    AB_POOL_INTO_MESSAGE_PLEDGEE_PMS_ERROR("12270"), 
    
    
    ;
    private String code;
    AbPoolMessageEnum(String code) {
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
