package com.bytter.report.domain.cms.entity;
import com.bytter.core.base.entity.IdEntity;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.io.Serializable;
/**
 * @ClassName BiRemainTerm
 * @Description 剩余期限维护实体
 * @Author fusb
 * @Date 2020/1/9 9:01
 * @Version 1.0
 **/
@Entity
@Table(name = "BI_REMAIN_TERM" , schema = "")
public class BiRemainTerm extends IdEntity implements Serializable {
    /**期限名称*/
    private String termName;
    /**有效标志：“1”有效，“0”无效*/
    private String isValid;
    /**排序*/
    private Integer sort;
    /**创建人*/
    private String createBy;
    /**创建时间*/
    private java.util.Date createDate;
    /**修改人*/
    private String updateBy;
    /**修改时间*/
    private java.util.Date updateDate;
    /**
     * 方法: 取得java.lang.String
     * @return java.lang.String 期限名称
     */
    @Column(name = "TERM_NAME")
    public String getTermName() {
        return termName;
    }
    /**
     *方法: 设置java.lang.String
     *@param: java.lang.String  期限名称
     */
    public void setTermName(String termName) {
        this.termName = termName;
    }
    /**
     * 方法: 取得java.lang.String
     * @return java.lang.String 有效标志
     */
    @Column(name = "IS_VALID")
    public String getIsValid() {
        return isValid;
    }
    /**
     *方法: 设置java.lang.String
     *@param: java.lang.String  有效标志
     */
    public void setIsValid(String isValid) {
        this.isValid = isValid;
    }
    /**
     * 方法: 取得java.lang.Integer
     * @return java.lang.Integer 排序
     */
    @Column(name = "SORT")
    public Integer getSort() {
        return sort;
    }
    /**
     *方法: 设置jjava.lang.Integer
     *@param: java.lang.Integer  排序
     */
    public void setSort(Integer sort) {
        this.sort = sort;
    }
    /**
     * 方法: 取得java.lang.String
     * @return java.lang.String 创建人
     */
    @Column(name = "CREATE_BY")
    public String getCreateBy() {
        return createBy;
    }
    /**
     *方法: 设置java.lang.String
     *@param: java.lang.String  创建人
     */
    public void setCreateBy(String createBy) {
        this.createBy = createBy;
    }
    /**
     * 方法: 取得java.util.Date
     * @return java.util.Date 创建日期
     */
    @Column(name = "CREATE_DATE")
    public java.util.Date getCreateDate() {
        return createDate;
    }
    /**
     *方法: 设置java.util.Date
     *@param: java.util.Date  创建日期
     */
    public void setCreateDate(java.util.Date createDate) {
        this.createDate = createDate;
    }
    /**
     * 方法: 取得java.lang.String
     * @return java.lang.String 修改人
     */
    @Column(name = "UPDATE_BY")
    public String getUpdateBy() {
        return updateBy;
    }
    /**
     *方法: 设置java.lang.String
     *@param: java.lang.String  修改人
     */
    public void setUpdateBy(String updateBy) {
        this.updateBy = updateBy;
    }
    /**
     * 方法: 取得java.util.Date
     * @return java.util.Date 修改日期
     */
    @Column(name = "UPDATE_DATE")
    public java.util.Date getUpdateDate() {
        return updateDate;
    }
    /**
     *方法: 设置java.util.Date
     *@param: java.util.Date  修改日期
     */
    public void setUpdateDate(java.util.Date updateDate) {
        this.updateDate = updateDate;
    }
}
