package com.bytter.report.domain.cms.dto;
import java.io.Serializable;
import java.util.Date;
/**
 * @ClassName BiRemainTermDTO
 * @Description 剩余期限维护DTO
 * @Author fusb
 * @Date 2020/1/15 17:47
 * @Version 1.0
 **/
public class BiRemainTermDTO implements Serializable {
    /**剩余期限id*/
    private String id;
    /**期限名称*/
    private String termName;
    /**期限对应的查询条件*/
    private String searchSql;
    /**最大期限日期*/
    private Date maxTermDate;
    /**
     *方法: 取得java.lang.String
     *@return: java.lang.String  剩余期限id
     */
    public String getId() {
        return id;
    }
    /**
     *方法: 设置java.lang.String
     *@param: java.lang.String  剩余期限id
     */
    public void setId(String id) {
        this.id = id;
    }
    /**
     *方法: 取得java.lang.String
     *@return: java.lang.String  剩余期限名称
     */
    public String getTermName() {
        return termName;
    }
    /**
     *方法: 设置java.lang.String
     *@param: java.lang.String  剩余期限名称
     */
    public void setTermName(String termName) {
        this.termName = termName;
    }
    /**
     *方法: 取得java.lang.String
     *@return: java.lang.String  剩余期限对应的查询条件
     */
    public String getSearchSql() {
        return searchSql;
    }
    /**
     *方法: 设置java.lang.String
     *@param: java.lang.String  剩余期限对应的查询条件
     */
    public void setSearchSql(String searchSql) {
        this.searchSql = searchSql;
    }
    /**
     *方法: 取得java.lang.String
     *@return: java.lang.String  剩余期限最大日期
     */
    public Date getMaxTermDate() {
        return maxTermDate;
    }
    /**
     *方法: 设置java.util.Date
     *@param: java.lang.Date  剩余期限最大日期
     */
    public void setMaxTermDate(Date maxTermDate) {
        this.maxTermDate = maxTermDate;
    }
}
