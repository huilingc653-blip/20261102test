package com.bytter.biz.netbank.vo;
import lombok.Data;
import java.io.Serializable;
/**
 * 自动支付优先策略表
 */
@Data
public class BisPayPolicySortVo implements Serializable {
    /**id*/
    private String id;
    /**策略ID*/
    private String policyId;
    /**优先策略类型 1-同行优先，2-小额优先，3-早制单优先，4-加急支付优先，5-同城优先*/
    private String policyType;
    private String policyTypeName;
    /**序号*/
    private Integer sort;
}
