package com.bytter.biz.foreign.vo;

import com.bytter.check.vo.IBaseDataInject;

/**
 * @Title: VO
 * @Description: 单位敞口设置表
 * @author huangx
 * @date 2022-06-24 15:20:33
 * @version 1.0
 */
public class FexCorpExposureVO implements IBaseDataInject, java.io.Serializable {
    /**单位ID*/
    private String corpId;
    private String corpName;
    private String corpCode;
    private String corpParentId;
    private Integer corpType;
    private String corpNetId;
    /**是否汇总 0 -否 1 -是*/
    private String isCollect;
    /**是否自然对冲 0 -否 1 -是*/
    private String isNaturalHedge;
    /**组织架构类型 0 -系统的单位组织架构 1 -资金计划的单位组织架构 */
    private String orgType;
    /**下级单位数量*/
    private Integer childCount;

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

    public String getCorpParentId() {
        return corpParentId;
    }

    public void setCorpParentId(String corpParentId) {
        this.corpParentId = corpParentId;
    }

    public Integer getCorpType() {
        return corpType;
    }

    public void setCorpType(Integer corpType) {
        this.corpType = corpType;
    }

    public String getCorpNetId() {
        return corpNetId;
    }

    public void setCorpNetId(String corpNetId) {
        this.corpNetId = corpNetId;
    }

    public String getIsCollect() {
        return isCollect;
    }

    public void setIsCollect(String isCollect) {
        this.isCollect = isCollect;
    }

    public String getIsNaturalHedge() {
        return isNaturalHedge;
    }

    public void setIsNaturalHedge(String isNaturalHedge) {
        this.isNaturalHedge = isNaturalHedge;
    }

    public String getOrgType() {
        return orgType;
    }

    public void setOrgType(String orgType) {
        this.orgType = orgType;
    }

    public Integer getChildCount() {
        return childCount;
    }

    public void setChildCount(Integer childCount) {
        this.childCount = childCount;
    }
}
