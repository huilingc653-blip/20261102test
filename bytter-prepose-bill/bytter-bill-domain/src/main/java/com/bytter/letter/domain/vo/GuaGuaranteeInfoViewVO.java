package com.bytter.letter.domain.vo;

import com.bytter.credit.dto.RatUrationDTO;
import com.bytter.credit.dto.RatUrationDisDTO;
import com.bytter.guarantee.velocity.tags.FcBondTag;
import com.bytter.system.api.dto.BtCurrencyDTO;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

@Data
public class GuaGuaranteeInfoViewVO implements Serializable {
    private String ratContains;// 获取挂接授信参数
    private String startOpenRat;// 参数设置是否启用敞口授信
    private String isHangBail;// 参数设置
    private Object LgInfoVo;
    /**
     * 开户行名称
     */
    private String bankName;
    /**
     * 標準幣種
     */
    BtCurrencyDTO standCur;
    /**
     * 是否挂接保证金
     */
    private String isHookBond;
    /**
     * 银行账号,不必赋值,只是在查询占用信息的时候保存相应信息
     */
    private String bondBankAcc;
    /**
     * 占用金额 即为保证金金额
     */
    private BigDecimal bailmoney;
    /**
     * 币别
     */
    private String bondCurId;
    /**
     * 保证金比例
     */
    private BigDecimal bailmoneyRate;
    /**
     * 保证金合同
     */
    private String bailmoneyContract;
    /**
     * 保证金ID
     */
    private String bondId;
    private BigDecimal curExrate;
    /**
     * 保证金利率
     */
    private BigDecimal bondRate;
    /**
     * 业务品种ID
     */
    private String billName;
    /**
     * 授信品种
     */
    private String ratItemCode;
    /**
     * 是否折合本位币
     */
    private String isRmbRat;
    private BigDecimal exchangerate;
    private RatUrationDTO urationDTO;
    /**
     * 银行编号
     */
    private String ratBankCode;
    /**
     * 是否本位币
     */
    private String naturalSign;
    private FcBondTag fcBondTag;
    private String taskId;
    private GuaPerformRegVO guaPerformRegVO;

    private List<RatUrationDisDTO> rations;
    private String guaranteeedCorp;
}
