package com.bytter.tally.api.dto;
import com.bytter.core.annotation.BtCompositeSearch;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

/**
 * Title: Entity
 * Description: 网点往来账务信息表
 *
 * @author onlineGenerator
 * @version 1.0
 * @since 2017-07-13 10:02:21
 */
@Data
public class FcUnionAccbookDTO implements java.io.Serializable {
    /**
     *
     */
    private static final long serialVersionUID = 1L;
    private String id;
    /**
     * 发送方网点
     */
    @BtCompositeSearch(fieldName = "a.send_Net")
    private String sendNet;
    private String sendNetName;
    /**
     * 发送方账号
     */
    private String sendAcc;
    /**
     * 发送方凭证号
     */
    private String sendVoucher;
    /**
     * 记账方向
     */
    @BtCompositeSearch(fieldName = "a.way")
    private Integer way;
    private String wayName;
    /**
     * 金额
     */
    @BtCompositeSearch(fieldName = "a.send_Money")
    private java.math.BigDecimal sendMoney;
    /**
     * 摘要
     */
    private String summaryContent;
    /**
     * 凭证种类
     */
    @BtCompositeSearch(fieldName = "a.voucher_type")
    private String voucherType;
    private String voucherName;
    /**
     * 接收方网点
     */
    @BtCompositeSearch(fieldName = "a.receive_Net")
    private String receiveNet;
    private String receiveNetName;
    /**
     * 接收方账号
     */
    private String receiveAcc;
    /**
     * 外单位
     */
    @BtCompositeSearch(fieldName = "a.external_Corp")
    private String externalCorp;
    /**
     * 外单位账号
     */
    @BtCompositeSearch(fieldName = "a.external_Acc")
    private String externalAcc;
    /**
     * 外单位开户行
     */
    @BtCompositeSearch(fieldName = "a.bank")
    private String bank;
    /**
     * 外单位信息省
     */
    private String externalProv;
    private String provInputName;
    /**
     * 外单位信息市
     */
    private String externalCity;
    private String cityName;
    /**
     * 接收方流水号
     */
    private String receiveOrder;
    /**
     * 往来状态
     */
    @BtCompositeSearch(fieldName = "a.bill_state")
    private String billState;
    private String billName;
    /**
     * 委托收款的收款单位
     */
    private String startCorp;
    /**
     * 委托收款的收款账号
     */
    private String startCorpAcc;
    /**
     * 委托收款的收款银行
     */
    private String startBank;
    /**
     * 返回信息
     */
    private String returnMsg;
    /**
     * 发送凭证日期
     */
    @JsonFormat(pattern = "yyyy-MM-dd")
    private java.util.Date sendVoucherDate;
    /**
     * 发起方业务全局唯一ID
     */
    private String sendBusGlobalId;
    /**
     * 业务来源类型
     */
    private Integer busOriType;
    /**
     * 接收方业务全局唯一ID
     */
    private String receiveBusGlobalId;
    /**
     * 创建时间
     */
    private java.util.Date createTime;
    /**
     * 创建用户代码
     */
    private String createBy;
    /**
     * 最后更新时间
     */
    private java.util.Date updateTime;
    /**
     * 最后更新用户代码
     */
    private String updateBy;
    /**
     * 记账员代码
     */
    private String operatorCode;
}
