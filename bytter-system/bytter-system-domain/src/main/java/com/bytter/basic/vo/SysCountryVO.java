package com.bytter.basic.vo;
import com.bytter.core.annotation.BtCompositeSearch;
import com.bytter.poi.annotation.ExcelTarget;
import com.bytter.poi.handler.ExcelModel;
import java.io.Serializable;
import java.util.Date;
/**
 * 实体-国家表
 *
 * @author qinl
 * @date 2019-07-15
 */
@ExcelTarget("SysCountryVO")
public class SysCountryVO extends ExcelModel implements Serializable {
    private String id;
    /**
     * code
     */
    @BtCompositeSearch(fieldName = "t.code")
    private String code;
    /**
     * 中文名称
     */
    @BtCompositeSearch(fieldName = "t.name_zh")
    private String nameZh;
    /**
     * 英文名称
     */
    @BtCompositeSearch(fieldName = "t.name_en")
    private String nameEn;
    /**
     * 系统初始化标志
     */
    @BtCompositeSearch(fieldName = "t.is_system")
    private String isSystem;
    /**
     * 创建人
     */
    private String createBy;
    /**
     * 创建时间
     */
    private Date createDate;
    /**
     * 更新人
     */
    private String updateBy;
    /**
     * 更新时间
     */
    private Date updateDate;
    /**
     * 国家所属大洲
     */
    private String continent;
    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }
    public String getCode() {
        return code;
    }
    public void setCode(String code) {
        this.code = code;
    }
    public String getNameZh() {
        return nameZh;
    }
    public void setNameZh(String nameZh) {
        this.nameZh = nameZh;
    }
    public String getNameEn() {
        return nameEn;
    }
    public void setNameEn(String nameEn) {
        this.nameEn = nameEn;
    }
    public String getIsSystem() {
        return isSystem;
    }
    public void setIsSystem(String isSystem) {
        this.isSystem = isSystem;
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
    public String getContinent() {
        return continent;
    }
    public void setContinent(String continent) {
        this.continent = continent;
    }
    @Override
    public String getBeanName() {
        return "SysCountryService";
    }
}
