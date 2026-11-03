package com.bytter.biz.finacial.paramconfig.entity;

import com.bytter.core.base.entity.IdEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * 业务种类规则表
 */
@Entity
@Table(name = "FC_NIS_BUS_TYPE_RULE", schema = "")
public class FcNisBusTypeRule extends IdEntity implements java.io.Serializable{
    /**业务类别ID*/
    private String  busId;
    /**匹配规则  01-或者  02-并且*/
    private String  matchRule;
    /**匹配要素 remark-摘要、bankAcc-本方账户、accName-本方账户名、oppAccNo-对方账户、oppAccName-对方账户名、curId-币别、corpId-单位名称、uses-用途、postscript-附言*/
    private String matchElements;
    /**匹配关系 1-包含、2-不包含、3-等于、4-不等于*/
    private String matchRelations;
    /**匹配内容*/
    private String matchContent;
    /**排序*/
    private int sort;
    /**左括号*/
    private String leftParenthesis;
    /**右括号*/
    private String rightParenthesis;
    @Column(name ="BUS_ID")
    public String getBusId() {
        return busId;
    }
    public void setBusId(String busId) {
        this.busId = busId;
    }
    @Column(name ="MATCH_RULE")
    public String getMatchRule() {
        return matchRule;
    }
    public void setMatchRule(String matchRule) {
        this.matchRule = matchRule;
    }
    @Column(name ="MATCH_ELEMENTS")
    public String getMatchElements() {
        return matchElements;
    }
    public void setMatchElements(String matchElements) {
        this.matchElements = matchElements;
    }
    @Column(name ="MATCH_RELATIONS")
    public String getMatchRelations() {
        return matchRelations;
    }
    public void setMatchRelations(String matchRelations) {
        this.matchRelations = matchRelations;
    }
    @Column(name ="MATCH_CONTENT")
    public String getMatchContent() {
        return matchContent;
    }
    public void setMatchContent(String matchContent) {
        this.matchContent = matchContent;
    }
    @Column(name ="SORT")
    public int getSort() {
        return sort;
    }
    public void setSort(int sort) {
        this.sort = sort;
    }
    @Column(name ="LEFT_PARENTHESIS")
    public String getLeftParenthesis() {
        return leftParenthesis;
    }
    public void setLeftParenthesis(String leftParenthesis) {
        this.leftParenthesis = leftParenthesis;
    }
    @Column(name ="RIGHT_PARENTHESIS")
    public String getRightParenthesis() {
        return rightParenthesis;
    }
    public void setRightParenthesis(String rightParenthesis) {
        this.rightParenthesis = rightParenthesis;
    }
}
