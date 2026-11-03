package com.bytter.increment.domain.vo;
import com.bytter.core.annotation.BtCompositeSearch;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import java.math.BigDecimal;
import java.util.Date;
/**
 * 存款查询信息。
 *
 * @author zhaohc
 */
@Data
public class ChoFinanceReportVO implements java.io.Serializable {
    private static final long serialVersionUID = 1476706608242453549L;
    private String id;
    /**单位ID*/
    // @Corp(inject = "corpName")
    @BtCompositeSearch(fieldName="f.corpId")
    private String corpId;
    @BtCompositeSearch(fieldName="f.corpId")
    private String corpName;
    /**币别*/
    // @Currency(inject = "curName")
    @BtCompositeSearch(fieldName = "f.curId")
    private String curId;
    private String curName;
    /**类型 req 银行理财 finance 银行定期存款 inner 内部定期存款*/
    private String type;
    /**内部/银行存款 1-银行存款，2-内部存款*/
    @BtCompositeSearch(fieldName = "f.opType")
    private String opType;
    /**购买金额*/
    private BigDecimal buyMoney;
    /**赎回（支取）金额*/
    private BigDecimal drawMoney;
    /**起息日期*/
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date interestStartDate;
    /**到期日期*/
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date deadlineDate;
    /**截止日期*/
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date calEndDate;
    /**参考收益率*/
    private BigDecimal referIncomeRate;
    /**利率*/
    private BigDecimal interestRate;
    /**补录利率*/
    private BigDecimal blInterest;
    /**补录利率*/
    private BigDecimal blInterestRate;
    /**收益条款:360-360天;365-365天*/
    private Integer incomeClause;
    /**最大转账日期*/
    private Date maxWishPayday;
    /**到期处理方式0-不处理 1-本金续存 2-本息续存*/
    private Integer certificateWay;
    /**账号利率*/
    private BigDecimal accInterestRate;
    /**计算天数*/
    private Integer calDays;
    /**参考收益*/
    private BigDecimal referInterest;
    /**应收利息*/
    private BigDecimal interestReceivable;
    /**实收利息*/
    private BigDecimal interestEarned;
    /**应收未收利息*/
    private BigDecimal interestReceivableNot;
    /**余额*/
    private BigDecimal balanceTotal;
    /**利息*/
    private BigDecimal interestTotal;
    /**合计*/
    private BigDecimal total;
    /**汇总*/
    private BigDecimal collect;
    /**期限Id*/
    private String termId;
    /**存款期限(保证金专用)*/
    private String dayLimit;
}
