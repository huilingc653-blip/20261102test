package com.bytter.system.api.vo;
import com.bytter.core.annotation.BtCompositeSearch;
import com.bytter.core.annotation.Corp;
import com.bytter.core.annotation.Currency;
import com.bytter.core.constant.InnerAccountConstant;
import com.bytter.poi.annotation.Excel;
import com.bytter.poi.annotation.ExcelTarget;
import com.bytter.poi.handler.ExcelModel;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
/**
 * Created by zhangxb on 2017/6/14.
 */
@ExcelTarget("FcInitAccVO")
public class FcInitAccVO extends ExcelModel implements Serializable {
    /**
	 * 
	 */
	private static final long serialVersionUID = -5000380136171098061L;
    private String id;
    @BtCompositeSearch(fieldName = "netId")
    @Corp(inject="netName")
    private String netId;
    @BtCompositeSearch(fieldName = "netId")
    private String netName;
    @BtCompositeSearch(fieldName = "corpId")
    private String corpId;
	/**账号*/
    @BtCompositeSearch(fieldName = "account")
    @Excel(name = "*账号",isImportField="true")
    @Size(min = 1, message = InnerAccountConstant.NETBANK_INNER_EXCEL_ERRORMSG_ACCOUNT_NULL)
    private String account;
    @BtCompositeSearch(fieldName = "curId")
    @Currency(inject="curName")
    private String curId;
    @BtCompositeSearch(fieldName = "curId")
    private String curName;
    @BtCompositeSearch(fieldName = "accStartDate")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date accStartDate;
    /**账名*/
    @BtCompositeSearch(fieldName = "accName")
    @Excel(name = "账名",isImportField="true")
    private String accName;
    /**余额*/
    private BigDecimal balance;
    /**余额*/
    private Integer balanceWay;
    /**借方余额*/
    @Excel(name = "*借方金额",isImportField="true",numFormat = "##########0.00",align = HorizontalAlignment.RIGHT)
    private BigDecimal debitMoney;
    /**贷方余额*/
    @Excel(name = "*贷方金额",isImportField="true",numFormat = "##########0.00",align = HorizontalAlignment.RIGHT)
    private BigDecimal creditMoney;
    /**积数*/
    @Excel(name = "*积数",isImportField="true",numFormat = "##########0.00",align = HorizontalAlignment.RIGHT)
    private BigDecimal cumulation;
  
    /**年余额*/
    @Excel(name = "*年初余额",isImportField="true",numFormat = "##########0.00",align = HorizontalAlignment.RIGHT)
    private BigDecimal yearBalance;
    /**年合计*/
    @Excel(name = "*累计发生额",isImportField="true",numFormat = "##########0.00",align = HorizontalAlignment.RIGHT)
    private BigDecimal yearTotal;
    /**状态*/
    @BtCompositeSearch(fieldName = "status")
    private Integer status;
    @Override
	public String getBeanName() {
        return "fcInitAccService";
    }
    public String getNetName() {
        return netName;
    }
    public void setNetName(String netName) {
        this.netName = netName;
    }
    public Date getAccStartDate() {
        return accStartDate;
    }
    public void setAccStartDate(Date accStartDate) {
        this.accStartDate = accStartDate;
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
    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }
    public String getNetId() {
        return netId;
    }
    public void setNetId(String netId) {
        this.netId = netId;
    }
    public String getCorpId() {
        return corpId;
    }
    public void setCorpId(String corpId) {
        this.corpId = corpId;
    }
    public BigDecimal getBalance() {
        return balance;
    }
    public void setBalance(BigDecimal balance) {
        if(this.balanceWay != null) {
            if (this.balanceWay == 1) {
                setDebitMoney(balance);
            } else {
                setCreditMoney(balance);
            }
        }
        this.balance = balance;
    }
    public Integer getBalanceWay() {
        return balanceWay;
    }
    public void setBalanceWay(Integer balanceWay) {
        this.balanceWay = balanceWay;
    }
    public String getAccount() {
        return account;
    }
    public void setAccount(String account) {
        this.account = account;
    }
    public String getAccName() {
        return accName;
    }
    public void setAccName(String accName) {
        this.accName = accName;
    }
    public BigDecimal getDebitMoney() {
        return debitMoney;
    }
    public void setDebitMoney(BigDecimal debitMoney) {
        this.debitMoney = debitMoney;
    }
    public BigDecimal getCreditMoney() {
        return creditMoney;
    }
    public void setCreditMoney(BigDecimal creditMoney) {
        this.creditMoney = creditMoney;
    }
    public BigDecimal getCumulation() {
        return cumulation;
    }
    public void setCumulation(BigDecimal cumulation) {
        this.cumulation = cumulation;
    }
    public BigDecimal getYearBalance() {
        return yearBalance;
    }
    public void setYearBalance(BigDecimal yearBalance) {
        this.yearBalance = yearBalance;
    }
    public BigDecimal getYearTotal() {
        return yearTotal;
    }
    public void setYearTotal(BigDecimal yearTotal) {
        this.yearTotal = yearTotal;
    }
    public Integer getStatus() {
        return status;
    }
    public void setStatus(Integer status) {
        this.status = status;
    }
}
