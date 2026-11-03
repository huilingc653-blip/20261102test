package com.bytter.increment.domain.vo;
import com.bytter.core.annotation.BtCompositeSearch;
import com.bytter.core.bpm.IBusinessKey;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
/**
 * @Title: VO
 * @Description: 内部理财信息表
 * @author huangx
 * @date 2019-11-1 14:03:30
 * @version 1.0
 *
 */
@Data
public class ChoInnerFinanceVO implements Serializable,IBusinessKey {
    private static final long serialVersionUID = 3650731181127660208L;
    private String id;
    /**业务单号*/
    @BtCompositeSearch(fieldName = "f.bill_No")
    private String billNo;
    /**单位ID*/
    @BtCompositeSearch(fieldName = "f.corp_Id")
    // @Corp(inject="corpName")
    private String corpId;
    /**单位名称*/
    @BtCompositeSearch(fieldName = "f.corp_name")
    private String corpName;
    /**业务类型 10-活期转定期 11-定期转活期 12-活期转通知 13-通知转活期 14-通知预约 15-取消通知预约*/
    @BtCompositeSearch(fieldName = "f.bus_Type")
    private Integer busType;
    /**付款账号ID*/
    private String payerAccId;
    /**付款账号*/
    @BtCompositeSearch(fieldName = "f.payer_Acc")
    private String payerAcc;
    /**付款账户名称*/
    @BtCompositeSearch(fieldName = "f.payer_acc_name")
    private String payerAccName;
    /**付款账户余额*/
    private BigDecimal balance;
    /**收款账号ID*/
    private String payeeAccId;
    /**收款账号*/
    @BtCompositeSearch(fieldName = "f.payee_Acc")
    private String payeeAcc;
    /**收款账户名称*/
    @BtCompositeSearch(fieldName = "f.payee_acc_name")
    private String payeeAccName;
    /**本金存入账号ID*/
    private String capitalAccId;
    /**本金存入账号*/
    private String capitalAcc;
    /**本金存入账户名称*/
    private String capitalAccName;
    /**利息存入账号ID*/
    private String interestAccId;
    /**利息存入账号*/
    private String interestAcc;
    /**利息存入账户名称*/
    private String interestAccName;
    /**存款类型*/
    private String depositRateType;
    /**存款类型名称*/
    private String depositRateTypeName;
    /**币别*/
    // @Currency(inject="curName")
    @BtCompositeSearch(fieldName = "f.cur_Id")
    private String curId;
    /**币别名称*/
    @BtCompositeSearch(fieldName = "f.cur_Id")
    private String curName;
    /**金额*/
    @BtCompositeSearch(fieldName = "f.pay_Money")
    private BigDecimal payMoney;
    /**期限*/
    @BtCompositeSearch(fieldName = "f.day_limit")
    private Integer dayLimit;
    /**日期单位 Y-年、M-月、D-日*/
    private String dateUnit;
    /**通知类型*/
    @BtCompositeSearch(fieldName = "f.notice_Type")
    private String noticeType;
    /**到期处理方式 0-自动终止、1-本金续存、2-本息续存*/
    @BtCompositeSearch(fieldName = "f.certificate_Way")
    private Integer certificateWay;
    /**起息日期*/
    @BtCompositeSearch(fieldName = "f.interest_start_date")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date interestStartDate;
    /**到期日期*/
    @BtCompositeSearch(fieldName = "f.deadline_date")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date deadlineDate;
    /**用途*/
    @BtCompositeSearch(fieldName = "f.purpose")
    private String purpose;
    /**备注*/
    private String addWord;
    /**利率*/
    private BigDecimal interestRate;
    /**取款日期*/
    @BtCompositeSearch(fieldName = "case when f.pay_date is null then f.interest_start_date else f.pay_date end ")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date payDate;
    /**关联单据ID*/
    private String relatedBillId;
    /**关联单据号*/
    @BtCompositeSearch(fieldName = "r.bill_no")
    private String relatedBillNo;
    /**预计利息*/
    private BigDecimal expectInterest;
    /**状态*/
    @BtCompositeSearch(fieldName = "f.status")
    private Integer status;
    /**记账日期*/
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date tallyDate;
    /**凭证流水号*/
    private String orderNo;
    /**记账凭证号码*/
    private String voucherNo;
    /**记账标志*/
    private String tallySign;
    /**记账员代码*/
    private String tallyCode;
    /**创建人登录名称*/
    private String createBy;
    /**创建日期*/
    private Date createDate;
    /**更新人登录名称*/
    private String updateBy;
    /**更新日期*/
    private Date updateDate;
    /**已支取金额*/
    private BigDecimal drawMoney;
    /**定期账号余额*/
    private BigDecimal timeBalance;
    /**账号利率*/
    private BigDecimal accInterestRate;
    /**截止日期*/
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date calEndDate;
    /**计算天数*/
    private Integer calDays;
    /**应收利息*/
    private BigDecimal interestReceivable;
    /**实收利息*/
    private BigDecimal interestEarned;
    /**应收未收利息*/
    private BigDecimal interestReceivableNot;
    /**预约金额*/
    private BigDecimal bookMoney;
    /**可预约金额*/
    private BigDecimal allowBookMoney;
    /**网点*/
    private String netId;
    @Override
    public String getBusinessKey() {
        return getId();
    }
}
