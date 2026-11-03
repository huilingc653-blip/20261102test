package com.bytter.finance.dto;
import java.io.Serializable;
/**
 * Title: DepositRateTypeDTO Description: 存款利率类型DTO
 *
 * @author luxy
 * @since 2019/5/27 10:23
 */
public class DepositRateTypeDTO implements Serializable {
    private String id;
    /**
     * 类型编码
     */
    private String code;
    /**
     * 类型名称
     */
    private String name;
    /**
     * 定期期限/通知类型
     */
    private Integer period;
    /**
     * 期限周期类型
     */
    private Integer periodCycle;
    /**
     * 货币类型
     */
    private Integer moneyType;
    /**
     * 存款的总天数
     */
    private Integer depositDays;
    /**
     * 存款种类
     */
    private String depositClass;
    /**
     * 是否为系统预设值
     */
    private String isSysvalue;
    /**
     * 利率种类 1-存款 0或者""-贷款
     */
    private Integer interestRateType;
    /**
     * 备注
     */
    private String remark;
    /**
     * 通知类型
     */
    private Integer noticeType;
    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }
    public String getCode() {
        return code;
    }
    public void setCode(String code) {
        this.code = code;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public Integer getPeriod() {
        return period;
    }
    public void setPeriod(Integer period) {
        this.period = period;
    }
    public Integer getPeriodCycle() {
        return periodCycle;
    }
    public void setPeriodCycle(Integer periodCycle) {
        this.periodCycle = periodCycle;
    }
    public Integer getMoneyType() {
        return moneyType;
    }
    public void setMoneyType(Integer moneyType) {
        this.moneyType = moneyType;
    }
    public Integer getDepositDays() {
        return depositDays;
    }
    public void setDepositDays(Integer depositDays) {
        this.depositDays = depositDays;
    }
    public String getDepositClass() {
        return depositClass;
    }
    public void setDepositClass(String depositClass) {
        this.depositClass = depositClass;
    }
    public String getIsSysvalue() {
        return isSysvalue;
    }
    public void setIsSysvalue(String isSysvalue) {
        this.isSysvalue = isSysvalue;
    }
    public Integer getInterestRateType() {
        return interestRateType;
    }
    public void setInterestRateType(Integer interestRateType) {
        this.interestRateType = interestRateType;
    }
    public String getRemark() {
        return remark;
    }
    public void setRemark(String remark) {
        this.remark = remark;
    }
    public Integer getNoticeType() {
        return noticeType;
    }
    public void setNoticeType(Integer noticeType) {
        this.noticeType = noticeType;
    }
}
