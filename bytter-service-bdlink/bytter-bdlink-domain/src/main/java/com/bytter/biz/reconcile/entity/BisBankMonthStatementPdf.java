package com.bytter.biz.reconcile.entity;

import com.bytter.core.base.entity.IdEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.Date;
/**
 * 银行对账单Entity
 *
 * @author huangx 2022-12-01
 */
@Entity
@Table(name = "BIS_BANK_MONTH_STATEMENT_PDF", schema = "")
@SuppressWarnings("serial")
public class BisBankMonthStatementPdf extends IdEntity implements java.io.Serializable {
    /**
     * 账单文件大字段信息
     */
    private byte[] statementImg;
    /**
     * 创建时间
     */
    private Date createTime;
    @Column(name ="STATEMENT_IMG")
    public byte[] getStatementImg() {
        return statementImg;
    }
    public void setStatementImg(byte[] statementImg) {
        this.statementImg = statementImg;
    }
    @Column(name ="CREATE_TIME")
    public Date getCreateTime() {
        return createTime;
    }
    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }
}
