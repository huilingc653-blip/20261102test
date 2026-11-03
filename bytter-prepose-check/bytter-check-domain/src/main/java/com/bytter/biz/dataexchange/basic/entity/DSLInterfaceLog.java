package com.bytter.biz.dataexchange.basic.entity;

import com.bytter.core.base.entity.IdEntity;
import com.bytter.core.utils.DateUtils;

import javax.persistence.*;
import java.util.Date;
import java.util.UUID;

/**
 * @author lijinquan
 * @description:
 * @date 2024-04-29 15:29
 */
@Entity
@Table(name = "DSL_INTERFACE_LOG")
public class DSLInterfaceLog extends IdEntity {

    /**
     * 来源主键
     */
    private String sourceId;
    /**
     * 日志类型（1:成功、0:失败）
     */
    private String type;

    /**
     * 接口名称
     */
    private String interfaceName;

    /**
     * 接口编号
     */
    private String interfaceCode;

    /**
     * 日志时间
     */
    private Date logTime;

    /**
     * 接口日志
     */
    private String log;
    @Transient
    private StringBuilder logStr;


    public DSLInterfaceLog(){}


    public DSLInterfaceLog(String sourceId, String interfaceCode, String interfaceName, Date logTime){
        super();
        this.setId(UUID.randomUUID().toString().replace("-", ""));// 主键ID
        this.setInterfaceCode(interfaceCode);
        this.setInterfaceName(interfaceName);
        this.setSourceId(sourceId);
        this.setLogTime(logTime);
    }

    @Column(name = "SOURCE_ID")
    public String getSourceId() {
        return sourceId;
    }

    public void setSourceId(String sourceId) {
        this.sourceId = sourceId;
    }

    @Column(name = "TYPE",nullable = false,length = 1)
    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    @Column(name = "INTERFACE_NAME",nullable = false,length = 200)
    public String getInterfaceName() {
        return interfaceName;
    }

    public void setInterfaceName(String interfaceName) {
        this.interfaceName = interfaceName;
    }

    @Column(name = "INTERFACE_CODE",nullable = false,length = 200)
    public String getInterfaceCode() {
        return interfaceCode;
    }

    public void setInterfaceCode(String interfaceCode) {
        this.interfaceCode = interfaceCode;
    }

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "LOG_TIME",nullable = false)
    public Date getLogTime() {
        return logTime;
    }

    public void setLogTime(Date logTime) {
        this.logTime = logTime;
    }

    @Column(name = "LOG")
    public String getLog() {
        return log;
    }

    public void setLog(String log) {
        this.log = log;
    }

    @Transient
    public StringBuilder getLogStr() {
        return logStr !=null ? logStr : new StringBuilder() ;
    }

    public void setLogStr(StringBuilder logStr) {
        this.logStr = logStr;
    }


    /**
     * @param msg
     * @param doSave 是否保存到数据库（true:保存\false:不保存）
     */
    public void debug(String msg,boolean doSave) {
        if(doSave){
            StringBuilder logStr = getLogStr();
            logStr.append(DateUtils.format(new Date()) + "-[DEBUG]" + msg).append("\n");
            this.logStr = logStr;
        }
    }

    /**
     * @param msg
     * @param doSave 是否保存到数据库（true:保存\false:不保存）
     */
    public void info(String msg, boolean doSave) {
        if(doSave){
            StringBuilder logStr = getLogStr();
            logStr.append(DateUtils.format(new Date()) + "-[INFO]" + msg).append("\n");
            this.logStr = logStr;
        }
    }

    /**错误日志
     * @param msg
     * @param doSave 是否保存到数据库（true:保存\false:不保存）
     */
    public void error(String msg, boolean doSave) {
        if(doSave){
            StringBuilder logStr = getLogStr();
            logStr.append(DateUtils.format(new Date()) + "-[ERROR]" + msg).append("\n");
            this.logStr = logStr;
        }
    }

    /**打印Exception日志，
     * @param e
     * @param doSave  是否保存到数据库（true:保存\false:不保存）
     */
    public synchronized void printStackTrace(Exception e, boolean doSave) {
        StringBuilder logStr = getLogStr();
        logStr.append(DateUtils.format(new Date()) + "-[ERROR]" + "错误信息：" + e.getMessage()).append("\n");
        StackTraceElement[] trace = e.getStackTrace();
        for (int i = 0; i < trace.length; i++){
            if(doSave && i<3){
                logStr.append(DateUtils.format(new Date()) + "-[ERROR]" + "\tat：" + trace[i]).append("\n");
            }
        }
        this.logStr = logStr;
    }
}
