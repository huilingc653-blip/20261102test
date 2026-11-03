package com.bytter.finance.domain.bond.entity;
import com.bytter.core.base.entity.IdEntity;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.Date;
/**
 * 债券类型实体
 *
 * @author ouyj
 */
@Entity
@Table(name = "CMS_BOND_TYPE")
@SuppressWarnings("serial")
public class CmsBondType extends IdEntity implements Serializable {
    /**
     * 债卷类型编码
     */
    private String bondCode;
    /**
     * 债卷类型名称
     */
    private String bondName;
    /**
     * 是否已删除
     */
    private String isDelete;
    /**
     * 备注
     */
    private String rmk;
    /**
     * 创建日期
     */
    private Date createDate;
    /**
     * 创建人登录名称
     */
    private String createBy;
    /**
     * 更新人登录名称
     */
    private String updateBy;
    /**
     * 更新日期
     */
    private Date updateDate;
    @Column(name = "BOND_CODE")
    public String getBondCode() {
        return bondCode;
    }
    public void setBondCode(String bondCode) {
        this.bondCode = bondCode;
    }
    @Column(name = "BOND_NAME")
    public String getBondName() {
        return bondName;
    }
    public void setBondName(String bondName) {
        this.bondName = bondName;
    }
    @Column(name = "IS_DELETE")
    public String getIsDelete() {
        return isDelete;
    }
    public void setIsDelete(String isDelete) {
        this.isDelete = isDelete;
    }
    @Column(name = "RMK")
    public String getRmk() {
        return rmk;
    }
    public void setRmk(String rmk) {
        this.rmk = rmk;
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
    @Override
    public String toString() {
        return "CmsBondType{" + "bondCode='" + bondCode + '\'' + ", bondName='" + bondName + '\'' + ", isDelete='"
                + isDelete + '\'' + ", rmk='" + rmk + '\'' + ", createDate=" + createDate + ", createBy='" + createBy
                + '\'' + ", updateBy='" + updateBy + '\'' + ", updateDate=" + updateDate + '}';
    }
}
