package com.bytter.report.domain.financial.vo;
import com.bytter.core.annotation.Corp;
import com.bytter.core.annotation.Currency;
import java.io.Serializable;
/**
 * 总账查询VO对象
 *
 * @author zuoy 2020-11-19
 */
public class GeneralLedgerVO implements Serializable {
    /**
     * 单位ID
     */
    @Corp(inject = "netName")
    private String netId;
    /**
     * 单位名称
     */
    private String netName;
    /**
     * 币别ID
     */
    @Currency(inject = "curName")
    private String curId;
    /**
     * 币别名称
     */
    private String curName;
    /**
     * 记账日期
     */
    private String ledgerDate;
    public String getNetId() {
        return netId;
    }
    public void setNetId(String netId) {
        this.netId = netId;
    }
    public String getNetName() {
        return netName;
    }
    public void setNetName(String netName) {
        this.netName = netName;
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
    public String getLedgerDate() {
        return ledgerDate;
    }
    public void setLedgerDate(String ledgerDate) {
        this.ledgerDate = ledgerDate;
    }
}
