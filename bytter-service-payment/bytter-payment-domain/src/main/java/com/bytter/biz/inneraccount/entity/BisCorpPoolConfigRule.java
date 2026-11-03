package com.bytter.biz.inneraccount.entity;

import com.bytter.core.base.entity.IdEntity;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.Date;

/**
 * @Title: Entity
 * @Description: 多级单位余额核算-单位归集配置规则表
 * @author huangx
 * @date 2025年6月20日11:03:04
 * @version 1.0
 *
 */
@Entity
@Table(name = "BIS_CORP_POOL_CONFIG_RULE", schema = "")
@SuppressWarnings("serial")
@Data
public class BisCorpPoolConfigRule extends IdEntity implements java.io.Serializable  {
    /**业务类别ID*/
    private String configId;
    /**匹配规则  01-或者  02-并且*/
    private String  matchRule;
    /**匹配要素 remark-摘要、bankAcc-本方账户、accName-本方账户名、oppAccNo-对方账户、oppAccName-对方账户名、curId-币别、corpId-单位名称、uses-用途、postscript-附言*/
    private String matchElements;
    /**匹配关系 1-包含、2-不包含、3-等于、4-不等于*/
    private String matchRelations;
    /**匹配内容*/
    private String matchContent;
    /**左括号*/
    private String leftParenthesis;
    /**右括号*/
    private String rightParenthesis;
    /**排序*/
    private int sort;
    /**创建人登录名称*/
    private String createBy;
    /**创建日期*/
    private Date createTime;
    /**更新人登录名称*/
    private String updateBy;
    /**更新日期*/
    private Date updateTime;

    @Column(name ="CONFIG_ID")
    public String getConfigId() {
        return configId;
    }

    public void setConfigId(String configId) {
        this.configId = configId;
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

    @Column(name ="SORT")
    public int getSort() {
        return sort;
    }
    
    public void setSort(int sort) {
        this.sort = sort;
    }

    @Column(name = "CREATE_BY", nullable = true, length = 50)
    public String getCreateBy() {
        return createBy;
    }

    public void setCreateBy(String createBy) {
        this.createBy = createBy;
    }

    @Column(name = "CREATE_TIME", nullable = true)
    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    @Column(name = "UPDATE_BY", nullable = true, length = 50)
    public String getUpdateBy() {
        return updateBy;
    }

    public void setUpdateBy(String updateBy) {
        this.updateBy = updateBy;
    }

    @Column(name = "UPDATE_TIME", nullable = true)
    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }
}
