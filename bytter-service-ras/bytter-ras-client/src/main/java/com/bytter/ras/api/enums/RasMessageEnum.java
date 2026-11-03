package com.bytter.ras.api.enums;

import com.bytter.core.enums.IMessageEnum;

/**
 * 营收稽核异常提醒信息
 * @author zhengg
 *
 */
public enum RasMessageEnum implements IMessageEnum {
    /** 操作成功!*/
    RAS_MESSAGE_SUCCESS("91000"),
	/** 失败!*/
	RAS_MESSAGE_ERROR("91999"),
	//稽核策略维护
	/** 稽核策略维护新增修改对象为空！ */
	RAS_MESSAGE_AUDITSTRATEGY_NULL("91001"),
	/** 稽核策略维护不能维护相同的缴款方式和策略！*/
	RAS_MESSAGE_AUDITSTRATEGY_CHECK("91002"),
	//稽核任务维护
	/** 稽核任务维护新增修改对象为空！ */
	RAS_MESSAGE_AUDITTASK_NULL("91011"),
	/** 稽核任务维护不能维护重复的缴款方式！*/
	RAS_MESSAGE_AUDITTASK_CHECK("91012"),
	/** 稽核任务编码不允许重复！*/
	RAS_TASK_CODECHECK_UNIQ("91013"),
	/** 当前时间未到任务启动时间不允许执行！*/
	RAS_TASK_CODECHECK_RUN_STATUSDATE("91014"),

    /**营收稽核 pos机绑定异常81000*/
    /** 此终端号已绑定门店，请修改后再提交 */
    RAS_POSBINDING_MESSAGE_POSNO_REPEAT("91100"),
    /** 数据已变更，重新刷新再做业务！ */
    RAS_MESSAGE_ERROR_DATAISUPDATEERROR("91101"),
    /**传递参数值不允许为空！*/
    RAS_MESSAGE_ERROR_VALISNOTNULL("91102"),
    /**pos机借出人与借入人不能为同一家门店*/
    RAS_POSBINDING_MESSAGE_BORROWPOSNO_REPEAT("91103"),

    /**营收稽核 门店管理唯一校验异常91201*/
    RAS_SHOP_MESSAGE_REPEAT("91201"),
    
    /**营收稽核 银行账户判断是否被绑定*/
    RAS_RASBANKCARDBINDING_MESSAGE_REPEAT("91202"),
    /**营收稽核 第三方账户绑定 */
    RAS_RASOTHERCARDBINDING_MESSAGE_REPEAT("91203"),
    
    
    /**
     * 营收稽核，导入配置不存在此文件类型
     */
    RAS_IMPORT_FILE_NON_EXISTENT("91204"),

    /**
     * 营收数据导入存在重复数据
     */
    RAS_IMPORT_REVENUE_REPEAT_EXIST("91205"),
    /**
     * 营收数据导入存在未配置的
     */
    RAS_IMPORT_REVENUE_NOT_CONFIGURED_EXIST("91207"),

    /**
     * 营收稽核,实收数据认领自动匹配没匹配到任何数据！
     */
    RAS_RECEIVED_CLAIM_NOCLAIMNUM("91206"),
    
      /**
     * 实收数据导入存在重复数据
     */
    RAS_IMPORT_RECEIVED_REPEAT_EXIST("91208"),
        /**
     * 数据来源不能为空
     */
    RAS_MESSAGE_DATASOURE_NOTNULL("91209"),
    /**营收稽核 单位级别门店唯一校验异常91210*/
    RAS_SHOP_ISCORP_REPEAT("91210"),

    /**营收稽核 没反稽核到任何数据!*/
    RAS_AUDITBACK_AUTOBACK_NODATA("91211")
    ;

    
    private String code;

    RasMessageEnum(String code) {
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
