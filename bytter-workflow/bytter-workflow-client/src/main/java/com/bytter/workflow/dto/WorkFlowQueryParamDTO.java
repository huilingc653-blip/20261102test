package com.bytter.workflow.dto;
import java.io.Serializable;
import java.math.BigDecimal;
/**
 * @author geyq 2021/12/29
 * @since 1.0.0
 */
public class WorkFlowQueryParamDTO implements Serializable {
    // 单据来源
    private String eqs_billFrom;
    // 待审批 “todo”
    private String flowStatus;
    // 单位
    private String ins_corpId;
    // 金额
    private BigDecimal geg_payMoney;
    private BigDecimal leg_payMoney;
    // 会计凭证号
    private String likes_bursarBillNo;
    // 收款账号名称
    private String likes_payeeAccName;
    // 登录用户
    private String loginName;
    // 排序 asc/desc
    private String sortFlag;
    public String getEqs_billFrom() {
        return eqs_billFrom;
    }
    public void setEqs_billFrom(String eqs_billFrom) {
        this.eqs_billFrom = eqs_billFrom;
    }
    public String getFlowStatus() {
        return flowStatus;
    }
    public void setFlowStatus(String flowStatus) {
        this.flowStatus = flowStatus;
    }
    public String getIns_corpId() {
        return ins_corpId;
    }
    public void setIns_corpId(String ins_corpId) {
        this.ins_corpId = ins_corpId;
    }
    public BigDecimal getGeg_payMoney() {
        return geg_payMoney;
    }
    public void setGeg_payMoney(BigDecimal geg_payMoney) {
        this.geg_payMoney = geg_payMoney;
    }
    public BigDecimal getLeg_payMoney() {
        return leg_payMoney;
    }
    public void setLeg_payMoney(BigDecimal leg_payMoney) {
        this.leg_payMoney = leg_payMoney;
    }
    public String getLikes_bursarBillNo() {
        return likes_bursarBillNo;
    }
    public void setLikes_bursarBillNo(String likes_bursarBillNo) {
        this.likes_bursarBillNo = likes_bursarBillNo;
    }
    public String getLikes_payeeAccName() {
        return likes_payeeAccName;
    }
    public void setLikes_payeeAccName(String likes_payeeAccName) {
        this.likes_payeeAccName = likes_payeeAccName;
    }
    public String getLoginName() {
        return loginName;
    }
    public void setLoginName(String loginName) {
        this.loginName = loginName;
    }
    public String getSortFlag() {
        return sortFlag;
    }
    public void setSortFlag(String sortFlag) {
        this.sortFlag = sortFlag;
    }
}
