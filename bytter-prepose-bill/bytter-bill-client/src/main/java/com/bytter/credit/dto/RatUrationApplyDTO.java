package com.bytter.credit.dto;
import java.io.Serializable;
import java.util.List;
/**
 * 授信占用DTO
 * 
 * @author liyh
 *
 */
public class RatUrationApplyDTO implements Serializable {
    /**
     * 授信编号
     */
    private String ratCode;
    /**
     * 授信分配编号
     */
    private String disCode;
    /**
     * 占用授信单位
     */
    private String corpId;
    /**
     * 币别
     */
    private String curCode;
    /**
     * 状态
     */
    private Integer status;
    /** 占用授信类型 */
    private String ratType;
    /** 担保方式 */
    private String guaWay;
    /**
     * 授信占用明细
     */
    private List<RatUrationApplyListDTO> applyDtls;
    /** 附件ID */
    private String attachmentId;
    public String getRatCode() {
        return ratCode;
    }
    public void setRatCode(String ratCode) {
        this.ratCode = ratCode;
    }
    public String getDisCode() {
        return disCode;
    }
    public void setDisCode(String disCode) {
        this.disCode = disCode;
    }
    public String getCorpId() {
        return corpId;
    }
    public void setCorpId(String corpId) {
        this.corpId = corpId;
    }
    public String getCurCode() {
        return curCode;
    }
    public void setCurCode(String curCode) {
        this.curCode = curCode;
    }
    public Integer getStatus() {
        return status;
    }
    public void setStatus(Integer status) {
        this.status = status;
    }
    public String getRatType() {
        return ratType;
    }
    public void setRatType(String ratType) {
        this.ratType = ratType;
    }
    public List<RatUrationApplyListDTO> getApplyDtls() {
        return applyDtls;
    }
    public void setApplyDtls(List<RatUrationApplyListDTO> applyDtls) {
        this.applyDtls = applyDtls;
    }
    public String getGuaWay() {
        return guaWay;
    }
    public void setGuaWay(String guaWay) {
        this.guaWay = guaWay;
    }
    public String getAttachmentId() {
        return attachmentId;
    }
    public void setAttachmentId(String attachmentId) {
        this.attachmentId = attachmentId;
    }
}
