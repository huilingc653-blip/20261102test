package com.bytter.biz.inneraccount.vo;

import com.bytter.core.annotation.BtCompositeSearch;
import com.bytter.poi.annotation.Excel;
import com.bytter.poi.annotation.ExcelTarget;
import com.bytter.poi.handler.ExcelModel;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.springframework.format.annotation.NumberFormat;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * @Title: VO
 * @Description: 多级单位余额核算-单位归集余额表
 * @author huangx
 * @date 2025年6月20日11:03:04
 * @version 1.0
 *
 */
@ExcelTarget("BisCorpPoolBalVO")
public class BisCorpPoolBalVO extends ExcelModel implements Serializable {
    private String id;
    /**单位ID*/
    @BtCompositeSearch(fieldName = "b.corp_Id")
    private String corpId;
    @Excel(name = "*单位")
    @BtCompositeSearch(fieldName = "b.corp_Id")
    private String corpName;
    /**币别ID*/
    @BtCompositeSearch(fieldName = "b.cur_id")
    private java.lang.String curId;
    @Excel(name = "*币别")
    @BtCompositeSearch(fieldName = "b.cur_id")
    private java.lang.String curName;
    /**余额*/
    @NumberFormat(pattern = "#,##0.00")
    @Excel(name = "*余额", align = HorizontalAlignment.RIGHT, numFormat = "##########0.00")
    @BtCompositeSearch(fieldName = "b.bal")
    private BigDecimal bal;
    /**余额日期*/
    @Excel(name = "*余额日期")
    @JsonFormat(pattern = "yyyy-MM-dd")
    @BtCompositeSearch(fieldName = "b.bal_date")
    private Date balDate;
    /**获取时间*/
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @BtCompositeSearch(fieldName = "b.get_Date")
    private Date getDate;

    /**初始化标志0-否 1-是*/
    @BtCompositeSearch(fieldName = "b.init_Sign")
    private String initSign;
    private String initSignName;
    /**创建人登录名称*/
    private String createBy;
    /**创建日期*/
    private Date createDate;
    /**更新人登录名称*/
    private String updateBy;
    /**更新日期*/
    private Date updateDate;
    /**透支金额*/
    private BigDecimal overdraftAmt;
    /**归集可用余额=余额+透支金额*/
    private BigDecimal availBal;

    /**余额开始日期*/
    private Date balDateBegin;
    /**余额结束日期*/
    private Date balDateEnd;

    /**网点ID*/
    private String netId;

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


    public String getCorpName() {
        return corpName;
    }

    public void setCorpName(String corpName) {
        this.corpName = corpName;
    }

    public BigDecimal getBal() {
        return bal;
    }

    public void setBal(BigDecimal bal) {
        this.bal = bal;
    }

    public Date getBalDate() {
        return balDate;
    }

    public void setBalDate(Date balDate) {
        this.balDate = balDate;
    }

    public Date getGetDate() {
        return getDate;
    }

    public void setGetDate(Date getDate) {
        this.getDate = getDate;
    }

    public String getInitSign() {
        return initSign;
    }

    public void setInitSign(String initSign) {
        this.initSign = initSign;
    }

    public String getInitSignName() {
        return initSignName;
    }

    public void setInitSignName(String initSignName) {
        this.initSignName = initSignName;
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

    public BigDecimal getOverdraftAmt() {
        return overdraftAmt;
    }

    public void setOverdraftAmt(BigDecimal overdraftAmt) {
        this.overdraftAmt = overdraftAmt;
    }

    public BigDecimal getAvailBal() {
        return availBal;
    }

    public void setAvailBal(BigDecimal availBal) {
        this.availBal = availBal;
    }

    public Date getBalDateBegin() {
        return balDateBegin;
    }

    public void setBalDateBegin(Date balDateBegin) {
        this.balDateBegin = balDateBegin;
    }

    public Date getBalDateEnd() {
        return balDateEnd;
    }

    public void setBalDateEnd(Date balDateEnd) {
        this.balDateEnd = balDateEnd;
    }

    @Override
    public String getBeanName() {
        return "bisCorpPoolBalService";
    }

    public String getNetId() {
        return netId;
    }

    public void setNetId(String netId) {
        this.netId = netId;
    }
}
