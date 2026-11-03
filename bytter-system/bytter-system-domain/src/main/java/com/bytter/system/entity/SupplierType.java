package com.bytter.system.entity;
import com.bytter.core.annotation.BtCompositeSearch;
import com.bytter.core.base.entity.IdEntity;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.Date;
@Entity
@Table(name = "SYS_SUPPLIER_TYPE")
public class SupplierType extends IdEntity implements Serializable {
    /**
     * 供应商类别编码
     */
    @BtCompositeSearch(fieldName = "st.supplier_code")
    private String code;
    /**
     * 供应商类别名称
     */
    @BtCompositeSearch(fieldName = "st.supplier_name")
    private String name;
    /**
     * 备注
     */
    private String rmk;
    /**
     * 有效标志
     */
    @BtCompositeSearch(fieldName = "st.valid_sign")
    private String validSign;
    /**
     * 状态 1保存,-2删除
     */
    private String status;
    /**
     * 创建时间
     */
    private Date createDate;
    /**
     * 创建时间
     */
    private String createBy;
    /**
     * 修改时间
     */
    private Date updateDate;
    /**
     * 更新人
     */
    private String updateBy;
    @Column(name = "SUPPLIER_CODE")
    public String getCode() {
        return code;
    }
    public void setCode(String code) {
        this.code = code;
    }
    @Column(name = "SUPPLIER_NAME")
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    @Column(name = "RMK")
    public String getRmk() {
        return rmk;
    }
    public void setRmk(String rmk) {
        this.rmk = rmk;
    }
    @Column(name = "VALID_SIGN")
    public String getValidSign() {
        return validSign;
    }
    public void setValidSign(String validSign) {
        this.validSign = validSign;
    }
    @Column(name = "STATUS")
    public String getStatus() {
        return status;
    }
    public void setStatus(String status) {
        this.status = status;
    }
    @Column(name = "CREATE_DATE")
    public Date getCreateDate() {
        return createDate;
    }
    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }
    @Column(name = "CREATE_BY")
    public String getCreateBy() {
        return createBy;
    }
    public void setCreateBy(String createBy) {
        this.createBy = createBy;
    }
    @Column(name = "UPDATE_DATE")
    public Date getUpdateDate() {
        return updateDate;
    }
    public void setUpdateDate(Date updateDate) {
        this.updateDate = updateDate;
    }
    @Column(name = "UPDATE_BY")
    public String getUpdateBy() {
        return updateBy;
    }
    public void setUpdateBy(String updateBy) {
        this.updateBy = updateBy;
    }
}
