package com.bytter.bill.domain.bill.vo;
import lombok.Data;
/**
 * @Title: AbBillSignStrategyDataVO
 * @Description: 电票签收策略数据信息VO
 * @author zhengg
 * @date 2020/7/21 11:25
 */
@Data
public class AbBillSignStrategyDataVO implements java.io.Serializable{
    /**Id*/
    private String id;
    /**策略表Id*/
    private String ssId;
    /**数据类型字典（BILL_SIGN_STRATEGY_DATA_TYPE）：01出票人，02承兑人，03申请人*/
    private String dataType;
    /**组织标识字典（BILL_SIGN_STRATEGY_ORG_TAG）：01单位，02客户/供应商*/
    private String orgTag;
    /**数据值对应单位或者客户供应商的ID*/
    private String dataVal;
    /**数据对应代码*/
    private String code;
    /**数据对应名称*/
    private String name;
}
