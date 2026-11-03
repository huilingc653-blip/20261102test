package com.bytter.biz.erp.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * Erp代发工资明细
 *
 * @author: fangcf
 * @date: 2026-5-13
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ErpNisWageDetailVo implements Serializable {
    /**
     * 明细号
     */
    private String wageDtlNo;
    /**
     * 代发工资ID
     */
    private String wageId;
    /**
     * 工号
     */
    private String workNo;
    /**
     * 姓名
     */
    private String userName;
    /**
     * 银行账号
     */
    private String bankAcc;
    /**
     * 账户名
     */
    private String accName;
    /**
     * 实发工资
     */
    private String wagesVerify;
    /**
     * 实发工资
     */
    private BigDecimal wages;
    /**
     * 用途/附言
     */
    private String purpose;
    /**
     * 所在省
     */
    private String prov;
    private String provName;
    /**
     * 所在市
     */
    private String city;
    private String cityName;
    /**
     * 开户行行号
     */
    private String bankCode;
    /**
     * 开户银行名称
     */
    private String bankName;
    /**
     * 跨行标识 replace={"同行_0","他行_1"}
     */
    private String difBank;
    /**
     * 地域标识, replace={"同城_0","异地_1"}
     */
    private String areaSign;
    /**
     * 交易状态
     */
    private String voucherStat;
    /**
     * 返回时间
     */
    private Date returnTime;
    /**
     * 返回信息（交易前）
     */
    private String firstReturnMsg;
    /**
     * 返回信息（交易后）
     */
    private String nextReturnMsg;
    /**
     * 创建人登录名称
     */
    private String createBy;
    /**
     * 创建日期
     */
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
     * 指令单号
     */
    private String voucherNo;


}
