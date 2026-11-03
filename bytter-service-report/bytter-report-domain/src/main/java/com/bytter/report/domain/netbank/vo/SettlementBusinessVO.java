package com.bytter.report.domain.netbank.vo;
import com.bytter.core.annotation.Currency;
import com.bytter.core.annotation.Dict;
import java.io.Serializable;
import java.math.BigDecimal;
/**
 * 结算业务统计表VO
 *
 * @author qijh 2022-11-30
 */
public class SettlementBusinessVO implements Serializable {
    /**
     * 付款方式ID
     */
    @Dict(tableName="FC_VOUCHER_TYPE",valueFieldName="id",showFiledName="VOUCHER_NAME",inject="voucherName")
    private String voucherTypeId;
    /**
     * 付款方式名称
     */
    private String voucherName;
    /**
     * 币别
     */
    @Currency(inject="curName")
    private String curId;
    /**
     * 币别名称
     */
    private String curName;
    /**
     * 记账交易金额
     */
    private BigDecimal tallyMoney;
    /**
     * 记账处理笔数
     */
    private Integer tallyNumbers;
    /**折合汇率*/
    private BigDecimal curExrate;
    public String getVoucherTypeId() {
        return voucherTypeId;
    }
    public void setVoucherTypeId(String voucherTypeId) {
        this.voucherTypeId = voucherTypeId;
    }
    public String getVoucherName() {
        return voucherName;
    }
    public void setVoucherName(String voucherName) {
        this.voucherName = voucherName;
    }
    public String getCurId() {
        return curId;
    }
    public void setCurId(String curId) {
        this.curId = curId;
    }
    public String getCurName() {
        return curName;
    }
    public void setCurName(String curName) {
        this.curName = curName;
    }
    public BigDecimal getTallyMoney() {
        return tallyMoney;
    }
    public void setTallyMoney(BigDecimal tallyMoney) {
        this.tallyMoney = tallyMoney;
    }
    public Integer getTallyNumbers() {
        return tallyNumbers;
    }
    public void setTallyNumbers(Integer tallyNumbers) {
        this.tallyNumbers = tallyNumbers;
    }
    public BigDecimal getCurExrate() {
        return curExrate;
    }
    public void setCurExrate(BigDecimal curExrate) {
        this.curExrate = curExrate;
    }
}
