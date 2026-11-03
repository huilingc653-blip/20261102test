package com.bytter.biz.dataexchange.basic.vo;

import com.bytter.check.vo.IBaseDataInject;
import com.bytter.core.annotation.BtCompositeSearch;
import com.fasterxml.jackson.annotation.JsonFormat;

import java.util.Date;

public class DSLInterfaceLogVo implements IBaseDataInject {

    private String id;
    /**
     * 来源主键
     */
    private String sourceId;
    /**
     * 日志类型（1:成功、0:失败）
     */
    @BtCompositeSearch(fieldName = "l.type")
    private String type;

    /**
     * 接口名称
     */
    private String interfaceName;

    /**
     * 接口编号
     */
    @BtCompositeSearch(fieldName = "l.interface_Code")
    private String interfaceCode;

    /**
     * 日志时间
     */
    @BtCompositeSearch(fieldName = "l.log_Time")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date logTime;

    private String logTimeStr;

    /**
     * 接口日志
     */
    private String log;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getInterfaceName() {
        return interfaceName;
    }

    public void setInterfaceName(String interfaceName) {
        this.interfaceName = interfaceName;
    }

    public String getInterfaceCode() {
        return interfaceCode;
    }

    public void setInterfaceCode(String interfaceCode) {
        this.interfaceCode = interfaceCode;
    }

    public Date getLogTime() {
        return logTime;
    }

    public void setLogTime(Date logTime) {
        this.logTime = logTime;
    }

    public String getLogTimeStr() {
        return logTimeStr;
    }

    public void setLogTimeStr(String logTimeStr) {
        this.logTimeStr = logTimeStr;
    }

    public String getSourceId() {
        return sourceId;
    }

    public void setSourceId(String sourceId) {
        this.sourceId = sourceId;
    }

    public String getLog() {
        return log;
    }

    public void setLog(String log) {
        this.log = log;
    }
}
