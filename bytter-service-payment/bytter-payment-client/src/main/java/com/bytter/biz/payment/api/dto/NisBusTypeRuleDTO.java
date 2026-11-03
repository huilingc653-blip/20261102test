package com.bytter.biz.payment.api.dto;
import com.bytter.core.annotation.Dict;
import java.io.Serializable;
public class NisBusTypeRuleDTO  implements Serializable {
    /**业务类别ID*/
    private String  busId;
    /**匹配规则  01-或者  02-并且*/
    @Dict(dictGroup="FUNDWARN_NUM_OPERATOR",inject="matchRuleName")
    private String  matchRule;
    private String  matchRuleName;
    /**匹配要素 remark-摘要、bankAcc-本方账户、accName-本方账户名、oppAccNo-对方账户、oppAccName-对方账户名、curId-币别、corpId-单位名称、uses-用途、postscript-附言*/
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
    public String getBusId() {
        return busId;
    }
    public void setBusId(String busId) {
        this.busId = busId;
    }
    public String getMatchRule() {
        return matchRule;
    }
    public void setMatchRule(String matchRule) {
        this.matchRule = matchRule;
    }
    public String getMatchRuleName() {
        return matchRuleName;
    }
    public void setMatchRuleName(String matchRuleName) {
        this.matchRuleName = matchRuleName;
    }
    public String getMatchElements() {
        return matchElements;
    }
    public void setMatchElements(String matchElements) {
        this.matchElements = matchElements;
    }
    public String getMatchElementsName() {
        return matchElementsName;
    }
    public void setMatchElementsName(String matchElementsName) {
        this.matchElementsName = matchElementsName;
    }
    public String getMatchRelations() {
        return matchRelations;
    }
    public void setMatchRelations(String matchRelations) {
        this.matchRelations = matchRelations;
    }
    public String getMatchRelationsName() {
        return matchRelationsName;
    }
    public void setMatchRelationsName(String matchRelationsName) {
        this.matchRelationsName = matchRelationsName;
    }
    public String getMatchContent() {
        return matchContent;
    }
    public void setMatchContent(String matchContent) {
        this.matchContent = matchContent;
    }
    public int getSort() {
        return sort;
    }
    public void setSort(int sort) {
        this.sort = sort;
    }
    public String getLeftParenthesis() {
        return leftParenthesis;
    }
    public void setLeftParenthesis(String leftParenthesis) {
        this.leftParenthesis = leftParenthesis;
    }
    public String getRightParenthesis() {
        return rightParenthesis;
    }
    public void setRightParenthesis(String rightParenthesis) {
        this.rightParenthesis = rightParenthesis;
    }
}
