package com.bytter.biz.finacial.api.vo;

import com.bytter.core.annotation.*;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * Created by zhangxb on 2017/6/6.
 */
@Data
public class FcAccVO implements Serializable {
    private String id;
    /**更新日期*/
    private Date updateDate;
    /**账户属性*/
    private String attributeId;
    /**账户性质*/
    private String natureCodeId;
    /**银行代码*/
    @Dict(tableName="BT_INPUT_BANK_INFO",valueFieldName="SYS_BANK_CODE",showFiledName="BANK_NAME", inject = "bankName")
    private String bankCode;
    private String bankName;
    /**计息标志 */
    private String interestSign;
    /**起息日期*/
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date interestStartDate;
    /**利率*/
    private BigDecimal interestRate;
    /**透支标志*/
    private String overdraftSign;
    /**透支利率*/
    private BigDecimal overdraftRate;
    /**透支金额*/
    private BigDecimal overdraftMoney;
    /**合同编号*/
    private String contractNo;
    /**到期日期*/
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date deadlineDate;
    /**展期到期日期*/
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date delayDeadlineDate;
    /**逾期利率*/
    private BigDecimal overdueRate;
    /**扣息活期存款账户*/
    private String accForDeduct;
    /**扣息方式*/
    private String deductMode;
    /**通知类型*/
    private Integer notifyType;
    /**协定存款标志*/
    private String agreedDepositSign;
    /**预提标志*/
    private String predrawSign;
    /**是否产生对账数据---对账标志*/
    private String ifCompare;
    /**往来账号*/
    private String accId;
    /**账号启用日期*/
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date accStartDate;
    /**经办人代码*/
    private String operatorCode;
    /**复核员代码*/
    private String checkerCode;
    /**复核日期*/
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date checkTime;
    /**是否归集账号*/
    private String attributeAccSign;
    /**区域代码*/
    private String areaCode;
    /**行业代码*/
    private String vocationCode;
    /**助记码*/
    private String shortcut;
    /**联行网点*/
    @Corp(inject="unionNetName")
    private String unionNet;
    /**展期利率*/
    private BigDecimal delayRate;
    /**联行账号*/
    private String unionAcc;
    /**委托贷款帐号*/
    private String precationAcc;
    /**委托贷款方*/
    @Corp(inject="precationCorpName")
    private String precationCorp;
    private String precationCorpName;
    /**委托存款的手续费率*/
    private BigDecimal poundageRate;
    /**手续费收取账户*/
    private String chargeAcc;
    /**办理方式*/
    private Integer transactWay;
    /**销户日期*/
    private Date cancelDate;
    /**有效标志*/
    private String validSign;
    /**打回原因*/
    private String checkBackReason;
    /**到期单位*/
    private String endUnit;
    /**到期续存方式*/
    private Integer certificateWay;
    /**是否收取手续费*/
    private String handleCharge;
    /**创建人登录名称*/
    private String createBy;
    /**创建日期*/
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createDate;
    /**更新人登录名称*/
    private String updateBy;
    private Integer accAccNoRule;
    /**
     * 贷款账号
     */
    private String accCussent;
    @BtCompositeSearch(fieldName = "blnctr_Acc")
    private String blnctrAcc;
    private String unionNetName;
    @BtCompositeSearch(fieldName = "acc_Name")
    private String accName;
    @Corp(inject="netName")
    @BtCompositeSearch(fieldName = "net_id")
    private String netId;
    @BtCompositeSearch(fieldName = "net_id")
    private String netName;
    @Corp(inject="corpName")
    @BtCompositeSearch(fieldName = "corp_id")
    private String corpId;
    @BtCompositeSearch(fieldName = "corp_id")
    private String corpName;
    @Currency(inject="curName")
    @BtCompositeSearch(fieldName = "cur_id")
    private String curId;
    @BtCompositeSearch(fieldName = "cur_id")
    private String curName;
    @BtCompositeSearch(fieldName = "subject_Id")
    @Dict(tableName="FC_SUBJECT",valueFieldName="id",showFiledName="SUBJECT_NAME",inject="subjectName")
    private String subjectId;
    private String sysSubjectCode;
    @BtCompositeSearch(fieldName = "subject_name")
    private String subjectName;
    @BtCompositeSearch(fieldName = "t.acc_type")
    @Dict(dictGroup = "InnerAccount.AccType", inject = "accTypeName")
    private String accType;
    @BtCompositeSearch(fieldName = "t.acc_type")
    private String accTypeName;
    @BtCompositeSearch(fieldName = "t.balance_Way")
    @Dict(dictGroup = "Admin.BalanceWay", inject = "balanceWayName")
    private Integer balanceWay;
    @BtCompositeSearch(fieldName = "t.balance_Way")
    private String balanceWayName;
    @Dict(dictGroup = "inneraccount.fcStatus", inject = "accStateName")
    @BtCompositeSearch(fieldName = "acc_state")
    private String accState;
    @BtCompositeSearch(fieldName = "t.acc_state")
    private String accStateName;
    private Date accOpenTime;
    private String attributeName;
    private String natureName;
    /**计量单位*/
    private String measureUnit;
    /**
     * 资金池标识 0-非资金池；1-资金池
     */
    private String poolSign;
    /**
     * 资金池
     */
    @CorpPool(inject="netPoolName")
    private String netPoolId;
    private String netPoolName;
    @Dict(tableName="FC_SUBJECT_POOL",valueFieldName="ID",showFiledName="SUBJECT_NAME", inject = "subjectPoolName")
    private String subjectPoolId;
    private String subjectPoolName;
    /**
     * 关联活期账号
     */
    private String relationAcc;
    /**
     * 备注
     */
    private String rmk;
    /**利率种类 0-基准利率，1-基础利率*/
    private String referenceRateType;
    /**
     * 利息收入支出户
     */
    private String interestInExp;
    /**
     * 应收应付利息户
     */
    private String interestRecPay;
    /**计息账号*/
    private String interestAcc;
    /**内部账户余额*/
    private BigDecimal fcBalance;
   
}
