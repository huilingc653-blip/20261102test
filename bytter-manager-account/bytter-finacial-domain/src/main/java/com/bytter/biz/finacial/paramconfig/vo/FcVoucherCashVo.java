package com.bytter.biz.finacial.paramconfig.vo;
import com.bytter.core.annotation.BtCompositeSearch;
/**   
 * @Title: Entity
 * @Description: 现金流量科目
 * @author
 * @date 2020-09-10 09:30:39
 * @version V1.0
 *
 */
@SuppressWarnings("serial")
public class FcVoucherCashVo implements java.io.Serializable {
    private String id;
    /**
     * 借方科目代码
     */
    private String debitSubject;
    private String debitSubCode;
    @BtCompositeSearch(fieldName = "fc.subject_name")
    private String debitSubName;
    /**
     * 贷方科目代码
     */
    private String creditSubject;
    private String creditSubCode;
    @BtCompositeSearch(fieldName = "fs.subject_name")
    private String creditSubName;
    /**
     * 借方现金流项目
     */
    private String debitItem;
    private String debitItemName;
    /**
     * 贷方现金流项目
     */
    private String creditItem;
    private String creditItemName;
    /**
     * 有效标识
     */
    private String validSign;
    /**
     * 备注
     */
    private String rmk;
    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }
    public String getDebitSubject() {
        return debitSubject;
    }
    public void setDebitSubject(String debitSubject) {
        this.debitSubject = debitSubject;
    }
    public String getCreditSubject() {
        return creditSubject;
    }
    public void setCreditSubject(String creditSubject) {
        this.creditSubject = creditSubject;
    }
    public String getDebitItem() {
        return debitItem;
    }
    public void setDebitItem(String debitItem) {
        this.debitItem = debitItem;
    }
    public String getCreditItem() {
        return creditItem;
    }
    public void setCreditItem(String creditItem) {
        this.creditItem = creditItem;
    }
    public String getRmk() {
        return rmk;
    }
    public void setRmk(String rmk) {
        this.rmk = rmk;
    }
    public String getDebitSubCode() {
        return debitSubCode;
    }
    public void setDebitSubCode(String debitSubCode) {
        this.debitSubCode = debitSubCode;
    }
    public String getDebitSubName() {
        return debitSubName;
    }
    public void setDebitSubName(String debitSubName) {
        this.debitSubName = debitSubName;
    }
    public String getCreditSubCode() {
        return creditSubCode;
    }
    public void setCreditSubCode(String creditSubCode) {
        this.creditSubCode = creditSubCode;
    }
    public String getCreditSubName() {
        return creditSubName;
    }
    public void setCreditSubName(String creditSubName) {
        this.creditSubName = creditSubName;
    }
    public String getDebitItemName() {
        return debitItemName;
    }
    public void setDebitItemName(String debitItemName) {
        this.debitItemName = debitItemName;
    }
    public String getCreditItemName() {
        return creditItemName;
    }
    public void setCreditItemName(String creditItemName) {
        this.creditItemName = creditItemName;
    }
    public String getValidSign() {
        return validSign;
    }
    public void setValidSign(String validSign) {
        this.validSign = validSign;
    }
}
