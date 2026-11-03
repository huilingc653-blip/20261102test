package com.bytter.biz.finacial.api.dto;
import javax.persistence.Column;
import java.io.Serializable;
import java.util.Date;
/**
 * @author YonJar
 * @date 2023/4/23.
 * @description
 */
public class FcCashItemDTO implements Serializable {
    private static final long serialVersionUID = -2352631738123239095L;
    private String id;
    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }
    /**现金流项目代码*/
    private String itemCode;
    /**现金流项目名称*/
    private String itemName;
    /**备注*/
    private String rmk;
    /**有效标识*/
    private String validSign;
    /**流入流出*/
    private String inOut;
    /**创建时间*/
    private Date createTime;
    /**创建用户代码*/
    private String createBy;
    /**最后更新时间*/
    private Date updateTime;
    /**最后更新用户代码*/
    private String updateBy;
    public String getItemCode() {
        return itemCode;
    }
    public void setItemCode(String itemCode) {
        this.itemCode = itemCode;
    }
    public String getItemName() {
        return itemName;
    }
    public void setItemName(String itemName) {
        this.itemName = itemName;
    }
    public String getRmk() {
        return rmk;
    }
    public void setRmk(String rmk) {
        this.rmk = rmk;
    }
    public String getValidSign() {
        return validSign;
    }
    public void setValidSign(String validSign) {
        this.validSign = validSign;
    }
    public String getInOut() {
        return inOut;
    }
    public void setInOut(String inOut) {
        this.inOut = inOut;
    }
    /**
     *方法: 取得java.util.Date
     *@return: java.util.Date  创建时间
     */
    public Date getCreateTime(){
        return this.createTime;
    }
    /**
     *方法: 设置java.util.Date
     *@param: java.util.Date  创建时间
     */
    public void setCreateTime(Date createTime){
        this.createTime = createTime;
    }
    /**
     *方法: 取得java.lang.String
     *@return: java.lang.String  创建用户代码
     */
    public String getCreateBy(){
        return this.createBy;
    }
    /**
     *方法: 设置java.lang.String
     *@param: java.lang.String  创建用户代码
     */
    public void setCreateBy(String createBy){
        this.createBy = createBy;
    }
    /**
     *方法: 取得java.util.Date
     *@return: java.util.Date  最后更新时间
     */
    public Date getUpdateTime(){
        return this.updateTime;
    }
    /**
     *方法: 设置java.util.Date
     *@param: java.util.Date  最后更新时间
     */
    public void setUpdateTime(Date updateTime){
        this.updateTime = updateTime;
    }
    /**
     *方法: 取得java.lang.String
     *@return: java.lang.String  最后更新用户代码
     */
    public String getUpdateBy(){
        return this.updateBy;
    }
    /**
     *方法: 设置java.lang.String
     *@param: java.lang.String  最后更新用户代码
     */
    public void setUpdateBy(String updateBy){
        this.updateBy = updateBy;
    }
}
