package com.bytter.core.base.persistence.tree;
import com.bytter.core.base.persistence.tree.interfaces.TreeStructure;
import java.util.List;
/**
 * 树形结构简单实体，提供给VO或者DTO使用 Created by zhoul on 2017/10/26.
 */
public abstract class AbstractTreeStructure<M> implements TreeStructure<M> {
    private List<M> children;
    @Override
    public List<M> getChildren() {
        return children;
    }
    @Override
    public void setChildren(List<M> children) {
        this.children = children;
    }
}
