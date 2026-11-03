package com.bytter.biz.payment.api.dto;

import lombok.Data;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Data
public class NisBillheadSplitDTO implements java.io.Serializable {
    private String id;
    /**
     * 付款单号
     */
    private java.lang.String billNo;
    /**
     * 单位
     */
    private java.lang.String corpId;
    /**
     * 付款方式
     */
    private java.lang.String voucherTypeId;
    /**
     * 账号
     */
    private java.lang.String accId;
    /**
     * 付款账号
     */
    private java.lang.String payerAcc;
    /**
     * 付款账号名称
     */
    private java.lang.String payerAccName;
    /**
     * 付款开户行名称
     */
    private java.lang.String payerBank;
    /**
     * 下拨账号
     */
    private java.lang.String downAccId;
    /**
     * 币别
     */
    private java.lang.String curId;
    /**
     * 金额
     */
    private java.math.BigDecimal payMoney;
    /**
     * 票据金额
     */
    private java.math.BigDecimal billMoney;
    /**
     * 收款账号
     */
    private java.lang.String payeeAcc;
    /**
     * 收款账号名称
     */
    private java.lang.String payeeAccName;
    /**
     * 收款方开户行行号
     */
    private java.lang.String payeeBankCode;
    /**
     * 收款开户行名称
     */
    private java.lang.String payeeBank;
    /**
     * 收款行别
     */
    private java.lang.String bankTypeId;
    /**
     * 所在省
     */
    private java.lang.String prov;
    /**
     * 所在市
     */
    private java.lang.String city;
    /**
     * 期望支付日期
     */
    private java.util.Date wishPayday;
    /**
     * 加急标志
     */
    private java.lang.String isQuick;
    /**
     * 是否对私付款
     */
    private java.lang.String isForindividual;
    /**
     * 接口代码
     */
    private java.lang.String bifCode;
    /**
     * 跨行标识
     */
    private java.lang.String difBank;
    /**
     * 地域标识
     */
    private java.lang.String areaSign;
    /**
     * 会计凭证号
     */
    private java.lang.String bursarBillNo;
    /**
     * 用途
     */
    private java.lang.String purpose;
    /**
     * 附言
     */
    private java.lang.String addWord;
    /**
     * 是否直接付款
     */
    private java.lang.String directPayFlag;
    /**
     * 证书信息
     */
    private java.lang.String caInfoId;
    /**
     * 记账日期
     */
    private java.util.Date tallyDate;
    /**
     * 凭证流水号
     */
    private java.lang.String orderNo;
    /**
     * 记账凭证号码
     */
    private java.lang.String voucherNo;
    /**
     * ERP编号
     */
    private java.lang.String erpId;
    /**
     * 业务类别
     */
    private java.lang.String busType;
    /**
     * 单据来源
     */
    private java.lang.String billFrom;
    /**
     * 拆分前单据号
     */
    private java.lang.String splitId;
    /**
     * 原始拆分前单据号
     */
    private java.lang.String origSplitId;
    /**
     * 合并后单据号
     */
    private java.lang.String mergeId;
    /**
     * 状态
     */
    private java.lang.Integer status;
    /**
     * 创建人登录名称
     */
    private java.lang.String createBy;
    /**
     * 创建日期
     */
    private java.util.Date createDate;
    /**
     * 更新人登录名称
     */
    private java.lang.String updateBy;
    /**
     * 更新日期
     */
    private java.util.Date updateDate;
    /**
     * 附件ID
     */
    private java.lang.String attachmentId;
    /**
     * 是否已付款
     */
    private java.lang.String isPayed;
    /**
     * 内部资金计划标识
     */
    private String innerSign;
    /**
     * 记账标志
     **/
    private java.lang.String tallySign;
    /**
     * 记账员代码
     **/
    private java.lang.String tallyCode;
    /**
     * 是否垫支
     **/
    private java.lang.String isAdvance;
    /**
     * 营运资金比例
     **/
    private java.math.BigDecimal workCapitalRatio;
    /**
     * 预计收回日期
     **/
    private java.util.Date takeBackDate;
    /**
     * 是否已开票
     **/
    private java.lang.String isOpenBill;
    /**
     * 银行流水号
     **/
    private java.lang.String bankSerialNo;
    /**
     * 是否自动支付
     */
    private java.lang.String isAutoPay;
    /**
     * 策略ID
     */
    private java.lang.String policyId;
    /**
     * 票据类型
     */
    private Integer billType;
    /**
     * 票据流通介质
     */
    private Integer billFlag;
    /**
     * 时间戳
     */
    private String timeStamp;
    /**
     * 备注说明
     */
    private java.lang.String rmk;
    /**
     * 业务单位（票据付款专用）
     */
    private String busCorpId;
    /**
     * 业务人员（票据付款专用）
     */
    private String busPersonnel;
    /**
     * 票据池id（票据池开票专用）
     */
    private String bpId;
    /**
     * 排期要素id SCHEDULING_ELEMENTS_ID
     */
    private String schedulingElementsId;
    /**
     * 排期日期 SCHEDULING_DATE
     */
    private Date schedulingDate;
    /**
     * 待付款池id
     */
    private String nisBillheadPoolId;
    /**
     * 审批时间
     */
    private java.util.Date approveDate;
    /**
     * 交易状态
     */
    private String voucherStat;
    /**
     * 收款人统一社会信用代码
     */
    private String blRcvpymtpsUnnIdNo;
    /**
     * 共享计划付款日期
     */
    private Date sharingDate;
    /**
     * 结算中心实际付款账号
     */
    private String realPayerAccId;
    /**
     * 共享业务类型
     */
    private String shareBusTypeId;
    /**
     * 退款勾兑原单ID：只有退款勾兑新生成得单据才会有这个值
     */
    private String refundOrgId;

    private List<BigDecimal> payMoneys=new ArrayList<BigDecimal>();

    private List<String> budgets=new ArrayList<>();
}
