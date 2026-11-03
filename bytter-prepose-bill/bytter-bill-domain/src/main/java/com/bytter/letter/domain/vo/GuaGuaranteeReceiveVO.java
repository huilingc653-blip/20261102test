package com.bytter.letter.domain.vo;

import com.bytter.credit.dto.RatUrationDTO;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

@Data
public class GuaGuaranteeReceiveVO implements Serializable {
    private static final long serialVersionUID = 1L;

    /** 保函基本信息 */
    private GuaGuaranteeInfoVO vo;
    /** 被担保单位名称（逗号分隔） */
    private String guaranteeedCorp;
    /** 是否挂接保证金 */
    private String isHangBail;
    /** 是否启用敞口授信 */
    private String startOpenRat;
    /** 挂接授信参数 */
    private String ratContains;
    /** 授信明细 */
    private RatUrationDTO urationDTO;
    /** 授信银行 */
    private String ratBankCode;
    /** 原始授信占用金额 */
    private BigDecimal ratApplyMoneyPre;
    /** 汇率 */
    private BigDecimal exchangeRate;
    /** 单位本位币 */
    private String corpCurId;
    /** 本位币标识 */
    private String naturalSign;
    /** 操作人 */
    private String operator;
    /** 是否外部挂接保证金 */
    private String isHookBond;
    /** 保证金银行账户 */
    private String bondBankAcc;
    /** 保证金金额 */
    private BigDecimal bailmoney;
    /** 保证金币别 */
    private String bondCurId;
    /** 保证金比例 */
    private BigDecimal bailmoneyRate;
    /** 保证金合同 */
    private String bailmoneyContract;
    /** 保证金ID */
    private String bondId;
}
