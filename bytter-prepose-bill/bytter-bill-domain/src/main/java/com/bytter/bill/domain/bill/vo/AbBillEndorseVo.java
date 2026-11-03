package com.bytter.bill.domain.bill.vo;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.bytter.bill.api.bill.constant.AccebillConstant;
import com.bytter.core.annotation.BtCompositeSearch;
import com.bytter.core.bpm.IBusinessKey;
import com.bytter.core.enums.CommonDictEnum;
import com.bytter.poi.annotation.Excel;
import com.bytter.poi.annotation.ExcelTarget;
import com.bytter.system.api.dto.DictDTO;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.EqualsAndHashCode;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Map;
import java.util.Optional;
/**
 * @author onlineGenerator
 * @version 1.0
 * @Title: Vo
 * @Description: 票据背书
 * @date 2017-06-21 17:35:25
 */
@EqualsAndHashCode(callSuper = true)
@SuppressWarnings("serial")
@ExcelTarget("AbBillEndorseVo")
@Data
public class AbBillEndorseVo extends AbAccrecordsVO implements Serializable,  IBusinessKey {
	private String billIds;
    private String id;
    /**
     * 票据主表id
     */
    private String vid;
    /**
     * 票据从表id
     */
    private String vvid;
    private String ids;
    private String vids;
    private String vvids;
	private String transMoneys;
    @BtCompositeSearch(fieldName="c.bank_acc")
    private String bankAcc;
    /**
     * 票据号码
     */
    @Excel(name = "*票据号码")
    @NotNull(message = AccebillConstant.AB_BILL_EXCEL_ERRORMSG_ORIGNO_NULL)
    @BtCompositeSearch(fieldName = "z.orig_No")
    private String origNo;
    /**
     * 票据类型：10 银票、11 商票
     */
    @BtCompositeSearch(fieldName = "z.bill_Type")
    // @Dict(dictGroup = "AB_BILL_TYPE", inject = "billTypeName")
    private Integer billType;
    /**
     * 票据类型名称：10 银票、11 商票
     */
    @Excel(name = "票据类型")
    @BtCompositeSearch(fieldName = "z.bill_Type")
    private String billTypeName;
    /**
     * 持票单位
     */
    @Excel(name = "*票据单位代码")
    @NotNull(message = AccebillConstant.AB_BILL_EXCEL_ERRORMSG_CORPCODE_NULL)
    @BtCompositeSearch(fieldName = "c.corp_Id")
    private String corpId;
    @Excel(name = "票据单位")
    @BtCompositeSearch(fieldName = "c.corp_code")
    private String corpIdName;
    private String billStatus;
    /**
     * 流通介质：10 纸票、11 电票
     */
    // @Dict(dictGroup = "AB_BILL_FLAG", inject = "billFlagName")
    @BtCompositeSearch(fieldName = "z.bill_Flag")
    private Integer billFlag;
    /**
     * 流通介质名称：10 纸票、11 电票
     */
    @Excel(name = "流通介质")
    @BtCompositeSearch(fieldName = "z.bill_Flag")
    private String billFlagName;
    /**
     * 票据金额
     */
    @Excel(name = "票据金额")
    @BtCompositeSearch(fieldName = "z.bill_Money")
    private BigDecimal billMoney;

