package com.bytter.core.base.persistence.tree.interfaces;
import java.util.List;
/**
 * 不连续、不连贯的树形结构 <br>
 * 即： <br>
 * root<br>
 * |-a1<br>
 * |--a2 (该节点被禁用)<br>
 * |---a3（该节点会紧接a1）<br>
 * |-b<br>
 *
 * @author zhoul
 * @since 2019-11-24
 */
public interface DiscontinuityTreeNode<T> {
    /**
     * @return 主键ID
     */
    String getId();
    /**
     * @return 父ID
     */
    String getParentId();
    /**
     * 设置子节点
     *
     * @param children
     *            孩子对象
     */
    void setChildren(List<T> children);
    /**
     * @return 子节点
     */
    List<T> getChildren();
    /**
     * 设置层级
     *
     * @param level
     */
    void setLevel(Integer level);
    /**
     * 获取层级
     *
     * @return
     */
    Integer getLevel();
    /**
     * 设置叶子节点
     *
     * @param isLeaf
     */
    void setIsLeaf(Boolean isLeaf);
    /**
     * 叶子节点
     *
     * @return
     */
    Boolean getIsLeaf();
}
