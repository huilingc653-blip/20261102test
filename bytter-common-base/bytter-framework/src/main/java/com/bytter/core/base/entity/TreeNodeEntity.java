package com.bytter.core.base.entity;
import com.bytter.core.base.persistence.tree.interfaces.TreeNode;
import javax.persistence.Transient;
import java.util.List;
/**
 * 用于构建树形结构数据
 *
 * @author zhoul
 * @since 2017/4/14.
 */
public abstract class TreeNodeEntity<T> extends IdEntity implements TreeNode<T> {
    private List<T> children;
    private String parentId;
    private Boolean expanded = true;
    private Boolean isLeaf;
    private Integer level;
    @Transient
    public List<T> getChildren() {
        return children;
    }
    public void setChildren(List<T> children) {
        this.children = children;
    }
    /**
     * 获取父节节点ID 实现此方法，必须在此方法上加入 @Transient 注解，否则工程启动错误
     *
     * @return 上级ID
     */
    public abstract String getParentId();
    public Boolean getExpanded() {
        return expanded;
    }
    public void setExpanded(Boolean expanded) {
        this.expanded = expanded;
    }
    public Integer getLevel() {
        return level;
    }
    public void setLevel(Integer level) {
        this.level = level;
    }
    public Boolean getIsLeaf() {
        return isLeaf;
    }
    public void setIsLeaf(Boolean leaf) {
        isLeaf = leaf;
    }
    public Integer getSort() {
        return null;
    }
}