    /**
     * 交易金额
     */
    @Excel(name = "*交易金额")
    private BigDecimal transMoney;
    /**
     * 出票日期
     */
    @Excel(name = "出票日期")
    @JsonFormat(pattern="yyyy-MM-dd")
    @BtCompositeSearch(fieldName = "z.start_Date")
    private java.util.Date startDate;
    /**
     * 到期日期
     */
    @Excel(name = "到期日期")
    @JsonFormat(pattern="yyyy-MM-dd")
    @BtCompositeSearch(fieldName = "z.end_Date")
    private java.util.Date endDate;
    /**
     * 出票人
     */
    @BtCompositeSearch(fieldName = "z.payer")
    private String payer;
    /**
     * 收票人全称
     */
    @BtCompositeSearch(fieldName = "z.payee")
    private String payee;
    /**
     * 承兑人
     */
    @BtCompositeSearch(fieldName = "z.acceptor")
    private String acceptor;
    /**
     * 开出方式：0  否；1 是
     */
    // @Dict(dictGroup = "sf_yn", inject = "insideSingName")
    private Integer insideSing;
    @BtCompositeSearch(fieldName = "l.inside_Sing")
    private String insideSingName;
    /**
     * 业务处理状态
     */
    // @Dict(dictGroup = "AB_BILL_AbBillEndorse_STATUS", inject = "statusName")
    @BtCompositeSearch(fieldName = "l.status")
    private Integer status;
    private String statusName;
    /**
     * 业务状态
     */
   /* @Dict(dictGroup = "AB_BILL_AbBillEndorse_BRIEF_ID", inject = "briefIdName")
    @BtCompositeSearch(fieldName = "l.brief_Id")
    private java.lang.String briefId;
    private java.lang.String briefIdName;*/
    //@Excel(name = "*是否内部背书")
    // @Dict(dictGroup = "sf_yn", inject = "interiorApplyName")
    private String interiorApply;
    @Excel(name = "*是否内部背书")
    @NotNull(message = AccebillConstant.AB_BILL_EXCEL_ERRORMSG_ENDORSE_INTERIORAPPLY_NULL)
    private String interiorApplyName;
    @Excel(name = "备注")
    private String rmk;
    private Integer checkboxHide;
    /**
     * 所属库存
     */
    @BtCompositeSearch(fieldName = "c.enter_stock")
    // @Dict(dictGroup = "AB_ENTER_STOCK", inject = "enterStockName")
    private String enterStock;
    @Excel(name = "所属库存")
    @BtCompositeSearch(fieldName = "c.enter_stock")
    private String enterStockName;
    /**
     * 是否可转让
     */
   // @Excel(name = "*是否可转让")
   //  @Dict(dictGroup = "AB_BANEM", inject = "bankendrSmtmkName")
    private String bankendrSmtmk;
    private String bankName;
    @Excel(name = "*是否可转让")
    @NotNull(message = AccebillConstant.AB_BILL_EXCEL_ERRORMSG_ENDORSE_BANKENDRSMTMK_NULL)
    @BtCompositeSearch(fieldName = "l.bankendrSmtmk")
    private String bankendrSmtmkName;
    private String endorseItem;
	/**银行类别**/
	private String bankTypeName;
	@BtCompositeSearch(fieldName="c.bank_type_id")
	private String bankTypeId;
    /**资金计划科目代码信息*/
    @Excel(name = "资金计划科目代码",isImportField="true")
    private String itemCodeStr;
    /**
     * 计划科目
     */
    private String[] itemCode;
    /**
     * 计划科目名称
     */
    private String[] itemName;
    /**
     * 执行金额
     */
    private BigDecimal[] processMoney;
    /**
     * 被背书人单位ID
     */
    private String pledgeeCorpid;
    @Excel(name = "*被背书人账号")
    @NotNull(message = AccebillConstant.AB_BILL_EXCEL_ERRORMSG_ENDORSE_ENDORSEACC_NULL)
    private String pledgeeAcc;
    @BtCompositeSearch(fieldName = "l.rec_nm")
    @Excel(name = "被背书人名称")
    private String pledgeeName;
    @Excel(name = "被背书人开户行行号")
    private String pledgeeBankAcc;
    @Excel(name = "被背书人开户行行名")
    private String pledgeeBankName;
    @Excel(name = "*业务办理日期",importFormat = "yyyy-MM-dd",isImportField="true")
    @JsonFormat(pattern="yyyy-MM-dd")
    private java.util.Date businessDate;
    @Excel(name = "内部单位代码")
    private String endorseCorpCode;
    @Excel(name = "内部单位")
    private String endorseCorpName;
    private String businessKey;
    /**
     * 付款单ID
     */
    private String payId;
	/**是否发送指令标志：0否； 1是 ；*/
	@BtCompositeSearch(fieldName="l.send_Sign")
	// @Dict(dictGroup="AB_BILL_SEND_SIGN",inject = "sendSignName")
	private String sendSign;
	@BtCompositeSearch(fieldName="l.send_Sign")
	private String sendSignName;
	private String cnAccrecords;
    /**申请人名称*/
    @BtCompositeSearch(fieldName="applyName")
    private String applyName;
    /** 申请单位 */
    @BtCompositeSearch(fieldName="c.APPLY_CORP_ID")
    // @Corp(inject="applyCorpName")
    private String applyCorpId;
    /**申请单位名称*/
    @BtCompositeSearch(fieldName="c.APPLY_CORP_NAME")
    private String applyCorpName;
    /**申请单位代码*/
    private String applyCorpCode;
    /** 业务单位 */
    // @Corp(inject="busCorpName")
    @BtCompositeSearch(fieldName="c.BUS_CORP_ID")
    private String busCorpId;
    /** 业务单位名称 */
    @BtCompositeSearch(fieldName="c.BUS_CORP_NAME")
    private String busCorpName;
    /** 业务人员 */
    @BtCompositeSearch(fieldName="c.BUS_PERSONNEL")
    private String busPersonnel;
    /**业务状态*/
    // @Dict(dictGroup = "AB_BILL_BUSSTATUS", inject = "busStatusName")
    @BtCompositeSearch(fieldName="l.status")
    private Integer busStatus;
    /**业务状态名称*/
    @BtCompositeSearch(fieldName="l.status")
    private String busStatusName;
    /**
     * 流水号
     */
    @BtCompositeSearch(fieldName="l.serial_no")
    private String serialNo;
    /**
     * 指令状态
     */
    // @Dict(dictGroup = "AB_BILL_INSOPTSTATUS", inject = "optStatusName")
    @BtCompositeSearch(fieldName="l.opt_status")
    private String optStatus;
    @BtCompositeSearch(fieldName="l.opt_status")
    private String optStatusName;
    /**
     * 是否同意背书请求
     */
    private String isEndorse;
    /** 出票标志 */
    @BtCompositeSearch(fieldName="z.NEW_STM_ISSUOFPPR_IND")
    //@Dict(dictGroup = "AB_BILL_NEW_STM_ISSUOFPPR_IND", inject = "newStmIssuofpprIndName")
    private String newStmIssuofpprInd;
    @BtCompositeSearch(fieldName="z.NEW_STM_ISSUOFPPR_IND")
    @Excel(name = "出票标志",isImportField="false")
    private String newStmIssuofpprIndName;
    /** 是否允许分包 */
    @BtCompositeSearch(fieldName="z.BLGRDBAGCRCL_PRMT_IND")
    //@Dict(dictGroup = "AB_BILL_BLGRDBAGCRCL_PRMT_IND", inject = "blgrdbagcrclPrmtIndName")
    private String blgrdbagcrclPrmtInd;
    @BtCompositeSearch(fieldName="z.BLGRDBAGCRCL_PRMT_IND")
    @Excel(name = "是否允许分包",isImportField="false")
    private String blgrdbagcrclPrmtIndName;
    /**
     * 被背书人识别类型
     */
    @Excel(name = "*被背书人识别类型")
   // @Dict(dictGroup = "AB_BILL_BL_ID_TPCD", inject = "recDghTypeName")
    private String recDghType;
    //@Excel(name = "*被背书人识别类型")
    private String recDghTypeName;
    /**
     * 业务接收人社会统一信用代码
     */
    private String recSocNo;
    private String msgtype;
    @Excel(name = "*子票区间开始序号")
    @BtCompositeSearch(fieldName = "z.SBBLL_RNG_STRT_SN")
    private String sbbllRngStrtSn;
    @Excel(name = "*子票区间结束序号")
    @BtCompositeSearch(fieldName = "z.SBBLL_RNG_END_SN")
    private String sbbllRngEndSn;

