package com.bytter.biz.foreign.vo;

import com.bytter.core.annotation.BtCompositeSearch;
import com.bytter.core.annotation.Corp;
import com.bytter.check.vo.IBaseDataInject;
import com.fasterxml.jackson.annotation.JsonFormat;

import java.util.Date;

/**
 * @Title: VO
 * @Description: 单位敞口模板表
 * @author huangx
 * @date 2022-06-24 15:20:33
 * @version 1.0
 */
public class FexCorpExposureTemplateVO implements IBaseDataInject, java.io.Serializable {
    /**ID*/
    private String id;
    /**单位ID*/
    @BtCompositeSearch(fieldName = "f.corp_Id")
    @Corp(inject = "corpName")
    private String corpId;
    private String corpName;
    /**币别ID*/
    private String curId;
    private String curName;
    private String englishCode;
    /**模板ID*/
    private String templateId;
    private String templateCode;
    @BtCompositeSearch(fieldName = "t.template_Name")
    private String templateName;
    /**有效标志 0 无效 1 有效*/
    @BtCompositeSearch(fieldName = "f.valid_Sign")
    private String validSign;
    /**创建人登录名称*/
    private String createBy;
    /**创建日期*/
    @BtCompositeSearch(fieldName = "f.create_Date")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createDate;
    /**更新人登录名称*/
    private String updateBy;
    /**更新日期*/
    @BtCompositeSearch(fieldName = "f.update_Date")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date updateDate;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

    public String getEnglishCode() {
        return englishCode;
    }

    public void setEnglishCode(String englishCode) {
        this.englishCode = englishCode;
    }

    public String getTemplateId() {
        return templateId;
    }

    public void setTemplateId(String templateId) {
        this.templateId = templateId;
    }

    public String getTemplateCode() {
        return templateCode;
    }

    public void setTemplateCode(String templateCode) {
        this.templateCode = templateCode;
    }

    public String getTemplateName() {
        return templateName;
    }

    public void setTemplateName(String templateName) {
        this.templateName = templateName;
    }

    public String getValidSign() {
        return validSign;
    }

    public void setValidSign(String validSign) {
        this.validSign = validSign;
    }

    public String getCreateBy() {
        return createBy;
    }

    public void setCreateBy(String createBy) {
        this.createBy = createBy;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public String getUpdateBy() {
        return updateBy;
    }

    public void setUpdateBy(String updateBy) {
        this.updateBy = updateBy;
    }

    public Date getUpdateDate() {
        return updateDate;
    }

    public void setUpdateDate(Date updateDate) {
        this.updateDate = updateDate;
    }
}
