package com.bytter.biz.finacial.api.vo;
import lombok.Data;
@Data
public class FcOnlineLedgerVO implements java.io.Serializable {
    private String id;
    /**账号*/
    private java.lang.String account;
    /**币别代码*/
    private java.lang.String curCode;
    /**记账总笔数*/
    private java.lang.Integer tallyCnt;
    /**复核总笔数*/
    private java.lang.Integer checkCnt;
    /**记账联机余额*/
    private java.math.BigDecimal onlineBalance;
    /**复核联机余额*/
    private java.math.BigDecimal checkBalance;
    /**当前冻结金额*/
    private java.math.BigDecimal freezeMoney;
    /**创建时间*/
    private java.util.Date createTime;
    /**创建用户代码*/
    private java.lang.String createBy;
    /**最后更新时间*/
    private java.util.Date updateTime;
    /**最后更新用户代码*/
    private java.lang.String updateBy;
}
