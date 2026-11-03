package com.bytter.finance.domain.bond.vo;
import com.bytter.core.annotation.BtCompositeSearch;
import java.io.Serializable;
import java.util.Date;
/**
 * 债券类型vo
 *
 * @author ouyj
 */
public class CmsBondTypeVo implements Serializable {
    private String id;
    /**
     * 类别编码
     */
    @BtCompositeSearch(fieldName = "t.bond_code")
    private String bondCode;
    /**
     * 类别名称
     */
    @BtCompositeSearch(fieldName = "t.bond_name")
    private String bondName;
    /**
     * 备注
     */
    private String rmk;
    /**
     * 是否已删除
     */
    private String isDelete;
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
    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }
    public Date getCreateDate() {
        return createDate;
    }
    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }
    public String getCreateBy() {
        return createBy;
    }
    public void setCreateBy(String createBy) {
        this.createBy = createBy;
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
    public String getBondCode() {
        return bondCode;
    }
    public void setBondCode(String bondCode) {
        this.bondCode = bondCode;
    }
    public String getBondName() {
        return bondName;
    }
    public void setBondName(String bondName) {
        this.bondName = bondName;
    }
    public String getRmk() {
        return rmk;
    }
    public void setRmk(String rmk) {
        this.rmk = rmk;
    }
    public String getIsDelete() {
        return isDelete;
    }
    public void setIsDelete(String isDelete) {
        this.isDelete = isDelete;
    }
    @Override
    public String toString() {
        return "CmsBondTypeVo{" + "id='" + id + '\'' + ", bondCode='" + bondCode + '\'' + ", bondName='" + bondName
                + '\'' + ", rmk='" + rmk + '\'' + ", isDelete='" + isDelete + '\'' + ", createDate=" + createDate
                + ", createBy='" + createBy + '\'' + ", updateBy='" + updateBy + '\'' + ", updateDate=" + updateDate
                + '}';
    }
}
