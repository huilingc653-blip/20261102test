package com.bytter.credit.vo;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
/**
 * 银行授信使用明细VO
 * 
 * @author liyh
 *
 */
public class RatUrationUseVO implements Serializable {
    /**
     * 授信ID
     */
    private String id;
    /**
     * 分配方式
     */
    private String disWay;
    // 授信模式
    private String ratMode;
    /**
     * 授信CODE
     */
    private String ratCode;
    /** 单位代码 */
    private String corpId;
    /** 单位代码 */
    private String corpName;
    /** 单位代码 */
    private String corpNo;
    /** 授信种类 */
    private String ratType;
    /** 银行类型 */
    private String bankType;
    /** 银行编号 */
    private String bankCode;
    /** 银行编号 */
    private String bankName;
    /** 币别 */
    private String curCode;
    private String curName;
    /** 授信开始日期 **/
    private Date beginDate;
    /** 授信结束日期 **/
    private Date endDate;
    /**
     * 品种总条数
     */
    private Integer rationSize;
    /**
     * 分配明细
     */
    List<RatUrationUseDtlVO> rationDlts;
    /**
     * 是否对接合同
     */
    private String conId;
    /**
     * 总行数
     */
    private Integer lineNos;
    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }
    public String getDisWay() {
        return disWay;
    }
    public void setDisWay(String disWay) {
        this.disWay = disWay;
    }
    public String getRatMode() {
        return ratMode;
    }
    public void setRatMode(String ratMode) {
        this.ratMode = ratMode;
    }
    public String getRatCode() {
        return ratCode;
    }
    public void setRatCode(String ratCode) {
        this.ratCode = ratCode;
    }
    public String getCorpId() {
        return corpId;
    }
    public void setCorpId(String corpId) {
        this.corpId = corpId;
    }
    public String getCorpName() {
        return corpName;
    }
    public void setCorpName(String corpName) {
        this.corpName = corpName;
    }
    public String getCorpNo() {
        return corpNo;
    }
    public void setCorpNo(String corpNo) {
        this.corpNo = corpNo;
    }
    public String getRatType() {
        return ratType;
    }
    public void setRatType(String ratType) {
        this.ratType = ratType;
    }
    public String getBankType() {
        return bankType;
    }
    public void setBankType(String bankType) {
        this.bankType = bankType;
    }
    public String getBankCode() {
        return bankCode;
    }
    public void setBankCode(String bankCode) {
        this.bankCode = bankCode;
    }
    public String getBankName() {
        return bankName;
    }
    public void setBankName(String bankName) {
        this.bankName = bankName;
    }
    public String getCurCode() {
        return curCode;
    }
    public void setCurCode(String curCode) {
        this.curCode = curCode;
    }
    public String getCurName() {
        return curName;
    }
    public void setCurName(String curName) {
        this.curName = curName;
    }
    public Date getBeginDate() {
        return beginDate;
    }
    public void setBeginDate(Date beginDate) {
        this.beginDate = beginDate;
    }
    public Date getEndDate() {
        return endDate;
    }
    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }
    public Integer getRationSize() {
        if (null == this.rationSize) {
            this.rationSize = Integer.valueOf(0);
        }
        return rationSize;
    }
    public void setRationSize(Integer rationSize) {
        this.rationSize = rationSize;
    }
    public List<RatUrationUseDtlVO> getRationDlts() {
        return rationDlts;
    }
    public void setRationDlts(List<RatUrationUseDtlVO> rationDlts) {
        this.rationDlts = rationDlts;
    }
    // 添加综合品种
    public void addRationDtl(RatUrationUseDtlVO rationDtl) {
        if (null == this.rationDlts) {
            this.rationDlts = new ArrayList<RatUrationUseDtlVO>();
        }
        this.rationDlts.add(rationDtl);
    }
    public String getConId() {
        return conId;
    }
    public void setConId(String conId) {
        this.conId = conId;
    }
    public Integer getLineNos() {
        return lineNos;
    }
    public void setLineNos(Integer lineNos) {
        this.lineNos = lineNos;
    }
}
