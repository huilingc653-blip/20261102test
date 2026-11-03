package com.bytter.erp.dto;

import java.io.Serializable;
import java.util.Date;

/**
 * @author zhenghj
 * @version 1.0
 * @Description: ERP贴现申请-接口参数对象
 * @date 2019-06-21
 */

public class ErpAbBillDiscountApplyDTO implements Serializable {


    /** 持票单位 */
    private String corpId;

    /**  票据号码 */
    private String origNo;

    /**业务接收人名称*/
    private String recNm;

    /**业务接收人账号*/
    private String recAcctId;

    /**业务接收人开户行号*/
    private String recAcctsvcr;

    /**内部业务标志：0  否；1 是*/
    private Integer insideSing;

    /** 业务办理日期 */
    private Date businessDate;

    /** 备注  */
    private String rmk;

    public String getCorpId() {
        return corpId;
    }

    public void setCorpId(String corpId) {
        this.corpId = corpId;
    }

    public String getOrigNo() {
        return origNo;
    }

    public void setOrigNo(String origNo) {
        this.origNo = origNo;
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

    public Integer getInsideSing() {
        return insideSing;
    }

    public void setInsideSing(Integer insideSing) {
        this.insideSing = insideSing;
    }

    public Date getBusinessDate() {
        return businessDate;
    }

    public void setBusinessDate(Date businessDate) {
        this.businessDate = businessDate;
    }

    public String getRmk() {
        return rmk;
    }

    public void setRmk(String rmk) {
        this.rmk = rmk;
    }

    @Override
    public String toString() {
        return "ErpAbBillDiscountApplyDTO{" +
                "corpId='" + corpId + '\'' +
                ", origNo='" + origNo + '\'' +
                ", recNm='" + recNm + '\'' +
                ", recAcctId='" + recAcctId + '\'' +
                ", recAcctsvcr='" + recAcctsvcr + '\'' +
                ", insideSing=" + insideSing +
                ", businessDate=" + businessDate +
                ", rmk='" + rmk + '\'' +
                '}';
    }
}
