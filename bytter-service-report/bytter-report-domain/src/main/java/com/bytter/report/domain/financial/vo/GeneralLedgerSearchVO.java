package com.bytter.report.domain.financial.vo;
import java.io.Serializable;
/**
 * 总账查询条件VO对象
 *
 * @author zuoy 2020-11-19
 */
public class GeneralLedgerSearchVO implements Serializable {
    /**
     * 单位ID
     */
    private String netId;
    private String netName;
    /**
     * 币别ID
     */
    private String curId;
    private String curName;
    /**
     * 科目ID
     */
    private String subjectId;
    /**
     * 科目编码
     */
    private String subjectCode;
    /**
     * 科目名称
     */
    private String subjectName;
    /**
     * 余额方向
     */
    private Integer balanceWay;
    /**
     * 余额方向
     */
    private String balanceWayName;
    /**
     * 明细科目ID
     */
    private String showDetailSubject;
    /**
     * 开始日期 yyyy-MM
     */
    private String beginDate;
    /**
     * 结束日期 yyyy-MM
     */
    private String endDate;
    public String getNetId() {
        return netId;
    }
    public void setNetId(String netId) {
        this.netId = netId;
    }
    public String getNetName() {
        return netName;
    }
    public void setNetName(String netName) {
        this.netName = netName;
    }
    public String getCurId() {
        return curId;
    }
    public void setCurId(String curId) {
        this.curId = curId;
    }
    public String getCurName() {
        return curName;
    }
    public void setCurName(String curName) {
        this.curName = curName;
    }
    public String getSubjectId() {
        return subjectId;
    }
    public void setSubjectId(String subjectId) {
        this.subjectId = subjectId;
    }
    public String getSubjectName() {
        return subjectName;
    }
    public String getSubjectCode() {
        return subjectCode;
    }
    public void setSubjectCode(String subjectCode) {
        this.subjectCode = subjectCode;
    }
    public void setSubjectName(String subjectName) {
        this.subjectName = subjectName;
    }
    public Integer getBalanceWay() {
        return balanceWay;
    }
    public void setBalanceWay(Integer balanceWay) {
        this.balanceWay = balanceWay;
    }
    public String getBalanceWayName() {
        return balanceWayName;
    }
    public void setBalanceWayName(String balanceWayName) {
        this.balanceWayName = balanceWayName;
    }
    public String getShowDetailSubject() {
        return showDetailSubject;
    }
    public void setShowDetailSubject(String showDetailSubject) {
        this.showDetailSubject = showDetailSubject;
    }
    public String getBeginDate() {
        return beginDate;
    }
    public void setBeginDate(String beginDate) {
        this.beginDate = beginDate;
    }
    public String getEndDate() {
        return endDate;
    }
    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }
}
