package com.bytter.biz.inneraccount.vo;
import com.bytter.biz.finacial.api.dto.BtSummaryDTO;
import com.bytter.biz.finacial.api.dto.FcAccbookVoucherDTO;
import com.bytter.biz.finacial.api.dto.FcVoucherEntryDefineDTO;
import com.bytter.biz.finacial.api.vo.FcOnlineLedgerVO;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.bytter.system.api.vo.FcAccVO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
/**
 * Created by zhangxb on 2017/9/29.
 */
@ApiModel("销户信息")
public class FcCancelAccountVO implements Serializable {
    @ApiModelProperty(value = "账户信息")
    private FcAccVO fcAcc=new FcAccVO();
    @ApiModelProperty(value = "在线账簿信息")
    private FcOnlineLedgerVO fcOnlineLedger=new FcOnlineLedgerVO();
    /**
     * 自定义会计分录
     */
    @ApiModelProperty(value = "自定义会计分录")
    private List<FcVoucherEntryDefineDTO> listFcVoucher;
    /**
     * 转出账号集合
     */
    @ApiModelProperty(value = "转出账号集合")
    private List<FcAccVO> transferOutAcc;
    /**
     * 摘要集合
     */
    @ApiModelProperty(value = "摘要集合")
    private List<BtSummaryDTO> listBtSummary=new ArrayList<>();
    /**
     * 调用会计分录得到到的结果
     */
    @ApiModelProperty(value = "调用会计分录得到到的结果")
    private FcAccbookVoucherDTO fcAccbookVoucher;
    /**
     * 利息
     */
    @ApiModelProperty(value = "利息")
    private BigDecimal interest;
    /**
     * 预提/冲预提金额
     */
    @ApiModelProperty(value = "预提/冲预提金额")
    private BigDecimal rushMoney;
    /**
     * 记账日期
     */
    @ApiModelProperty(value = "记账日期")
    private Date dayOver;
    /**
     * 1 需要 0 不需要
     * 如果账号余额和利息都为0，则不需要定义会计分录false
     */
    @ApiModelProperty(value = "是否需要业务规则 1 需要 0 不需要")
    private String isNeedBusRule;
    /**
     * 1 标识单位下没有利息支出企业活期账号 ，给予提示
     *
     */
    @ApiModelProperty(value = "标识单位下没有利息支出企业活期账号")
    private Integer isInterestAcc;
	/**
	 * 摘要
	 */
	@ApiModelProperty(value = "摘要数组")
	private String[] summaryContent = new String[0];
	/**
	 * 账号
	 */
	@ApiModelProperty(value = "账号数组")
	private String[] account = new String[0];
    /**
     * 凭证种类
     */
    @ApiModelProperty(value = "凭证种类")
    private String voucherType;
    @JsonFormat(pattern = "yyyy-MM-dd")
    @ApiModelProperty(value = "开始日期")
    private Date startDate;
    @ApiModelProperty(value = "余额账户")
    private String blnctrAcc;
    @ApiModelProperty(value = "主键ID")
    private String id;
    @ApiModelProperty(value = "业务全局ID")
    private String busGlobalId;
    /**
     * 转入账号
     */
    @ApiModelProperty(value = "转入账号")
    private String transferAcc;
	/**
	 * 借方金额
	 */
	@ApiModelProperty(value = "借方金额数组")
	private BigDecimal[] debitMoney = new BigDecimal[0];
	/**
	 * 贷方金额
	 */
	@ApiModelProperty(value = "贷方金额数组")
	private BigDecimal[] creditMoney = new BigDecimal[0];
    public String getBusGlobalId() {
        return busGlobalId;
    }
    public void setBusGlobalId(String busGlobalId) {
        this.busGlobalId = busGlobalId;
    }
    public String getTransferAcc() {
        return transferAcc;
    }
    public void setTransferAcc(String transferAcc) {
        this.transferAcc = transferAcc;
    }
    public FcAccbookVoucherDTO getFcAccbookVoucher() {
        return fcAccbookVoucher;
    }
    public void setFcAccbookVoucher(FcAccbookVoucherDTO fcAccbookVoucher) {
        this.fcAccbookVoucher = fcAccbookVoucher;
    }
    public String[] getSummaryContent() {
        return summaryContent;
    }
    public void setSummaryContent(String[] summaryContent) {
        this.summaryContent = summaryContent;
    }
    public String[] getAccount() {
        return account;
    }
    public void setAccount(String[] account) {
        this.account = account;
    }
    public Date getStartDate() {
        return startDate;
    }
    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }
    public String getBlnctrAcc() {
        return blnctrAcc;
    }
    public void setBlnctrAcc(String blnctrAcc) {
        this.blnctrAcc = blnctrAcc;
    }
    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }
    public String getVoucherType() {
        return voucherType;
    }
    public void setVoucherType(String voucherType) {
        this.voucherType = voucherType;
    }
    public BigDecimal[] getDebitMoney() {
        return debitMoney;
    }
    public void setDebitMoney(BigDecimal[] debitMoney) {
        this.debitMoney = debitMoney;
    }
    public BigDecimal[] getCreditMoney() {
        return creditMoney;
    }
    public void setCreditMoney(BigDecimal[] creditMoney) {
        this.creditMoney = creditMoney;
    }
    public List<BtSummaryDTO> getListBtSummary() {
        return listBtSummary;
    }
    public void setListBtSummary(List<BtSummaryDTO> listBtSummary) {
        this.listBtSummary = listBtSummary;
    }
    public String getIsNeedBusRule() {
        return isNeedBusRule;
    }
    public void setIsNeedBusRule(String isNeedBusRule) {
        this.isNeedBusRule = isNeedBusRule;
    }
    public List<FcAccVO> getTransferOutAcc() {
        return transferOutAcc;
    }
    public void setTransferOutAcc(List<FcAccVO> transferOutAcc) {
        this.transferOutAcc = transferOutAcc;
    }
    public List<FcVoucherEntryDefineDTO> getListFcVoucher() {
        return listFcVoucher;
    }
    public void setListFcVoucher(List<FcVoucherEntryDefineDTO> listFcVoucher) {
        this.listFcVoucher = listFcVoucher;
    }
    public FcAccVO getFcAcc() {
        return fcAcc;
    }
    public void setFcAcc(FcAccVO fcAcc) {
        this.fcAcc = fcAcc;
    }
    public FcOnlineLedgerVO getFcOnlineLedger() {
        return fcOnlineLedger;
    }
    public void setFcOnlineLedger(FcOnlineLedgerVO fcOnlineLedger) {
        this.fcOnlineLedger = fcOnlineLedger;
    }
    public BigDecimal getInterest() {
        return interest;
    }
    public void setInterest(BigDecimal interest) {
        this.interest = interest;
    }
    public BigDecimal getRushMoney() {
        return rushMoney;
    }
    public void setRushMoney(BigDecimal rushMoney) {
        this.rushMoney = rushMoney;
    }
    public Date getDayOver() {
        return dayOver;
    }
    public void setDayOver(Date dayOver) {
        this.dayOver = dayOver;
    }
    public Integer getIsInterestAcc() {
        return isInterestAcc;
    }
    public void setIsInterestAcc(Integer isInterestAcc) {
        this.isInterestAcc = isInterestAcc;
    }
}
