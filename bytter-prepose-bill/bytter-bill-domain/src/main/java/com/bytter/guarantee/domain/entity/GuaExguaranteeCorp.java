package com.bytter.guarantee.domain.entity;
import com.bytter.core.base.entity.IdEntity;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
@Entity
@Table(name = "GUA_EXGUARANTEE_CORP", schema = "")
public class GuaExguaranteeCorp extends IdEntity implements java.io.Serializable {
    /**
     * 对外担保限额id
     */
    private String exAmountId;
    /**
     * 单位ID
     */
    private String corpId;
    /**
     * 单位名称
     */
    private String corpName;
    /**
     * 币别ID
     */
    private String curId;
    /**
     * 币别名称
     */
    private String curName;
    /**
     * 排序
     */
    private Integer sort;
    /**
     * 租户ID
     */
    private String tenantId;

    @Column(name = "EX_AMOUNT_ID")
    public String getExAmountId() {
        return exAmountId;
    }
    public void setExAmountId(String exAmountId) {
        this.exAmountId = exAmountId;
    }
    @Column(name = "CORP_ID")
    public String getCorpId() {
        return corpId;
    }
    public void setCorpId(String corpId) {
        this.corpId = corpId;
    }
    @Column(name = "CORP_NAME")
    public String getCorpName() {
        return corpName;
    }
    public void setCorpName(String corpName) {
        this.corpName = corpName;
    }
    @Column(name = "SORT")
    public Integer getSort() {
        return sort;
    }
    public void setSort(Integer sort) {
        this.sort = sort;
    }

    @Column(name = "CUR_ID")
    public String getCurId() {
        return curId;
    }

    public void setCurId(String curId) {
        this.curId = curId;
    }

    @Column(name = "CUR_NAME")
    public String getCurName() {
        return curName;
    }

    public void setCurName(String curName) {
        this.curName = curName;
    }

    @Column(name = "TENANT_ID")
    public String getTenantId() {
        return tenantId;
    }

    public void setTenantId(String tenantId) {
        this.tenantId = tenantId;
    }
}
