package com.bytter.biz.finacial.closedeal.vo;
import java.io.Serializable;
/**
 * 期终处理日结检查信息Bean
 * @author liyh
 *
 */
public class CldBaseCheckInfoBean implements Serializable {
    private static final long serialVersionUID = 1L;
    /** 检查类型  */
    private String checkType;
    /** 检查项目序号 */
    private int checkNumber;
    /** 模块名称*/
    private String modelName;
    /** 检查提示信息 */
    private String checkMessage;
    
    public CldBaseCheckInfoBean() {
        super();
    }
    
    public CldBaseCheckInfoBean(String key){
        this.checkType = key;
    }
    
    
    public String getCheckType() {
        return checkType;
    }
    public void setCheckType(String checkType) {
        this.checkType = checkType;
    }
    public int getCheckNumber() {
        return checkNumber;
    }
    public void setCheckNumber(int checkNumber) {
        this.checkNumber = checkNumber;
    }
    public String getModelName() {
        return modelName;
    }
    public void setModelName(String modelName) {
        this.modelName = modelName;
    }
    public String getCheckMessage() {
        return checkMessage;
    }
    public void setCheckMessage(String checkMessage) {
        this.checkMessage = checkMessage;
    }
    
    /**
     * 
     * <br>
     * <b>功能：</b>检查不控制消息<br>
     * <b>作者：</b>李小龙<br>
     * <b>日期：</b> 2011-9-7 <br>
     * @param count
     * @param detail
     * @return
     */
    public static String getCheckNoControlMessage(int count,String detail){
        return "存在"+count+"笔"+detail+",请及时处理！";
    }
    /**
     * 
     * <br>
     * <b>功能：</b>检查控制消息<br>
     * <b>作者：</b>李小龙<br>
     * <b>日期：</b> 2011-9-7 <br>
     * @param count
     * @param detail
     * @return
     */
    
    public static String getCheckControlMessage(String detail){
        return "存在未复核的"+detail+",请及时处理！";
    }
    
}
