package com.bytter.biz.foreign.entity;

import com.bytter.core.base.entity.IdEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.Date;

/**
 * @Title: Entity
 * @Description: 外汇牌价类型表
 * @author qinl
 * @date 2020-07-09
 *
 */
@Entity
@Table(name = "FEX_RATE_TYPE")
public class FexRateType extends IdEntity implements Serializable {

    /**名称*/
    private String typeName;
    /**备注*/
    private String rmk;
    /**外汇牌价接口类别 1-银企直联 2-网络机器人*/
    private String rateInterfaceType;
    /**接口*/
    private String bifCode;
    /**是否需要按标准币种查询*/
    private String isNeedQueryCur;
    /**有效标志*/
    private String validSign;
    /**创建人登录名称*/
    private String createBy;
    /**创建日期*/
    private Date createDate;
    /**更新人登录名称*/
    private String updateBy;
    /**更新日期*/
    private Date updateDate;

    @Column(name ="TYPE_NAME ",nullable=true,length=100)
    public String getTypeName() {
        return typeName;
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }

    @Column(name ="RMK",nullable=true,length=200)
    public String getRmk() {
        return rmk;
    }

    public void setRmk(String rmk) {
        this.rmk = rmk;
    }

    @Column(name ="RATE_INTERFACE_TYPE",nullable=true,length=1)
    public String getRateInterfaceType() {
        return rateInterfaceType;
    }

    public void setRateInterfaceType(String rateInterfaceType) {
        this.rateInterfaceType = rateInterfaceType;
    }

    @Column(name ="BIF_CODE",nullable=true,length=32)
    public String getBifCode() {
        return bifCode;
    }

    public void setBifCode(String bifCode) {
        this.bifCode = bifCode;
    }
    @Column(name ="IS_NEED_QUERY_CUR",nullable=true,length=1)
    public String getIsNeedQueryCur() {
        return isNeedQueryCur;
    }

    public void setIsNeedQueryCur(String isNeedQueryCur) {
        this.isNeedQueryCur = isNeedQueryCur;
    }

    /**
     *方法: 取得java.lang.String
     *@return: java.lang.String  有效标志
     */
    @Column(name ="VALID_SIGN",nullable=true,length=1)
    public String getValidSign(){
        return this.validSign;
    }

    /**
     *方法: 设置java.lang.String
     *@param: java.lang.String  有效标志
     */
    public void setValidSign(String validSign){
        this.validSign = validSign;
    }
    /**
     *方法: 取得java.lang.String
     *@return: java.lang.String  创建人登录名称
     */
    @Column(name ="CREATE_BY",nullable=true,length=50)
    public String getCreateBy(){
        return this.createBy;
    }

    /**
     *方法: 设置java.lang.String
     *@param: java.lang.String  创建人登录名称
     */
    public void setCreateBy(String createBy){
        this.createBy = createBy;
    }
    /**
     *方法: 取得java.util.Date
     *@return: java.util.Date  创建日期
     */
    @Column(name ="CREATE_DATE",nullable=true)
    public Date getCreateDate(){
        return this.createDate;
    }

    /**
     *方法: 设置java.util.Date
     *@param: java.util.Date  创建日期
     */
    public void setCreateDate(Date createDate){
        this.createDate = createDate;
    }
    /**
     *方法: 取得java.lang.String
     *@return: java.lang.String  更新人登录名称
     */
    @Column(name ="UPDATE_BY",nullable=true,length=50)
    public String getUpdateBy(){
        return this.updateBy;
    }

    /**
     *方法: 设置java.lang.String
     *@param: java.lang.String  更新人登录名称
     */
    public void setUpdateBy(String updateBy){
        this.updateBy = updateBy;
    }
    /**
     *方法: 取得java.util.Date
     *@return: java.util.Date  更新日期
     */
    @Column(name ="UPDATE_DATE",nullable=true)
    public Date getUpdateDate(){
        return this.updateDate;
    }

    /**
     *方法: 设置java.util.Date
     *@param: java.util.Date  更新日期
     */
    public void setUpdateDate(Date updateDate){
        this.updateDate = updateDate;
    }
}
