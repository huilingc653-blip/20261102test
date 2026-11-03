package com.bytter.system.vo;
import com.bytter.core.base.persistence.tree.interfaces.TreeSortStructure;
import java.util.List;
/**
 * Created by zhoul on 2017/5/23.
 */
public class CorpSimpleVO implements TreeSortStructure<CorpSimpleVO> {
    private String id;
    private String pId;
    private String parentId;
    private String netId;
    private String name;
    private String code;
    private Long childrenSize;
    private boolean canSelect = true;
    private Integer type;
    private Integer sort;
    private List<CorpSimpleVO> children;
    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }
    public String getpId() {
        return pId;
    }
    public void setpId(String pId) {
        this.pId = pId;
    }
    public String getNetId() {
        return netId;
    }
    public void setNetId(String netId) {
        this.netId = netId;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getCode() {
        return code;
    }
    public void setCode(String code) {
        this.code = code;
    }
    public Long getChildrenSize() {
        return childrenSize;
    }
    public void setChildrenSize(Long childrenSize) {
        this.childrenSize = childrenSize;
    }
    public boolean isCanSelect() {
        return canSelect;
    }
    public void setCanSelect(boolean canSelect) {
        this.canSelect = canSelect;
    }
    public String getParentId() {
        return parentId;
    }
    public Integer getType() {
        return type;
    }
    public void setType(Integer type) {
        this.type = type;
    }
    public void setParentId(String parentId) {
        this.parentId = parentId;
        this.pId = parentId;
    }
    @Override
    public void setChildren(List<CorpSimpleVO> object) {
        this.children = object;
    }
    @Override
    public List<CorpSimpleVO> getChildren() {
        return children;
    }
    @Override
    public Boolean getExpanded() {
        return null;
    }
    @Override
    public void setExpanded(Boolean expanded) {
    }
    @Override
    public Boolean getIsLeaf() {
        return null;
    }
    @Override
    public void setIsLeaf(Boolean leaf) {
    }
    @Override
    public void setLevel(Integer level) {
    }
    @Override
    public Integer getLevel() {
        return null;
    }
    @Override
    public Integer getSort() {
        return sort;
    }
    public void setSort(Integer sort) {
        this.sort = sort;
    }
}