    @Excel(name = "*原子票区间开始序号")
    private String sbbllRngStrtSnOld;

    @Excel(name = "*原子票区间结束序号")
    private String sbbllRngEndSnOld;


    @Excel(name = "拆分子票区间开始序号")
    private String sbbllRngStrtSnSplit;

    @Excel(name = "拆分子票区间结束序号")
    private String sbbllRngEndSnSplit;
	

    @Override
    public String getSbbllRngStrtSn() {
        return sbbllRngStrtSn;
    }

    @Override
    public void setSbbllRngStrtSn(String sbbllRngStrtSn) {
        if(StrUtil.isNotEmpty(sbbllRngStrtSn) && !"0".equals(sbbllRngStrtSn)){
            sbbllRngStrtSn=StrUtil.fill(sbbllRngStrtSn,'0',12,true);
        }
        this.sbbllRngStrtSn = StrUtil.replace(sbbllRngStrtSn," ","0");
    }

    @Override
    public String getSbbllRngEndSn() {
        return sbbllRngEndSn;
    }

    @Override
    public void setSbbllRngEndSn(String sbbllRngEndSn) {
        if(StrUtil.isNotEmpty(sbbllRngEndSn) && !"0".equals(sbbllRngEndSn)){
            sbbllRngEndSn=StrUtil.fill(sbbllRngEndSn,'0',12,true);
        }
        this.sbbllRngEndSn = StrUtil.replace(sbbllRngEndSn," ","0");
    }
	
