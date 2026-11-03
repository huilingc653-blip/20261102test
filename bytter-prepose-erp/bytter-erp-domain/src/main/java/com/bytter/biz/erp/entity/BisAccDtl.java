package com.bytter.biz.erp.entity;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 银行账号明细表
 *
 * @TableName bis_acc_dtl
 */
@Data
public class BisAccDtl implements Serializable {
    /**
     * id
     */
    private String id;
    /**
     * 租户id
     */
    private String tenantId;

    /**
     * 本方账号
     */
    private String bankAcc;

    /**
     * 本方账户名
     */
    private String accName;

    /**
     * 本方账户开户行
     */
    private String bankName;

    /**
     * 接口代码
     */
    private String bifCode;

    /**
     * 对方账号
     */
    private String oppAccNo;

    /**
     * 对方账户名
     */
    private String oppAccName;

    /**
     * 对方账户开户行
     */
    private String oppAccBank;

    /**
     * 借贷标志
     */
    private String cdSign;

    /**
     * 交易金额
     */
    private BigDecimal amt;

    /**
     * 余额
     */
    private BigDecimal bal;

    /**
     * 币别ID
     */
    private String curId;

    /**
     * 用途
     */
    private String uses;

    /**
     * 附言
     */
    private String postscript;

    /**
     * 摘要
     */
    private String remark;

    /**
     * 交易时间
     */
    private Date transTime;

    /**
     * 企业流水号
     */
    private String voucherNo;

    /**
     * 银行流水号
     */
    private String bankSerialNo;

    /**
     * 返回时间
     */
    private Date returnTime;

    /**
     * 红蓝标志
     */
    private String rbSign;

    /**
     * 业务单号
     */
    private String accNo;

    /**
     * 导出标识
     */
    private String importSign;

    /**
     * 导出时间
     */
    private Date importDate;

    /**
     * 资金计划挂接状态 0未确认 1已确认 2已删除
     */
    private String fbsStatus;

    /**
     * 项目信息
     */
    private String projectRemark;

    /**
     * 结算类型  0-现款 1-票据
     */
    private String settlType;

    /**
     * 0-未勾兑，1-已勾兑，默认0-未勾兑
     */
    private String blendSign;

    /**
     * 1-不处理原单、2-生成新单、3-打回原单
     */
    private String refundBlendType;

    /**
     * 备注
     */
    private String rmk;

    /**
     * 业务单id
     */
    private String billId;

    /**
     * 合并后单据ID
     */
    private String mergeId;

    /**
     * 拆分前单据ID
     */
    private String splitId;

    /**
     * 状态
     */
    private Integer status;

    /**
     * 业务类别id
     */
    private String busType;

    /**
     * 创建时间
     */
    private Date createDate;

    /**
     * 创建人
     */
    private String createBy;

    /**
     * 更新时间
     */
    private Date updateDate;

    /**
     * 更新人
     */
    private String updateBy;
    /**
     *企业流水号
     * */
//    private String firmSerialNo;

}
