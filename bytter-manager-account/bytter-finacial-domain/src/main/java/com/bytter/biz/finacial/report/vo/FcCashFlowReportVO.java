package com.bytter.biz.finacial.report.vo;
import com.bytter.core.annotation.Currency;
import java.io.Serializable;
import java.math.BigDecimal;
public class FcCashFlowReportVO implements Serializable {
    /**
     * 左边项目id
     */
    private String leftItemId;
    /**
     *左边项目
     */
    private String leftItemName;
    /**
     *左边行号
     */
    private String leftOrderNo;
    /**
     *左边金额
     */
    private BigDecimal leftMoney;
    /**
     * 右边边项目id
     */
    private String rightItemId;
    /**
     *右边项目
     */
    private String rightItemName;
    /**
     *右边行号
     */
    private String rightOrderNo;
    /**
     *右边金额
     */
    private BigDecimal rightMoney;
    /**
     * 币种
     */
    private String curCode;
    /**
     *币种名称
     */
    private String curName;
    public String getLeftItemName() {
        return leftItemName;
    }
    public void setLeftItemName(String leftItemName) {
        this.leftItemName = leftItemName;
    }
    public String getLeftOrderNo() {
        return leftOrderNo;
    }
    public void setLeftOrderNo(String leftOrderNo) {
        this.leftOrderNo = leftOrderNo;
    }
    public BigDecimal getLeftMoney() {
        return leftMoney;
    }
    public void setLeftMoney(BigDecimal leftMoney) {
        this.leftMoney = leftMoney;
    }
    public String getRightItemName() {
        return rightItemName;
    }
    public void setRightItemName(String rightItemName) {
        this.rightItemName = rightItemName;
    }
    public String getRightOrderNo() {
        return rightOrderNo;
    }
    public void setRightOrderNo(String rightOrderNo) {
        this.rightOrderNo = rightOrderNo;
    }
    public BigDecimal getRightMoney() {
        return rightMoney;
    }
    public void setRightMoney(BigDecimal rightMoney) {
        this.rightMoney = rightMoney;
    }
    public String getCurCode() {
        return curCode;
    }
    public void setCurCode(String curCode) {
        this.curCode = curCode;
    }
    public String getCurName() {
        return curName;
    }
    public void setCurName(String curName) {
        this.curName = curName;
    }
    public String getLeftItemId() {
        return leftItemId;
    }
    public void setLeftItemId(String leftItemId) {
        this.leftItemId = leftItemId;
    }
    public String getRightItemId() {
        return rightItemId;
    }
    public void setRightItemId(String rightItemId) {
        this.rightItemId = rightItemId;
    }
}
