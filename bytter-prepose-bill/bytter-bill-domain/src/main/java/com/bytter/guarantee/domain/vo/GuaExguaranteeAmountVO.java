package com.bytter.guarantee.domain.vo;
import com.bytter.core.annotation.BtCompositeSearch;
import com.bytter.core.annotation.Corp;
import com.bytter.core.annotation.Currency;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
public class GuaExguaranteeAmountVO implements Serializable {
    private String id;
    /** 当期净资产 */
    @BtCompositeSearch(fieldName = "g.assets")
    private BigDecimal assets;
    /** 币种id */
//    @Currency(inject = "curName")
    @BtCompositeSearch(fieldName = "g.cur_id")
    private String curId;
    @BtCompositeSearch(fieldName = "g.cur_id")
    private String curName;
    /** 对外担保限额占比 */
    @BtCompositeSearch(fieldName = "g.proportion")

    private BigDecimal proportion;
    /** 单位 */
//    @Corp(inject = "corpName")
    @BtCompositeSearch(fieldName = "gc.corp_id")
    private String corpId;
    private String corpName;
    /** 备注 */
    @BtCompositeSearch(fieldName = "g.rmk")
    private String rmk;

    private BigDecimal assetsProportion;
    /**
     * 创建人登录名称
     */
    private String createBy;
    /**
     * 创建日期
     */
    private Date createDate;
    /**
     * 更新人登录名称
     */
    private String updateBy;
    /**
     * 更新日期
     */
    private Date updateDate;

    public BigDecimal getAssetsProportion() {
        return assetsProportion;
    }

    public void setAssetsProportion(BigDecimal assetsProportion) {
        this.assetsProportion = assetsProportion;
    }

    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }
    public BigDecimal getAssets() {
        return assets;
    }
    public void setAssets(BigDecimal assets) {
        this.assets = assets;
    }
    public String getCurId() {
        return curId;
    }
    public void setCurId(String curId) {
        this.curId = curId;
    }
    public String getCurName() {
        return curName;
    }
    public void setCurName(String curName) {
        this.curName = curName;
    }
    public BigDecimal getProportion() {
        return proportion;
    }
    public void setProportion(BigDecimal proportion) {
        this.proportion = proportion;
    }
    public String getCorpId() {
        return corpId;
    }
    public void setCorpId(String corpId) {
        this.corpId = corpId;
    }
    public String getCorpName() {
        return corpName;
    }
    public void setCorpName(String corpName) {
        this.corpName = corpName;
    }
    public String getRmk() {
        return rmk;
    }
    public void setRmk(String rmk) {
        this.rmk = rmk;
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
