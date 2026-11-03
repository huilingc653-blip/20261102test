package com.bytter.biz.finacial.api.dto;
import com.bytter.core.annotation.Currency;
import com.bytter.core.annotation.Dict;
import com.bytter.core.annotation.User;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
/**
 * 账务凭证对象
 *
 * @author liyh
 */
@Data
public class FcAccbookDTO implements java.io.Serializable {
    private String id;
    /**
     * 所属网点
     */
    private java.lang.String netId;
    /**
     * 流水号
     */
    private java.lang.String orderNo;
    /**
     * 凭证号
     */
    private java.lang.String voucherNo;
    /**
     * 录入日期
     */
    @JsonFormat(pattern = "yyyy-MM-dd")
    private java.util.Date enrolTime;
    /**
     * 记账日期
     */
    @JsonFormat(pattern = "yyyy-MM-dd")
    private java.util.Date tallyDate;
    /**
     * 起息日期
     */
    @JsonFormat(pattern = "yyyy-MM-dd")
    private java.util.Date interestStartDate;
    /**
     * 凭证种类代码
     */
    private java.lang.String voucherType;
    /**
     * 凭证种类名称
     */
    private java.lang.String voucherName;
    /**
     * 原凭证号
     */
    private java.lang.String origVoucherNo;
    /**
     * 加急标志
     */
    private java.lang.String emergencySign;
    /**
     * 记账员代码
     */
    private java.lang.String operatorCode;
    /**
     * 复核员代码
     */
    private java.lang.String checkerCode;
    /**
     * 复核日期
     */
    private java.util.Date checkTime;
    /**
     * 附件张数
     */
    private java.lang.Integer affixAmount;
    /**
     * 红蓝标志
     */
    private java.lang.String redblueSign;
    /**
     * 凭证状态
     */
    private java.lang.String voucherState;
    /**
     * 已计手续费标志
     */
    private java.lang.String chargedSign;
    /**
     * 冲账标志
     */
    private java.lang.String rushSign;
    /**
     * 产生方式
     */
    private java.lang.String generateMode;
    /**
     * 业务来源类型
     */
    private java.lang.Integer busOriType;
    /**
     * 业务种类
     */
    private java.lang.String busRaceId;
    /**
     * 业务种类
     */
    private java.lang.String raceName;
    /**
     * 备注
     */
    private java.lang.String rmk;
    /**
     * 凭证附言
     */
    private java.lang.String postscript;
    /**
     * 币别代码
     */
    private java.lang.String curCode;
    private java.lang.String curName;
    /**
     * 业务种类名称
     */
    private String busRaceName;
    /**
     * 全局业务ID
     */
    private java.lang.String busGlobalId;
    /**
     * 内部资金计划标识
     */
    private String innerSign;
    private String attachmentId;
    /**
     * 所属网点
     */
    private java.lang.String netName;
    /**
     * 记账员名称
     */
    private String operatorName;
    /**
     * 复核员名称
     */
    private String checkerName;
    /**
     * 是否对现金流项目做过更改
     */
    private String isChangeCashflux;
    /**
     * 管理员名称
     */
    private String manageName;
}
