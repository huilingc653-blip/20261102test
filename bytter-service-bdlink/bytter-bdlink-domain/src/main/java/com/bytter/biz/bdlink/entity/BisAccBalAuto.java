package com.bytter.biz.bdlink.entity;

import com.bytter.core.base.entity.IdEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.Date;

@Entity
@Table(name = "BIS_ACC_BAL_AUTO")
public class BisAccBalAuto extends IdEntity implements java.io.Serializable {
    /**
     * 账号Id
     * */
    private String bankAccId;
    /**
     * 币别Id
     * */
    private String curId;
    /**
     * 计算截止日期
     **/
    private Date endDate;
    /**
     *方法: 取得java.lang.String
     *@return: java.lang.String  账号Id
     */
    @Column(name ="BANK_ACC_ID",nullable=true,length=32)
    public String getBankAccId() {
        return bankAccId;
    }
    /**
     *方法: 设置java.lang.String
     *@param: java.lang.String  账号Id
     */
    public void setBankAccId(String bankAccId) {
        this.bankAccId = bankAccId;
    }
    /**
     *方法: 取得java.lang.String
     *@return: java.lang.String  币别
     */
    @Column(name ="CUR_ID",nullable=true,length=32)
    public String getCurId() {
        return curId;
    }
    /**
     *方法: 设置java.lang.String
     *@param: java.lang.String  币别
     */
    public void setCurId(String curId) {
        this.curId = curId;
    }
    /**
     *方法: 取得java.util.Date
     *@return: java.util.Date  计算截止日期
     */
    @Column(name = "END_DATE", nullable = true)
    public Date getEndDate() {
        return endDate;
    }
    /**
     *方法: 设置java.util.Date
     *@param: java.util.Date  计算截止日期
     */
    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }
}
