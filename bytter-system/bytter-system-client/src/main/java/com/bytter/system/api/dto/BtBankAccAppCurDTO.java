package com.bytter.system.api.dto;
import com.bytter.core.base.entity.IdEntity;
public class BtBankAccAppCurDTO extends IdEntity implements java.io.Serializable {
    private java.lang.String appId;
    /**币别ID*/
    private java.lang.String curId;
    /**
     *方法: 取得java.util.Date
     *@return: java.util.Date  更新日期
     */
    /**
     *方法: 取得java.lang.String
     *@return: java.lang.String  appId
     */
    //@Column(name ="APP_ID",nullable=false,length=32)
    public java.lang.String getAppId(){
        return this.appId;
    }
    /**
     *方法: 设置java.lang.String
     *@param: java.lang.String  appId
     */
    public void setAppId(java.lang.String appId){
        this.appId = appId;
    }
    /**
     *方法: 取得java.lang.String
     *@return: java.lang.String  币别ID
     */
    //@Column(name ="CUR_ID",nullable=true,length=2)
    public java.lang.String getCurId(){
        return this.curId;
    }
    /**
     *方法: 设置java.lang.String
     *@param: java.lang.String  币别ID
     */
    public void setCurId(java.lang.String curId){
        this.curId = curId;
    }
}
