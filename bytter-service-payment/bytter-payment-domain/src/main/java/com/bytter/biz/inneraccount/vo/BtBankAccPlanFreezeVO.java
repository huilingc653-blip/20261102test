package com.bytter.biz.inneraccount.vo;

import com.bytter.core.annotation.BtCompositeSearch;
import com.bytter.poi.annotation.Excel;
import com.bytter.poi.handler.ExcelModel;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 计划冻结申请表VO
 *
 * @author fangcf
 * @date 2025-02-11
 */
@Data
public class BtBankAccPlanFreezeVO extends ExcelModel implements Serializable {

    private String id;
    /**
     * 计划冻结单号
     */
    @BtCompositeSearch(fieldName = "bf.PLAN_FREEZE_NO")
    private String planFreezeNo;

    @BtCompositeSearch(fieldName = "bf.FREEZE_NO")
    private String freezeNo;
    /**
     * 银行账号ID
     */
    private String bankAccId;
    /**
     * 银行账号
     */
    @BtCompositeSearch(fieldName = "bf.BANK_ACC")
    @Excel(name = "*冻结账号")
    private String bankAcc;
    /**
     * 银行账号户名
     */
    private String bankAccName;
    /**
     * 银行账号开户行名称
     */
    private String bankName;
    /**
     * 单位ID
     */
    @BtCompositeSearch(fieldName = "bf.CORP_ID")
    private String corpId;
    /**
     * 单位ID
     */
    private String corpName;
    /**
     * 申请冻结金额
     */
    @BtCompositeSearch(fieldName = "bf.APPLY_FREEZE_MONEY")
    @Excel(name = "*申请冻结金额")
    private BigDecimal applyFreezeMoney;
    /**
     * 实际已冻结金额
     */
    @BtCompositeSearch(fieldName = "bf.REAL_FREEZE_MONEY")
    private BigDecimal realFreezeMoney;
    /**
     * 冻结开始时间
     */
    @BtCompositeSearch(fieldName = "bf.FREEZE_START_DATE")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Excel(name = "*冻结开始时间")
    private Date freezeStartDate;
    /**
     * 冻结开始时间
     */
    @BtCompositeSearch(fieldName = "bf.FREEZE_END_DATE")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Excel(name = "*冻结结束时间")
    private Date freezeEndDate;
    /**
     * 原因
     */
    @Excel(name = "*冻结原因")
    private String freezeRession;
    /**
     * 币别id
     */
    private String curId;
    /**
     * 币别名称
     */
    @Excel(name = "*币别")
    private String curName;
    /**
     * 状态
     */
    @BtCompositeSearch(fieldName = "bf.STATUS")
    private Integer status;
    /**
     * 状态名称
     */
    private String statusName;
    /**
     * 冻结状态：0-未冻结，1-部分冻结，2-全部冻结
     */
    @BtCompositeSearch(fieldName = "bf.FREEZE_STATUS")
    private Integer freezeStatus;
    /**
     * 冻结状态名称
     */
    private String freezeStatusName;
    /**
     * 是否冻结完成提醒：0-否，1-是
     */
    @BtCompositeSearch(fieldName = "bf.IS_FREEZE_COMPLETE")
    private String isFreezeComplete;
    private String isFreezeCompleteName;
    /**
     * 是否解冻：0-否，1-是
     */
    @BtCompositeSearch(fieldName = "bf.IS_UNFREEZE")
    private String isUnfreeze;
    private String isUnfreezeName;
    /**
     * 创建人登录名称ID
     */
    private String createBy;
    /**
     * 创建人登录名称
     */
    private String loginName;
    /**
     * 创建日期
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @BtCompositeSearch(fieldName = "bf.CREATE_DATE")
    private Date createDate;
    /**
     * 更新人登录名称
     */
    private String updateBy;
    /**
     * 更新日期
     */
    private Date updateDate;

    /**
     * 账户余额
     */
    private BigDecimal bal;
    /**
     * 可用余额
     */
    private BigDecimal availBal;
    /**
     * 冻结金额合计
     */
    private BigDecimal freezeMoneySum;
    /**
     * 解冻金额合计
     */
    private BigDecimal unFreezeMoneySum;
    /**
     * 冻结次数
     */
    private Integer freezeCount;
    /**
     * 解冻次数
     */
    private Integer unfreezeCount;
    /**
     * 是否上线
     */
    private String isOnline;
    /**
     * 是否隐藏复选框 1为是
     */
    private String checkboxHide;

    @Override
    public String getBeanName() {
        return "btBankAccPlanFreezeService";
    }
}
