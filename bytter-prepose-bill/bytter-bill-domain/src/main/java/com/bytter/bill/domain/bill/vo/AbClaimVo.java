package com.bytter.bill.domain.bill.vo;
import com.bytter.core.annotation.BankType;
import com.bytter.core.annotation.BtCompositeSearch;
import com.bytter.system.api.dto.DictDTO;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.EqualsAndHashCode;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.Map;
import java.util.Optional;
/**
 * 票据认领流水vo
 *
 * @author ouyj
 */
@EqualsAndHashCode(callSuper = true)
@SuppressWarnings("serial")
@Data
public class AbClaimVo extends AbAccrecordsVO implements Serializable {
    /**
     * 票据认领中间表id
     */
    private String id;
    /**
     * 票据主表id
     */
    private String vid;
    /**
     * 流水id
     */
    private String aid;
    /**
     * 票据从表id
     */
    private String vvid;
    /**
     * 票据号码
     */
    @BtCompositeSearch(fieldName = "origNo")
    private String origNo;
    /**
     * 是否隐藏多选框 1为是
     */
    private String checkboxHide;
    /**
     * 持票单位
     */
    // @Corp(inject = "corpName")
    @BtCompositeSearch(fieldName = "corpId")
    private String corpId;
    @BtCompositeSearch(fieldName = "corpId")
    private String corpName;
    /**
     * 认领业务类型 贴现011、兑付、托收020
     */
    // @Dict(dictGroup = "AB_BILL_CLAIM_TYPE", inject = "claimTypeName")
    @BtCompositeSearch(fieldName = "claimType")
    private String claimType;
    @BtCompositeSearch(fieldName = "claimType")
    private String claimTypeName;
    /**
     * 类型 0应付票据，1应收票据
     */
    @BtCompositeSearch(fieldName = "bill.billSort")
    // @Dict(dictGroup = "BILL_PAYERPAYEE_TYPE", inject = "billSortName")
    private String billSort;
    @BtCompositeSearch(fieldName = "bill.billSort")
    private String billSortName;
    /**
     * 贴现业务显示贴现日期，兑付业务显示到期日期，托收业务显示到期日期
     */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @BtCompositeSearch(fieldName = "claimDate")
    private Date claimDate;

    @JsonFormat(pattern = "yyyy-MM-dd")
    @BtCompositeSearch(fieldName = "startDate")
    private Date startDate;

    @JsonFormat(pattern = "yyyy-MM-dd")
    @BtCompositeSearch(fieldName = "endDate")
    private Date endDate;

    /** 申请单位 */
    @BtCompositeSearch(fieldName="applyCorpId")
    // @Corp(inject="applyCorpName")
    private String applyCorpId;
    /**申请单位名称*/
    @BtCompositeSearch(fieldName="applyCorpName")
    private String applyCorpName;
    /** 业务单位 */
    // @Corp(inject="busCorpName")
    @BtCompositeSearch(fieldName="busCorpId")
    private String busCorpId;
    /** 业务单位名称 */
    @BtCompositeSearch(fieldName="busCorpName")
    private String busCorpName;
    /**
     * 票据单位账号
     */
    @BtCompositeSearch(fieldName="billBankAcc")
    private String billBankAcc;
    /**
     * 票据金额
     */
    @BtCompositeSearch(fieldName = "billMoney")
    private BigDecimal billMoney;

    /**保证金*/
    @BtCompositeSearch(fieldName="bailmoney")
    private BigDecimal bailmoney;

    /**保证金比率*/
    @BtCompositeSearch(fieldName="bailpercent")
    private BigDecimal bailpercent;

    /**
     * 票据解付金额
     */
    @BtCompositeSearch(fieldName = "payMoney")
    private BigDecimal payMoney;

    /**
     * 贴现业务显示贴现办理入账账户，托收业务显示票据所属账号，
     * 兑付业务银票显示出票人账号，兑付业务商票显示承兑人账号
     */
    @BtCompositeSearch(fieldName = "bankAcc")
    private String bankAcc;
    /**
     * 银行类型
     */
    @BankType(inject = "bankTypeName")
//    @BtCompositeSearch(fieldName = "acc.BANK_TYPE_ID")
    @BtCompositeSearch(fieldName = "bankTypeId")
    private String bankTypeId;
//    @BtCompositeSearch(fieldName = "acc.BANK_TYPE_ID")
    private String bankTypeName;
    /**
     * 序列
     */
    @BtCompositeSearch(fieldName = "claimSequence")
    private String claimSequence;
    /**
     * 认领状态 待认领11 打回-1 已确认95
     */
    // @Dict(dictGroup = "AB_BILL_CLAIM_STATUS", inject = "claimStatusName")
    @BtCompositeSearch(fieldName = "claimStatus")
    private String claimStatus;
    @BtCompositeSearch(fieldName = "claimStatus")
    private String claimStatusName;
    /**
     * 认领人
     */
    @BtCompositeSearch(fieldName = "claimPerson")
    private String claimPerson;
    /**
     * 认领时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @BtCompositeSearch(fieldName = "claimTime")
    private Date claimTime;
    /**
     * 流通介质：10 纸票、11 电票
     */
    // @Dict(dictGroup = "AB_BILL_FLAG", inject = "billFlagName")
    private Integer billFlag;
    /**
     * 流通介质名称：10 纸票、11 电票
     */
    @BtCompositeSearch(fieldName = "billFlag")
    private String billFlagName;
    /**
     * 票据类型：10 银票、11 商票
     */
    // @Dict(dictGroup = "AB_BILL_TYPE", inject = "billTypeName")
    @BtCompositeSearch(fieldName = "billType")
    private Integer billType;
    /**
     * 票据类型名称：10 银票、11 商票
     */
    @BtCompositeSearch(fieldName = "billType")
    private String billTypeName;
    /**
     * 出票人
     */
    @BtCompositeSearch(fieldName = "payer")
    private String payer;
    /**
     * 出票人银行
     */
    @BtCompositeSearch(fieldName = "payerBankName")
    private String payerBankName;
    /**
     * 收票人全称
     */
    @BtCompositeSearch(fieldName = "payee")
    private String payee;
    /**
     * 前手
     */
    @BtCompositeSearch(fieldName = "cnAccrecords")
    private String cnAccrecords;
    /**
     * 后手
     */
    @BtCompositeSearch(fieldName = "rearAccrecords")
    private String rearAccrecords;
    /**
     * 线上线下标识：0否； 1是 ；
     */
    // @Dict(dictGroup = "AB_BILL_SEND_SIGN", inject = "sendSignName")
    private String sendSign;
    @BtCompositeSearch(fieldName = "sendSign")
    private String sendSignName;
    /**
     * 实付金额
     */
    private BigDecimal realMoney;
    /** 出票标志 */
    //@Dict(dictGroup = "AB_BILL_NEW_STM_ISSUOFPPR_IND", inject = "newStmIssuofpprIndName")
    private String newStmIssuofpprInd;
    private String newStmIssuofpprIndName;
    /** 是否允许分包 */
    //@Dict(dictGroup = "AB_BILL_BLGRDBAGCRCL_PRMT_IND", inject = "blgrdbagcrclPrmtIndName")
    private String blgrdbagcrclPrmtInd;
    private String blgrdbagcrclPrmtIndName;
    /** 子票区间开始序号*/
    @BtCompositeSearch(fieldName = "sbbllRngStrtSn")
    private String sbbllRngStrtSn;