    public String getSbbllRngStrtSnOld() {
        return sbbllRngStrtSnOld;
    }

    public void setSbbllRngStrtSnOld(String sbbllRngStrtSnOld) {
        if(StrUtil.isNotEmpty(sbbllRngStrtSnOld)){
            if(!"0".equals(sbbllRngStrtSnOld)) {
                sbbllRngStrtSnOld = StrUtil.fill(sbbllRngStrtSnOld, '0', 12, true);
            }
            setSbbllRngStrtSn(sbbllRngStrtSnOld);
        }
        this.sbbllRngStrtSnOld = sbbllRngStrtSnOld;
    }

    public String getSbbllRngEndSnOld() {
        return sbbllRngEndSnOld;
    }

    public void setSbbllRngEndSnOld(String sbbllRngEndSnOld) {
        if(StrUtil.isNotEmpty(sbbllRngEndSnOld)){
            if(!"0".equals(sbbllRngEndSnOld)) {
                sbbllRngEndSnOld = StrUtil.fill(sbbllRngEndSnOld, '0', 12, true);
            }
            setSbbllRngEndSn(sbbllRngEndSnOld);
        }
        this.sbbllRngEndSnOld = sbbllRngEndSnOld;
    }

    public String getSbbllRngStrtSnSplit() {
        return sbbllRngStrtSnSplit;
    }

    public void setSbbllRngStrtSnSplit(String sbbllRngStrtSnSplit) {
        if(StrUtil.isNotEmpty(sbbllRngStrtSnSplit)&&!"0".equals(sbbllRngStrtSnSplit)) {
            sbbllRngStrtSnSplit = StrUtil.fill(sbbllRngStrtSnSplit, '0', 12, true);
        }
        this.sbbllRngStrtSnSplit = sbbllRngStrtSnSplit;
    }

    public String getSbbllRngEndSnSplit() {
        return sbbllRngEndSnSplit;
    }

