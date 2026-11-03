package com.bytter.bill.domain.bill.vo;

import com.bytter.core.annotation.BtCompositeSearch;
import com.bytter.core.annotation.Dict;
import com.bytter.core.bpm.IBusinessKey;
import com.bytter.poi.annotation.Excel;
import com.bytter.system.api.dto.DictDTO;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;
import java.util.Map;
import java.util.Optional;

@SuppressWarnings("serial")
@Data
public class AbBillNonassignableCancelVo implements java.io.Serializable, IBusinessKey {
    private String id;
    /**
     * ab_voucherinfo.id
     */
    private String vid;
    /**
     * ab_voucherinf_vice.id
     */
    private String vvid;
    /**
     * ab_accrecords.id
     */
    private String accId;
    /**
     * 申请单位
     */
    @BtCompositeSearch(fieldName = "vice.apply_corp_id")
    private String applyCorpId;
    /**
     * 申请单位名称
     */
    private String applyCorpName;
    private String applyCorpCode;
    /**
     * 票据单位
     */
    @BtCompositeSearch(fieldName = "vice.corp_id")
    private String corpId;
    /**
     * 票据单位名称
     */
    private String corpName;
    /**
     * 业务单位
     */
    @BtCompositeSearch(fieldName = "vice.bus_corp_id")
    private String busCorpId;
    /**
     * 业务单位名称
     */
    private String busCorpName;
    /**
     * 业务人员
     */
    @BtCompositeSearch(fieldName = "vice.bus_personnel")
    private String busPersonnel;
    /**
     * 流通介质：10 纸票、11 电票
     */
    @Dict(dictGroup = "AB_BILL_FLAG", inject = "billFlagName")
    @BtCompositeSearch(fieldName = "info.bill_Flag")
    private Integer billFlag;
    /**
     * 流通介质名称：10 纸票、11 电票
     */
    @Excel(name = "流通介质")
    @BtCompositeSearch(fieldName = "info.bill_Flag")
    private String billFlagName;
    /**
     * 票据类型
     */
    @Dict(dictGroup = "AB_BILL_TYPE", inject = "billTypeName")
    @BtCompositeSearch(fieldName = "info.bill_type")
    private String billType;
    @BtCompositeSearch(fieldName = "info.bill_type")
    private String billTypeName;
    /**
     * 票据号码
     */
    @BtCompositeSearch(fieldName = "info.orig_no")
    private String origNo;
    /**
     * 子票区间开始序号
     */
    @BtCompositeSearch(fieldName = "info.sbbll_rng_strt_sn")
    private String sbbllRngStrtSn;

    /**
     * 子票区间结束序号
     */
    @BtCompositeSearch(fieldName = "info.sbbll_rng_end_sn")
    private String sbbllRngEndSn;
    /**
     * BL_STD_AMT 标准金额
     */
    private BigDecimal blStdAmt;
    /**
     *  票据(包)金额
     */
    @BtCompositeSearch(fieldName = "info.bill_money")
    private BigDecimal billMoney;

    /**
     * payer 出票人
     */
    private String payer;

    /**
     * 出票行
     */
    private String payBank;

    /**
     * 承兑人
     */
    private String acceptor;

    /**
     * info.start_date 出票日期
     */
    @BtCompositeSearch(fieldName = "info.start_date")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date startDate;

    /**
     * info.end_date  到期日期'
     */
    @BtCompositeSearch(fieldName = "info.end_date")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date endDate;

    @BtCompositeSearch(fieldName = "records.business_date")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date businessDate;

    /**
     * 前手
     */
    private String cnAccrecords;

    /**
     * 转让方式
     */
    private String billSource;

    /**
     * 业务类型
     */
    private String briefId;

    /**
     * send_sign '是否发送指令标志：0否； 1是 ；' 线上线下标志
     */
    @Dict(dictGroup = "AB_BILL_ONLINE_OFFLINE_TYPE", inject = "sendSignName")
    private String sendSign;

    private String sendSignName;


    /**
     * 是否允许分包 blgrdbagcrcl_prmt_ind is '是否允许分包。1 是  0 否
     */
    @BtCompositeSearch(fieldName = "info.BLGRDBAGCRCL_PRMT_IND")
    @Dict(dictGroup = "AB_BILL_BLGRDBAGCRCL_PRMT_IND", inject = "blgrdbagcrclPrmtIndName")
    private String blgrdbagcrclPrmtInd;

