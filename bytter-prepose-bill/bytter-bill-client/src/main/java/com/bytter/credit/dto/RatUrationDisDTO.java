package com.bytter.credit.dto;
import com.bytter.credit.constant.RationConstant;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
/**
 * 授信额度统计bean
 * 
 * @author liyh
 *
 */
public class RatUrationDisDTO implements Serializable {
    /**
     * 授信ID
     */
    private String id;
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
    private String curName;
    /** 授信开始日期 **/
    private Date beginDate;
    /** 授信结束日期 **/
    private Date endDate;
    private Integer rationSize;
    /**
     * 分配明细
     */
    List<RatUrationDisDtlDTO> rationDlts;
    /** 已占用比列 **/
    private double ratOccupiedRation;
    /** 业务品种 **/
    private String billName;
    /** 是否挂接担保 */
    private String isHangGuarantee;
    /**
     * 授信流水号
     */
    private String ratNo;

    /**授信合同号*/
    private String contractNo;

    /** 银行类型名称 */
    private String bankTypeName;

    public String getBankTypeName() {
        return bankTypeName;
    }

    public void setBankTypeName(String bankTypeName) {
        this.bankTypeName = bankTypeName;
    }

    public String getContractNo() {
        return contractNo;
    }

    public void setContractNo(String contractNo) {
        this.contractNo = contractNo;
    }

    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
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
    public String getCorpNo() {
        return corpNo;
    }
    public void setCorpNo(String corpNo) {
        this.corpNo = corpNo;
    }
    public String getCorpName() {
        return corpName;
    }
    public void setCorpName(String corpName) {
        this.corpName = corpName;
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
        return rationSize;
    }
    public void setRationSize(Integer rationSize) {
        this.rationSize = rationSize;
    }
    public List<RatUrationDisDtlDTO> getRationDlts() {
        return rationDlts;
    }
    public void setRationDlts(List<RatUrationDisDtlDTO> rationDlts) {
        this.rationDlts = rationDlts;
    }
    public double getRatOccupiedRation() {
        return ratOccupiedRation;
    }
    public void setRatOccupiedRation(double ratOccupiedRation) {
        this.ratOccupiedRation = ratOccupiedRation;
    }
    public String getBillName() {
        return billName;
    }
    public void setBillName(String billName) {
        this.billName = billName;
    }
    // 添加综合品种
    public void addRationDtl(RatUrationDisDtlDTO rationDtl) {
        if (null == this.rationDlts) {
            this.rationDlts = new ArrayList<RatUrationDisDtlDTO>();
        }
        this.rationDlts.add(rationDtl);
        calRationSize(rationDtl);
    }
    // 添加单品种
    public void setInitRationDlts(RatUrationDisDtlDTO rationDtl) {
        this.rationDlts = new ArrayList<RatUrationDisDtlDTO>();
        this.rationDlts.add(rationDtl);
        calRationSize(rationDtl);
    }
    public void calRationSize(RatUrationDisDtlDTO rationDtl) {
        if (null == this.rationSize) {
            this.rationSize = Integer.valueOf(0);
        }
        if (RationConstant.RAT_COMFLAG_0.equals(rationDtl.getItemFlag())) {
            this.rationSize++;
        } else {
            this.rationSize += rationDtl.getRationDlts().size();
        }
    }
    public void addRationSize() {
        if (null == this.rationSize) {
            this.rationSize = Integer.valueOf(0);
        }
        this.rationSize++;
    }
    public String getIsHangGuarantee() {
        return isHangGuarantee;
    }
    public void setIsHangGuarantee(String isHangGuarantee) {
        this.isHangGuarantee = isHangGuarantee;
    }
    public String getRatNo() {
        return ratNo;
    }
    public void setRatNo(String ratNo) {
        this.ratNo = ratNo;
    }
}
