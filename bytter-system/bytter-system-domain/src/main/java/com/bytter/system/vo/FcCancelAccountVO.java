package com.bytter.system.vo;
import com.bytter.system.api.vo.FcAccVO;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
/**
 * Created by zhangxb on 2017/9/29.
 */
@Data
public class FcCancelAccountVO implements Serializable {
    private FcAccVO fcAcc = new FcAccVO();
    /**
     * 转出账号集合
     */
    private List<FcAccVO> transferOutAcc;
    /**
     * 利息
     */
    private BigDecimal interest;
    /**
     * 预提/冲预提金额
     */
    private BigDecimal rushMoney;
    /**
     * 记账日期
     */
    private Date dayOver;
    /**
     * 1 需要 0 不需要
     * 如果账号余额和利息都为0，则不需要定义会计分录false
     */
    private String isNeedBusRule;
    /**
     * 1 标识单位下没有利息支出企业活期账号 ，给予提示
     */
    private Integer isInterestAcc;
    /**
     * 摘要
     */
    private String[] summaryContent = new String[0];
    /**
     * 账号
     */
    private String[] account = new String[0];
    /**
     * 凭证种类
     */
    private String voucherType;
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date startDate;
    private String blnctrAcc;
    private String id;
    private String busGlobalId;
    /**
     * 转入账号
     */
    private String transferAcc;
    /**
     * 借方金额
     */
    private BigDecimal[] debitMoney = new BigDecimal[0];
    /**
     * 贷方金额
     */
    private BigDecimal[] creditMoney = new BigDecimal[0];
}
