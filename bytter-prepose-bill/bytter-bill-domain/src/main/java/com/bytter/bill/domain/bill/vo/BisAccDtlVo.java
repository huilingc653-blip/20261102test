package com.bytter.bill.domain.bill.vo;
import com.bytter.core.annotation.BtCompositeSearch;
import com.bytter.system.api.dto.DictDTO;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.format.annotation.NumberFormat;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.Map;
import java.util.Optional;
/**
 * 票据认领处理 银行流水信息vo
 *
 * @author ouyj
 */
@SuppressWarnings("serial")
@Data
public class BisAccDtlVo implements Serializable {
    private String id;
    /**
     * 票据认领中间表id
     */
    @BtCompositeSearch(fieldName = "acc.claim_id")
    private String claimId;
    /**
     * 交易时间
     */
    @BtCompositeSearch(fieldName = "acc.trans_time")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date transTime;
    /**
     * 单位ID
     */
    // @Corp(inject = "corpName")
    @BtCompositeSearch(fieldName = "b.corp_id")
    private String corpId;
    /**
     * 单位
     */
    @BtCompositeSearch(fieldName = "b.corp_id")
    private String corpName;
    /**
     * 银行类型
     */
    // @BankType(inject = "bankTypeName")
    @BtCompositeSearch(fieldName = "b.bank_type_id")
    private String bankTypeId;
    @BtCompositeSearch(fieldName = "b.bank_type_id")
    private String bankTypeName;
    /**
     * 本方账号
     */
    @BtCompositeSearch(fieldName = "acc.bank_acc")
    private String bankAcc;
    /**
     * 本方账户名
     */
    @BtCompositeSearch(fieldName = "acc.acc_name")
    private String accName;
    /**
     * 本方账户开户行
     */
    @BtCompositeSearch(fieldName = "acc.BANK_NAME")
    private String bankName;
    /**
     * 对方账号
     */
    @BtCompositeSearch(fieldName = "acc.OPP_ACC_NO")
    private String oppAccNo;
    /**
     * 对方账户名
     */
    @BtCompositeSearch(fieldName = "acc.OPP_ACC_NAME")
    private String oppAccName;
    /**
     * 对方账户开户行
     */
    @BtCompositeSearch(fieldName = "acc.OPP_ACC_BANK")
    private String oppAccBank;
    /**
     * 交易金额
     */
    @NumberFormat(pattern = "#,##0.00")
    @BtCompositeSearch(fieldName = "acc.amt")
    private BigDecimal amt;
    /**
     * 借贷标志 0-收 1-支
     */
    // @Dict(dictGroup = "btBankAccDtl.cdSign", inject = "cdSignName")
    @BtCompositeSearch(fieldName = "acc.cd_sign")
    private String cdSign;
    @BtCompositeSearch(fieldName = "acc.cd_sign")
    private String cdSignName;
    /**
     * 币别ID
     */
    private String curId;
    /**
     * 币别
     */
    @BtCompositeSearch(fieldName = " acc.cur_id")
    private String curName;
    /**
     * 序列
     */
    @BtCompositeSearch(fieldName = "m.claim_sequence")
    private String claimSequence;
    /**
     * 摘要
     */
    @BtCompositeSearch(fieldName = "acc.remark")
    private String remark;
    /**
     * 用途
     */
    @BtCompositeSearch(fieldName = "acc.uses")
    private String uses;
    /**
     * 附言
     */
    @BtCompositeSearch(fieldName = "acc.postscript")
    private String postscript;
    /**
     * 银行流水号
     */
    @BtCompositeSearch(fieldName = "acc.bank_serial_no")
    private String bankSerialNo;
    /**
     * 余额
     */
    @NumberFormat(pattern = "#,##0.00")
    @BtCompositeSearch(fieldName = "acc.bal")
    private BigDecimal bal;
    /**
     * 认领备注
     */
    @BtCompositeSearch(fieldName = "m.rmk")
    private String claimRmk;
    /**
     * 票据号码
     */
    @BtCompositeSearch(fieldName = "m.orig_no")
    private String origNo;
  /** 出票标志 */
   // @Dict(dictGroup = "AB_BILL_NEW_STM_ISSUOFPPR_IND", inject = "newStmIssuofpprIndName")
    private String newStmIssuofpprInd;
    private String newStmIssuofpprIndName;
    /** 子票区间开始序号*/
    private String sbbllRngStrtSn;
    /** 子票区间结束序号 */
    private String sbbllRngEndSn;
    /** 票据（包）金额 */
    private BigDecimal billMoney;
    public void injectValues(Map<String, Map<String, DictDTO>> listDictMap){
        // 借贷标志 0-收 1-支
        Optional.ofNullable(this.getCdSign()).ifPresent(cdSign -> {
            Map<String, DictDTO> dictDtoMap = listDictMap.get("btBankAccDtl.cdSign");
            Optional.ofNullable(dictDtoMap).flatMap(dictMap -> Optional.ofNullable(dictMap.get(cdSign)))
                    .ifPresent(dict -> this.setCdSignName(dict.getName()));
        });
    }
}
