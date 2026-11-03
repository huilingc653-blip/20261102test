package com.bytter.report.domain.cms.vo;

import com.bytter.core.annotation.Corp;
import com.bytter.core.annotation.Currency;
import com.bytter.core.annotation.Dict;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author luxy
 * @Title: RatBkCalculateVO
 * @ProjectName T3.4
 * @Description: TODO
 * @date 2020/7/28 11:17
 */
public class RatBkCalculateVO  {

    /** 银行编号 */
    private java.lang.String bankCode;
    /** 银行编号 */
    private java.lang.String bankName;
    /** 币别*/
    @Currency(inject = "curName")
    private java.lang.String curCode;
    private java.lang.String curName;
    /** 单位代码 */
    @Corp(inject = "corpName")
    private java.lang.String corpId;
    /** 单位代码 */
    private java.lang.String corpName;
    /** 授信开始日期 **/
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date beginDate;
    /** 授信结束日期 **/
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date endDate;
    /** 授信品种标识  0-单品种 1-综合品种 **/
    private String itemFlag;
    /** 授信品种编号 **/
    @Dict(tableName="RAT_ITEM_CUSTOM",valueFieldName="id",showFiledName="NAME",inject="itemName")
    private String itemCode;
    /** 授信品种名称 **/
    private String itemName;
    /** 授信品种排序 **/
    private Integer itemOrder;
    /** 授信总额度(单品种) **/
    private BigDecimal money;
    /** 授信总额度(综合品种) **/
    private BigDecimal maxMoney;
    /** 占用额度 **/
    private BigDecimal useMoney;
    /** 在审占用额度 **/
    private BigDecimal approveMoney;
    /**预测释放额度**/
    private BigDecimal calReleaseMoney;
    /**预测剩余额度**/
    private BigDecimal calLeftMoney;
    /**
     * 授信code
     */
    private String ratCode;
    /**
     * 分配ID
     */
    private String disCode;
    /**
     * 系统授信品种类型
     */
    private String sysCode;
    /**
     * 占用授信业务ID
     */
    private String billId;

    /**
     * 占用授信业务单号
     */
    private String billNo;

    private int totalSize;
    //单位分组明细
    private List<RatBkUseCorpVO> corpDtls;

    /**占用授信业务币别**/
    private String billCurCode;
    /**占用授信汇率*/
    private java.math.BigDecimal exchangeRate;


    public String getBankCode() {
        return bankCode;
    }

    public void setBankCode(String bankCode) {
        this.bankCode = bankCode;
    }

    public String getBankName() {
        return bankName;
    }

    public void setBankName(String bankName) {
        this.bankName = bankName;
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

    public String getCorpId() {
        return corpId;
    }

    public void setCorpId(String corpId) {
        this.corpId = corpId;
    }

    public String getCorpName() {
        return corpName;
    }

    public void setCorpName(String corpName) {
        this.corpName = corpName;
    }

    public Date getBeginDate() {
        return beginDate;
    }

    public void setBeginDate(Date beginDate) {
        this.beginDate = beginDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public String getItemFlag() {
        return itemFlag;
    }

    public void setItemFlag(String itemFlag) {
        this.itemFlag = itemFlag;
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

    public Integer getItemOrder() {
        return itemOrder;
    }

    public void setItemOrder(Integer itemOrder) {
        this.itemOrder = itemOrder;
    }

    public BigDecimal getMoney() {
        return money;
    }

    public void setMoney(BigDecimal money) {
        this.money = money;
    }

    public BigDecimal getMaxMoney() {
        return maxMoney;
    }

    public void setMaxMoney(BigDecimal maxMoney) {
        this.maxMoney = maxMoney;
    }

    public BigDecimal getUseMoney() {
        return useMoney;
    }

    public void setUseMoney(BigDecimal useMoney) {
        this.useMoney = useMoney;
    }

    public BigDecimal getApproveMoney() {
        return approveMoney;
    }

    public void setApproveMoney(BigDecimal approveMoney) {
        this.approveMoney = approveMoney;
    }

    public BigDecimal getCalReleaseMoney() {
        return calReleaseMoney;
    }

    public void setCalReleaseMoney(BigDecimal calReleaseMoney) {
        this.calReleaseMoney = calReleaseMoney;
    }

    public BigDecimal getCalLeftMoney() {
        return calLeftMoney;
    }

    public void setCalLeftMoney(BigDecimal calLeftMoney) {
        this.calLeftMoney = calLeftMoney;
    }

    public String getRatCode() {
        return ratCode;
    }

    public void setRatCode(String ratCode) {
        this.ratCode = ratCode;
    }

    public String getDisCode() {
        return disCode;
    }

    public void setDisCode(String disCode) {
        this.disCode = disCode;
    }

    public String getSysCode() {
        return sysCode;
    }

    public void setSysCode(String sysCode) {
        this.sysCode = sysCode;
    }

    public String getBillId() {
        return billId;
    }

    public void setBillId(String billId) {
        this.billId = billId;
    }

    public String getBillNo() {
        return billNo;
    }

    public void setBillNo(String billNo) {
        this.billNo = billNo;
    }

    public int getTotalSize() {
        return totalSize;
    }

    public void setTotalSize(int totalSize) {
        this.totalSize = totalSize;
    }

    public List<RatBkUseCorpVO> getCorpDtls() {
        return corpDtls;
    }

    public void setCorpDtls(List<RatBkUseCorpVO> corpDtls) {
        this.corpDtls = corpDtls;
    }

    // 添加单位明细
    public void addCorpDtl(RatBkUseCorpVO corpDtl) {
        if (null == this.corpDtls) {
            this.corpDtls = new ArrayList<RatBkUseCorpVO>();
        }
        this.corpDtls.add(corpDtl);
    }

    public String getBillCurCode() {
        return billCurCode;
    }

    public void setBillCurCode(String billCurCode) {
        this.billCurCode = billCurCode;
    }

    public BigDecimal getExchangeRate() {
        return exchangeRate;
    }

    public void setExchangeRate(BigDecimal exchangeRate) {
        this.exchangeRate = exchangeRate;
    }
}
