package com.bytter.system.api.vo;
import com.bytter.core.annotation.Corp;
import com.bytter.core.annotation.Dict;
import com.bytter.core.base.persistence.tree.interfaces.TreeStructure;
import java.io.Serializable;
import java.util.List;
/**
 * @author wangzx
 * @Description 资金池单位表
 * @date 2019.04.30
 */
public class FcCorpPoolVO implements Serializable,  TreeStructure<FcCorpPoolVO> {
    private static final long serialVersionUID = 2313478363314999846L;
    private String id;
    /**资金池单位ID*/
    @Corp(inject="corpName")
    private String corpId;
    private String corpCode;
    /**单位名称*/
    private String corpName;
    /**资金池代码*/
    private String poolCode;
    /**资金池名称*/
    private String poolName;
    /**状态*/
    @Dict(dictGroup = "fcCorpPool.status",inject = "statusName")
    private String status;
    private String statusName;
    /**所属资金池*/
    private String poolId;
    /**主体单位标识*/
    private String poolSign;
    /**备注*/
    private String rmk;
    private List<FcCorpPoolVO> children;
    private Boolean expanded = true;
    private Boolean isLeaf;
    private Integer level;
    public static long getSerialVersionUID() {
       return serialVersionUID;
    }
    @Override
    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }
    public String getCorpId() {
        return corpId;
    }
    public void setCorpId(String corpId) {
        this.corpId = corpId;
    }
    public String getCorpCode() {
        return corpCode;
    }
    public void setCorpCode(String corpCode) {
        this.corpCode = corpCode;
    }
    public String getCorpName() {
        return corpName;
    }
    public void setCorpName(String corpName) {
        this.corpName = corpName;
    }
    public String getPoolCode() {
        return poolCode;
    }
    public void setPoolCode(String poolCode) {
        this.poolCode = poolCode;
    }
    public String getPoolName() {
        return poolName;
    }
    public void setPoolName(String poolName) {
        this.poolName = poolName;
    }
    public String getStatus() {
        return status;
    }
    public void setStatus(String status) {
        this.status = status;
    }
    public String getPoolId() {
        return poolId;
    }
    public void setPoolId(String poolId) {
        this.poolId = poolId;
    }
    public String getPoolSign() {
        return poolSign;
    }
    public void setPoolSign(String poolSign) {
        this.poolSign = poolSign;
    }
    public String getRmk() {
        return rmk;
    }
    public void setRmk(String rmk) {
        this.rmk = rmk;
    }
    public Boolean getExpanded() {
        return expanded;
    }
    public void setExpanded(Boolean expanded) {
        this.expanded = expanded;
    }
     public Boolean getIsLeaf() {
        return isLeaf;
    }
    public void setIsLeaf(Boolean leaf) {
        isLeaf = leaf;
    }
    public Integer getLevel() {
        return level;
    }
    public void setLevel(Integer level) {
        this.level = level;
    }
    @Override
    public void setChildren(List<FcCorpPoolVO> object) {
        this.children = object;
    }
    @Override
    public List<FcCorpPoolVO> getChildren() {
        return children;
    }
    @Override
    public String getParentId() {
        return poolId;
    }

    public String getStatusName() {
        return statusName;
    }

    public void setStatusName(String statusName) {
        this.statusName = statusName;
    }
}
