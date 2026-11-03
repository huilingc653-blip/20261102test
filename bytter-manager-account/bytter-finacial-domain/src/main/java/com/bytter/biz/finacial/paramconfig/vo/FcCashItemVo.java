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
public class FcCashItemVo implements java.io.Serializable {
    private String id;
    /**现金流项目代码*/
    @BtCompositeSearch(fieldName = "a.item_code")
    private String itemCode;
    /**现金流项目名称*/
    @BtCompositeSearch(fieldName = "a.item_name")
    private String itemName;
    /**备注*/
    @BtCompositeSearch(fieldName = "a.rmk")
    private String rmk;
    /**有效标识*/
    @BtCompositeSearch(fieldName = "a.valid_sign")
    private String validSign;
    /**流入流出*/
    private String inOut;
    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }
    public String getItemCode() {
        return itemCode;
    }
    public void setItemCode(String itemCode) {
        this.itemCode = itemCode;
    }
    public String getItemName() {
        return itemName;
    }
    public void setItemName(String itemName) {
        this.itemName = itemName;
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
    public String getInOut() {
        return inOut;
    }
    public void setInOut(String inOut) {
        this.inOut = inOut;
    }
}
