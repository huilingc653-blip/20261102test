package com.bytter.system.api.dto;
import java.io.Serializable;
/**
 * Created by zhangxb on 2017/5/22 0022.
 */
public class BisBifInitDTO implements Serializable {
    private String id;
    /**
     * 接口代码
     */
    private String bifCode;
    /**
     * 接口代码名称
     */
    private String name;
    /**
     * 银行类别编码
     */
    private String bankTypeId;
    /**
     * 有效标志
     */
    private String validSign;
    /**
     * 是否境内银行
     */
    private String isDomesticBank;
    /**
     * 掉线提醒
     */
    private String disConnectionAlert;
    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }
    public String getBifCode() {
        return bifCode;
    }
    public void setBifCode(String bifCode) {
        this.bifCode = bifCode;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getBankTypeId() {
        return bankTypeId;
    }
    public void setBankTypeId(String bankTypeId) {
        this.bankTypeId = bankTypeId;
    }
    public String getValidSign() {
        return validSign;
    }
    public void setValidSign(String validSign) {
        this.validSign = validSign;
    }
    public String getIsDomesticBank() {
        return isDomesticBank;
    }
    public void setIsDomesticBank(String isDomesticBank) {
        this.isDomesticBank = isDomesticBank;
    }
    public String getDisConnectionAlert() {
        return disConnectionAlert;
    }
    public void setDisConnectionAlert(String disConnectionAlert) {
        this.disConnectionAlert = disConnectionAlert;
    }
}
