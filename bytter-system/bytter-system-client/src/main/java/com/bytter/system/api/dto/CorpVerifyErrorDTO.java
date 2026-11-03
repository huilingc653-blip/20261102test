package com.bytter.system.api.dto;
import java.io.Serializable;
/**
 * @author zhengg
 * @version 1.0
 * @Title: CorpVerifyErrorDTO
 * @Description: 单位验证异常数据DTO
 * @date 2020-05-19 17:53:54
 */
public class CorpVerifyErrorDTO implements Serializable {
    /**
     * 单位id
     */
    private String corpId;
    /**
     * 系统代码 SysConfigConstant.Module
     */
    private String sysCode;
    /**
     * 业务代码 SysConfigConstant.BusType
     */
    private String busCode;
    /**
     * 错误信息（记得做国际化处理）
     */
    private String errorMsg;
    public CorpVerifyErrorDTO() {
    }
    public CorpVerifyErrorDTO(String sysCode, String busCode, String errorMsg) {
        this.sysCode = sysCode;
        this.busCode = busCode;
        this.errorMsg = errorMsg;
    }
    public CorpVerifyErrorDTO(String corpId, String sysCode, String busCode, String errorMsg) {
        this.corpId = corpId;
        this.sysCode = sysCode;
        this.busCode = busCode;
        this.errorMsg = errorMsg;
    }
    public String getCorpId() {
        return corpId;
    }
    public void setCorpId(String corpId) {
        this.corpId = corpId;
    }
    public String getSysCode() {
        return sysCode;
    }
    public void setSysCode(String sysCode) {
        this.sysCode = sysCode;
    }
    public String getBusCode() {
        return busCode;
    }
    public void setBusCode(String busCode) {
        this.busCode = busCode;
    }
    public String getErrorMsg() {
        return errorMsg;
    }
    public void setErrorMsg(String errorMsg) {
        this.errorMsg = errorMsg;
    }
}
