package com.bytter.supply.domain.platform.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import com.bytter.core.base.entity.IdEntity;

/**
 * @Title: ScfFunderInfo
 * @Description: 资金方信息
 * @version 1.0
 */
@Entity
@Table(name = "SCF_FUNDER_INFO", schema = "")
@SuppressWarnings("serial")
public class ScfFunderInfo extends IdEntity implements java.io.Serializable {

    /** 资金方编码 */
    private String code;
    /** 机构类型 01-保理公司 02-银行 03-其他*/
    private String type;
    /** 银行行号 */
    private String bankNo;
    /** 资金方名称 */
    private String name;
    /** 资金方标识 */
    private String sign;
    /** 有效标识 1-有效 0-无效 */
    private String valid;
    /** 备注 */
    private String rmk;
    /** 创建人 */
    private String createBy;
    /** 创建日期 */
    private java.util.Date createDate;
    /** 更新人 */
    private String updateBy;
    /** 更新日期 */
    private java.util.Date updateDate;

    @Column(name = "CODE")
    public String getCode() {
        return this.code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    @Column(name = "TYPE")
    public String getType() {
        return this.type;
    }

    public void setType(String type) {
        this.type = type;
    }

    @Column(name = "BANK_NO")
    public String getBankNo() {
        return this.bankNo;
    }

    public void setBankNo(String bankNo) {
        this.bankNo = bankNo;
    }

    @Column(name = "NAME")
    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Column(name = "SIGN")
    public String getSign() {
        return this.sign;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }

    @Column(name = "VALID")
    public String getValid() {
        return this.valid;
    }

    public void setValid(String valid) {
        this.valid = valid;
    }

    @Column(name = "RMK")
    public String getRmk() {
        return this.rmk;
    }

    public void setRmk(String rmk) {
        this.rmk = rmk;
    }


    @Column(name = "CREATE_BY")
    public String getCreateBy() {
        return this.createBy;
    }

    public void setCreateBy(String createBy) {
        this.createBy = createBy;
    }

    @Column(name = "CREATE_DATE")
    public java.util.Date getCreateDate() {
        return this.createDate;
    }

    public void setCreateDate(java.util.Date createDate) {
        this.createDate = createDate;
    }


    @Column(name = "UPDATE_BY")
    public String getUpdateBy() {
        return this.updateBy;
    }

    public void setUpdateBy(String updateBy) {
        this.updateBy = updateBy;
    }

    @Column(name = "UPDATE_DATE")
    public java.util.Date getUpdateDate() {
        return this.updateDate;
    }

    public void setUpdateDate(java.util.Date updateDate) {
        this.updateDate = updateDate;
    }
}