    public void setSbbllRngEndSnSplit(String sbbllRngEndSnSplit) {
        if(StrUtil.isNotEmpty(sbbllRngEndSnSplit)&&!"0".equals(sbbllRngEndSnSplit)) {
            sbbllRngEndSnSplit = StrUtil.fill(sbbllRngEndSnSplit, '0', 12, true);
        }
        this.sbbllRngEndSnSplit = sbbllRngEndSnSplit;
    }
    public BigDecimal getTransMoney() {
        return transMoney;
    }
    public void setTransMoney(BigDecimal transMoney) {
        this.transMoney = transMoney;
    }
    @Override
    public String getBeanName() {
        return "abBillEndorseService";
    }
    public String getInteriorApplyName() {
        if (ObjectUtil.isNotEmpty(interiorApply)) {
            return CommonDictEnum.getDictValue("sf_yn", interiorApply);
        }
        return interiorApplyName;
    }
    @Override
    public String getInsideSingName() {
        if (ObjectUtil.isNotEmpty(insideSing)) {
            return CommonDictEnum.getDictValue("sf_yn", insideSing + "");
        }
        return insideSingName;
    }
    @Override
    public String getBusinessKey() {
        return getId();
    }
    public void injectValues(Map<String, Map<String, DictDTO>> listDictMap) {
        // 票据类型：10 银票、11 商票
        Optional.ofNullable(this.getBillType()).ifPresent(billType -> {
            Map<String, DictDTO> dictDtoMap = listDictMap.get("AB_BILL_TYPE");
            Optional.ofNullable(dictDtoMap).flatMap(dictMap -> Optional.ofNullable(dictMap.get(billType + "")))
                    .ifPresent(dict -> this.setBillTypeName(dict.getName()));
        });
        // 流通介质：10 纸票、11 电票
        Optional.ofNullable(this.getBillFlag()).ifPresent(billFlag -> {
            Map<String, DictDTO> dictDtoMap = listDictMap.get("AB_BILL_FLAG");
            Optional.ofNullable(dictDtoMap).flatMap(dictMap -> Optional.ofNullable(dictMap.get(billFlag + "")))
                    .ifPresent(dict -> this.setBillFlagName(dict.getName()));
        });
        // 业务处理状态
        Optional.ofNullable(this.getStatus()).ifPresent(status -> {
            Map<String, DictDTO> dictDtoMap = listDictMap.get("AB_BILL_AbBillEndorse_STATUS");
            Optional.ofNullable(dictDtoMap).flatMap(dictMap -> Optional.ofNullable(dictMap.get(status + "")))
                    .ifPresent(dict -> this.setStatusName(dict.getName()));
        });
        //业务标志
        Optional.ofNullable(this.getBriefId()).ifPresent(briefId -> {
            Map<String, DictDTO> dictDtoMap = listDictMap.get("AB_BRIEF_STATUS");
            Optional.ofNullable(dictDtoMap).flatMap(dictMap -> Optional.ofNullable(dictMap.get(briefId + "")))
                    .ifPresent(dict -> this.setBriefIdName(dict.getName()));
        });
        // 所属库存
        Optional.ofNullable(this.getEnterStock()).ifPresent(enterStock -> {
            Map<String, DictDTO> dictDtoMap = listDictMap.get("AB_ENTER_STOCK");
            Optional.ofNullable(dictDtoMap).flatMap(dictMap -> Optional.ofNullable(dictMap.get(enterStock)))
                    .ifPresent(dict -> this.setEnterStockName(dict.getName()));
        });
        // 不得转让标记
        Optional.ofNullable(this.getBankendrSmtmk()).ifPresent(bankendrSmtmk -> {
            Map<String, DictDTO> dictDtoMap = listDictMap.get("AB_BANEM");
            Optional.ofNullable(dictDtoMap).flatMap(dictMap -> Optional.ofNullable(dictMap.get(bankendrSmtmk)))
                    .ifPresent(dict -> this.setBankendrSmtmkName(dict.getName()));
        });
        // 是否发送指令标志：0否； 1是
        Optional.ofNullable(this.getSendSign()).ifPresent(sendSign -> {
            Map<String, DictDTO> dictDtoMap = listDictMap.get("AB_BILL_SEND_SIGN");
            Optional.ofNullable(dictDtoMap).flatMap(dictMap -> Optional.ofNullable(dictMap.get(sendSign)))
                    .ifPresent(dict -> this.setSendSignName(dict.getName()));
        });
        // 业务状态
        Optional.ofNullable(this.getBusStatus()).ifPresent(busStatus -> {
            Map<String, DictDTO> dictDtoMap = listDictMap.get("AB_BILL_BUSSTATUS");
            Optional.ofNullable(dictDtoMap).flatMap(dictMap -> Optional.ofNullable(dictMap.get(busStatus + "")))
                    .ifPresent(dict -> this.setBusStatusName(dict.getName()));
        });
        // 指令状态
        Optional.ofNullable(this.getOptStatus()).ifPresent(optStatus -> {
            Map<String, DictDTO> dictDtoMap = listDictMap.get("AB_BILL_INSOPTSTATUS");
            Optional.ofNullable(dictDtoMap).flatMap(dictMap -> Optional.ofNullable(dictMap.get(optStatus)))
                    .ifPresent(dict -> this.setOptStatusName(dict.getName()));
        });
        //出票标志: 1 新系统出票 、0 老系统出票
        Optional.ofNullable(this.getNewStmIssuofpprInd()).ifPresent(newStmIssuofpprInd ->{
            Map<String, DictDTO> dictDtoMap = listDictMap.get("AB_BILL_NEW_STM_ISSUOFPPR_IND");
            Optional.ofNullable(dictDtoMap).flatMap(dictMap -> Optional.ofNullable(dictMap.get(newStmIssuofpprInd)))
                    .ifPresent(dict -> this.setNewStmIssuofpprIndName(dict.getName()));
        });
        //是否允许分包：0 否、 1 是 blgrdbagcrclPrmtIndName
        Optional.ofNullable(this.getBlgrdbagcrclPrmtInd()).ifPresent(blgrdbagcrclPrmtIndName ->{
            Map<String, DictDTO> dictDtoMap = listDictMap.get("AB_BILL_BLGRDBAGCRCL_PRMT_IND");
            Optional.ofNullable(dictDtoMap).flatMap(dictMap -> Optional.ofNullable(dictMap.get(blgrdbagcrclPrmtIndName)))
                    .ifPresent(dict -> this.setBlgrdbagcrclPrmtIndName(dict.getName()));
        });
        //是否对内背书：0 否、 1 是 interiorApplyName
        Optional.ofNullable(this.getInteriorApply()).ifPresent(interiorApply ->{
            Map<String, DictDTO> dictDtoMap = listDictMap.get("sf_yn");
            Optional.ofNullable(dictDtoMap).flatMap(dictMap -> Optional.ofNullable(dictMap.get(interiorApply)))
                    .ifPresent(dict -> this.setInteriorApplyName(dict.getName()));
        });
        //是否对内背书：0 否、 1 是 insideSingName
        Optional.ofNullable(this.getInsideSing()).ifPresent(insideSing ->{
            Map<String, DictDTO> dictDtoMap = listDictMap.get("sf_yn");
            Optional.ofNullable(dictDtoMap).flatMap(dictMap -> Optional.ofNullable(dictMap.get(insideSing)))
                    .ifPresent(dict -> this.setInsideSingName(dict.getName()));
        });


    }
    @Override
    public String toString() {
        return "AbBillEndorseVo{" +
                "billIds='" + billIds + '\'' +
                ", id='" + id + '\'' +
                ", vid='" + vid + '\'' +
                ", vvid='" + vvid + '\'' +
                ", ids='" + ids + '\'' +
                ", vids='" + vids + '\'' +
                ", vvids='" + vvids + '\'' +
                ", transMoneys='" + transMoneys + '\'' +
                ", origNo='" + origNo + '\'' +
                ", billType=" + billType +
                ", billTypeName='" + billTypeName + '\'' +
                ", corpId='" + corpId + '\'' +
                ", corpIdName='" + corpIdName + '\'' +
                ", billStatus='" + billStatus + '\'' +
                ", billFlag=" + billFlag +
                ", billFlagName='" + billFlagName + '\'' +
                ", billMoney=" + billMoney +
                ", startDate=" + startDate +
                ", endDate=" + endDate +
                ", payer='" + payer + '\'' +
                ", payee='" + payee + '\'' +
                ", acceptor='" + acceptor + '\'' +
                ", insideSing=" + insideSing +
                ", insideSingName='" + insideSingName + '\'' +
                ", status=" + status +
                ", statusName='" + statusName + '\'' +
                ", interiorApply='" + interiorApply + '\'' +
                ", interiorApplyName='" + interiorApplyName + '\'' +
                ", rmk='" + rmk + '\'' +
                ", checkboxHide=" + checkboxHide +
                ", enterStock='" + enterStock + '\'' +
                ", enterStockName='" + enterStockName + '\'' +
                ", bankendrSmtmk='" + bankendrSmtmk + '\'' +
                ", bankName='" + bankName + '\'' +
                ", bankendrSmtmkName='" + bankendrSmtmkName + '\'' +
                ", endorseItem='" + endorseItem + '\'' +
                ", bankTypeName='" + bankTypeName + '\'' +
                ", bankTypeId='" + bankTypeId + '\'' +
                ", itemCodeStr='" + itemCodeStr + '\'' +
                ", itemCode=" + Arrays.toString(itemCode) +
                ", itemName=" + Arrays.toString(itemName) +
                ", processMoney=" + Arrays.toString(processMoney) +
                ", pledgeeCorpid='" + pledgeeCorpid + '\'' +
                ", pledgeeAcc='" + pledgeeAcc + '\'' +
                ", pledgeeName='" + pledgeeName + '\'' +
                ", pledgeeBankAcc='" + pledgeeBankAcc + '\'' +
                ", pledgeeBankName='" + pledgeeBankName + '\'' +
                ", businessDate=" + businessDate +
                ", endorseCorpCode='" + endorseCorpCode + '\'' +
                ", endorseCorpName='" + endorseCorpName + '\'' +
                ", businessKey='" + businessKey + '\'' +
                ", payId='" + payId + '\'' +
                ", sendSign='" + sendSign + '\'' +
                ", sendSignName='" + sendSignName + '\'' +
                ", cnAccrecords='" + cnAccrecords + '\'' +
                ", applyName='" + applyName + '\'' +
                ", applyCorpId='" + applyCorpId + '\'' +
                ", applyCorpName='" + applyCorpName + '\'' +
                ", applyCorpCode='" + applyCorpCode + '\'' +
                ", busCorpId='" + busCorpId + '\'' +
                ", busCorpName='" + busCorpName + '\'' +
                ", busPersonnel='" + busPersonnel + '\'' +
                ", busStatus=" + busStatus +
                ", busStatusName='" + busStatusName + '\'' +
                ", serialNo='" + serialNo + '\'' +
                ", optStatus='" + optStatus + '\'' +
                ", optStatusName='" + optStatusName + '\'' +
                ", isEndorse='" + isEndorse + '\'' +
                ", newStmIssuofpprInd='" + newStmIssuofpprInd + '\'' +
                ", newStmIssuofpprIndName='" + newStmIssuofpprIndName + '\'' +
                ", blgrdbagcrclPrmtInd='" + blgrdbagcrclPrmtInd + '\'' +
                ", blgrdbagcrclPrmtIndName='" + blgrdbagcrclPrmtIndName + '\'' +
                ", recDghType='" + recDghType + '\'' +
                ", recDghTypeName='" + recDghTypeName + '\'' +
                ", recSocNo='" + recSocNo + '\'' +
                ", msgtype='" + msgtype + '\'' +
                ", sbbllRngStrtSn='" + sbbllRngStrtSn + '\'' +
                ", sbbllRngEndSn='" + sbbllRngEndSn + '\'' +
                ", sbbllRngStrtSnOld='" + sbbllRngStrtSnOld + '\'' +
                ", sbbllRngEndSnOld='" + sbbllRngEndSnOld + '\'' +
                ", sbbllRngStrtSnSplit='" + sbbllRngStrtSnSplit + '\'' +
                ", sbbllRngEndSnSplit='" + sbbllRngEndSnSplit + '\'' +
                ", transMoney=" + transMoney +
                '}';
    }
}
