package com.bytter.system.entity;
import com.bytter.core.base.entity.IdEntity;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.Date;
/**
 * @author wangzy
 * @Title: CellManage
 * @Description: 管理单元实体
 * @date 2020/5/12 11:37
 */
@Entity
@Table(name = "SYS_CELL_MANAGE")
public class CellManage extends IdEntity implements java.io.Serializable {
    /**
     * 单元代码
     **/
    private String cellCode;
    /**
     * 单元名称
     **/
    private String cellName;
    /**
     * 父节点ID
     */
    private String parentId;
    /**
     * 备注
     */
    private String rmk;
    /**
     * 有效标志
     **/
    private String validSign;
    /**
     * 创建人id
     **/
    private String createId;
    /**
     * 创建日期	CREATE_DATE
     **/
    private Date createTime;
    /**
     * 更新人id
     **/
    private String updateId;
    /**
     * 更新日期
     **/
    private Date updateTime;
    @Column(name = "CELL_CODE")
    public String getCellCode() {
        return cellCode;
    }
    public void setCellCode(String cellCode) {
        this.cellCode = cellCode;
    }
    @Column(name = "CELL_NAME")
    public String getCellName() {
        return cellName;
    }
    public void setCellName(String cellName) {
        this.cellName = cellName;
    }
    @Column(name = "PARENT_ID")
    public String getParentId() {
        return parentId;
    }
    public void setParentId(String parentId) {
        this.parentId = parentId;
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
    @Column(name = "CREATE_ID")
    public String getCreateId() {
        return createId;
    }
    public void setCreateId(String createId) {
        this.createId = createId;
    }
    @Column(name = "CREATE_TIME")
    public Date getCreateTime() {
        return createTime;
    }
    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }
    @Column(name = "UPDATE_ID")
    public String getUpdateId() {
        return updateId;
    }
    public void setUpdateId(String updateId) {
        this.updateId = updateId;
    }
    @Column(name = "UPDATE_TIME")
    public Date getUpdateTime() {
        return updateTime;
    }
    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }
}
