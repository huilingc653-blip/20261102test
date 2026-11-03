package com.bytter.bill.domain.billPool.vo;
import com.bytter.bill.api.billPool.constant.AbPoolConstant;
import com.bytter.core.annotation.BtCompositeSearch;
import com.bytter.core.annotation.Dict;
/**
 * @author zhenghj
 * @version 1.0
 * @Description: 票据池Vo
 * @date 2020-1-3
 */
@SuppressWarnings("serial")
public class AbPoolVO implements java.io.Serializable {
    /**
     * 票据池Id
     */
    private String id;
    /**
     * 票据池代码
     */
    @BtCompositeSearch(fieldName = "AP.BP_CODE")
    private String bpCode;
    /**
     * 票据池名称
     */
    @BtCompositeSearch(fieldName = "AP.BP_NAME")
    private String bpName;
    /**
     * 备注
     */
    @BtCompositeSearch(fieldName = "AP.RMK")
    private String rmk;
    /**
     * 池接口Id
     */
    private String pifId;
    /**
     * 池接口名称
     */
    @BtCompositeSearch(fieldName = "API.PIF_NAME")
    private String pifName;
    /**
     * 池类型:1：结算中心\ 2：财务公司\3：银行  默认为银行
     */
    @Dict(dictGroup = "AB_BILL_POOL_BP_TYPE", inject = "bpTypeName")
    @BtCompositeSearch(fieldName = "AP.BP_TYPE")
    private String bpType;
    private String bpTypeName;
    /**
     * 池模式:1：集团票据池（统收统支）\ 2：集团票据池（自收自用）\3：单一票据池  默认为1
     */
    @Dict(dictGroup = "AB_BILL_POOL_BP_MODE", inject = "bpModeName")
    private String bpMode;
    @BtCompositeSearch(fieldName = "AP.BP_MODE")
    private String bpModeName;
    /**
     * 银行接口代码
     */
    private String bifCode;
    /**
     * 池控制机构
     */
    @BtCompositeSearch(fieldName = "AP.BP_ORG")
    private String bpOrg;
    /**
     * 状态：1启用、0禁用 默认禁用
     */
    @Dict(dictGroup = "AB_POOL_VALID_SIGN", inject = "validSignName")
    @BtCompositeSearch(fieldName = "AP.VALID_SIGN")
    private String validSign;
    @BtCompositeSearch(fieldName = "AP.VALID_SIGN")
    private String validSignName;
    /**
     * 质权人账号
     */
    private String bankAcc;
    /**
     * 质权人名称
     */
    private String accName;
    /**
     * 质权人联行号
     */
    private String bankNo;
    /**
     * 质权人银行名称
     */
    private String bankName;
    /**
     * 是否隐藏多选框 1为是
     */
    private String checkboxHide;
    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }
    public String getRmk() {
        return rmk;
    }
    public void setRmk(String rmk) {
        this.rmk = rmk;
    }
    public String getBpCode() {
        return bpCode;
    }
    public void setBpCode(String bpCode) {
        this.bpCode = bpCode;
    }
    public String getBpName() {
        return bpName;
    }
    public void setBpName(String bpName) {
        this.bpName = bpName;
    }
    public String getPifId() {
        return pifId;
    }
    public void setPifId(String pifId) {
        this.pifId = pifId;
    }
    public String getPifName() {
        return pifName;
    }
    public void setPifName(String pifName) {
        this.pifName = pifName;
    }
    public String getBpType() {
        return bpType;
    }
    public void setBpType(String bpType) {
        this.bpType = bpType;
    }
    public String getBpTypeName() {
        return bpTypeName;
    }
    public void setBpTypeName(String bpTypeName) {
        this.bpTypeName = bpTypeName;
    }
    public String getBpMode() {
        return bpMode;
    }
    public void setBpMode(String bpMode) {
        this.bpMode = bpMode;
    }
    public String getBpModeName() {
        return bpModeName;
    }
    public void setBpModeName(String bpModeName) {
        this.bpModeName = bpModeName;
    }
    public String getBifCode() {
        return bifCode;
    }
    public void setBifCode(String bifCode) {
        this.bifCode = bifCode;
    }
    public String getBpOrg() {
        return bpOrg;
    }
    public void setBpOrg(String bpOrg) {
        this.bpOrg = bpOrg;
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
    public String getBankAcc() {
        return bankAcc;
    }
    public void setBankAcc(String bankAcc) {
        this.bankAcc = bankAcc;
    }
    public String getAccName() {
        return accName;
    }
    public void setAccName(String accName) {
        this.accName = accName;
    }
    public String getBankNo() {
        return bankNo;
    }
    public void setBankNo(String bankNo) {
        this.bankNo = bankNo;
    }
    public String getBankName() {
        return bankName;
    }
    public void setBankName(String bankName) {
        this.bankName = bankName;
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
        return "AbPoolVO{" +
                "id='" + id + '\'' +
                ", bpCode='" + bpCode + '\'' +
                ", bpName='" + bpName + '\'' +
                ", rmk='" + rmk + '\'' +
                ", pifId='" + pifId + '\'' +
                ", pifName='" + pifName + '\'' +
                ", bpType='" + bpType + '\'' +
                ", bpTypeName='" + bpTypeName + '\'' +
                ", bpMode='" + bpMode + '\'' +
                ", bpModeName='" + bpModeName + '\'' +
                ", bifCode='" + bifCode + '\'' +
                ", bpOrg='" + bpOrg + '\'' +
                ", validSign='" + validSign + '\'' +
                ", validSignName='" + validSignName + '\'' +
                ", bankAcc='" + bankAcc + '\'' +
                ", accName='" + accName + '\'' +
                ", bankNo='" + bankNo + '\'' +
                ", bankName='" + bankName + '\'' +
                ", checkboxHide='" + checkboxHide + '\'' +
                '}';
    }
}
