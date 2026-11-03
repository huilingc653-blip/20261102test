package com.bytter.biz.payment.api.dto;
import java.io.Serializable;
/**
 * @Title: DTO
 * @Description: 付款方式分配DTO
 * @author fusb
 * @date 2020-02-20 10:08:00
 * @version 1.0
 *
 */
public class BtAssignPayWayDTO implements Serializable {
    /**单位ID*/
    private String corpId;
    /**单位编码*/
    private String corpCode;
    /**单位名称*/
    private String corpName;
    /**付款方式*/
    private String voucherCode;
    /**付款方式*/
    private String voucherName;
    /**默认付款方式*/
    private String defVoucherCode;
    /**默认付款方式名称*/
    private String defVoucherName;
    /**系统结算方式*/
    private String sysVoucherCode;
    public String getCorpId() {
        return corpId;
    }
    public void setCorpId(String corpId) {
        this.corpId = corpId;
    }
    public String getCorpCode() {
        return corpCode;
    }
    public void setCorpCode(String corpCode) {
        this.corpCode = corpCode;
    }
    public String getCorpName() {
        return corpName;
    }
    public void setCorpName(String corpName) {
        this.corpName = corpName;
    }
    public String getVoucherCode() {
        return voucherCode;
    }
    public void setVoucherCode(String voucherCode) {
        this.voucherCode = voucherCode;
    }
    public String getVoucherName() {
        return voucherName;
    }
    public void setVoucherName(String voucherName) {
        this.voucherName = voucherName;
    }
    public String getDefVoucherCode() {
        return defVoucherCode;
    }
    public void setDefVoucherCode(String defVoucherCode) {
        this.defVoucherCode = defVoucherCode;
    }
    public String getDefVoucherName() {
        return defVoucherName;
    }
    public void setDefVoucherName(String defVoucherName) {
        this.defVoucherName = defVoucherName;
    }
    public String getSysVoucherCode() {
        return sysVoucherCode;
    }
    public void setSysVoucherCode(String sysVoucherCode) {
        this.sysVoucherCode = sysVoucherCode;
    }
}
