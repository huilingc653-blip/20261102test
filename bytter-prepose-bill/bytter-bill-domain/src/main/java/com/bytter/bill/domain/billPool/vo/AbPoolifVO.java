package com.bytter.bill.domain.billPool.vo;
import com.bytter.bill.api.billPool.constant.AbPoolConstant;
import com.bytter.core.annotation.BtCompositeSearch;
import com.bytter.core.annotation.Dict;
/**
 * @author zhenghj
 * @version 1.0
 * @Description: 票据池接口Vo
 * @date 2019-12-30
 */
@SuppressWarnings("serial")
public class AbPoolifVO implements java.io.Serializable {
    /**
     * 票据池接口Id
     */
    private String id;
    /**
     * 池接口代码
     */
    @BtCompositeSearch(fieldName = "AP.PIF_CODE")
    private String pifCode;
    /**
     * 池接口名称
     */
    @BtCompositeSearch(fieldName = "AP.PIF_NAME")
    private String pifName;
    /**
     * 银行接口代码ID
     */
    private String bifId;
    /**
     * 银行接口代码
     */
    private String bifCode;
    /**
     * 银行接口名称
     */
    @BtCompositeSearch(fieldName = "AP.BIF_CODE")
    private String bifName;
    /**
     * 备注
     */
    @BtCompositeSearch(fieldName = "AP.RMK")
    private String rmk;
    /**
     * 状态：1启用、0禁用 默认禁用
     */
    @Dict(dictGroup = "AB_POOL_VALID_SIGN", inject = "validSignName")
    @BtCompositeSearch(fieldName = "AP.VALID_SIGN")
    private String validSign;
    @BtCompositeSearch(fieldName = "AP.VALID_SIGN")
    private String validSignName;
    /**
     * 是否隐藏多选框 1为是
     */
    private String checkboxHide;

    public String getBifId() {
        return bifId;
    }

    public void setBifId(String bifId) {
        this.bifId = bifId;
    }

    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }
    public String getPifCode() {
        return pifCode;
    }
    public void setPifCode(String pifCode) {
        this.pifCode = pifCode;
    }
    public String getPifName() {
        return pifName;
    }
    public void setPifName(String pifName) {
        this.pifName = pifName;
    }
    public String getBifCode() {
        return bifCode;
    }
    public void setBifCode(String bifCode) {
        this.bifCode = bifCode;
    }
    public String getBifName() {
        return bifName;
    }
    public void setBifName(String bifName) {
        this.bifName = bifName;
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
    public String getValidSignName() {
        return validSignName;
    }
    public void setValidSignName(String validSignName) {
        this.validSignName = validSignName;
    }
    public String getCheckboxHide() {
        if (this.getValidSign() != null && AbPoolConstant.AB_POLL_VALID_SIGN_1.equals(this.getValidSign())) {
            return "1";
        }
        return checkboxHide;
    }
    public void setCheckboxHide(String checkboxHide) {
        this.checkboxHide = checkboxHide;
    }
    @Override
    public String toString() {
        return "AbPoolifVO{" +
                "id='" + id + '\'' +
                ", pifCode='" + pifCode + '\'' +
                ", pifName='" + pifName + '\'' +
                ", bifId='" + bifId + '\'' +
                ", bifCode='" + bifCode + '\'' +
                ", bifName='" + bifName + '\'' +
                ", rmk='" + rmk + '\'' +
                ", validSign='" + validSign + '\'' +
                ", validSignName='" + validSignName + '\'' +
                ", checkboxHide='" + checkboxHide + '\'' +
                '}';
    }
}
