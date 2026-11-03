package com.bytter.core.base.persistence.tree;
import com.bytter.core.base.persistence.tree.interfaces.TreeStructureAttribute;
/**
 * 树形结构简单实体，带附加属性，提供给VO或者DTO使用 Created by zhoul on 2017/10/26.
 */
public abstract class AbstractTreeStructureAttrbute<M> extends AbstractTreeStructure<M>
        implements TreeStructureAttribute<M> {
    private Boolean expanded = true;
    private Boolean isLeaf;
    private Integer level;
    @Override
    public Boolean getExpanded() {
        return expanded;
    }
    @Override
    public void setExpanded(Boolean expanded) {
        this.expanded = expanded;
    }
    public Boolean getLeaf() {
        return isLeaf;
    }
    public void setLeaf(Boolean leaf) {
        isLeaf = leaf;
    }
    @Override
    public Integer getLevel() {
        return level;
    }
    @Override
    public void setLevel(Integer level) {
        this.level = level;
    }
}
