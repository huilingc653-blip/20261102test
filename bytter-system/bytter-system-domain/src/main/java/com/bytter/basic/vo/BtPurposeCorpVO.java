package com.bytter.basic.vo;
import com.bytter.core.annotation.BtCompositeSearch;
import com.bytter.core.annotation.Corp;
import com.bytter.core.base.entity.TreeEntity;
/**
 * @author shenc
 * @version 1.0
 * @Title
 * @Description:
 * @date 2018/6/1 11:24
 */
public class BtPurposeCorpVO extends TreeEntity<BtPurposeCorpVO> implements java.io.Serializable {
    /**
     * 用途ID
     */
    private String purposeId;
    /**
     * 单位ID
     */
    @Corp(inject = "corpName")
    @BtCompositeSearch(fieldName = "pc.corp_id")
    private String corpId;
    private String corpName;
    /**
     * 单位编码
     */
    private String corpCode;
    /**
     * 父单位ID(用于树形排序)
     */
    @BtCompositeSearch(fieldName = "c.parent_id")
    private String parentCorpId;
    public String getPurposeId() {
        return purposeId;
    }
    public void setPurposeId(String purposeId) {
        this.purposeId = purposeId;
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
    public String getParentCorpId() {
        return parentCorpId;
    }
    public void setParentCorpId(String parentCorpId) {
        this.parentCorpId = parentCorpId;
    }
    public String getCorpCode() {
        return corpCode;
    }
    public void setCorpCode(String corpCode) {
        this.corpCode = corpCode;
    }
    @Override
    public String getId() {
        return corpId;
    }
    @Override
    public String getParentId() {
        return parentCorpId;
    }
}
