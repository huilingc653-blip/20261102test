package com.bytter.subject.entity;

import com.bytter.core.base.entity.IdEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.math.BigDecimal;
import java.util.Date;

/**
 * @Title: Entity
 * @Description: 科目利率表
 * @author onlineGenerator
 * @date 2017-05-18 15:45:32
 * @version 1.0
 *
 */
@Entity
@Table(name = "FC_INSTRATE", schema = "")
@SuppressWarnings("serial")
public class FcInstrate extends IdEntity implements java.io.Serializable {
    /**科目代码*/
    private String subjectCode;
    /**利率类型*/
    private String instrtType;
    /**原利率*/
    private BigDecimal oldInstrate;
    /**利率*/
    private BigDecimal interestRate;
    /**执行日期*/
    private Date exeDate;
    /**期限代码*/
    private Integer termFlag;
    /**期限单位*/
    private String endUnit;
    /**币别代码*/
    private String curCode;
    /**状态*/
    private Integer status;
    /**createTime*/
    private Date createTime;
    /**createBy*/
    private String createBy;
    /**updateTime*/
    private Date updateTime;
    /**updateBy*/
    private String updateBy;
    /**
     *方法: 取得java.lang.String
     *@return: java.lang.String  科目代码
     */
    @Column(name ="SUBJECT_CODE")
    public java.lang.String getSubjectCode(){
        return this.subjectCode;
    }
    /**
     *方法: 设置java.lang.String
     *@param: java.lang.String  科目代码
     */
    public void setSubjectCode(java.lang.String subjectCode){
        this.subjectCode = subjectCode;
    }
    /**
     *方法: 取得java.lang.String
     *@return: java.lang.String  利率类型
     */
    @Column(name ="INSTRT_TYPE")
    public java.lang.String getInstrtType(){
        return this.instrtType;
    }
    /**
     *方法: 设置java.lang.String
     *@param: java.lang.String  利率类型
     */
    public void setInstrtType(java.lang.String instrtType){
        this.instrtType = instrtType;
    }
    /**
     *方法: 取得java.math.BigDecimal
     *@return: java.math.BigDecimal  原利率
     */
    @Column(name ="OLD_INSTRATE")
    public java.math.BigDecimal getOldInstrate(){
        return this.oldInstrate;
    }
    /**
     *方法: 设置java.math.BigDecimal
     *@param: java.math.BigDecimal  原利率
     */
    public void setOldInstrate(java.math.BigDecimal oldInstrate){
        this.oldInstrate = oldInstrate;
    }
    /**
     *方法: 取得java.math.BigDecimal
     *@return: java.math.BigDecimal  利率
     */
    @Column(name ="INTEREST_RATE")
    public java.math.BigDecimal getInterestRate(){
        return this.interestRate;
    }
    /**
     *方法: 设置java.math.BigDecimal
     *@param: java.math.BigDecimal  利率
     */
    public void setInterestRate(java.math.BigDecimal interestRate){
        this.interestRate = interestRate;
    }
    /**
     *方法: 取得java.util.Date
     *@return: java.util.Date  执行日期
     */
    @Column(name ="EXE_DATE")
    public java.util.Date getExeDate(){
        return this.exeDate;
    }
    /**
     *方法: 设置java.util.Date
     *@param: java.util.Date  执行日期
     */
    public void setExeDate(java.util.Date exeDate){
        this.exeDate = exeDate;
    }
    /**
     *方法: 取得java.lang.Integer
     *@return: java.lang.Integer  期限代码
     */
    @Column(name ="TERM_FLAG")
    public java.lang.Integer getTermFlag(){
        return this.termFlag;
    }
    /**
     *方法: 设置java.lang.Integer
     *@param: java.lang.Integer  期限代码
     */
    public void setTermFlag(java.lang.Integer termFlag){
        this.termFlag = termFlag;
    }
    /**
     *方法: 取得java.lang.String
     *@return: java.lang.String  期限单位
     */
    @Column(name ="END_UNIT")
    public java.lang.String getEndUnit(){
        return this.endUnit;
    }
    /**
     *方法: 设置java.lang.String
     *@param: java.lang.String  期限单位
     */
    public void setEndUnit(java.lang.String endUnit){
        this.endUnit = endUnit;
    }
    /**
     *方法: 取得java.lang.String
     *@return: java.lang.String  币别代码
     */
    @Column(name ="CUR_CODE")
    public java.lang.String getCurCode(){
        return this.curCode;
    }
    /**
     *方法: 设置java.lang.String
     *@param: java.lang.String  币别代码
     */
    public void setCurCode(java.lang.String curCode){
        this.curCode = curCode;
    }
    /**
     *方法: 取得java.lang.Integer
     *@return: java.lang.Integer  状态
     */
    @Column(name ="STATUS")
    public java.lang.Integer getStatus(){
        return this.status;
    }
    /**
     *方法: 设置java.lang.Integer
     *@param: java.lang.Integer  状态
     */
    public void setStatus(java.lang.Integer status){
        this.status = status;
    }
    /**
     *方法: 取得java.util.Date
     *@return: java.util.Date  createTime
     */
    @Column(name ="CREATE_TIME")
    public java.util.Date getCreateTime(){
        return this.createTime;
    }
    /**
     *方法: 设置java.util.Date
     *@param: java.util.Date  createTime
     */
    public void setCreateTime(java.util.Date createTime){
        this.createTime = createTime;
    }
    /**
     *方法: 取得java.lang.String
     *@return: java.lang.String  createBy
     */
    @Column(name ="CREATE_BY")
    public java.lang.String getCreateBy(){
        return this.createBy;
    }
    /**
     *方法: 设置java.lang.String
     *@param: java.lang.String  createBy
     */
    public void setCreateBy(java.lang.String createBy){
        this.createBy = createBy;
    }
    /**
     *方法: 取得java.util.Date
     *@return: java.util.Date  updateTime
     */
    @Column(name ="UPDATE_TIME")
    public java.util.Date getUpdateTime(){
        return this.updateTime;
    }
    /**
     *方法: 设置java.util.Date
     *@param: java.util.Date  updateTime
     */
    public void setUpdateTime(java.util.Date updateTime){
        this.updateTime = updateTime;
    }
    /**
     *方法: 取得java.lang.String
     *@return: java.lang.String  updateBy
     */
    @Column(name ="UPDATE_BY")
    public java.lang.String getUpdateBy(){
        return this.updateBy;
    }
    /**
     *方法: 设置java.lang.String
     *@param: java.lang.String  updateBy
     */
    public void setUpdateBy(java.lang.String updateBy){
        this.updateBy = updateBy;
    }
}