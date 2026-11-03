package com.bytter.biz.report.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 定时任务-自动新增票据计划冻结申请VO
 *
 * @author fangcf
 * @date 2025-10-22
 */
@Data
public class AutoBillBalFreezeVO implements Serializable {

    // 单位id
    private String corpId;

    // 创建人id
    private String createBy;

    // 登录账号
    private String loginName;

    // 银行账号主键id
    private String bankAccId;

    // 银行账号
    private String bankAcc;

    // 银行类型
    private String bankTypeId;

    // 银行类型名称
    private String bankTypeName;

    // 到期日期
    private Date endDate;

    // 到期日期字符串
    private String endDateStr;

    // 票据金额
    private BigDecimal billMoney;

    // 冻结开始时间
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date freezeStartDate;

    // 冻结结束时间
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date freezeEndDate;

    /**
     * 有参构造
     *
     * @param corpId          单位id
     * @param createBy        创建人id
     * @param loginName       登录账号
     * @param bankAccId       银行账号主键id
     * @param bankAcc         银行账号
     * @param bankTypeId      银行类型
     * @param bankTypeName    银行类型名称
     * @param freezeStartDate 冻结开始时间
     * @param freezeEndDate   冻结结束时间
     */
    public AutoBillBalFreezeVO(String corpId, String createBy, String loginName, String bankAccId, String bankAcc,
                               String bankTypeId, String bankTypeName, Date freezeStartDate, Date freezeEndDate) {
        this.corpId = corpId;
        this.createBy = createBy;
        this.loginName = loginName;
        this.bankAccId = bankAccId;
        this.bankAcc = bankAcc;
        this.bankTypeId = bankTypeId;
        this.bankTypeName = bankTypeName;
        this.freezeStartDate = freezeStartDate;
        this.freezeEndDate = freezeEndDate;
    }
}