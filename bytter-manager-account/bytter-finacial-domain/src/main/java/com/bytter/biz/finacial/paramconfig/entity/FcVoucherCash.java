package com.bytter.biz.finacial.paramconfig.entity;
import com.bytter.core.base.entity.IdEntity;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.Date;
/**
 * @author
 * @version V1.0
 * @Title: Entity
 * @Description: 现金流量项目科目关系表
 * @date 2020-09-10 09:18:39
 */
@Entity
@Table(name = "FC_VOUCHER_CASH", schema = "")
@SuppressWarnings("serial")
public class FcVoucherCash extends IdEntity implements java.io.Serializable {
    /**
     * 借方科目代码
     */
    private String debitSubject;
    /**
     * 贷方科目代码
     */
    private String creditSubject;
    /**
     * 借方现金流项目代码
     */
    private String debitItem;
    /**
     * 贷方现金流项目代码
     */
    private String creditItem;
    /**
     * 备注
     */
    private String rmk;
    /**
     * 是否有效
     */
    private String validSign;
    /**
     * 创建时间
     */
    private Date createTime;
    /**
     * 创建用户代码
     */
    private String createBy;
    /**
     * 最后更新时间
     */
    private Date updateTime;
    /**
     * 最后更新用户代码
     */
    private String updateBy;
    @Column(name = "DEBIT_SUBJECT")
    public String getDebitSubject() {
        return debitSubject;
    }
    public void setDebitSubject(String debitSubject) {
        this.debitSubject = debitSubject;
    }
    @Column(name = "CREDIT_SUBJECT")
    public String getCreditSubject() {
        return creditSubject;
    }
    public void setCreditSubject(String creditSubject) {
        this.creditSubject = creditSubject;
    }
    @Column(name = "DEBIT_ITEM")
    public String getDebitItem() {
        return debitItem;
    }
    public void setDebitItem(String debitItem) {
        this.debitItem = debitItem;
    }
    @Column(name = "CREDIT_ITEM")
    public String getCreditItem() {
        return creditItem;
    }
    public void setCreditItem(String creditItem) {
        this.creditItem = creditItem;
    }
    public String getRmk() {
        return rmk;
    }
    public void setRmk(String rmk) {
        this.rmk = rmk;
    }
    /**
     * 方法: 取得java.util.Date
     *
     * @return: java.util.Date  创建时间
     */
    @Column(name = "CREATE_TIME")
    public Date getCreateTime() {
        return this.createTime;
    }
    /**
     * 方法: 设置java.util.Date
     *
     * @param: java.util.Date  创建时间
     */
    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }
    /**
     * 方法: 取得java.lang.String
     *
     * @return: java.lang.String  创建用户代码
     */
    @Column(name = "CREATE_BY")
    public String getCreateBy() {
        return this.createBy;
    }
    /**
     * 方法: 设置java.lang.String
     *
     * @param: java.lang.String  创建用户代码
     */
    public void setCreateBy(String createBy) {
        this.createBy = createBy;
    }
    /**
     * 方法: 取得java.util.Date
     *
     * @return: java.util.Date  最后更新时间
     */
    @Column(name = "UPDATE_TIME")
    public Date getUpdateTime() {
        return this.updateTime;
    }
    /**
     * 方法: 设置java.util.Date
     *
     * @param: java.util.Date  最后更新时间
     */
    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }
    /**
     * 方法: 取得java.lang.String
     *
     * @return: java.lang.String  最后更新用户代码
     */
    @Column(name = "UPDATE_BY")
    public String getUpdateBy() {
        return this.updateBy;
    }
    /**
     * 方法: 设置java.lang.String
     *
     * @param: java.lang.String  最后更新用户代码
     */
    public void setUpdateBy(String updateBy) {
        this.updateBy = updateBy;
    }
    @Column(name = "VALID_SIGN")
    public String getValidSign() {
        return validSign;
    }
    public void setValidSign(String validSign) {
        this.validSign = validSign;
    }
}
