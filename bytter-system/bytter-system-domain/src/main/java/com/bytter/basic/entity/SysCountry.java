package com.bytter.basic.entity;
import com.bytter.core.base.entity.IdEntity;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.Date;
/**
 * 实体-国家表
 *
 * @author qinl
 * @date 2019-07-15
 */
@Entity
@Table(name = "SYS_COUNTRY")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class SysCountry extends IdEntity implements java.io.Serializable {
    /**
     * code
     */
    private String code;
    /**
     * 中文名称
     */
    private String nameZh;
    /**
     * 英文名称
     */
    private String nameEn;
    /**
     * 系统初始化标志
     */
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

    /**
     * ISO编码-2
     */
    private String isoTwo;

    /**
     * ISO编码-3
     */
    private String isoThree;

    @Column(name = " CODE", nullable = false, length = 30)
    public String getCode() {
        return code;
    }
    public void setCode(String code) {
        this.code = code;
    }
    @Column(name = " NAME_ZH", nullable = false, length = 100)
    public String getNameZh() {
        return nameZh;
    }
    public void setNameZh(String nameZh) {
        this.nameZh = nameZh;
    }
    @Column(name = " NAME_EN", nullable = false, length = 200)
    public String getNameEn() {
        return nameEn;
    }
    public void setNameEn(String nameEn) {
        this.nameEn = nameEn;
    }
    @Column(name = " IS_SYSTEM", nullable = false, length = 1)
    public String getIsSystem() {
        return isSystem;
    }
    public void setIsSystem(String isSystem) {
        this.isSystem = isSystem;
    }
    @Column(name = "CREATE_BY", nullable = true, length = 50)
    public String getCreateBy() {
        return createBy;
    }
    public void setCreateBy(String createBy) {
        this.createBy = createBy;
    }
    @Column(name = "CREATE_DATE", nullable = true)
    public Date getCreateDate() {
        return createDate;
    }
    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }
    @Column(name = "UPDATE_BY", nullable = true, length = 50)
    public String getUpdateBy() {
        return updateBy;
    }
    public void setUpdateBy(String updateBy) {
        this.updateBy = updateBy;
    }
    @Column(name = "UPDATE_DATE", nullable = true)
    public Date getUpdateDate() {
        return updateDate;
    }
    public void setUpdateDate(Date updateDate) {
        this.updateDate = updateDate;
    }
    @Column(name = "CONTINENT")
    public String getContinent() {
        return continent;
    }
    public void setContinent(String continent) {
        this.continent = continent;
    }

    @Column(name = "ISO_TWO")
    public String getIsoTwo() {
        return isoTwo;
    }

    public void setIsoTwo(String isoTwo) {
        this.isoTwo = isoTwo;
    }

    @Column(name = "ISO_THREE")
    public String getIsoThree() {
        return isoThree;
    }

    public void setIsoThree(String isoThree) {
        this.isoThree = isoThree;
    }
}
