package com.bytter.bill.domain.bill.vo;
import lombok.Data;
import java.io.Serializable;
/**
 * @author zhenghj
 * @version 1.0
 * @Description: 智能选票规则组VO
 * @date 2022-11-03
 */
@SuppressWarnings("serial")
@Data
public class AbIntellectGroupVO implements Serializable {
    /**
     * 规则组ID
     */
    private String ruleGroupId;
    /**
     * 规则组编码
     */
    private String ruleGroupCode;
    /**
     * 规则组名称
     */
    private String ruleGroupName;
    /**
     * 有效标识 0无效 1有效
     */
    private String validSign;
    /**
     * 筛选数量
     */
    private String screenNum;
    /**
     * 明细规则（组）关系：or或 and与
     */
    private String relation;
    /**
     * 备注
     */
    private String rmk;
    /**
     * 申请单号
     */
    private String applyNo;
    /**
     * 规则组明细ID
     */
    private String ruleGroupDtlId;
    /**
     * 选票规则id
     */
    private String ruleId;
    /**
     * 选票规则名称
     */
    private String ruleName;
    /**
     * 明细组号
     */
    private String dtlGroupCode;
    /**
     * 明细规则（组）关系：or或 and与
     */
    private String dtlGroupRelation;
    /**
     * 顺序
     */
    private Integer serialNo;
}
