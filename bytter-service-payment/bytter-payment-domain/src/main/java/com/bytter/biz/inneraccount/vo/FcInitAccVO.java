package com.bytter.biz.inneraccount.vo;
import com.bytter.core.annotation.BtCompositeSearch;
import com.bytter.core.annotation.Corp;
import com.bytter.core.annotation.Currency;
import com.bytter.biz.payment.api.constant.InnerAccountConstant;
import com.bytter.core.annotation.Dict;
import com.bytter.poi.annotation.Excel;
import com.bytter.poi.annotation.ExcelTarget;
import com.bytter.poi.handler.ExcelModel;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
/**
 * Created by zhangxb on 2017/6/14.
 */
@ExcelTarget("FcInitAccVO")
@ApiModel("账户初始化信息")
public class FcInitAccVO extends ExcelModel implements Serializable {
    /**
	 * 
	 */
	private static final long serialVersionUID = -5000380136171098061L;
    @ApiModelProperty(value = "主键ID")
    private String id;
    @BtCompositeSearch(fieldName = "netId")
    @Corp(inject="netName")
    @ApiModelProperty(value = "网点ID")
    private String netId;
    @BtCompositeSearch(fieldName = "netId")
    @ApiModelProperty(value = "网点名称")
    private String netName;
    @BtCompositeSearch(fieldName = "corpId")
    @ApiModelProperty(value = "单位ID")
    private String corpId;
	/**账号*/
    @BtCompositeSearch(fieldName = "account")
    @Excel(name = "*账号",isImportField="true")
    @Size(min = 1, message = InnerAccountConstant.NETBANK_INNER_EXCEL_ERRORMSG_ACCOUNT_NULL)
    @ApiModelProperty(value = "账号")
    private String account;
    @BtCompositeSearch(fieldName = "curId")
    @Currency(inject="curName")
    @ApiModelProperty(value = "币种ID")
    private String curId;
    @BtCompositeSearch(fieldName = "curId")
    @ApiModelProperty(value = "币种名称")
    private String curName;
    @BtCompositeSearch(fieldName = "accStartDate")
    @JsonFormat(pattern = "yyyy-MM-dd")
    @ApiModelProperty(value = "账户开始日期")
    private Date accStartDate;
    /**账名*/
    @BtCompositeSearch(fieldName = "accName")
    @Excel(name = "账名",isImportField="true")
    @ApiModelProperty(value = "账名")
    private String accName;
    /**余额*/
    @ApiModelProperty(value = "余额")
    private BigDecimal balance;
    /**余额*/
    @ApiModelProperty(value = "余额方式")
    private Integer balanceWay;
    /**借方余额*/
    @Excel(name = "*借方金额",isImportField="true",numFormat = "##########0.00",align = HorizontalAlignment.RIGHT)
    @ApiModelProperty(value = "借方金额")
    private BigDecimal debitMoney;
    /**贷方余额*/
    @Excel(name = "*贷方金额",isImportField="true",numFormat = "##########0.00",align = HorizontalAlignment.RIGHT)
    @ApiModelProperty(value = "贷方金额")
    private BigDecimal creditMoney;
    /**积数*/
    @Excel(name = "*积数",isImportField="true",numFormat = "##########0.00",align = HorizontalAlignment.RIGHT)
    @ApiModelProperty(value = "积数")
    private BigDecimal cumulation;
  
    /**年余额*/
    @Excel(name = "*年初余额",isImportField="true",numFormat = "##########0.00",align = HorizontalAlignment.RIGHT)
    @ApiModelProperty(value = "年初余额")
    private BigDecimal yearBalance;
    /**年合计*/
    @Excel(name = "*累计发生额",isImportField="true",numFormat = "##########0.00",align = HorizontalAlignment.RIGHT)
    @ApiModelProperty(value = "累计发生额")
    private BigDecimal yearTotal;
    /**状态*/
    @BtCompositeSearch(fieldName = "status")
    @Dict(dictGroup = "inneraccount.fcInitStatus", inject = "statusName")
    @ApiModelProperty(value = "状态")
    private Integer status;
    @ApiModelProperty(value = "状态名称")
    private String statusName;
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

    public String getStatusName() {
        return statusName;
    }

    public void setStatusName(String statusName) {
        this.statusName = statusName;
    }
}