    @BtCompositeSearch(fieldName = "info.BLGRDBAGCRCL_PRMT_IND")
    private String blgrdbagcrclPrmtIndName;

    /**
     * 票据单位社会统一识别代码
     */
    private String blHoldUnnIdNo;
    @BtCompositeSearch(fieldName = "vice.BANK_TYPE_ID")
    private String bankTypeId;
    public String getBankTypeId() {
        return bankTypeId;
    }
    public void setBankTypeId(String bankTypeId) {
        this.bankTypeId = bankTypeId;
    }
    @Override
    public String getBusinessKey() {
        return id;
    }
    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }
    public AbBillNonassignableCancelVo() {
    }
    public String getApplyCorpCode() {
        return applyCorpCode;
    }
    public void setApplyCorpCode(String applyCorpCode) {
        this.applyCorpCode = applyCorpCode;
    }
    public AbBillNonassignableCancelVo(String id, String vid, String vvid, String accId, String applyCorpId, String applyCorpName,
                                       String applyCorpCode, String corpId, String corpName, String busCorpId, String busCorpName,
                                       String busPersonnel, Integer billFlag, String billFlagName, String billType, String billTypeName,
                                       String origNo, String sbbllRngStrtSn, String sbbllRngEndSn, BigDecimal blStdAmt, BigDecimal billMoney,
                                       String payer, String payBank, String acceptor, Date startDate, Date endDate, Date businessDate, String cnAccrecords,
                                       String billSource, String briefId, String sendSign, String sendSignName, String blgrdbagcrclPrmtInd,
                                       String blgrdbagcrclPrmtIndName, String blHoldUnnIdNo) {
        this.id = id;
        this.vid = vid;
        this.vvid = vvid;
        this.accId = accId;
        this.applyCorpId = applyCorpId;
        this.applyCorpName = applyCorpName;
        this.applyCorpCode = applyCorpCode;
        this.corpId = corpId;
        this.corpName = corpName;
        this.busCorpId = busCorpId;
        this.busCorpName = busCorpName;
        this.busPersonnel = busPersonnel;
        this.billFlag = billFlag;
        this.billFlagName = billFlagName;
        this.billType = billType;
        this.billTypeName = billTypeName;
        this.origNo = origNo;
        this.sbbllRngStrtSn = sbbllRngStrtSn;
        this.sbbllRngEndSn = sbbllRngEndSn;
        this.blStdAmt = blStdAmt;
        this.billMoney = billMoney;
        this.payer = payer;
        this.payBank = payBank;
        this.acceptor = acceptor;
        this.startDate = startDate;
        this.endDate = endDate;
        this.businessDate = businessDate;
        this.cnAccrecords = cnAccrecords;
        this.billSource = billSource;
        this.briefId = briefId;
        this.sendSign = sendSign;
        this.sendSignName = sendSignName;
        this.blgrdbagcrclPrmtInd = blgrdbagcrclPrmtInd;
        this.blgrdbagcrclPrmtIndName = blgrdbagcrclPrmtIndName;
        this.blHoldUnnIdNo = blHoldUnnIdNo;
    }

    public String getVid() {
        return vid;
    }

    public void setVid(String vid) {
        this.vid = vid;
    }

    public String getVvid() {
        return vvid;
    }

    public void setVvid(String vvid) {
        this.vvid = vvid;
    }

    public String getAccId() {
        return accId;
    }

    public void setAccId(String accId) {
        this.accId = accId;
    }

    public String getApplyCorpId() {
        return applyCorpId;
    }

    public void setApplyCorpId(String applyCorpId) {
        this.applyCorpId = applyCorpId;
    }

    public String getCorpId() {
        return corpId;
    }

    public void setCorpId(String corpId) {
        this.corpId = corpId;
    }

    public String getBusCorpId() {
        return busCorpId;
    }

    public void setBusCorpId(String busCorpId) {
        this.busCorpId = busCorpId;
    }

    public String getBusPersonnel() {
        return busPersonnel;
    }

    public void setBusPersonnel(String busPersonnel) {
        this.busPersonnel = busPersonnel;
    }

    public Integer getBillFlag() {
        return billFlag;
    }

    public void setBillFlag(Integer billFlag) {
        this.billFlag = billFlag;
    }

    public String getBillFlagName() {
        return billFlagName;
    }

    public void setBillFlagName(String billFlagName) {
        this.billFlagName = billFlagName;
    }

    public String getBlgrdbagcrclPrmtIndName() {
        return blgrdbagcrclPrmtIndName;
    }

    public void setBlgrdbagcrclPrmtIndName(String blgrdbagcrclPrmtIndName) {
        this.blgrdbagcrclPrmtIndName = blgrdbagcrclPrmtIndName;
    }

    public String getBillType() {
        return billType;
    }

    public void setBillType(String billType) {
        this.billType = billType;
    }

    public String getOrigNo() {
        return origNo;
    }

    public void setOrigNo(String origNo) {
        this.origNo = origNo;
    }

    public String getSbbllRngStrtSn() {
        return sbbllRngStrtSn;
    }

    public void setSbbllRngStrtSn(String sbbllRngStrtSn) {
        this.sbbllRngStrtSn = sbbllRngStrtSn;
    }

    public String getSbbllRngEndSn() {
        return sbbllRngEndSn;
    }

    public void setSbbllRngEndSn(String sbbllRngEndSn) {
        this.sbbllRngEndSn = sbbllRngEndSn;
    }

    public BigDecimal getBlStdAmt() {
        return blStdAmt;
    }

    public void setBlStdAmt(BigDecimal blStdAmt) {
        this.blStdAmt = blStdAmt;
    }

    public BigDecimal getBillMoney() {
        return billMoney;
    }

    public void setBillMoney(BigDecimal billMoney) {
        this.billMoney = billMoney;
    }

    public String getPayer() {
        return payer;
    }

    public void setPayer(String payer) {
        this.payer = payer;
    }

    public String getPayBank() {
        return payBank;
    }

    public void setPayBank(String payBank) {
        this.payBank = payBank;
    }

    public String getAcceptor() {
        return acceptor;
    }

    public void setAcceptor(String acceptor) {
        this.acceptor = acceptor;
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

    public String getCnAccrecords() {
        return cnAccrecords;
    }

    public void setCnAccrecords(String cnAccrecords) {
        this.cnAccrecords = cnAccrecords;
    }

    public String getBillSource() {
        return billSource;
    }

    public void setBillSource(String billSource) {
        this.billSource = billSource;
    }

    public String getBriefId() {
        return briefId;
    }

    public void setBriefId(String briefId) {
        this.briefId = briefId;
    }

    public String getSendSign() {
        return sendSign;
    }

    public void setSendSign(String sendSign) {
        this.sendSign = sendSign;
    }

    public String getBlgrdbagcrclPrmtInd() {
        return blgrdbagcrclPrmtInd;
    }

    public void setBlgrdbagcrclPrmtInd(String blgrdbagcrclPrmtInd) {
        this.blgrdbagcrclPrmtInd = blgrdbagcrclPrmtInd;
    }

    public String getBlHoldUnnIdNo() {
        return blHoldUnnIdNo;
    }

    public void setBlHoldUnnIdNo(String blHoldUnnIdNo) {
        this.blHoldUnnIdNo = blHoldUnnIdNo;
    }

    public String getApplyCorpName() {
        return applyCorpName;
    }

    public void setApplyCorpName(String applyCorpName) {
        this.applyCorpName = applyCorpName;
    }

    public String getCorpName() {
        return corpName;
    }

    public void setCorpName(String corpName) {
        this.corpName = corpName;
    }

    public String getBusCorpName() {
        return busCorpName;
    }

    public void setBusCorpName(String busCorpName) {
        this.busCorpName = busCorpName;
    }

    public String getBillTypeName() {
        return billTypeName;
    }

    public void setBillTypeName(String billTypeName) {
        this.billTypeName = billTypeName;
    }

    public String getSendSignName() {
        return sendSignName;
    }

    public void setSendSignName(String sendSignName) {
        this.sendSignName = sendSignName;
    }

    public Date getBusinessDate() {
        return businessDate;
    }

    public void setBusinessDate(Date businessDate) {
        this.businessDate = businessDate;
    }

    @Override
    public String toString() {
        return "AbBillNonassignableCancelVo{" +
                "id='" + id + '\'' +
                ", vid='" + vid + '\'' +
                ", vvid='" + vvid + '\'' +
                ", accId='" + accId + '\'' +
                ", applyCorpId='" + applyCorpId + '\'' +
                ", applyCorpName='" + applyCorpName + '\'' +
                ", applyCorpCode='" + applyCorpCode + '\'' +
                ", corpId='" + corpId + '\'' +
                ", corpName='" + corpName + '\'' +
                ", busCorpId='" + busCorpId + '\'' +
                ", busCorpName='" + busCorpName + '\'' +
                ", busPersonnel='" + busPersonnel + '\'' +
                ", billFlag=" + billFlag +
                ", billFlagName='" + billFlagName + '\'' +
                ", billType='" + billType + '\'' +
                ", billTypeName='" + billTypeName + '\'' +
                ", origNo='" + origNo + '\'' +
                ", sbbllRngStrtSn='" + sbbllRngStrtSn + '\'' +
                ", sbbllRngEndSn='" + sbbllRngEndSn + '\'' +
                ", blStdAmt=" + blStdAmt +
                ", billMoney=" + billMoney +
                ", payer='" + payer + '\'' +
                ", payBank='" + payBank + '\'' +
                ", acceptor='" + acceptor + '\'' +
                ", startDate=" + startDate +
                ", endDate=" + endDate +
                ", businessDate=" + businessDate +
                ", cnAccrecords='" + cnAccrecords + '\'' +
                ", billSource='" + billSource + '\'' +
                ", briefId='" + briefId + '\'' +
                ", sendSign='" + sendSign + '\'' +
                ", sendSignName='" + sendSignName + '\'' +
                ", blgrdbagcrclPrmtInd='" + blgrdbagcrclPrmtInd + '\'' +
                ", blgrdbagcrclPrmtIndName='" + blgrdbagcrclPrmtIndName + '\'' +
                ", blHoldUnnIdNo='" + blHoldUnnIdNo + '\'' +
                '}';
    }

    public void injectValues(Map<String, Map<String, DictDTO>> listDictMap) {
// 票据类型
        Optional.ofNullable(this.getBillType()).ifPresent(billType -> {
            Map<String, DictDTO> dictDtoMap = listDictMap.get("AB_BILL_TYPE");
            Optional.ofNullable(dictDtoMap).flatMap(dictMap -> Optional.ofNullable(dictMap.get(billType+"")))
                    .ifPresent(dict -> this.setBillTypeName(dict.getName()));
        });
        // 流通介质
        Optional.ofNullable(this.getBillFlag()).ifPresent(billFlag -> {
            Map<String, DictDTO> dictDtoMap = listDictMap.get("AB_BILL_FLAG");
            Optional.ofNullable(dictDtoMap).flatMap(dictMap -> Optional.ofNullable(dictMap.get(billFlag+"")))
                    .ifPresent(dict -> this.setBillFlagName(dict.getName()));
        });
        //线上线下标志: 1 线上 、0 线下
        Optional.ofNullable(this.getSendSign()).ifPresent(sendSignName ->{
            Map<String, DictDTO> dictDtoMap = listDictMap.get("AB_BILL_ONLINE_OFFLINE_TYPE");
            Optional.ofNullable(dictDtoMap).flatMap(dictMap -> Optional.ofNullable(dictMap.get(sendSignName)))
                    .ifPresent(dict -> this.setSendSignName(dict.getName()));
        });
        //是否允许分包：0 否、 1 是 blgrdbagcrclPrmtIndName
        Optional.ofNullable(this.getBlgrdbagcrclPrmtInd()).ifPresent(blgrdbagcrclPrmtIndName ->{
            Map<String, DictDTO> dictDtoMap = listDictMap.get("AB_BILL_BLGRDBAGCRCL_PRMT_IND");
            Optional.ofNullable(dictDtoMap).flatMap(dictMap -> Optional.ofNullable(dictMap.get(blgrdbagcrclPrmtIndName)))
                    .ifPresent(dict -> this.setBlgrdbagcrclPrmtIndName(dict.getName()));
        });
    }
}