    /** 子票区间结束序号 */
    @BtCompositeSearch(fieldName = "sbbllRngEndSn")
    private String sbbllRngEndSn;
    /**过滤明细数据清单*/
    private String transTimeLeft;
    /**匹配要素*/
    private String matchElements;
    /**匹配内容*/
    private String matchContent;
    public String getCheckboxHide() {
        return "0";
    }
    public void injectValues(Map<String, Map<String, DictDTO>> listDictMap){
        // 认领业务类型 贴现011、兑付、托收020
        Optional.ofNullable(this.getClaimType()).ifPresent(claimType -> {
            Map<String, DictDTO> dictDtoMap = listDictMap.get("AB_BILL_CLAIM_TYPE");
            Optional.ofNullable(dictDtoMap).flatMap(dictMap -> Optional.ofNullable(dictMap.get(claimType)))
                    .ifPresent(dict -> this.setClaimTypeName(dict.getName()));
        });
        // 类型 0应付票据，1应收票据
        Optional.ofNullable(this.getBillSort()).ifPresent(billSort -> {
            Map<String, DictDTO> dictDtoMap = listDictMap.get("BILL_PAYERPAYEE_TYPE");
            Optional.ofNullable(dictDtoMap).flatMap(dictMap -> Optional.ofNullable(dictMap.get(billSort)))
                    .ifPresent(dict -> this.setBillSortName(dict.getName()));
        });
        // 认领状态 待认领11 打回-1 已确认95
        Optional.ofNullable(this.getClaimStatus()).ifPresent(claimStatus -> {
            Map<String, DictDTO> dictDtoMap = listDictMap.get("AB_BILL_CLAIM_STATUS");
            Optional.ofNullable(dictDtoMap).flatMap(dictMap -> Optional.ofNullable(dictMap.get(claimStatus)))
                    .ifPresent(dict -> this.setClaimStatusName(dict.getName()));
        });
        // 流通介质：10 纸票、11 电票
        Optional.ofNullable(this.getBillFlag()).ifPresent(billFlag -> {
            Map<String, DictDTO> dictDtoMap = listDictMap.get("AB_BILL_FLAG");
            Optional.ofNullable(dictDtoMap).flatMap(dictMap -> Optional.ofNullable(dictMap.get(billFlag + "")))
                    .ifPresent(dict -> this.setBillFlagName(dict.getName()));
        });
        // '票据类型：10 银票、11 商票
        Optional.ofNullable(this.getBillType()).ifPresent(billType -> {
            Map<String, DictDTO> dictDtoMap = listDictMap.get("AB_BILL_TYPE");
            Optional.ofNullable(dictDtoMap).flatMap(dictMap -> Optional.ofNullable(dictMap.get(billType + "")))
                    .ifPresent(dict -> this.setBillTypeName(dict.getName()));
        });
        // 线上线下标识：0否； 1是 ；
        Optional.ofNullable(this.getSendSign()).ifPresent(sendSign -> {
            Map<String, DictDTO> dictDtoMap = listDictMap.get("AB_BILL_SEND_SIGN");
            Optional.ofNullable(dictDtoMap).flatMap(dictMap -> Optional.ofNullable(dictMap.get(sendSign)))
                    .ifPresent(dict -> this.setSendSignName(dict.getName()));
        });
        // 出票标志
        Optional.ofNullable(this.getNewStmIssuofpprInd()).ifPresent(sendSign -> {
            Map<String, DictDTO> dictDtoMap = listDictMap.get("AB_BILL_NEW_STM_ISSUOFPPR_IND");
            Optional.ofNullable(dictDtoMap).flatMap(dictMap -> Optional.ofNullable(dictMap.get(newStmIssuofpprInd)))
                    .ifPresent(dict -> this.setNewStmIssuofpprIndName(dict.getName()));
        });
    }
}
