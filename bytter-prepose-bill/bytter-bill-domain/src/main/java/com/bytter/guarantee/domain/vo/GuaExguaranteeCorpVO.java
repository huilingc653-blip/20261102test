package com.bytter.guarantee.domain.vo;

import com.bytter.core.annotation.BtCompositeSearch;
import com.bytter.core.base.entity.TreeEntity;

import java.io.Serializable;


/**
 * 对外担保额度与单位中间表
 * GuaExguaranteeCorpVO
 *
 * */
public class GuaExguaranteeCorpVO extends TreeEntity<GuaExguaranteeCorpVO> implements Serializable {
    /**
     * 对外担保限额id
     */
    private String exAmountId;
    /**
     * 单位ID
     */
//    @Corp(inject = "corpName")
    @BtCompositeSearch(fieldName = "gc.corp_id")
    private String corpId;
    @BtCompositeSearch(fieldName = "gc.corp_name")
    private String corpName;
    /**
     * 单位编码
     */
    private String corpCode;


    @BtCompositeSearch(fieldName = "gc.cur_id")
    private String curId;

    @BtCompositeSearch(fieldName = "gc.cur_name")
    private String curName;

    /**
     * 父单位ID(用于树形排序)
     */
    @BtCompositeSearch(fieldName = "c.parent_id")
    private String parentCorpId;
    public String getId() {
        return corpId;
    }
    public String getExAmountId() {
        return exAmountId;
    }
    public void setExAmountId(String exAmountId) {
        this.exAmountId = exAmountId;
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
    public String getCorpCode() {
        return corpCode;
    }
    public void setCorpCode(String corpCode) {
        this.corpCode = corpCode;
    }
    public String getParentCorpId() {
        return parentCorpId;
    }
    public void setParentCorpId(String parentCorpId) {
        this.parentCorpId = parentCorpId;
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

    @Override
    public String getParentId() {
        return parentCorpId;
    }
}
