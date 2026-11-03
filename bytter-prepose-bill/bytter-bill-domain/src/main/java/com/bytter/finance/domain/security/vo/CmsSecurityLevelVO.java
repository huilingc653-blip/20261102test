package com.bytter.finance.domain.security.vo;
import com.bytter.core.annotation.BtCompositeSearch;
import java.io.Serializable;
import java.util.Date;
/**
 * 资产证券化层级表vo
 *
 * @author tongqr
 */
public class CmsSecurityLevelVO implements Serializable {
    private String id;
    /**
     * 类型编号
     */
    @BtCompositeSearch(fieldName = "l.level_code")
    private String levelCode;
    /**
     * 类型名称
     */
    @BtCompositeSearch(fieldName = "l.level_name")
    private String levelName;
    /**
     * 备注
     */
    private String rmk;
    /**
     * 状态
     */
    private Integer status;
    /**
     * 创建人
     */
    private String createBy;
    /**
     * 创建日期
     */
    private Date createDate;
    /**
     * 修改人
     */
    private String updateBy;
    /**
     * 修改时间
     */
    private Date updateDate;
    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }
    public String getLevelCode() {
        return levelCode;
    }
    public void setLevelCode(String levelCode) {
        this.levelCode = levelCode;
    }
    public String getLevelName() {
        return levelName;
    }
    public void setLevelName(String levelName) {
        this.levelName = levelName;
    }
    public String getRmk() {
        return rmk;
    }
    public void setRmk(String rmk) {
        this.rmk = rmk;
    }
    public Integer getStatus() {
        return status;
    }
    public void setStatus(Integer status) {
        this.status = status;
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
}
