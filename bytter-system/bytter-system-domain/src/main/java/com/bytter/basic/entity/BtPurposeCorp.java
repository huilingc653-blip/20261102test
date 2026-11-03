package com.bytter.basic.entity;
import com.bytter.core.base.entity.IdEntity;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
/**
 * @author zuoy
 * @version 1.0
 * @Title: Entity
 * @Description: 用途单位中间表
 * @date 2017-10-26 09:57:15
 */
@Entity
@Table(name = "BT_PURPOSE_CORP", schema = "")
@SuppressWarnings("serial")
public class BtPurposeCorp extends IdEntity {
    private String purposeId;
    /**
     * 单位ID
     */
    private String corpId;
    /**
     * 方法: 取得java.lang.String
     *
     * @return: java.lang.String  用途ID
     */
    @Column(name = "PURPOSE_ID", nullable = false, length = 32)
    public String getPurposeId() {
        return purposeId;
    }
    /**
     * 方法: 设置java.lang.String
     *
     * @param: java.lang.String  用途ID
     */
    public void setPurposeId(String purposeId) {
        this.purposeId = purposeId;
    }
    /**
     * 方法: 取得java.lang.String
     *
     * @return: java.lang.String  单位ID
     */
    @Column(name = "CORP_ID", nullable = false, length = 32)
    public String getCorpId() {
        return this.corpId;
    }
    /**
     * 方法: 设置java.lang.String
     *
     * @param: java.lang.String  单位ID
     */
    public void setCorpId(String corpId) {
        this.corpId = corpId;
    }
}
