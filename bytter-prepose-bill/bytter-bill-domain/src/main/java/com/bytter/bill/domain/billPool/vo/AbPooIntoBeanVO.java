package com.bytter.bill.domain.billPool.vo;
import com.bytter.core.annotation.Dict;
/**
 * 入池提交信息VO
 *
 * @author Administrator
 */
public class AbPooIntoBeanVO implements java.io.Serializable {
    public String ids;
    public String bpId;
    @Dict(dictGroup = "AB_POOL_INOUT_TYPE", inject = "intoTypeName")
    public String intoType;
    public String intoTypeName;
    /**
     * 质权人账号
     */
    private String bankAcc;
    /**
     * 质权人名称
     */
    private String accName;
    /**
     * 质权人联行号
     */
    private String bankNo;
    /**
     * 质权人银行名称
     */
    private String bankName;
    /**
     *  贴现申请、背书申请
     */
    /**
     * 票据池交易明细Id
     */
    private String pdId;
    /**
     * 票据池上一笔交易明细Id
     */
    private String lastPdId;
    /**
     * 票据从表id
     */
    private String vvid;
    /**
     * 业务接收人名称
     */
    private String recNm;
    /**
     * 业务接收人账号
     */
    private String recAcctId;
    /**
     * 业务接收人开户行号
     */
    private String recAcctsvcr;
    /**
     * 不得转让标记EM00可再转让EM01不得转让
     */
    private String banendrsmtmk;
    public String getIds() {
        return ids;
    }
    public String getBpId() {
        return bpId;
    }
    public String getIntoType() {
        return intoType;
    }
    public String getIntoTypeName() {
        return intoTypeName;
    }
    public String getBankAcc() {
        return bankAcc;
    }
    public String getAccName() {
        return accName;
    }
    public String getBankNo() {
        return bankNo;
    }
    public String getBankName() {
        return bankName;
    }
    public void setIds(String ids) {
        this.ids = ids;
    }
    public void setBpId(String bpId) {
        this.bpId = bpId;
    }
    public void setIntoType(String intoType) {
        this.intoType = intoType;
    }
    public void setIntoTypeName(String intoTypeName) {
        this.intoTypeName = intoTypeName;
    }
    public void setBankAcc(String bankAcc) {
        this.bankAcc = bankAcc;
    }
    public void setAccName(String accName) {
        this.accName = accName;
    }
    public void setBankNo(String bankNo) {
        this.bankNo = bankNo;
    }
    public void setBankName(String bankName) {
        this.bankName = bankName;
    }
    public String getPdId() {
        return pdId;
    }
    public void setPdId(String pdId) {
        this.pdId = pdId;
    }
    public String getLastPdId() {
        return lastPdId;
    }
    public void setLastPdId(String lastPdId) {
        this.lastPdId = lastPdId;
    }
    public String getVvid() {
        return vvid;
    }
    public void setVvid(String vvid) {
        this.vvid = vvid;
    }
    public String getRecNm() {
        return recNm;
    }
    public void setRecNm(String recNm) {
        this.recNm = recNm;
    }
    public String getRecAcctId() {
        return recAcctId;
    }
    public void setRecAcctId(String recAcctId) {
        this.recAcctId = recAcctId;
    }
    public String getRecAcctsvcr() {
        return recAcctsvcr;
    }
    public void setRecAcctsvcr(String recAcctsvcr) {
        this.recAcctsvcr = recAcctsvcr;
    }
    public String getBanendrsmtmk() {
        return banendrsmtmk;
    }
    public void setBanendrsmtmk(String banendrsmtmk) {
        this.banendrsmtmk = banendrsmtmk;
    }
    @Override
    public String toString() {
        return "AbPooIntoBeanVO{" +
                "ids='" + ids + '\'' +
                ", bpId='" + bpId + '\'' +
                ", intoType='" + intoType + '\'' +
                ", intoTypeName='" + intoTypeName + '\'' +
                ", bankAcc='" + bankAcc + '\'' +
                ", accName='" + accName + '\'' +
                ", bankNo='" + bankNo + '\'' +
                ", bankName='" + bankName + '\'' +
                ", pdId='" + pdId + '\'' +
                ", lastPdId='" + lastPdId + '\'' +
                ", vvid='" + vvid + '\'' +
                ", recNm='" + recNm + '\'' +
                ", recAcctId='" + recAcctId + '\'' +
                ", recAcctsvcr='" + recAcctsvcr + '\'' +
                ", banendrsmtmk='" + banendrsmtmk + '\'' +
                '}';
    }
}
