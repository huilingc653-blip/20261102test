package com.bytter.report.domain.cms.vo;
import com.bytter.core.annotation.BtCompositeSearch;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
/**
 * @ClassName BiRemainTermVO
 * @Description 剩余期限维护VO
 * @Author fusb
 * @Date 2020/1/9 9:45
 * @Version 1.0
 **/
public class BiRemainTermVO implements Serializable {
    /**主键id*/
    private String id;
    /**期限名称*/
    @BtCompositeSearch(fieldName = "term.term_name")
    private String termName;
    /**有效标志：“1”有效，“0”无效*/
    @BtCompositeSearch(fieldName = "term.is_valid")
    private String isValid;
    /**排序*/
    @BtCompositeSearch(fieldName = "term.sort")
    private Integer sort;
    /**创建人*/
    private String createBy;
    /**创建时间*/
    private java.util.Date createDate;
    /**修改人*/
    private String updateBy;
    /**修改时间*/
    private java.util.Date updateDate;
    /**剩余期限维护字表信息*/
    private List<BiRemainTermDetailVO> remainTermDtl = new ArrayList<BiRemainTermDetailVO>();
    /**公式*/
    private String formulas;
    /**
     * 方法: 取得java.lang.String
     * @return java.lang.String id
     */
    public String getId() {
        return id;
    }
    /**
     *方法: 设置java.lang.String
     *@param: java.lang.String  id
     */
    public void setId(String id) {
        this.id = id;
    }
    /**
     * 方法: 取得java.lang.String
     * @return java.lang.String 期限名称
     */
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
    public Integer getSort() {
        return sort;
    }
    /**
     *方法: 设置java.lang.Integer
     *@param: java.lang.Integer  排序
     */
    public void setSort(Integer sort) {
        this.sort = sort;
    }
    /**
     * 方法: 取得java.lang.String
     * @return java.lang.String 创建人
     */
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
    /**
     *方法: 设置java.util.List
     *@param: java.util.List  剩余期限子表信息集合
     */
    public List<BiRemainTermDetailVO> getRemainTermDtl() {
        return remainTermDtl;
    }
    /**
     *方法: 设置java.util.List
     *@param: java.util.List  剩余期限子表信息集合
     */
    public void setRemainTermDtl(List<BiRemainTermDetailVO> remainTermDtl) {
        this.remainTermDtl = remainTermDtl;
    }
    /**
     * 方法: 取得java.lang.String
     * @return java.lang.String 公式
     */
    public String getFormulas() {
        return formulas;
    }
    /**
     *方法: 设置java.lang.String
     *@param: java.lang.String  公式
     */
    public void setFormulas(String formulas) {
        this.formulas = formulas;
    }
}
