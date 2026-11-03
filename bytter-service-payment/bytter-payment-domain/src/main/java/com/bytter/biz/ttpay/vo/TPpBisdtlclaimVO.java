package com.bytter.biz.ttpay.vo;
import com.bytter.core.annotation.BtCompositeSearch;
import com.bytter.core.annotation.Currency;
import com.bytter.core.annotation.Dict;
import com.bytter.core.utils.DateUtils;
import com.bytter.core.utils.EmptyUtils;
import com.bytter.biz.payment.api.constant.TtPayConstant;
import com.fasterxml.jackson.annotation.JsonFormat;
import java.math.BigDecimal;
import java.util.Date;
/**
 * @author zhupw
 * @version 1.0
 * @Description 流水认领vo
 * @date 2022-05-17
 */
public class TPpBisdtlclaimVO implements java.io.Serializable {
    private static final long serialVersionUID = 1L;
    private String id;
    /**
     * 单位ID	CORP_ID	VARCHAR2(32)
     **/
    @BtCompositeSearch(fieldName = "d.corp_id")
    private String corpId;
    /**
     * 单位名称
     **/
    @BtCompositeSearch(fieldName = "d.corp_id")
    private String corpCode;
    /**
     * 单位名称
     **/
    @BtCompositeSearch(fieldName = "d.corp_id")
    private String corpName;
    /**
     * 第三方支付方式
     **/
    //@Dict(dictGroup = TtPayConstant.TTPAY_PAYTYPE_DICT, inject = "payTypeName")
    @BtCompositeSearch(fieldName = "d.payType")
    private String payType;
    @BtCompositeSearch(fieldName = "d.payType")
    private String payTypeName;
    /**
     * 应用id
     **/
    @BtCompositeSearch(fieldName = "d.appId")
    private String appId;
    /**
     * 科目信息
     */
    private String itemInfo;
    /*
    计划执行日期
     */
    private String  processDate;
    /*
    流水认领状态
     */
    @BtCompositeSearch(fieldName = "d.fbsStatus")
    private String fbsStatus;
    /**
     * 商户号
     **/
    @BtCompositeSearch(fieldName = "d.mchId")
    private String mchId;
    /**
     * 交易流水号
     **/
    @BtCompositeSearch(fieldName = "d.serialNo")
    private String serialNo;
    /**
     * 订单号
     **/
    @BtCompositeSearch(fieldName = "d.trandNo")
    private String trandNo;
    /**
     * 交易类型
     **/
    @BtCompositeSearch(fieldName = "d.tradeType")
    private String tradeType;
    /**
     * 对方账户
     **/
    @BtCompositeSearch(fieldName = "d.payAcc")
    private String payAcc;
    /**
     * 交易日期
     **/
    @BtCompositeSearch(fieldName = "d.trandTime")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date trandTime;
    /**
     * 退款金额
     **/
    private BigDecimal backMoney;
    /**
     * 交易金额
     **/
    @BtCompositeSearch(fieldName = "d.trandMoney")
    private BigDecimal trandMoney;
    /**
     * 退款审批状态
     **/
    //@Dict(dictGroup = "TTP_REFUND_APPROVE_STATUS", inject = "appStatusName")
    @BtCompositeSearch(fieldName = "t.app_Status")
    private String appStatus;
    private String appStatusName;
    /**
     * 退款ID
     **/
    private String refundId;
    /**
     * 收支标志
     **/
    private String cdSign;
    /**
     * 已退款金额
     **/
    @BtCompositeSearch(fieldName = "o.refundedMoney")
    private BigDecimal refundedMoney;
    /**
     * 退款申请备注
     **/
    private String rmk;
    /**
     * 币别
     */
    @Currency(inject = "curName")
    private String curId;
    /**
     * 币别
     */
    private String curName;
    /**
     * 业务类型
     */
    private String busType;
    /**
     * 业务类型名称
     */
    private String busTypeName;
    /**
     * 预算科目
     */
    private String budget;
    /**
     * 单位是否受控
     */
    private Boolean control;
    /**
     * 币别汇率
     */
    private BigDecimal curExrate;
    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }
    public String getCdSign() {
        return cdSign;
    }
    public void setCdSign(String cdSign) {
        this.cdSign = cdSign;
    }
    public String getCorpId() {
        return corpId;
    }
    public void setCorpId(String corpId) {
        this.corpId = corpId;
    }
    public String getFbsStatus() {
        return fbsStatus;
    }
    public void setFbsStatus(String fbsStatus) {
        this.fbsStatus = fbsStatus;
    }
    public String getTradeType() {
        return tradeType;
    }
    public void setTradeType(String tradeType) {
        this.tradeType = tradeType;
    }
    public String getCorpCode() {
        return corpCode;
    }
    public void setCorpCode(String corpCode) {
        this.corpCode = corpCode;
    }
    public String getCorpName() {
        return corpName;
    }
    public void setCorpName(String corpName) {
        this.corpName = corpName;
    }
    public String getPayType() {
        return payType;
    }
    public void setPayType(String payType) {
        this.payType = payType;
    }
    public String getPayTypeName() {
        return payTypeName;
    }
    public void setPayTypeName(String payTypeName) {
        this.payTypeName = payTypeName;
    }
    public String getAppId() {
        return appId;
    }
    public void setAppId(String appId) {
        this.appId = appId;
    }
    public String getItemInfo() {
        return itemInfo;
    }
    public void setItemInfo(String itemInfo) {
        this.itemInfo = itemInfo;
    }
    public String getProcessDate() {
        return processDate;
    }
    public void setProcessDate(String processDate) {
        this.processDate = processDate;
    }
    public String getMchId() {
        return mchId;
    }
    public void setMchId(String mchId) {
        this.mchId = mchId;
    }
    public String getSerialNo() {
        return serialNo;
    }
    public void setSerialNo(String serialNo) {
        this.serialNo = serialNo;
    }
    public String getTrandNo() {
        return trandNo;
    }
    public void setTrandNo(String trandNo) {
        this.trandNo = trandNo;
    }
    public String getPayAcc() {
        return payAcc;
    }
    public void setPayAcc(String payAcc) {
        this.payAcc = payAcc;
    }
    public Date getTrandTime() {
        return trandTime;
    }
    public void setTrandTime(Date trandTime) {
        this.trandTime = trandTime;
    }
    public BigDecimal getBackMoney() {
        return backMoney;
    }
    public void setBackMoney(BigDecimal backMoney) {
        this.backMoney = backMoney;
    }
    public BigDecimal getTrandMoney() {
        return trandMoney;
    }
    public void setTrandMoney(BigDecimal trandMoney) {
        this.trandMoney = trandMoney;
    }
    public String getAppStatus() {
        return appStatus;
    }
    public void setAppStatus(String appStatus) {
        this.appStatus = appStatus;
    }
    public String getAppStatusName() {
        return appStatusName;
    }
    public void setAppStatusName(String appStatusName) {
        this.appStatusName = appStatusName;
    }
    public String getRefundId() {
        return refundId;
    }
    public void setRefundId(String refundId) {
        this.refundId = refundId;
    }
    public BigDecimal getRefundedMoney() {
        return refundedMoney;
    }
    public void setRefundedMoney(BigDecimal refundedMoney) {
        this.refundedMoney = refundedMoney;
    }
    public String getRmk() {
        return rmk;
    }
    public void setRmk(String rmk) {
        this.rmk = rmk;
    }
    public String getCurId() {
        return curId;
    }
    public void setCurId(String curId) {
        this.curId = curId;
    }
    public String getBusType() {
        return busType;
    }
    public void setBusType(String busType) {
        this.busType = busType;
    }
    public String getBusTypeName() {
        return busTypeName;
    }
    public void setBusTypeName(String busTypeName) {
        this.busTypeName = busTypeName;
    }
    public String getBudget() {
        return budget;
    }
    public void setBudget(String budget) {
        this.budget = budget;
    }
    public Boolean getControl() {
        return control;
    }
    public void setControl(Boolean control) {
        this.control = control;
    }
    public BigDecimal getCurExrate() {
        return curExrate;
    }
    public void setCurExrate(BigDecimal curExrate) {
        this.curExrate = curExrate;
    }
    public String getCurName() {
        return curName;
    }
    public void setCurName(String curName) {
        this.curName = curName;
    }
}
