package com.bytter.biz.bdlink.api.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;
/**
 * Title: Entity
 * Description: BT_BANK_ACC
 *
 * @author onlineGenerator
 * @version 1.0
 * @since 2017-06-02 17:50:16
 */
@Data
public class BisAccBalDTO implements Serializable {
    private String id;
    /**
     * 业务单号
     */
    private String accNo;
    /**
     * 银行账号
     */
    private String bankAcc;
    /**
     * 银行名称
     */
    private String accName;
    /**
     * 开户行
     */
    private String bankName;
    /**
     * 单位ID
     */
    private String corpId;
    /**
     * 单位
     */
    private String corpName;
    /**
     * 币别ID
     */
    private String curId;
    /**
     * 币别
     */
    private String curName;
    /**
     * 接口代码
     */
    private String bifCode;
    /**
     * 获取时间
     */
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private java.util.Date getDate;
    private String getDateStr;
    /**
     * 余额日期
     */
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private java.util.Date balDate;
    private String balDateStr;
    /**
     * 余额
     */
    private java.math.BigDecimal bal;
    /**
     * 可用余额
     */
    private java.math.BigDecimal availBal;
    /**
     * 冻结金额
     */
    private java.math.BigDecimal frzBal;
    /**
     * 系统冻结金额
     */
    private java.math.BigDecimal sysFrzBal;
    /**
     * 返回码
     */
    private String returnCode;
    /**
     * 返回描述信息
     */
    private String returnMsg;
    /**
     * 状态
     */
    private Integer status;
    /**
     * 创建人登录名称
     */
    private String createBy;
    /**
     * 创建日期
     */
    private java.util.Date createDate;
    /**
     * 更新人登录名称
     */
    private String updateBy;
    /**
     * 更新日期
     */
    private java.util.Date updateDate;
    /**
     * 初始化余额标记（针对离线账号）
     */
    private String initSign;
    /**
     * 查询银行账户集合
     */
    private List<Map<String, Object>> btBankAccList;
    /**
     * 由明细生成余额开始时间
     */
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private Date beginDate;
    /**
     * 由明细生成余额结束时间
     */
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private Date endDate;
    /**
     * 是否需要重新生成余额
     */
    private Integer generateBalance;
    /**
     * 留底金额
     */
    private BigDecimal leaveMoney;
}
