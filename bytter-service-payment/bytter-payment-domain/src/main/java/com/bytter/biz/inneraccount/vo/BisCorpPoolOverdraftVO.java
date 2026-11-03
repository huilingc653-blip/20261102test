package com.bytter.biz.inneraccount.vo;

import com.bytter.core.annotation.BtCompositeSearch;
import com.bytter.core.annotation.Currency;
import com.bytter.poi.annotation.Excel;
import com.bytter.poi.annotation.ExcelTarget;
import com.bytter.poi.handler.ExcelModel;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.springframework.format.annotation.NumberFormat;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * @Title: VO
 * @Description: 多级单位余额核算-单位归集透支金额表
 * @author huangx
 * @date 2025年6月20日11:03:04
 * @version 1.0
 *
 */
@ExcelTarget("BisCorpPoolOverdraftVO")
public class BisCorpPoolOverdraftVO extends ExcelModel implements Serializable {
    private String id;
    /**单位ID*/
    @BtCompositeSearch(fieldName = "b.corp_Id")
    private String corpId;
    @Excel(name = "*单位")
    @BtCompositeSearch(fieldName = "b.corp_Id")
    private String corpName;
    /**币别ID*/
    @Currency(inject = "curName")
    @BtCompositeSearch(fieldName = "b.cur_id")
    private java.lang.String curId;
    @Excel(name = "*币别")
    @BtCompositeSearch(fieldName = "b.cur_id")
    private java.lang.String curName;
    /**余额*/
    @NumberFormat(pattern = "#,##0.00")
    @Excel(name = "*透支金额", align = HorizontalAlignment.RIGHT, numFormat = "##########0.00")
    @BtCompositeSearch(fieldName = "b.overdraft_Amt")
    private BigDecimal overdraftAmt;
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

    public BigDecimal getOverdraftAmt() {
        return overdraftAmt;
    }

    public void setOverdraftAmt(BigDecimal overdraftAmt) {
        this.overdraftAmt = overdraftAmt;
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
        return "bisCorpPoolOverdraftService";
    }
}
