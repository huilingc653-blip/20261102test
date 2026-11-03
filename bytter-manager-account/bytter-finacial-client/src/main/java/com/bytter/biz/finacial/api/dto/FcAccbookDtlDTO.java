package com.bytter.biz.finacial.api.dto;
import lombok.Data;
import java.math.BigDecimal;
/**
 * 账务明细对象
 *
 * @author liyh
 */
@Data
public class FcAccbookDtlDTO implements java.io.Serializable {
    /**
     * 主键ID
     */
    private String id;
    /**
     * 流水ID
     */
    private java.lang.String bookId;
    private java.lang.String cashFluxName;
    /**
     * 行号
     */
    private java.lang.Integer lineNo;
    /**
     * 摘要
     */
    private java.lang.String summaryContent;
    /**
     * 账号
     */
    private java.lang.String account;
    /**
     * 放款账号
     */
    private java.lang.String blnctrAcc;
    /**
     * 币别代码
     */
    private java.lang.String curCode;
    /**
     * 账户名称
     */
    private java.lang.String accName;
    /**
     * 单价
     */
    private java.math.BigDecimal price;
    /**
     * 数量
     */
    private java.lang.Integer amount;
    /**
     * 借方发生额
     */
    private java.math.BigDecimal debitMoney;
    /**
     * 贷方发生额
     */
    private java.math.BigDecimal creditMoney;
    /**
     * 余额
     */
    private java.math.BigDecimal balance;
    /**
     * 凭证种类代码
     */
    private java.lang.String voucherType;
    /**
     * 原始凭证号
     */
    private java.lang.String origVoucherNo;
    /**
     * 原始凭证ID
     */
    private java.lang.String origVoucherId;
    /**
     * 现金流量分类
     */
    private java.lang.String cashFlux;
    /**
     * 科目代码
     */
    private String subjectCode;
    /**
     * 冲预提金额
     */
    private BigDecimal rushMoney;
    private VoucherRenderControlDTO controlVo;
    private String subjectId;
    private String subjectName;
    private String corpId;
    private Integer balanceWay;
    private String curId;
    private String curName;
    private String netId;
    public BigDecimal getRushMoney() {
        if (rushMoney == null) {
            return BigDecimal.ZERO;
        }
        return rushMoney;
    }
    /**
     * 账户信息
     */
    private FcAccDTO accDTO;
}
