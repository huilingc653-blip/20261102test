package com.bytter.bill.domain.bill.vo;

import com.bytter.core.annotation.Dict;
import lombok.Data;

import java.io.Serializable;

@SuppressWarnings("serial")
@Data
public class AbBillRuleBillDtlRmkDtlVO implements Serializable {

    /**业务类别ID*/
    private String  busId;
    /**匹配规则  01-或者  02-并且*/
    @Dict(dictGroup="FUNDWARN_NUM_OPERATOR",inject="matchRuleName")
    private String  matchRule;
    private String  matchRuleName;
    /**匹配要素 remark-摘要、bankAcc-本方账户、accName-本方账户名、oppAccNo-对方账户、oppAccName-对方账户名、curId-币别、corpId-单位名称、uses-用途、postscript-附言,amt-金额*/
    @Dict(dictGroup="nisBusTypeRule.matchElements",inject="matchElementsName")
    private String matchElements;
    private String matchElementsName;
    /**匹配关系 1-包含、2-不包含、3-等于、4-不等于*/
    @Dict(dictGroup="nisBusTypeRule.matchRelations",inject="matchRelationsName")
    private String matchRelations;
    private String matchRelationsName;
    /**匹配内容*/
    private String matchContent;
    /**排序*/
    private int sort;
    /**左括号*/
    private String leftParenthesis;
    /**右括号*/
    private String rightParenthesis;

}
