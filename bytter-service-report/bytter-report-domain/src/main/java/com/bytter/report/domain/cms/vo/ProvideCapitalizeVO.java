package com.bytter.report.domain.cms.vo;
import com.bytter.core.annotation.BtCompositeSearch;
import com.fasterxml.jackson.annotation.JsonFormat;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
/**
 * <br>
 * <b>功能：</b> 借款融资情况查询VO <br>
 * <b>作者：</b>liyh<br>
 * <b>日期：</b> 2020/12/09 0022 <br>
 */
public class ProvideCapitalizeVO implements Serializable {
    private String repayMentId;//还款计划ID
    //业务ID
    private String infoId;
    @BtCompositeSearch(fieldName = "info.bill_code")
    private String infoBillCode;//申请编号
    @BtCompositeSearch(fieldName = "send.bill_code")
    private String sendBillCode;//发放编号
    @BtCompositeSearch(fieldName = "bt.id")
    private String bankType;//银行类型
    private String bankTypeName;//银行类型
    private String bankName;//银行名称
    @BtCompositeSearch(fieldName = "info.corp_id")
    private String corpId;//单位Id
    private String superCorpId;//上级单位Id
    private String netId;//网点Id
    private String corpName;//单位名称
    @BtCompositeSearch(fieldName = "info.cur_id")
    private String curId;//币别ID
    private String curName;//币别名称
    private String natureCur;//单位本位币
    @BtCompositeSearch(fieldName = "send.money")
    private BigDecimal sendMoney;//借款金额
    private BigDecimal leftMoney;//借款余额
    private BigDecimal converMoney;//贷款余额 = 借款余额 - 保证金金额
    private BigDecimal plRate;//借款利率
    @BtCompositeSearch(fieldName = "send.send_date")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date sendDate;//开始日期
    @BtCompositeSearch(fieldName = "crs.predice_repay_date")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date repayStartDate;//还款开始日期
    @BtCompositeSearch(fieldName = "crs.predice_repay_date")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date repayDate;//还款到期日期
    @BtCompositeSearch(fieldName = "info.pl_class")
    private String plClass;//借款种类名称
    private String plClassName;//借款种类名称
    @BtCompositeSearch(fieldName = "rb.money")
    private BigDecimal rationMoney;//授信额度
    @BtCompositeSearch(fieldName = "rb.begindate")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date rationBegindate;//授信开始时间
    @BtCompositeSearch(fieldName = "rb.enddate")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date rationEnddate;//授信结束时间
    private String hangGuarantee;//是否挂担保
    private BigDecimal guaMoney;//担保额度
    private String guaWay;//担保方式
    private String contractCode;//担保合同
    private Date startDate;//担保开始日
    private Date endDate;//担保截止日
    private String rmk;//担保备注
    private String guaOtherRmk;//其他担保备注
    private Date guaOtherDate;//担保合同披露时间
    private BigDecimal bailMoney;//保证金金额
    private BigDecimal bailRate;//保证金比例
    public String getRepayMentId() {
        return repayMentId;
    }
    public void setRepayMentId(String repayMentId) {
        this.repayMentId = repayMentId;
    }
    public String getInfoId() {
        return infoId;
    }
    public void setInfoId(String infoId) {
        this.infoId = infoId;
    }
    public String getInfoBillCode() {
        return infoBillCode;
    }
    public void setInfoBillCode(String infoBillCode) {
        this.infoBillCode = infoBillCode;
    }
    public String getSendBillCode() {
        return sendBillCode;
    }
    public void setSendBillCode(String sendBillCode) {
        this.sendBillCode = sendBillCode;
    }
    public String getBankType() {
        return bankType;
    }
    public void setBankType(String bankType) {
        this.bankType = bankType;
    }
    public String getBankTypeName() {
        return bankTypeName;
    }
    public void setBankTypeName(String bankTypeName) {
        this.bankTypeName = bankTypeName;
    }
    public String getBankName() {
        return bankName;
    }
    public void setBankName(String bankName) {
        this.bankName = bankName;
    }
    public String getCorpId() {
        return corpId;
    }
    public void setCorpId(String corpId) {
        this.corpId = corpId;
    }
    public String getSuperCorpId() {
        return superCorpId;
    }
    public void setSuperCorpId(String superCorpId) {
        this.superCorpId = superCorpId;
    }
    public String getNetId() {
        return netId;
    }
    public void setNetId(String netId) {
        this.netId = netId;
    }
    public String getCorpName() {
        return corpName;
    }
    public void setCorpName(String corpName) {
        this.corpName = corpName;
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
    public String getNatureCur() {
        return natureCur;
    }
    public void setNatureCur(String natureCur) {
        this.natureCur = natureCur;
    }
    public BigDecimal getSendMoney() {
        return sendMoney;
    }
    public void setSendMoney(BigDecimal sendMoney) {
        this.sendMoney = sendMoney;
    }
    public BigDecimal getLeftMoney() {
        if (null == leftMoney) {
            leftMoney = BigDecimal.ZERO;
        }
        return leftMoney;
    }
    public void setLeftMoney(BigDecimal leftMoney) {
        this.leftMoney = leftMoney;
    }
    public BigDecimal getConverMoney() {
        if (null == converMoney) {
            converMoney = BigDecimal.ZERO;
        }
        return converMoney;
    }
    public void setConverMoney(BigDecimal converMoney) {
        this.converMoney = converMoney;
    }
    public BigDecimal getPlRate() {
        return plRate;
    }
    public void setPlRate(BigDecimal plRate) {
        this.plRate = plRate;
    }
    public Date getSendDate() {
        return sendDate;
    }
    public void setSendDate(Date sendDate) {
        this.sendDate = sendDate;
    }
    public Date getRepayStartDate() {
        return repayStartDate;
    }
    public void setRepayStartDate(Date repayStartDate) {
        this.repayStartDate = repayStartDate;
    }
    public Date getRepayDate() {
        return repayDate;
    }
    public void setRepayDate(Date repayDate) {
        this.repayDate = repayDate;
    }
    public String getPlClass() {
        return plClass;
    }
    public void setPlClass(String plClass) {
        this.plClass = plClass;
    }
    public String getPlClassName() {
        return plClassName;
    }
    public void setPlClassName(String plClassName) {
        this.plClassName = plClassName;
    }
    public String getRmk() {
        return rmk;
    }
    public void setRmk(String rmk) {
        this.rmk = rmk;
    }
    public String getHangGuarantee() {
        return hangGuarantee;
    }
    public void setHangGuarantee(String hangGuarantee) {
        this.hangGuarantee = hangGuarantee;
    }
    public String getGuaWay() {
        return guaWay;
    }
    public void setGuaWay(String guaWay) {
        this.guaWay = guaWay;
    }
    public BigDecimal getBailMoney() {
        if (null == bailMoney) {
            bailMoney = BigDecimal.ZERO;
        }
        return bailMoney;
    }
    public void setBailMoney(BigDecimal bailMoney) {
        this.bailMoney = bailMoney;
    }
    public BigDecimal getBailRate() {
        return bailRate;
    }
    public void setBailRate(BigDecimal bailRate) {
        this.bailRate = bailRate;
    }
    public BigDecimal getGuaMoney() {
        if (null == guaMoney) {
            guaMoney = BigDecimal.ZERO;
        }
        return guaMoney;
    }
    public void setGuaMoney(BigDecimal guaMoney) {
        this.guaMoney = guaMoney;
    }
    public String getContractCode() {
        return contractCode;
    }
    public void setContractCode(String contractCode) {
        this.contractCode = contractCode;
    }
    public Date getStartDate() {
        return startDate;
    }
    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }
    public Date getEndDate() {
        return endDate;
    }
    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }
    public String getGuaOtherRmk() {
        return guaOtherRmk;
    }
    public void setGuaOtherRmk(String guaOtherRmk) {
        this.guaOtherRmk = guaOtherRmk;
    }
    public Date getGuaOtherDate() {
        return guaOtherDate;
    }
    public void setGuaOtherDate(Date guaOtherDate) {
        this.guaOtherDate = guaOtherDate;
    }
    public BigDecimal getRationMoney() {
        return rationMoney;
    }
    public void setRationMoney(BigDecimal rationMoney) {
        this.rationMoney = rationMoney;
    }
    public Date getRationBegindate() {
        return rationBegindate;
    }
    public void setRationBegindate(Date rationBegindate) {
        this.rationBegindate = rationBegindate;
    }
    public Date getRationEnddate() {
        return rationEnddate;
    }
    public void setRationEnddate(Date rationEnddate) {
        this.rationEnddate = rationEnddate;
    }
}
