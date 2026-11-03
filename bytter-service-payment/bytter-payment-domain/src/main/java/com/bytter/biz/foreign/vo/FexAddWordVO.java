package com.bytter.biz.foreign.vo;

import com.bytter.core.annotation.BtCompositeSearch;
import com.bytter.core.annotation.Dict;
import com.bytter.check.vo.IBaseDataInject;
import com.bytter.poi.annotation.Excel;
import com.bytter.poi.handler.ExcelModel;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Date;

/**
 * @Title: VO
 * @Description: 交易附言表
 * @author huangx
 * @date 2022-10-31 15:20:33
 * @version 1.0
 *
 */
public class FexAddWordVO extends ExcelModel implements Serializable, IBaseDataInject {
    private static final long serialVersionUID = 3650731181127660208L;
    /**单号*/
    private String id;
    /**交易编码*/
    @BtCompositeSearch(fieldName = "t.tran_Code")
    @Excel(name = "*交易编码")
    @NotNull( message = "foreign.addword.message.trancode.null")
    private String tranCode;
    /**交易编码名称*/
    @BtCompositeSearch(fieldName = "t.tran_Name")
    private String tranName;
    /**交易附言*/
    @BtCompositeSearch(fieldName = "f.add_Word")
    @Excel(name = "*交易附言")
    @NotNull( message = "foreign.addword.message.addword.null")
    private String addWord;
    /**收支方向 0-收汇 1-付汇*/
    @BtCompositeSearch(fieldName = "f.cd_Sign")
    @Dict(dictGroup = "fexAddWord.cdSign", inject = "cdSignName")
    private String cdSign;

    @BtCompositeSearch(fieldName = "f.cd_Sign")
    @Excel(name = "*收支方向")
    @NotNull( message = "foreign.addword.message.cdsign.null")
    private String cdSignName;
    /**有效标志*/
    @BtCompositeSearch(fieldName = "f.valid_Sign")
    @Dict(dictGroup = "valid_sign", inject = "validSignName")
    private String validSign;
    private String validSignName;
    /**创建人登录名称*/
    private String createBy;
    /**创建日期*/
    private Date createDate;
    /**更新人登录名称*/
    private String updateBy;
    /**更新日期*/
    private Date updateDate;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTranCode() {
        return tranCode;
    }

    public void setTranCode(String tranCode) {
        this.tranCode = tranCode;
    }

    public String getTranName() {
        return tranName;
    }

    public void setTranName(String tranName) {
        this.tranName = tranName;
    }

    public String getAddWord() {
        return addWord;
    }

    public void setAddWord(String addWord) {
        this.addWord = addWord;
    }

    public String getCdSign() {
        return cdSign;
    }

    public void setCdSign(String cdSign) {
        this.cdSign = cdSign;
    }

    public String getCdSignName() {
        return cdSignName;
    }

    public void setCdSignName(String cdSignName) {
        this.cdSignName = cdSignName;
    }

    public String getValidSign() {
        return validSign;
    }

    public void setValidSign(String validSign) {
        this.validSign = validSign;
    }

    public String getValidSignName() {
        return validSignName;
    }

    public void setValidSignName(String validSignName) {
        this.validSignName = validSignName;
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

    @Override
    public String getBeanName() {
        return "fexAddWordService";
    }
}
