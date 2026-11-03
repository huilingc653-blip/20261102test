package com.bytter.bill.domain.billPool.vo;
import java.io.Serializable;
import java.util.List;
/**
 * @Title: AbPoolCorpVO
 * @Description: 票据池单位VO
 * @author wangzy
 * @date 2019/12/30 15:17
 *
 */
@SuppressWarnings("serial")
public class AbPoolCorpVO implements Serializable {
    /**票据池单位Id*/
    private String id;
    /**单位代码*/
    private String corpCode;
    /**上级单位Id*/
    private String parentId;
    /**单位代码*/
    private String corpId;
    /**单位名称*/
    private String corpName;
    /**上级单位*/
    private String higherCorp;
    /**层级*/
    private Integer bustypeLevel;
    /**状态 1启用，0禁用*/
    private String validSign;
    /**子单位*/
    private List<AbPoolCorpVO> sonAbPoolCorpList;
    private String abCorpId;

    public String getAbCorpId() {
        return abCorpId;
    }

    public void setAbCorpId(String abCorpId) {
        this.abCorpId = abCorpId;
    }

    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }
    public String getCorpCode() {
        return corpCode;
    }
    public void setCorpCode(String corpCode) {
        this.corpCode = corpCode;
    }
    public String getParentId() {
        return parentId;
    }
    public void setParentId(String parentId) {
        this.parentId = parentId;
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
    public String getHigherCorp() {
        return higherCorp;
    }
    public void setHigherCorp(String higherCorp) {
        this.higherCorp = higherCorp;
    }
    public Integer getBustypeLevel() {
        return bustypeLevel;
    }
    public void setBustypeLevel(Integer bustypeLevel) {
        this.bustypeLevel = bustypeLevel;
    }
    public String getValidSign() {
        return validSign;
    }
    public void setValidSign(String validSign) {
        this.validSign = validSign;
    }
    public List<AbPoolCorpVO> getSonAbPoolCorpList() {
        return sonAbPoolCorpList;
    }
    public void setSonAbPoolCorpList(List<AbPoolCorpVO> sonAbPoolCorpList) {
        this.sonAbPoolCorpList = sonAbPoolCorpList;
    }
}
