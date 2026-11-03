package com.bytter.biz.wage.vo;

import lombok.Data;

import java.math.BigDecimal;

/**
 * 代发下拨金额自动同步VO
 *
 * @author fangcf
 * @date 2025-11-21
 */
@Data
public class NisWageAllocationSyncVO implements java.io.Serializable {

    /**
     * 主键id
     */
    private String id;
    /**
     * 单位id
     */
    private String corpId;
    /**
     * 银行账号
     */
    private String bankAcc;
    /**
     * 银行账号名称
     */
    private String bankAccName;
    /**
     * 开户行行号
     */
    private String bankCode;
    /**
     * 开户行名称
     */
    private String bankName;
    /**
     * 币别
     */
    private String curId;
    /**
     * 下拨金额
     */
    private BigDecimal releaseAmount;
    /**
     * 实发工资
     */
    private String wagesVerify;

    public NisWageAllocationSyncVO() {
    }

    /**
     * 有参构造
     *
     * @param corpId      单位id
     * @param bankAcc     银行账号
     * @param bankAccName 银行账号名称
     * @param bankCode    开户行行号
     * @param bankName    开户行名称
     * @param curId       币别
     */
    public NisWageAllocationSyncVO(String corpId, String bankAcc, String bankAccName, String bankCode, String bankName, String curId) {
        this.corpId = corpId;
        this.bankAcc = bankAcc;
        this.bankAccName = bankAccName;
        this.bankCode = bankCode;
        this.bankName = bankName;
        this.curId = curId;
    }
}
