package com.bytter.pricing.vo;
import com.bytter.core.annotation.BtCompositeSearch;
/**
 * @author luxy
 * @Title: DepositRateTypeVO
 * @ProjectName T2_IDEA
 * @Description: 存款利率类型
 * @date 2019/5/22 15:44
 */
public class DepositRateTypeVO {
    private String id;
    /**类型编码*/
    @BtCompositeSearch()
    private String code;
    /**类型名称 */
    @BtCompositeSearch()
    private String name;
    /**定期期限/通知类型*/
    private Integer period;
    /**期限周期类型*/
    private Integer periodCycle;
    /**货币类型*/
    private Integer moneyType;
    /**存款的总天数*/
    private Integer depositDays;
    /**存款种类*/
    @BtCompositeSearch()
    private String depositClass;
    /**是否为系统预设值*/
    private String isSysvalue;
    /**利率种类 1-存款 0或者""-贷款 */
    private Integer interestRateType;
    /**备注*/
    private String remark;
    /**通知类型*/
    private Integer noticeType;
    /**存款账户标识  1-内部账户存款 2-资金池账户存款 3-银行账户存款 */
    private String depositAccSign;
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
    public String getDepositAccSign() {
        return depositAccSign;
    }
    public void setDepositAccSign(String depositAccSign) {
        this.depositAccSign = depositAccSign;
    }
}
