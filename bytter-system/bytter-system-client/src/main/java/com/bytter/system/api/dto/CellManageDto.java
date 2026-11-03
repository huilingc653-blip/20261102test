package com.bytter.system.api.dto;
import com.bytter.core.base.entity.IdEntity;
import java.io.Serializable;
import java.util.Date;

public class CellManageDto extends IdEntity implements Serializable {
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

    public String getCellCode() {
        return cellCode;
    }
    public void setCellCode(String cellCode) {
        this.cellCode = cellCode;
    }

    public String getCellName() {
        return cellName;
    }
    public void setCellName(String cellName) {
        this.cellName = cellName;
    }

    public String getParentId() {
        return parentId;
    }
    public void setParentId(String parentId) {
        this.parentId = parentId;
    }

    public String getRmk() {
        return rmk;
    }
    public void setRmk(String rmk) {
        this.rmk = rmk;
    }

    public String getValidSign() {
        return validSign;
    }
    public void setValidSign(String validSign) {
        this.validSign = validSign;
    }

    public String getCreateId() {
        return createId;
    }
    public void setCreateId(String createId) {
        this.createId = createId;
    }

    public Date getCreateTime() {
        return createTime;
    }
    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public String getUpdateId() {
        return updateId;
    }
    public void setUpdateId(String updateId) {
        this.updateId = updateId;
    }

    public Date getUpdateTime() {
        return updateTime;
    }
    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }
}
