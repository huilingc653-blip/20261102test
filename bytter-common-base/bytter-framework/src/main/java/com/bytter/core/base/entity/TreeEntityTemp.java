package com.bytter.core.base.entity;
import com.bytter.core.base.persistence.tree.interfaces.TreeStructureAttribute;
/**
 * 用于构建树形结构数据
 *
 * @author zhoul
 * @since 2017/4/14.
 */
@SuppressWarnings("serial")
public class TreeEntityTemp extends TreeEntity<TreeEntityTemp> implements TreeStructureAttribute<TreeEntityTemp> {
    private String parentId;
    public void setParentId(String parentId) {
        this.parentId = parentId;
    }
    public String getParentId() {
        return parentId;
    }
}
